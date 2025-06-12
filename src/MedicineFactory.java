package src;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MedicineFactory {
    private static Map<String, List<Medicine>> medicinesByName = new HashMap<>();
    private static Map<String, String> drugCategoryMap = new HashMap<>(); 
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    static {
        if (medicinesByName.isEmpty()) { 
            addOrUpdateMedicineEntry("Paracetamol", "Obat Bebas", 25, new Date(System.currentTimeMillis() + 86400000L * 90), "PT. Kimia Farma"); 
            addOrUpdateMedicineEntry("Paracetamol", "Obat Bebas", 50, new Date(System.currentTimeMillis() + 86400000L * 180), "PT. Kalbe Farma");
            addOrUpdateMedicineEntry("Amoxicillin", "Antibiotik", 100, new Date(System.currentTimeMillis() + 86400000L * 60), " PT. Anugerah Argon Medica"); 
            addOrUpdateMedicineEntry("Amoxicillin", "Antibiotik", 75, new Date(System.currentTimeMillis() + 86400000L * 120), "PT. Penta Valent");
            addOrUpdateMedicineEntry("Vitamin C", "Vitamin", 10, new Date(System.currentTimeMillis() + 86400000L * 30), "PT. Kalbe Farma"); 
            addOrUpdateMedicineEntry("Ibuprofen", "Obat Bebas", 50, new Date(System.currentTimeMillis() + 86400000L * 30), "PharmaCo"); 
            addOrUpdateMedicineEntry("ExpiredDrug", "Obat Keras", 5, new Date(System.currentTimeMillis() - 86400000L * 10), "Bad Supplier"); 
        }
    }

    public MedicineFactory() {
        int maxIdNum = medicinesByName.values().stream()
                                  .flatMap(List::stream)
                                  .map(Medicine::getId)
                                  .filter(id -> id.startsWith("M"))
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

    private static void addOrUpdateMedicineEntry(String name, String category, int stock, Date expiryDate, String supplier) {
        String newId = "M" + String.format("%03d", idCounter.incrementAndGet());
        Medicine newMedicine = new Medicine(newId, name, category, stock, expiryDate, supplier);
        medicinesByName.computeIfAbsent(name, k -> new ArrayList<>()).add(newMedicine);
        drugCategoryMap.putIfAbsent(name, category); 
    }

    public Medicine addMedicine(String name, String category, int stock, Date expiryDate, String supplier) {
        if (drugCategoryMap.containsKey(name) && !drugCategoryMap.get(name).equalsIgnoreCase(category)) {
            throw new IllegalArgumentException("Kategori obat '" + name + "' harus konsisten. Kategori yang sudah ada: " + drugCategoryMap.get(name));
        }
        drugCategoryMap.putIfAbsent(name, category); 

        String newId = "M" + String.format("%03d", idCounter.incrementAndGet());
        Medicine newEntry = new Medicine(newId, name, category, stock, expiryDate, supplier);
        medicinesByName.computeIfAbsent(name, k -> new ArrayList<>()).add(newEntry);
        return newEntry;
    }

    public List<Medicine> getAllMedicines() {
        return medicinesByName.values().stream()
                .flatMap(List::stream) 
                .collect(Collectors.toList());
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

    public boolean issueMedicine(String drugName, int quantity) {
        List<Medicine> availableBatches = getMedicinesByNameSortedByExpiry(drugName); 
        if (availableBatches.isEmpty()) {
            return false; 
        }

        int remainingToIssue = quantity;
        for (Medicine batch : availableBatches) {
            if (remainingToIssue <= 0) break;

            if (batch.getStock() >= remainingToIssue) {
                batch.setStock(batch.getStock() - remainingToIssue); 
                remainingToIssue = 0;
            } else {
                remainingToIssue -= batch.getStock(); 
                batch.setStock(0); 
            }
        }

        List<Medicine> updatedBatches = availableBatches.stream()
                                            .filter(m -> m.getStock() > 0)
                                            .collect(Collectors.toList());
        medicinesByName.put(drugName, updatedBatches);

        if (updatedBatches.isEmpty()) {
            drugCategoryMap.remove(drugName);
        }

        return remainingToIssue == 0; 
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
}
