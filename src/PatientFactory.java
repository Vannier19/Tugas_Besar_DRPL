package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory untuk mengelola pembuatan dan penyimpanan objek Patient.
 */
public class PatientFactory {
    private static Map<String, Patient> patients = new HashMap<>();

    static {
        patients.put("P001", new Patient("P001", "Budi Santoso", 30));
        patients.put("P002", new Patient("P002", "Ani Wijaya", 25));
    }

    public PatientFactory() {}

    /**
     * Membuat atau memperbarui Patient berdasarkan ID.
     * Jika ID sudah ada, data pasien diperbarui.
     * @param id ID pasien
     * @param name Nama pasien
     * @param age Usia pasien
     * @return Objek Patient
     */
    public Patient createPatient(String id, String name, int age) {
        Patient patient = new Patient(id, name, age);
        patients.put(id, patient);
        return patient;
    }

    /**
     * Mendapatkan semua pasien.
     * @return Daftar semua pasien
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    /**
     * Mendapatkan pasien berdasarkan ID.
     * @param id ID pasien
     * @return Patient jika ditemukan, null jika tidak.
     */
    public Patient getPatientById(String id) {
        return patients.get(id);
    }

    /**
     * Menghapus pasien berdasarkan ID.
     * @param id ID pasien yang akan dihapus
     * @return true jika berhasil dihapus, false jika pasien tidak ditemukan.
     */
    public boolean deletePatient(String id) { 
        return patients.remove(id) != null;
    }
}