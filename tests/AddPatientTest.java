package tests; 

import src.controllers.PatientManagementController;

/**
 * Kelas untuk menguji fitur penambahan pasien setelah refaktor.
 */
public class AddPatientTest {
    public static void main(String[] args) {
        PatientManagementController controller = new PatientManagementController();

        System.out.println("--- Starting Patient Management Tests ---");

        // Test 1: Tambah pasien sukses
        try {
            System.out.println("\nRunning Test 1: Add patient successfully");
            String result = controller.addNewPatient("Rina Sari", 28); // Removed ID as argument
            System.out.println("Test 1 - Tambah pasien sukses: " + result);
            System.out.println("Current patients after Test 1:");
            controller.retrieveAllPatients().forEach(p -> System.out.println("- " + p.getId() + ", " + p.getName() + ", " + p.getAge()));
        } catch (Exception e) {
            System.err.println("Test 1 gagal: " + e.getMessage()); // Use System.err for errors
        }

        // Test 2: Tambah pasien gagal - Nama kosong
        try {
            System.out.println("\nRunning Test 2: Add patient with empty name");
            String result = controller.addNewPatient("", 30);
            System.out.println("Test 2 - Tambah pasien gagal (harusnya exception): " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Test 2 berhasil gagal: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Test 2 gagal: " + e.getMessage());
        }

        // Test 3: Tambah pasien gagal - Usia negatif
        try {
            System.out.println("\nRunning Test 3: Add patient with negative age");
            String result = controller.addNewPatient("Test Negatif", -5);
            System.out.println("Test 3 - Tambah pasien gagal (harusnya exception): " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Test 3 berhasil gagal: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Test 3 gagal: " + e.getMessage());
        }

        // Test 4: Update pasien
        try {
            System.out.println("\nRunning Test 4: Update existing patient P001");
            String updateResult = controller.updateExistingPatient("P001", "Budi Santoso Jr.", 32);
            System.out.println("Test 4 - Perbarui pasien sukses: " + updateResult);
            // Verify update
            System.out.println("Current patients after Test 4:");
            controller.retrieveAllPatients().forEach(p -> System.out.println("- " + p.getId() + ", " + p.getName() + ", " + p.getAge()));
        } catch (Exception e) {
            System.err.println("Test 4 gagal: " + e.getMessage());
        }

        // Test 5: Delete pasien
        try {
            System.out.println("\nRunning Test 5: Delete patient P002");
            String deleteResult = controller.deletePatient("P002");
            System.out.println("Test 5 - Hapus pasien sukses: " + deleteResult);
            // Verify deletion
            System.out.println("Current patients after Test 5:");
            controller.retrieveAllPatients().forEach(p -> System.out.println("- " + p.getId() + ", " + p.getName() + ", " + p.getAge()));
        } catch (Exception e) {
            System.err.println("Test 5 gagal: " + e.getMessage());
        }

        // Test 6: Mencoba menghapus pasien yang tidak ada
        try {
            System.out.println("\nRunning Test 6: Attempt to delete non-existent patient (P999)");
            String deleteResult = controller.deletePatient("P999");
            System.out.println("Test 6 - Hapus pasien gagal (harusnya exception): " + deleteResult);
        } catch (IllegalArgumentException e) {
            System.out.println("Test 6 berhasil gagal: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Test 6 gagal: " + e.getMessage());
        }

        System.out.println("\n--- All Tests Completed ---");
    }
}