package src;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Factory untuk mengelola objek Medicine, digunakan untuk simulasi data obat.
 */
public class MedicineFactory {
    private Map<String, Medicine> medicines;

    public MedicineFactory() {
        medicines = new HashMap<>();
        // Data awal untuk simulasi
        // Obat dengan stok rendah
        medicines.put("M001", new Medicine("M001", "Paracetamol", 25, new Date()));
        // Obat dengan stok cukup
        medicines.put("M002", new Medicine("M002", "Amoxicillin", 100, new Date()));
        // Obat dengan stok sangat rendah
        medicines.put("M003", new Medicine("M003", "Vitamin C", 10, new Date()));
    }

    public List<Medicine> getAllMedicines() {
        return new ArrayList<>(medicines.values());
    }
}