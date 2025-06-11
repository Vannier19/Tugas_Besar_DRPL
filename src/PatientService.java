package src;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PatientService {
    private static PatientFactory patientFactory = new PatientFactory(); 
    private static final AtomicInteger idCounter = new AtomicInteger(0); 

    static {
        int maxIdNum = patientFactory.getAllPatients().stream()
                                  .map(Patient::getId)
                                  .filter(id -> id.startsWith("P"))
                                  .mapToInt(id -> {
                                      try {
                                          return Integer.parseInt(id.substring(1));
                                      } catch (NumberFormatException e) {
                                          return 0;
                                      }
                                  })
                                  .max().orElse(0);
        idCounter.set(maxIdNum);
    }

    public PatientService() {}

    public List<Patient> getAllPatients() {
        return patientFactory.getAllPatients();
    }

    public Patient getPatientById(String id) {
        return patientFactory.getPatientById(id);
    }

    public boolean addPatient(Patient patient) {
        if (patientFactory.getPatientById(patient.getId()) != null) {
            return false;
        }
        patientFactory.createPatient(patient.getId(), patient.getName(), patient.getAge());
        return true;
    }

    public boolean updatePatient(Patient patient) {
        if (patientFactory.getPatientById(patient.getId()) == null) {
            return false;
        }
        patientFactory.createPatient(patient.getId(), patient.getName(), patient.getAge());
        return true;
    }

    public boolean deletePatient(String id) {
        return patientFactory.deletePatient(id); 
    }

    public String generateNewPatientId() {
        return "P" + String.format("%03d", idCounter.incrementAndGet());
    }
}