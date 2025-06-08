package tests;

import src.MedicalRecord;
import src.controllers.MedicalRecordController;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Kelas untuk menguji fitur manajemen riwayat medis pasien secara interaktif.
 * Kelas ini mensimulasikan interaksi Dokter dengan MedicalRecordPage.
 */
public class ManageMedicalRecordTest {
    public static void main(String[] args) {
        MedicalRecordController controller = new MedicalRecordController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Tes Interaktif Manajemen Riwayat Medis ===");

        // Skenario 1: Menambahkan riwayat medis baru
        System.out.println("\n--- Menambahkan Riwayat Medis Baru ---");
        System.out.print("Masukkan ID Pasien (e.g., P001): ");
        String patientIdAdd = scanner.nextLine();
        System.out.print("Masukkan Diagnosis: ");
        String diagnosis = scanner.nextLine();

        try {
            String result = controller.addMedicalRecord(patientIdAdd, diagnosis, new Date());
            System.out.println("Hasil: " + result);
        } catch (IllegalArgumentException e) {
            System.err.println("Gagal: " + e.getMessage());
        }

        // Skenario 2: Melihat riwayat medis pasien
        System.out.println("\n--- Melihat Riwayat Medis Pasien ---");
        System.out.print("Masukkan ID Pasien untuk dilihat riwayatnya (e.g., P001): ");
        String patientIdView = scanner.nextLine();

        
        
        try {
            List<MedicalRecord> records = controller.getMedicalRecords(patientIdView);
            if (records.isEmpty()) {
                System.out.println("Tidak ada riwayat medis untuk pasien dengan ID: " + patientIdView);
            } else {
                System.out.println("Menampilkan riwayat medis untuk pasien ID: " + patientIdView);
                for (MedicalRecord record : records) {
                    System.out.println(" - ID Rekam Medis: " + record.getId() + 
                                       ", Diagnosis: " + record.getDiagnosis() + 
                                       ", Tanggal: " + record.getDate());
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Gagal: " + e.getMessage());
        }

        

        scanner.close();
    }
}