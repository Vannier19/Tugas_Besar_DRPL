package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class GivePrescriptionController {

    // ComboBox pasien diganti menjadi Label
    @FXML private Label currentPatientLabel;
    @FXML private VBox prescriptionFormContainer;
    @FXML private ComboBox<Medicine> medicineComboBox;
    @FXML private TextField dosageField;
    @FXML private TextField quantityField;
    @FXML private TextArea notesTextArea;

    private User loggedInDoctor;
    private AppointmentFactory appointmentFactory;
    private PatientFactory patientFactory;
    private MedicineFactory medicineFactory;
    private PrescriptionFactory prescriptionFactory;
    private Patient activePatient;

    @FXML
    public void initialize() {
        this.loggedInDoctor = SessionManager.getCurrentUser();
        this.appointmentFactory = new AppointmentFactory();
        this.patientFactory = new PatientFactory();
        this.medicineFactory = new MedicineFactory();
        this.prescriptionFactory = new PrescriptionFactory();

        loadCurrentPatient();
        loadMedicines();
    }

    private void loadCurrentPatient() {
        if (loggedInDoctor == null) return;

        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        Appointment currentAppointment = appointmentFactory.getAppointmentAt(now);

        if (currentAppointment != null && currentAppointment.getDoctorId().equals(loggedInDoctor.getId())) {
            Patient currentPatient = patientFactory.getPatientById(currentAppointment.getPatientId());
            if (currentPatient != null) {
                this.activePatient = currentPatient;
                currentPatientLabel.setText(currentPatient.toString());
                prescriptionFormContainer.setDisable(false);
            }
        } else {
            currentPatientLabel.setText("Tidak ada pasien dengan jadwal saat ini.");
            prescriptionFormContainer.setDisable(true);
        }
    }

    private void loadMedicines() {
        medicineComboBox.setItems(FXCollections.observableArrayList(medicineFactory.getAllMedicines()));
    }

    @FXML
    void handleSubmitPrescription(ActionEvent event) {
        Medicine selectedMedicine = medicineComboBox.getValue();
        String dosage = dosageField.getText().trim();
        String quantity = quantityField.getText().trim();
        if (activePatient == null) {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.WARNING, "Peringatan", "Tidak Ada Pasien Aktif", "Tidak dapat membuat resep karena tidak ada pasien yang memiliki jadwal saat ini.");
            return;
        }

        if (selectedMedicine == null || dosage.isEmpty() || quantity.isEmpty()) {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Input Tidak Lengkap", "Harap pilih obat, serta isi dosis dan kuantitas.");
            return;
        }

        try {
            prescriptionFactory.addPrescription(
                activePatient.getId(),
                loggedInDoctor.getId(),
                selectedMedicine.getName(),
                dosage,
                quantity,
                notesTextArea.getText().trim()
            );
            
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Sukses", "Resep Terkirim", "Resep baru telah berhasil dibuat.");
            clearForm();

        } catch (Exception e) {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Gagal Membuat Resep", e.getMessage());
        }
    }

    private void clearForm() {
        medicineComboBox.getSelectionModel().clearSelection();
        dosageField.clear();
        quantityField.clear();
        notesTextArea.clear();
    }
}