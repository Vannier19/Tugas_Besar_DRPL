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

    public List<Medicine> getMedicinesByNameSortedByExpiry(String drugName) {
        return medicinesByName.getOrDefault(drugName, new ArrayList<>()).stream()
                .filter(m -> m.getStock() > 0) // Only consider available stock
                .sorted(Comparator.comparing(Medicine::getExpiryDate)) // Earliest expiry first (ascending order)
                .collect(Collectors.toList());
    }

    public String getCategoryForDrugName(String drugName) {
        return drugCategoryMap.get(drugName);
    }

    public void removeExpiredMedicines() {
        Date now = new Date();
        List<String> drugNamesToRemove = new ArrayList<>();

        medicinesByName.forEach((drugName, batches) -> {
            List<Medicine> nonExpiredBatches = batches.stream()
                                                .filter(m -> m.getExpiryDate().after(now))
                                                .collect(Collectors.toList());
            if (nonExpiredBatches.isEmpty()) {
                drugNamesToRemove.add(drugName); 
            } else {
                medicinesByName.put(drugName, nonExpiredBatches);
            }
        });

        drugNamesToRemove.forEach(drugName -> {
            medicinesByName.remove(drugName);
            drugCategoryMap.remove(drugName);
        });
    }
    public static Medicine getMedicineById(String id) {
    // Iterasi melalui setiap 'List<Medicine>' yang ada di dalam map
        for (List<Medicine> medicineList : medicinesByName.values()) {
            // Iterasi melalui setiap objek 'Medicine' di dalam list tersebut
            for (Medicine medicine : medicineList) {
                // Periksa apakah ID dari obat saat ini cocok dengan ID yang dicari
                if (medicine.getId().equals(id)) {
                    // Jika cocok, kembalikan objek obat tersebut
                    return medicine;
                }
            }
        }
        // Jika semua loop selesai dan tidak ada obat yang cocok, kembalikan null
        return null;
    }
}