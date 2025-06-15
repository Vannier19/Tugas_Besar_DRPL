package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PatientMedicalRecordController {

    @FXML private Label currentPatientLabel;
    @FXML private VBox recordDisplayContainer;
    @FXML private Label historyLabel;
    @FXML private VBox recordsVBox;
    @FXML private TextArea newDiagnosisTextArea;
    @FXML private Button addRecordButton;

    private User loggedInDoctor;
    private AppointmentFactory appointmentFactory;
    private PatientFactory patientFactory;
    private MedicalRecordFactory medicalRecordFactory;
    private Patient activePatient;

    @FXML
    public void initialize() {
        this.loggedInDoctor = SessionManager.getCurrentUser();
        this.appointmentFactory = new AppointmentFactory();
        this.patientFactory = new PatientFactory();
        this.medicalRecordFactory = new MedicalRecordFactory();
        loadCurrentAppointmentPatient();
    }

    private void loadCurrentAppointmentPatient() {
        if (loggedInDoctor == null) return;
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        Appointment currentAppointment = appointmentFactory.getAppointmentAt(now);

        if (currentAppointment != null && currentAppointment.getDoctorId().equals(loggedInDoctor.getId())) {
            Patient currentPatient = patientFactory.getPatientById(currentAppointment.getPatientId());
            if (currentPatient != null) {
                this.activePatient = currentPatient;
                currentPatientLabel.setText(currentPatient.toString());
                recordDisplayContainer.setVisible(true);
                recordDisplayContainer.setManaged(true);
                displayMedicalRecords(currentPatient);
            }
        } else {
            recordDisplayContainer.setVisible(false);
            recordDisplayContainer.setManaged(false);
            currentPatientLabel.setText("Tidak ada pasien dengan jadwal saat ini...");
        }
    }

    private void displayMedicalRecords(Patient patient) {
        recordsVBox.getChildren().clear();

        List<MedicalRecord> records = medicalRecordFactory.getRecordsByPatientId(patient.getId());

        List<MedicalRecord> recentRecords = records.stream()
                .sorted(Comparator.comparing(MedicalRecord::getDate).reversed())
                .limit(3)
                .collect(Collectors.toList());

        if (recentRecords.isEmpty()) {
            recordsVBox.getChildren().add(new Label("Belum ada rekam medis untuk pasien ini."));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
            for (MedicalRecord record : recentRecords) {
                VBox recordEntry = new VBox(5);
                recordEntry.setPadding(new Insets(10));
                recordEntry.setStyle("-fx-border-color: #DDDDDD; -fx-border-radius: 5;");
                
                Label dateLabel = new Label(sdf.format(record.getDate()));
                dateLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
                
                Label diagnosisLabel = new Label(record.getDiagnosis());
                diagnosisLabel.setWrapText(true);

                recordEntry.getChildren().addAll(dateLabel, diagnosisLabel);
                recordsVBox.getChildren().add(recordEntry);
            }
        }
    }

    @FXML
    private void handleAddRecord(ActionEvent event) {
        String newDiagnosis = newDiagnosisTextArea.getText().trim();

        if (activePatient == null) {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.WARNING, "Peringatan", "Tidak Ada Pasien Aktif", "Tidak ada pasien yang memiliki jadwal saat ini.");
            return;
        }

        if (newDiagnosis.isEmpty()) {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.WARNING, "Peringatan", "Input Kosong", "Kolom diagnosis tidak boleh kosong.");
            return;
        }
        
        // Memanggil factory untuk menambahkan record. Factory yang akan mengurus ID.
        MedicalRecordFactory.addMedicalRecord(activePatient.getId(), newDiagnosis, new Date());

        FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Sukses", "Data Tersimpan", "Rekam medis baru telah berhasil ditambahkan.");
        
        displayMedicalRecords(activePatient);
        newDiagnosisTextArea.clear();
    }
}