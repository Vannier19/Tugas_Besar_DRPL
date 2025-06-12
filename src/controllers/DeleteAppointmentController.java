package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import src.Appointment;
import src.AppointmentFactory;
import src.utils.FXMLUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class DeleteAppointmentController {

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, String> idColumn;
    @FXML private TableColumn<Appointment, String> doctorIdColumn;
    @FXML private TableColumn<Appointment, String> patientIdColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> dateTimeColumn;
    @FXML private Button deleteButton;

    private AppointmentFactory appointmentFactory;
    private ObservableList<Appointment> appointmentList;

    @FXML
    public void initialize() {
        appointmentFactory = new AppointmentFactory();
        appointmentList = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        doctorIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDoctorId()));
        patientIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatientId()));
        dateTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAppointmentDateTime()));
        formatDateTimeColumn();
        loadAppointmentData();
    }

    private void formatDateTimeColumn() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        dateTimeColumn.setCellFactory(column -> {
            return new TableCell<Appointment, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(formatter.format(item));
                    }
                }
            };
        });
    }


    private void loadAppointmentData() {
        appointmentList.clear();
        appointmentList.addAll(appointmentFactory.getAllAppointments());
        appointmentTable.setItems(appointmentList);
    }

    @FXML
    private void handleDeleteAppointment(ActionEvent event) {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            FXMLUtils.showAlert(Alert.AlertType.WARNING, "Peringatan", "Tidak Ada Pilihan", "Silakan pilih appointment yang ingin dihapus dari tabel.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Hapus");
        confirmationAlert.setHeaderText("Hapus Appointment ID: " + selectedAppointment.getId());
        confirmationAlert.setContentText("Apakah Anda yakin ingin menghapus jadwal ini?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            appointmentFactory.deleteAppointment(selectedAppointment.getId());
            
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Hapus Berhasil", "Jadwal appointment telah berhasil dihapus.");

            loadAppointmentData();
        }
    }
}