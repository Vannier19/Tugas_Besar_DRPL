package main.java.moduls;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Factory untuk mengelola objek Medicine menggunakan method static.
 */
public class MedicineFactory {
    private static final Map<String, List<Medicine>> medicinesByName = new HashMap<>();
    private static final Map<String, String> drugCategoryMap = new HashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    static {
        addMedicine("Paracetamol", "Obat Bebas", 50, new Date(System.currentTimeMillis() + 86400000L * 180), "PT. Kalbe Farma");
        addMedicine("Amoxicillin", "Antibiotik", 100, new Date(System.currentTimeMillis() + 86400000L * 60), "PT. Anugerah Argon Medica");
        addMedicine("Vitamin C", "Vitamin", 10, new Date(System.currentTimeMillis() + 86400000L * 30), "PT. Kalbe Farma");
    }

    public MedicineFactory() {} // Constructor private

    public static Medicine addMedicine(String name, String category, int stock, Date expiryDate, String supplier) {
        if (drugCategoryMap.containsKey(name) && !drugCategoryMap.get(name).equalsIgnoreCase(category)) {
            throw new IllegalArgumentException("Kategori obat '" + name + "' harus konsisten.");
        }
        drugCategoryMap.putIfAbsent(name, category);

        String newId = "M" + String.format("%03d", idCounter.incrementAndGet());
        Medicine newEntry = new Medicine(newId, name, category, stock, expiryDate, supplier);
        medicinesByName.computeIfAbsent(name, k -> new ArrayList<>()).add(newEntry);
        return newEntry;
    }
    
    public static void addMedicine(Medicine medicine) {
        medicinesByName.computeIfAbsent(medicine.getName(), k -> new ArrayList<>()).add(medicine);
    }
    
    public static void clearMedicines() {
        medicinesByName.clear();
        drugCategoryMap.clear();
    }

    public static List<Medicine> getAllMedicines() {
        return medicinesByName.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static boolean issueMedicine(String drugName, int quantity) {
        List<Medicine> batches = medicinesByName.getOrDefault(drugName, new ArrayList<>()).stream()
                .sorted(Comparator.comparing(Medicine::getExpiryDate))
                .collect(Collectors.toList());

        if (batches.stream().mapToInt(Medicine::getStock).sum() < quantity) {
            return false; // Stok total tidak mencukupi
        }

        int remainingToIssue = quantity;
        for (Medicine batch : batches) {
            if (remainingToIssue <= 0) break;
            int issueFromThisBatch = Math.min(batch.getStock(), remainingToIssue);
            batch.setStock(batch.getStock() - issueFromThisBatch);
            remainingToIssue -= issueFromThisBatch;
        }
        return true;
    }
}