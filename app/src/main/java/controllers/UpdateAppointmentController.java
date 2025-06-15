package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class UpdateAppointmentController {

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, String> idColumn;
    @FXML private TableColumn<Appointment, String> doctorIdColumn;
    @FXML private TableColumn<Appointment, String> patientIdColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> dateTimeColumn;
    
    @FXML private VBox editFormVBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeComboBox;

    private AppointmentFactory appointmentFactory;
    private ObservableList<Appointment> appointmentList;

    @FXML
    public void initialize() {
        appointmentFactory = new AppointmentFactory();
        appointmentList = FXCollections.observableArrayList();
        
        setupTableColumns();
        loadAppointmentData();

        ObservableList<String> timeSlots = FXCollections.observableArrayList();
        for (int hour = 9; hour <= 17; hour++) {
            timeSlots.add(String.format("%02d:00", hour));
        }
        timeComboBox.setItems(timeSlots);

        appointmentTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateEditForm(newSelection);
                } else {
                    clearEditForm();
                }
            }
        );
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        doctorIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDoctorId()));
        patientIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatientId()));
        dateTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAppointmentDateTime()));
        formatDateTimeColumn();
    }

    private void formatDateTimeColumn() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        dateTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });
    }

    private void loadAppointmentData() {
        appointmentList.setAll(appointmentFactory.getAllAppointments());
        appointmentTable.setItems(appointmentList);
    }

    private void populateEditForm(Appointment appointment) {
        editFormVBox.setDisable(false);
        datePicker.setValue(appointment.getAppointmentDateTime().toLocalDate());
        timeComboBox.setValue(String.format("%02d:00", appointment.getAppointmentDateTime().getHour()));
    }

    private void clearEditForm() {
        editFormVBox.setDisable(true);
        datePicker.setValue(null);
        timeComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleUpdateAppointment(ActionEvent event) {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            FXMLUtils.showAlert(Alert.AlertType.WARNING, "Peringatan", "Tidak Ada Pilihan", "Silakan pilih appointment dari tabel terlebih dahulu.");
            return;
        }

        LocalDate newDate = datePicker.getValue();
        String newTimeStr = timeComboBox.getValue();

        if (newDate == null || newTimeStr == null) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Input Tidak Lengkap", "Tanggal dan waktu baru harus diisi.");
            return;
        }

        LocalTime newTime = LocalTime.parse(newTimeStr);
        LocalDateTime newDateTime = LocalDateTime.of(newDate, newTime);

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Update");
        confirmationAlert.setHeaderText("Ubah Jadwal ID: " + selectedAppointment.getId());
        confirmationAlert.setContentText("Anda yakin ingin mengubah jadwal ke " + newDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            appointmentFactory.updateAppointment(selectedAppointment.getId(), newDateTime);
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Update Berhasil", "Jadwal appointment telah berhasil diperbarui.");
            loadAppointmentData();
            appointmentTable.getSelectionModel().clearSelection();
        }
    }
}