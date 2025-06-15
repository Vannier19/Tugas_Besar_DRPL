// Simpan di: src/test/java/tests/PharmacyTest.java
package tests;

import org.junit.jupiter.api.*;
import main.java.controllers.*;
import main.java.moduls.*;
import main.java.utils.*;
import main.resources.views.*;
import main.resources.assets.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pengujian Use Case Apotek (UC-06 & UC-07)")
class PharmacyTest {

    @BeforeEach
    void clearData() {
        // Membersihkan data obat sebelum setiap tes agar tidak tumpang tindih
        MedicineFactory.clearMedicines();
    }

    @Test
    @DisplayName("[U-6-01] Skenario Normal: Apoteker berhasil menambah dan melihat stok obat")
    void addAndSeeMedicine_Success() {
        Medicine newMedicine = new Medicine("M001", "Paracetamol", 100, LocalDate.now().plusYears(1));
        MedicineFactory.addMedicine(newMedicine);
        
        Medicine foundMedicine = MedicineFactory.getMedicineById("M001");
        assertNotNull(foundMedicine);
        assertEquals("Paracetamol", foundMedicine.getName());
    }

    @Test
    @DisplayName("[U-7-01] Skenario Normal: Sistem mengirim notifikasi jika stok obat di bawah ambang batas")
    void notification_TriggersForLowStock() {
        Medicine lowStockMedicine = new Medicine("M002", "Amoxicillin", 49, LocalDate.now().plusYears(1)); // Stok < 50
        MedicineFactory.addMedicine(lowStockMedicine);
        
        NotificationController notificationController = new NotificationController();
        notificationController.checkMedicineStockAndExpiry();
        List<Notification> notifications = notificationController.getNotifications();
        
        assertFalse(notifications.isEmpty(), "Seharusnya ada notifikasi untuk stok rendah.");
        assertTrue(notifications.get(0).getMessage().contains("stoknya menipis"));
    }

    @Test
    @DisplayName("[U-7-02] Skenario Normal: Sistem mengirim notifikasi jika obat akan kedaluwarsa")
    void notification_TriggersForExpiringSoon() {
        // Tanggal kedaluwarsa 20 hari dari sekarang (kurang dari 1 bulan)
        Medicine expiringMedicine = new Medicine("M003", "Aspirin", 100, LocalDate.now().plusDays(20));
        MedicineFactory.addMedicine(expiringMedicine);
        
        NotificationController notificationController = new NotificationController();
        notificationController.checkMedicineStockAndExpiry();
        List<Notification> notifications = notificationController.getNotifications();
        
        assertFalse(notifications.isEmpty(), "Seharusnya ada notifikasi untuk obat yang akan kedaluwarsa.");
        assertTrue(notifications.get(0).getMessage().contains("akan segera kedaluwarsa"));
    }

    @Test
    @DisplayName("[U-7-03] Skenario Alternatif: Tidak ada notifikasi jika stok aman")
    void notification_DoesNotTriggerForSafeStock() {
        Medicine safeMedicine = new Medicine("M004", "Vitamin C", 150, LocalDate.now().plusYears(2));
        MedicineFactory.addMedicine(safeMedicine);
        
        NotificationController notificationController = new NotificationController();
        notificationController.checkMedicineStockAndExpiry();
        List<Notification> notifications = notificationController.getNotifications();
        
        assertTrue(notifications.isEmpty(), "Seharusnya tidak ada notifikasi jika stok dan tanggal kedaluwarsa aman.");
    }
}