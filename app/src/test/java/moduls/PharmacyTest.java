// Simpan di: src/test/java/tests/PharmacyTest.java
package tests;

import org.junit.jupiter.api.*;
import main.java.controllers.*;
import main.java.moduls.*;
import main.java.utils.*;
import java.util.Date;
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
        Medicine newMedicine = new Medicine("M001", "Paracetamol", "Obat Bebas", 100, new Date(System.currentTimeMillis() + 86400000L * 365), "PT. Kimia Farma");
        MedicineFactory.addMedicine(newMedicine);
        
        Medicine foundMedicine = MedicineFactory.getMedicineById("M001");
        assertNotNull(foundMedicine);
        assertEquals("Paracetamol", foundMedicine.getName());
    }

    @Test
    @DisplayName("[U-7-01] Skenario Normal: Sistem mengirim notifikasi jika stok obat di bawah ambang batas")
    void notification_TriggersForLowStock() {
        Medicine lowStockMedicine = new Medicine("M002", "Amoxicillin", "Antibiotik", 10, new Date(System.currentTimeMillis() + 86400000L * 365), "PT. Penta Valent");
        MedicineFactory.addMedicine(lowStockMedicine);
        
        NotificationController notificationController = new NotificationController();
        notificationController.discoverNewNotifications();
        List<Notification> notifications = notificationController.getCurrentNotifications();
        
        assertFalse(notifications.isEmpty(), "Seharusnya ada notifikasi untuk stok rendah.");
        assertTrue(notifications.get(0).getMessage().contains("menipis"));
    }

    @Test
    @DisplayName("[U-7-02] Skenario Normal: Sistem mengirim notifikasi jika obat akan kedaluwarsa")
    void notification_TriggersForExpiringSoon() {
        // Tanggal kedaluwarsa 20 hari dari sekarang (kurang dari 1 bulan)
        Medicine expiringMedicine = new Medicine("M003", "Aspirin", "Obat Biasa", 100, new Date(System.currentTimeMillis() + 86400000L * 20), "PharmaCo");
        MedicineFactory.addMedicine(expiringMedicine);
        
        NotificationController notificationController = new NotificationController();
        notificationController.discoverNewNotifications();
        List<Notification> notifications = notificationController.getCurrentNotifications();
        
        assertFalse(notifications.isEmpty(), "Seharusnya ada notifikasi untuk obat yang akan kedaluwarsa.");
        assertTrue(notifications.get(0).getMessage().contains("akan kedaluwarsa"));
    }

    @Test
    @DisplayName("[U-7-03] Skenario Alternatif: Tidak ada notifikasi jika stok aman")
    void notification_DoesNotTriggerForSafeStock() {
        Medicine safeMedicine = new Medicine("M004", "Vitamin C", "Vitamin", 150, new Date(System.currentTimeMillis() + 86400000L * 365), "HealthyLife");
        MedicineFactory.addMedicine(safeMedicine);
        
        NotificationController notificationController = new NotificationController();
        notificationController.discoverNewNotifications();
        List<Notification> notifications = notificationController.getCurrentNotifications();
        
        assertTrue(notifications.isEmpty(), "Tidak ada notifikasi yang cocok dengan filter ini.");
    }
}