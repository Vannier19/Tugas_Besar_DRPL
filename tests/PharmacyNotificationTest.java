package tests;

import src.Medicine;
import src.MedicineFactory;
import src.Notification;
import src.controllers.NotificationController;

import java.util.List;

/**
 * Kelas untuk menguji fitur notifikasi apotek secara otomatis.
 * Kelas ini mensimulasikan sistem yang memeriksa stok obat dan menghasilkan notifikasi.
 */
public class PharmacyNotificationTest {
    public static void main(String[] args) {
        // 1. Ambil semua data obat dari factory
        MedicineFactory medicineFactory = new MedicineFactory();
        List<Medicine> allMedicines = medicineFactory.getAllMedicines();
        System.out.println("Memeriksa stok untuk " + allMedicines.size() + " jenis obat...");

        // 2. Gunakan NotificationController untuk memeriksa dan membuat notifikasi
        NotificationController notificationController = new NotificationController();
        List<Notification> generatedNotifications = notificationController.checkStockAndCreateNotifications(allMedicines);

        // 3. Tampilkan hasil
        System.out.println("\n=== Hasil Pemeriksaan Notifikasi Stok Obat ===");
        if (generatedNotifications.isEmpty()) {
            System.out.println("Tidak ada notifikasi baru. Semua stok obat aman.");
        } else {
            System.out.println("Ditemukan " + generatedNotifications.size() + " notifikasi baru:");
            for (Notification notification : generatedNotifications) {
                System.out.println(" - " + notification.toString());
            }
        }
    }
}