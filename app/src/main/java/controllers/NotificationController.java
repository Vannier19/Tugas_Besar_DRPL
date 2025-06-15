package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit; 
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap; 

public class NotificationController {
    private static final int LOW_STOCK_THRESHOLD = 50;
    private static final long EXPIRY_THRESHOLD_MILLIS = TimeUnit.DAYS.toMillis(30); 
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    private MedicineFactory medicineFactory;
    private static Map<String, Notification> persistentNotifications = new HashMap<>(); 
    
    private static Map<String, Long> dismissedNotifications = new HashMap<>();
    private static final long DISMISSAL_COOLDOWN_MILLIS = TimeUnit.HOURS.toMillis(1);

    public NotificationController() {
        this.medicineFactory = new MedicineFactory();
    }

    public void discoverNewNotifications() {
        List<Medicine> allMedicines = medicineFactory.getAllMedicines();
        Date now = new Date();

        for (Medicine medicine : allMedicines) {
            if (medicine.getStock() < LOW_STOCK_THRESHOLD) {
                String potentialMessage = "Stok obat '" + medicine.getName() + "' menipis. Sisa: " + medicine.getStock() + ".";
                String notificationType = "LOW_STOCK";
                String uniqueKey = notificationType + "_" + medicine.getName();

                if (wasRecentlyDismissed(uniqueKey, now)) {
                    continue;
                }

                boolean exists = persistentNotifications.values().stream()
                                    .anyMatch(n -> n.getType().equals(notificationType) && 
                                                   n.getMessage().contains("Stok obat '" + medicine.getName() + "'"));

                if (!exists) {
                    String newId = "NOTIF" + String.format("%03d", idCounter.getAndIncrement());
                    Notification newNotif = new Notification(newId, potentialMessage, now, notificationType);
                    persistentNotifications.put(newId, newNotif);
                }
            }

            if (medicine.getExpiryDate().after(now) && medicine.getStock() > 0) { 
                long diff = medicine.getExpiryDate().getTime() - now.getTime();
                if (diff <= EXPIRY_THRESHOLD_MILLIS) {
                    String potentialMessage = "Obat '" + medicine.getName() + "' (ID: " + medicine.getId() + ", Supplier: " + medicine.getSupplier() + ") akan kedaluwarsa pada " + medicine.getExpiryDate() + ". Sisa: " + medicine.getStock() + ".";
                    String notificationType = "EXPIRY_WARNING";
                    String uniqueKey = notificationType + "_" + medicine.getId(); 

                    if (wasRecentlyDismissed(uniqueKey, now)) {
                        continue; 
                    }

                    boolean exists = persistentNotifications.values().stream()
                                        .anyMatch(n -> n.getType().equals(notificationType) && 
                                                       n.getMessage().contains("ID: " + medicine.getId()));
                    
                    if (!exists) {
                        String newId = "NOTIF" + String.format("%03d", idCounter.getAndIncrement());
                        Notification newNotif = new Notification(newId, potentialMessage, now, notificationType);
                        persistentNotifications.put(newId, newNotif);
                    }
                }
            }
        }
    }

    private boolean wasRecentlyDismissed(String uniqueKey, Date currentTime) {
        Long dismissalTime = dismissedNotifications.get(uniqueKey);
        if (dismissalTime == null) {
            return false;
        }
        
        long timeSinceDismissal = currentTime.getTime() - dismissalTime;
        if (timeSinceDismissal > DISMISSAL_COOLDOWN_MILLIS) {
            dismissedNotifications.remove(uniqueKey);
            return false;
        }
        
        return true;
    }

    public List<Notification> getCurrentNotifications() {
        return new ArrayList<>(persistentNotifications.values());
    }

    public boolean deleteNotification(String notificationId) {
        Notification notification = persistentNotifications.get(notificationId);
        if (notification != null) {
            String uniqueKey = generateUniqueKeyFromNotification(notification);
            if (uniqueKey != null) {
                dismissedNotifications.put(uniqueKey, new Date().getTime());
            }
            
            return persistentNotifications.remove(notificationId) != null;
        }
        return false;
    }

    private String generateUniqueKeyFromNotification(Notification notification) {
        String message = notification.getMessage();
        String type = notification.getType();
        
        if ("LOW_STOCK".equals(type)) {
            int startIndex = message.indexOf("Stok obat '") + 11;
            int endIndex = message.indexOf("'", startIndex);
            if (startIndex > 10 && endIndex > startIndex) {
                String medicineName = message.substring(startIndex, endIndex);
                return type + "_" + medicineName;
            }
        } else if ("EXPIRY_WARNING".equals(type)) {
            int startIndex = message.indexOf("ID: ") + 4;
            int endIndex = message.indexOf(",", startIndex);
            if (startIndex > 3 && endIndex > startIndex) {
                String medicineId = message.substring(startIndex, endIndex);
                return type + "_" + medicineId;
            }
        }
        
        return null;
    }

    public void clearAllNotifications() {
        Date now = new Date();
        for (Notification notification : persistentNotifications.values()) {
            String uniqueKey = generateUniqueKeyFromNotification(notification);
            if (uniqueKey != null) {
                dismissedNotifications.put(uniqueKey, now.getTime());
            }
        }
        
        persistentNotifications.clear();
    }
}