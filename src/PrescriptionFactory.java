package src;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PrescriptionFactory {
    
    private static final Map<String, Prescription> prescriptions = new HashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    static {
        if (prescriptions.isEmpty()) { 
            AtomicInteger dummyIdCounter = new AtomicInteger(0);
            
            String id1 = "PRES-" + String.format("%03d", dummyIdCounter.incrementAndGet());
            prescriptions.put(id1, new Prescription(id1, "P001", "D001", "Paracetamol", "2x sehari", "20 tablet", "Minum setelah makan."));

            String id2 = "PRES-" + String.format("%03d", dummyIdCounter.incrementAndGet());
            prescriptions.put(id2, new Prescription(id2, "P002", "D001", "Amoxicillin", "3x sehari", "15 kapsul", "Habiskan resep."));

            String id3 = "PRES-" + String.format("%03d", dummyIdCounter.incrementAndGet());
            prescriptions.put(id3, new Prescription(id3, "P003", "D002", "Vitamin C", "1x sehari", "30 tablet", "Dianjurkan saat musim hujan."));
            
            String id4 = "PRES-" + String.format("%03d", dummyIdCounter.incrementAndGet());
            Prescription processedPrescription = new Prescription(id4, "P004", "D002", "Ibuprofen", "1x sehari", "10 tablet", "Jika nyeri.");
            processedPrescription.setStatus("Processed");
            prescriptions.put(id4, processedPrescription);
        }
    }


    public PrescriptionFactory() {}

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

    public boolean updatePrescriptionStatus(String id, String newStatus) {
        Prescription prescription = prescriptions.get(id);
        if (prescription != null) {
            prescription.setStatus(newStatus);
            return true;
        }
        return false;
    }

    public boolean deletePrescription(String id) {
        return prescriptions.remove(id) != null;
    }
}
