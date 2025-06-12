package src;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PrescriptionFactory {
    
    private static final Map<String, Prescription> prescriptions = new HashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    public void addPrescription(String patientId, String doctorId, String medicineName, String dosage, String quantity, String notes) {
        String newId = "PRES-" + String.format("%03d", idCounter.incrementAndGet());
        Prescription newPrescription = new Prescription(newId, patientId, doctorId, medicineName, dosage, quantity, notes);
        prescriptions.put(newPrescription.getId(), newPrescription);
    }

    public List<Prescription> getAllPrescriptions() {
        return new ArrayList<>(prescriptions.values());
    }

    public Prescription getPrescriptionById(String id) {
        return prescriptions.get(id);
    }
}