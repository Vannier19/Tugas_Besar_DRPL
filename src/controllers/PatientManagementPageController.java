package src.controllers;

import src.Patient;
import src.controllers.PatientManagementController;
import src.utils.FXMLUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class PatientManagementPageController {

    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, String> idColumn;
    @FXML private TableColumn<Patient, String> nameColumn;
    @FXML private TableColumn<Patient, Integer> ageColumn;

    private PatientManagementController patientManagementController;
    private ObservableList<Patient> patientList;

    @FXML
    public void initialize() {
        patientManagementController = new PatientManagementController();
        patientList = FXCollections.observableArrayList();

        // Set up table columns to bind to Patient properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        // Load initial data
        loadPatientData();

        // Add listener for table row selection
        patientTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showPatientDetails(newValue));
    }

    private void loadPatientData() {
        patientList.clear();
        patientList.addAll(patientManagementController.retrieveAllPatients());
        patientTable.setItems(patientList);
    }

    private void showPatientDetails(Patient patient) {
        if (patient != null) {
            nameField.setText(patient.getName());
            ageField.setText(String.valueOf(patient.getAge()));
            // ID field is usually not editable, but can be displayed if needed.
            // For now, we use selected item's ID for update/delete.
        } else {
            clearForm();
        }
    }

    @FXML
    private void handleAddPatient() {
        try {
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());

            String message = patientManagementController.addNewPatient(name, age);
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Tambah Pasien", message);
            loadPatientData(); // Refresh table
            clearForm();
        } catch (NumberFormatException e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Input Error", "Usia Tidak Valid", "Usia harus berupa angka.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Tambah Pasien Gagal", e.getMessage());
        }
    }

    @FXML
    private void handleUpdatePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            FXMLUtils.showAlert(Alert.AlertType.WARNING, "Pilih Pasien", "Tidak Ada Pasien Terpilih", "Pilih pasien dari tabel untuk diperbarui.");
            return;
        }

        try {
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());

            String message = patientManagementController.updateExistingPatient(selectedPatient.getId(), name, age);
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Perbarui Pasien", message);
            loadPatientData(); // Refresh table
            clearForm();
        } catch (NumberFormatException e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Input Error", "Usia Tidak Valid", "Usia harus berupa angka.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Perbarui Pasien Gagal", e.getMessage());
        }
    }

    @FXML
    private void handleDeletePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            FXMLUtils.showAlert(Alert.AlertType.WARNING, "Pilih Pasien", "Tidak Ada Pasien Terpilih", "Pilih pasien dari tabel untuk dihapus.");
            return;
        }

        try {
            String message = patientManagementController.deletePatient(selectedPatient.getId());
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Hapus Pasien", message);
            loadPatientData(); // Refresh table
            clearForm();
        } catch (IllegalArgumentException e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Hapus Pasien Gagal", e.getMessage());
        }
    }

    @FXML
    private void handleClearForm() {
        clearForm();
        patientTable.getSelectionModel().clearSelection(); // Deselect any selected row
    }

    private void clearForm() {
        nameField.clear();
        ageField.clear();
    }
}