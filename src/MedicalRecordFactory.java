package src;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicalRecordFactory {
    private Map<String, List<MedicalRecord>> recordsByPatient;

    public MedicalRecordFactory() {
        recordsByPatient = new HashMap<>();
        Date now = new Date();
        List<MedicalRecord> recordsP001 = new ArrayList<>();
        recordsP001.add(new MedicalRecord("MR001", "P001", "Common Cold", now));
        recordsByPatient.put("P001", recordsP001);
    }

    /**
     * Menambahkan riwayat medis baru untuk seorang pasien.
     * @param record Objek MedicalRecord yang akan ditambahkan.
     */
    public void addMedicalRecord(MedicalRecord record) {
        recordsByPatient.computeIfAbsent(record.getPatientId(), k -> new ArrayList<>()).add(record);
    }

    /**
     * Mengambil semua riwayat medis untuk ID pasien tertentu.
     * @param patientId ID pasien.
     * @return Daftar riwayat medis pasien.
     */
    public List<MedicalRecord> getRecordsByPatientId(String patientId) {
        return recordsByPatient.getOrDefault(patientId, new ArrayList<>());
    }
}