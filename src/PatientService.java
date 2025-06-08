package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PatientService {
    private static Map<String, Patient> patients = new HashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(0); // For generating simple IDs

    static {
        // Initial data, similar to PatientFactory
        patients.put("P001", new Patient("P001", "Budi Santoso", 30));
        patients.put("P002", new Patient("P002", "Ani Wijaya", 25));
        idCounter.set(2);
    }

    public PatientService() {
        // Ensure initial IDs are higher than existing ones if any are added programmatically
        // A more robust ID generation would be needed for a real DB
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    public Patient getPatientById(String id) {
        return patients.get(id);
    }

    public boolean addPatient(Patient patient) {
        if (patients.containsKey(patient.getId())) {
            return false;
        }
        patients.put(patient.getId(), patient);
        return true;
    }

    public boolean updatePatient(Patient patient) {
        if (!patients.containsKey(patient.getId())) {
            return false;
        }
        patients.put(patient.getId(), patient);
        return true;
    }

    public boolean deletePatient(String id) {
        return patients.remove(id) != null;
    }

    // A simple method to generate a new patient ID
    public String generateNewPatientId() {
        return "P" + String.format("%03d", idCounter.incrementAndGet());
    }
}