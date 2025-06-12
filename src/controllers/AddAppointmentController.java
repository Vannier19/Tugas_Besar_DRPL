package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import src.Appointment;
import src.AppointmentFactory;
import src.Patient;
import src.PatientFactory;
import src.User;
import src.UserFactory;
import src.utils.FXMLUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class AddAppointmentController {

    @FXML private TextField doctorIdField;
    @FXML private TextField patientIdField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeComboBox;

    private UserFactory userFactory;
    private PatientFactory patientFactory;
    private AppointmentFactory appointmentFactory;

    @FXML
    public void initialize() {
        userFactory = new UserFactory();
        patientFactory = new PatientFactory();
        appointmentFactory = new AppointmentFactory();
        ObservableList<String> timeSlots = FXCollections.observableArrayList();
        for (int hour = 9; hour <= 17; hour++) {
            timeSlots.add(String.format("%02d:00", hour));
        }
        timeComboBox.setItems(timeSlots);
    }

    @FXML
    private void handleAddAppointment(ActionEvent event) {
        String doctorId = doctorIdField.getText().trim();
        String patientId = patientIdField.getText().trim();
        LocalDate selectedDate = datePicker.getValue();
        String selectedTime = timeComboBox.getValue();

        if (doctorId.isEmpty() || patientId.isEmpty() || selectedDate == null || selectedTime == null) {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Input Tidak Lengkap", "Semua field harus diisi.");
            return;
        }

        User doctor = userFactory.getUserById(doctorId);
        if (doctor == null || !"Dokter".equals(doctor.getRole())) {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "ID Dokter Tidak Valid", "Tidak ditemukan dokter dengan ID tersebut.");
            return;
        }

        Patient patient = patientFactory.getPatientById(patientId);
        if (patient == null) {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "ID Pasien Tidak Valid", "Tidak ditemukan pasien dengan ID tersebut.");
            return;
        }

        try {
            LocalTime time = LocalTime.parse(selectedTime);
            LocalDateTime appointmentDateTime = LocalDateTime.of(selectedDate, time);

            String newAppointmentId = "APP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            Appointment newAppointment = new Appointment(newAppointmentId, patientId, doctorId, appointmentDateTime);
            appointmentFactory.addAppointment(newAppointment);
            
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Sukses", "Appointment Ditambahkan", "Jadwal appointment baru telah berhasil dibuat.");
            handleClearForm(null);
            
        } catch (Exception e) {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Gagal Menambahkan", e.getMessage());
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        doctorIdField.clear();
        patientIdField.clear();
        datePicker.setValue(null);
        timeComboBox.getSelectionModel().clearSelection();
    }
}