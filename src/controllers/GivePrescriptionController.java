package src.controllers;

import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import src.*;
import src.utils.FXMLUtils;
import src.utils.SessionManager;

public class GivePrescriptionController {

    @FXML private ComboBox<Patient> patientComboBox;
    @FXML private ComboBox<Medicine> medicineComboBox;
    @FXML private TextField dosageField;
    @FXML private TextField quantityField;
    @FXML private TextArea notesTextArea;
    @FXML private Button submitPrescriptionButton;

    private User loggedInDoctor;
    private AppointmentFactory appointmentFactory;
    private PatientFactory patientFactory;
    private MedicineFactory medicineFactory;
    private PrescriptionFactory prescriptionFactory;

    @FXML
    public void initialize() {
        this.loggedInDoctor = SessionManager.getCurrentUser();
        this.appointmentFactory = new AppointmentFactory();
        this.patientFactory = new PatientFactory();
        this.medicineFactory = new MedicineFactory();
        this.prescriptionFactory = new PrescriptionFactory();

        loadPatients();
        loadMedicines();
    }

    private void loadPatients() {
        if (loggedInDoctor == null) return;
        List<String> patientIds = appointmentFactory.getAllAppointments().stream()
                .filter(app -> app.getDoctorId().equals(loggedInDoctor.getId()))
                .map(Appointment::getPatientId)
                .distinct()
                .collect(Collectors.toList());
        List<Patient> doctorPatients = patientIds.stream()
                .map(id -> patientFactory.getPatientById(id))
                .collect(Collectors.toList());
        patientComboBox.setItems(FXCollections.observableArrayList(doctorPatients));
    }

    private void loadMedicines() {
        medicineComboBox.setItems(FXCollections.observableArrayList(medicineFactory.getAllMedicines()));
    }

    @FXML
    void handleSubmitPrescription(ActionEvent event) {
        Patient selectedPatient = patientComboBox.getValue();
        Medicine selectedMedicine = medicineComboBox.getValue();
        String dosage = dosageField.getText().trim();
        String quantity = quantityField.getText().trim();

        if (selectedPatient == null || selectedMedicine == null || dosage.isEmpty() || quantity.isEmpty()) {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Input Tidak Lengkap", "Harap pilih pasien, obat, serta isi dosis dan kuantitas.");
            return;
        }

        try {
            // Memanggil factory untuk menambahkan resep.
            // Factory yang akan mengurus pembuatan ID dan objek.
            prescriptionFactory.addPrescription(
                selectedPatient.getId(),
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
        patientComboBox.getSelectionModel().clearSelection();
        medicineComboBox.getSelectionModel().clearSelection();
        dosageField.clear();
        quantityField.clear();
        notesTextArea.clear();
    }
}