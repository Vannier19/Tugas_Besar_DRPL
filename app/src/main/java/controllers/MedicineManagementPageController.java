package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox; 
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent; 
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator; 
import java.util.List;
import java.util.stream.Collectors;

public class MedicineManagementPageController {

    @FXML private TableView<Medicine> inventoryTable;
    @FXML private TableColumn<Medicine, String> idColumn;
    @FXML private TableColumn<Medicine, String> nameColumn;
    @FXML private TableColumn<Medicine, String> categoryColumn; 
    @FXML private TableColumn<Medicine, Integer> stockColumn;
    @FXML private TableColumn<Medicine, Date> expiryDateColumn;
    @FXML private TableColumn<Medicine, String> supplierColumn; 

    @FXML private TitledPane addDrugPane;
    @FXML private TextField newDrugNameField;
    @FXML private TextField newDrugQuantityField;
    @FXML private ComboBox<String> newDrugCategoryComboBox; 
    @FXML private DatePicker newDrugExpiryDatePicker;
    @FXML private TextField newDrugSupplierField;

    @FXML private TitledPane recordDrugExitPane;
    @FXML private TextField exitDrugNameField;
    @FXML private TextField exitQuantityIssuedField;
    @FXML private TextField exitPatientIdField; 

    private MedicineFactory medicineFactory;
    private PatientFactory patientFactory; 
    private ObservableList<Medicine> medicineList;
    private ApothecaryDashboardController parentDashboardController;

    public MedicineManagementPageController() {
        this.medicineFactory = new MedicineFactory();
        this.patientFactory = new PatientFactory(); 
    }

    public void setParentDashboardController(ApothecaryDashboardController controller) {
        this.parentDashboardController = controller;
    }

