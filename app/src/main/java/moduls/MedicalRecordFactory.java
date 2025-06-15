package main.java.moduls;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Factory untuk mengelola objek MedicalRecord menggunakan method static.
 */
public class MedicalRecordFactory {
    private static final Map<String, List<MedicalRecord>> recordsByPatient = new HashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    static {
        createMedicalRecord("P001", "Demam, diberi paracetamol.", new Date(System.currentTimeMillis() - (2L * 24 * 60 * 60 * 1000)));
        createMedicalRecord("P001", "Common Cold, istirahat cukup.", new Date());
    }

    public MedicalRecordFactory() {} // Constructor private

    public static MedicalRecord createMedicalRecord(String patientId, String diagnosis, Date date) {
        String newId = "MR" + String.format("%03d", idCounter.incrementAndGet());
        MedicalRecord record = new MedicalRecord(newId, patientId, diagnosis, date);
        recordsByPatient.computeIfAbsent(patientId, k -> new ArrayList<>()).add(record);
        return record;
    }

    public static List<MedicalRecord> getRecordsByPatientId(String patientId) {
        return recordsByPatient.getOrDefault(patientId, new ArrayList<>());
    }
    
    public static List<MedicalRecord> getAllRecords() {
        List<MedicalRecord> allRecords = new ArrayList<>();
        for (List<MedicalRecord> records : recordsByPatient.values()) {
            allRecords.addAll(records);
        }
        return allRecords;
    }
    public static void addMedicalRecord(String patientId, String diagnosis, Date date) {
        String newId = "MR" + String.format("%03d", idCounter.incrementAndGet());
        MedicalRecord record = new MedicalRecord(newId, patientId, diagnosis, date);
        recordsByPatient.computeIfAbsent(patientId, k -> new ArrayList<>()).add(record);
    }

}