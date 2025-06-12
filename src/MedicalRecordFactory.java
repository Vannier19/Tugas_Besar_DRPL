package src;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MedicalRecordFactory {
    
    private static final Map<String, List<MedicalRecord>> recordsByPatient = new HashMap<>();

    private static final AtomicInteger idCounter = new AtomicInteger(0);

    static {
        addMedicalRecord("P001", "Demam, istirahatlah boi.", new Date(System.currentTimeMillis() - (2L * 24 * 60 * 60 * 15000)));
        addMedicalRecord("P001", "Demam, istirahat.", new Date(System.currentTimeMillis() - (2L * 24 * 60 * 60 * 10000)));
        addMedicalRecord("P001", "Demam, diberi paracetamol.", new Date(System.currentTimeMillis() - (2L * 24 * 60 * 60 * 1000)));
        addMedicalRecord("P001", "Common Cold, istirahat cukup.", new Date());
    }

    public MedicalRecordFactory() {
    }

    public static void addMedicalRecord(String patientId, String diagnosis, Date date) {
        String newId = "MR" + String.format("%03d", idCounter.incrementAndGet());
        MedicalRecord record = new MedicalRecord(newId, patientId, diagnosis, date);
        recordsByPatient.computeIfAbsent(patientId, k -> new ArrayList<>()).add(record);
    }

    public List<MedicalRecord> getRecordsByPatientId(String patientId) {
        return recordsByPatient.getOrDefault(patientId, new ArrayList<>());
    }
}