    @FXML
    public void initialize() {
        medicineList = FXCollections.observableArrayList();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category")); 
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier")); 

        newDrugCategoryComboBox.getItems().addAll("Obat Keras", "Obat Bebas", "Antibiotik", "Vitamin");

        loadMedicineData();

        addDrugPane.expandedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                recordDrugExitPane.setExpanded(false);
            }
        });
        recordDrugExitPane.expandedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                addDrugPane.setExpanded(false);
            }
        });
        
        addDrugPane.setExpanded(false);
        recordDrugExitPane.setExpanded(false);

        newDrugNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String drugName = newValue.trim();
                String existingCategory = medicineFactory.getCategoryForDrugName(drugName);
                if (existingCategory != null && !existingCategory.isEmpty()) {
                    newDrugCategoryComboBox.setValue(existingCategory);
                    newDrugCategoryComboBox.setDisable(true); 
                } else {
                    newDrugCategoryComboBox.getSelectionModel().clearSelection();
                    newDrugCategoryComboBox.setDisable(false);
                }
            }
        });
    }

    private void loadMedicineData() {
        medicineFactory.removeExpiredMedicines();
        medicineList.clear();
        medicineList.addAll(medicineFactory.getAllMedicines().stream()
                            .sorted(Comparator.comparing(Medicine::getExpiryDate)
                                      .thenComparing(Medicine::getName))
                            .collect(Collectors.toList()));
        inventoryTable.setItems(medicineList);
    }

    @FXML
    private void handleAddNewDrug() {
        try {
            String name = newDrugNameField.getText().trim();
            int stock = Integer.parseInt(newDrugQuantityField.getText().trim());
            String category = newDrugCategoryComboBox.getValue();
            LocalDate expiryLocalDate = newDrugExpiryDatePicker.getValue();
            String supplier = newDrugSupplierField.getText().trim();

            if (name.isEmpty() || category == null || category.isEmpty() || expiryLocalDate == null || supplier.isEmpty()) {
                FXMLUtils.showAlert(Alert.AlertType.WARNING, "Input Error", "Data Tidak Lengkap", "Harap lengkapi semua field untuk menambah obat baru.");
                return;
            }
            if (stock <= 0) {
                FXMLUtils.showAlert(Alert.AlertType.WARNING, "Input Error", "Kuantitas Tidak Valid", "Kuantitas obat harus lebih dari 0.");
                return;
            }

            Date expiryDate = Date.from(expiryLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            medicineFactory.addMedicine(name, category, stock, expiryDate, supplier);

            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Obat Ditambahkan", "Batch obat '" + name + "' dari supplier '" + supplier + "' berhasil ditambahkan.");
            loadMedicineData(); 
            clearAddDrugForm(); 
        } catch (NumberFormatException e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Input Error", "Kuantitas Tidak Valid", "Kuantitas harus berupa angka.");
        } catch (IllegalArgumentException e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Tambah Obat Gagal", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Tambah Obat Gagal", e.getMessage());
        }
    }

    @FXML
    private void handleRecordDrugExit() {
        try {
            String drugName = exitDrugNameField.getText().trim();
            int quantityIssued = Integer.parseInt(exitQuantityIssuedField.getText().trim());
            String patientId = exitPatientIdField.getText().trim();

            if (drugName.isEmpty() || patientId.isEmpty()) {
                FXMLUtils.showAlert(Alert.AlertType.WARNING, "Input Error", "Data Tidak Lengkap", "Harap lengkapi nama obat, kuantitas, dan ID pasien.");
                return;
            }
            if (quantityIssued <= 0) {
                FXMLUtils.showAlert(Alert.AlertType.WARNING, "Input Error", "Kuantitas Tidak Valid", "Kuantitas yang dikeluarkan harus lebih dari 0.");
                return;
            }

            if (patientFactory.getPatientById(patientId) == null) {
                FXMLUtils.showAlert(Alert.AlertType.WARNING, "Validasi Gagal", "ID Pasien Tidak Ditemukan", "Pasien dengan ID " + patientId + " tidak ditemukan.");
                return;
            }

            List<Medicine> allAvailableBatchesForDrug = medicineFactory.getMedicinesByNameSortedByExpiry(drugName);
            int totalStockForDrug = allAvailableBatchesForDrug.stream().mapToInt(Medicine::getStock).sum();

            if (totalStockForDrug < quantityIssued) {
                FXMLUtils.showAlert(Alert.AlertType.WARNING, "Stok Tidak Cukup", "Stok Kurang", "Stok obat " + drugName + " tidak cukup untuk mengeluarkan " + quantityIssued + " unit. Total stok tersedia: " + totalStockForDrug);
                return;
            }

            boolean issued = medicineFactory.issueMedicine(drugName, quantityIssued);

            if (issued) {
                FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Obat Dikeluarkan", quantityIssued + " unit " + drugName + " dikeluarkan untuk pasien " + patientId + ".");
                loadMedicineData(); 
                clearRecordDrugExitForm(); 
            } else {
                FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error Internal", "Pencatatan Keluar Obat Gagal", "Terjadi masalah saat mengeluarkan obat. Silakan coba lagi.");
            }
        } catch (NumberFormatException e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Input Error", "Kuantitas Tidak Valid", "Kuantitas harus berupa angka.");
        } catch (Exception e) {
            e.printStackTrace();
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Pencatatan Keluar Obat Gagal", e.getMessage());
        }
    }

    private void clearAddDrugForm() {
        newDrugNameField.clear();
        newDrugQuantityField.clear();
        newDrugCategoryComboBox.getSelectionModel().clearSelection();
        newDrugCategoryComboBox.setDisable(false);
        newDrugExpiryDatePicker.setValue(null);
        newDrugSupplierField.clear();
    }

    private void clearRecordDrugExitForm() {
        exitDrugNameField.clear();
        exitQuantityIssuedField.clear();
        exitPatientIdField.clear();
    }

    @FXML
    private void handleClearAddDrugForm(ActionEvent event) {
        clearAddDrugForm();
    }

    @FXML
    private void handleClearRecordDrugExitForm(ActionEvent event) {
        clearRecordDrugExitForm();
    }

    @FXML
    private void handleBackToMainMenu() {
        if (parentDashboardController != null) {
            parentDashboardController.showApothecaryWelcomeView();
        } else {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Navigasi Gagal", "Could not find parent dashboard controller.");
        }
    }
}
