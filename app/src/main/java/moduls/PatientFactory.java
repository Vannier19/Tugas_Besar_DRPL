package main.java.moduls;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory untuk mengelola objek Patient menggunakan method static.
 */
public class PatientFactory {
    private static final Map<String, Patient> patients = new HashMap<>();
    private static int idCounter = 1;

    static {
        addPatient(new Patient("P" + String.format("%03d", idCounter++), "Budi Santoso", 30));
        addPatient(new Patient("P" + String.format("%03d", idCounter++), "Ani Wijaya", 25));
    }

    public PatientFactory() {} // Constructor private
    
    public Patient createPatient(String id, String name, int age) {
        Patient patient = new Patient(id, name, age);
        patients.put(id, patient);
        return patient;
    }


    public static Patient createPatient(String name, int age) {
        String id = "P" + String.format("%03d", idCounter++);
        Patient patient = new Patient(id, name, age);
        patients.put(id, patient);
        return patient;
    }

    public static void addPatient(Patient patient) {
        if (!patients.containsKey(patient.getId())) {
            patients.put(patient.getId(), patient);
        }
    }

    public static List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    public static Patient getPatientById(String id) {
        return patients.get(id);
    }

    public static boolean deletePatient(String id) {
        return patients.remove(id) != null;
    }
}