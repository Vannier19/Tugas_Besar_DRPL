package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory untuk mengelola pembuatan dan penyimpanan objek Patient.
 */
public class PatientFactory {
    private Map<String, Patient> patients;

    public PatientFactory() {
        patients = new HashMap<>();
        // Data awal (bisa diisi dari database atau file di masa depan)
        patients.put("P001", new Patient("P001", "Budi Santoso", 30));
        patients.put("P002", new Patient("P002", "Ani Wijaya", 25));
    }

    /**
     * Membuat atau mengambil Patient berdasarkan ID.
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
    public ArrayList<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }
}
