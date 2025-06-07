package tests;
import src.*;

/**
 * Kelas untuk menguji fitur penambahan pasien.
 */
public class AddPatientTest {
    public static void main(String[] args) {
        PatientManagementController controller = new PatientManagementController();

        // Test 1: Tambah pasien sukses
        try {
            String result = controller.addPatient("P003", "Rina Sari", 28);
            System.out.println("Test 1 - Tambah pasien sukses: " + result);
        } catch (Exception e) {
            System.out.println("Test 1 gagal: " + e.getMessage());
        }

        // Test 2: Tambah pasien gagal - ID sudah ada
        try {
            String result = controller.addPatient("P001", "Duplikat Budi", 30);
            System.out.println("Test 2 - Tambah pasien gagal (harusnya exception): " + result);
        } catch (IllegalStateException e) {
            System.out.println("Test 2 berhasil gagal: " + e.getMessage());
        }

        // Test 3: Tambah pasien gagal - Usia negatif
        try {
            String result = controller.addPatient("P004", "Test Negatif", -5);
            System.out.println("Test 3 - Tambah pasien gagal (harusnya exception): " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Test 3 berhasil gagal: " + e.getMessage());
        }

        // Test 4: Tambah pasien gagal - Nama kosong
        try {
            String result = controller.addPatient("P005", "", 25);
            System.out.println("Test 4 - Tambah pasien gagal (harusnya exception): " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Test 4 berhasil gagal: " + e.getMessage());
        }

        // Test 5: Tambah pasien gagal - Usia terlalu tinggi
        try {
            String result = controller.addPatient("P006", "Test Tua", 150);
            System.out.println("Test 5 - Tambah pasien gagal (harusnya exception): " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Test 5 berhasil gagal: " + e.getMessage());
        }
    }
}