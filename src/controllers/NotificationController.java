package src.controllers;
import src.Medicine;
import src.Notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Controller untuk mengelola pembuatan notifikasi.
 * Logika di dalamnya didasarkan pada algoritma Notifikasi Apotek.
 */
public class NotificationController {
    private static final int LOW_STOCK_THRESHOLD = 50;
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    /**
     * Memeriksa daftar obat dan membuat notifikasi jika stok rendah.
     * Implementasi ini mengikuti algoritma yang dijelaskan pada seksi 2.4.7.
     * @param medicines Daftar obat yang akan diperiksa.
     * @return Daftar notifikasi yang baru dibuat.
     */
    public List<Notification> checkStockAndCreateNotifications(List<Medicine> medicines) {
        List<Notification> newNotifications = new ArrayList<>();

        for (Medicine medicine : medicines) {
            if (medicine.getStock() < LOW_STOCK_THRESHOLD) {
                String newId = "NOTIF" + String.format("%03d", idCounter.getAndIncrement());
                String message = "Stok obat " + medicine.getName() + " menipis. Sisa: " + medicine.getStock();
                Notification notification = new Notification(newId, message, new Date());
                newNotifications.add(notification);
            }
        }
        return newNotifications;
    }
}