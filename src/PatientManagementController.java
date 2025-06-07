package src;
import java.util.ArrayList;

/**
 * Controller untuk mengelola operasi terkait pasien.
 */
public class PatientManagementController {
    private PatientFactory patientFactory;

    public PatientManagementController() {
        patientFactory = new PatientFactory();
    }

    /**
     * Menambahkan pasien baru.
     * @param id ID pasien
     * @param name Nama pasien
     * @param age Usia pasien
     * @return Pesan sukses
     * @throws IllegalArgumentException Jika parameter tidak valid
     * @throws IllegalStateException Jika ID sudah ada
     */
    public String addPatient(String id, String name, int age) {
        if (id == null || name == null) {
            throw new IllegalArgumentException("ID dan nama tidak boleh null!");
        }
        if (id.isEmpty() || name.isEmpty()) {
            throw new IllegalArgumentException("ID dan nama tidak boleh kosong!");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Usia tidak boleh negatif!");
        }
        if (age > 120) {
            throw new IllegalArgumentException("Usia tidak boleh lebih dari 120 tahun!");
        }
        if (patientFactory.getAllPatients().stream().anyMatch(p -> p.getId().equals(id))) {
            throw new IllegalStateException("Gagal menambahkan pasien: ID sudah ada!");
        }
        patientFactory.createPatient(id, name, age);
        return "Pasien " + name + " berhasil ditambahkan!";
    }

    /**
     * Mendapatkan semua pasien.
     * @return Daftar semua pasien
     */
    public ArrayList<Patient> getAllPatients() {
        return patientFactory.getAllPatients();
    }
}