package main.java.moduls;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Factory untuk mengelola objek Prescription menggunakan method static.
 */
public class PrescriptionFactory {
    private static final Map<String, Prescription> prescriptions = new HashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    static {
        addPrescription("P001", "D001", "Paracetamol", "2x sehari", "20 tablet", "Minum setelah makan.");
        addPrescription("P002", "D001", "Amoxicillin", "3x sehari", "15 kapsul", "Habiskan resep.");
    }

    public PrescriptionFactory() {} // Constructor private

    public static Prescription addPrescription(String patientId, String doctorId, String medicineName, String dosage, String quantity, String notes) {
        String newId = "PRES-" + String.format("%03d", idCounter.incrementAndGet());
        Prescription newPrescription = new Prescription(newId, patientId, doctorId, medicineName, dosage, quantity, notes);
        prescriptions.put(newPrescription.getId(), newPrescription);
        return newPrescription;
    }

    public static List<Prescription> getAllPrescriptions() {
        return new ArrayList<>(prescriptions.values());
    }

    public static Prescription getPrescriptionById(String id) {
        return prescriptions.get(id);
    }

    public static boolean updatePrescriptionStatus(String id, String newStatus) {
        Prescription prescription = prescriptions.get(id);
        if (prescription != null) {
            prescription.setStatus(newStatus);
            return true;
        }
        return false;
    }
}