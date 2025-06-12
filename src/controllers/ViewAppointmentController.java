package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import src.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class ViewAppointmentController {

    @FXML
    private GridPane scheduleGrid;

    @FXML
    private ComboBox<User> doctorSelectorComboBox;

    private AppointmentFactory appointmentFactory;
    private UserFactory userFactory;
    private PatientFactory patientFactory;

    @FXML
    public void initialize() {
        appointmentFactory = new AppointmentFactory();
        userFactory = new UserFactory();
        patientFactory = new PatientFactory();

        // 1. Mengisi dropdown dengan data dokter
        ObservableList<User> doctors = FXCollections.observableArrayList(
            userFactory.getAllUsers().stream()
                .filter(user -> "Dokter".equals(user.getRole()))
                .collect(Collectors.toList())
        );
        doctorSelectorComboBox.setItems(doctors);

        // 2. Menambahkan listener ke dropdown
        //    Setiap kali user memilih dokter, jadwal akan diperbarui
        doctorSelectorComboBox.valueProperty().addListener((obs, oldDoctor, newDoctor) -> {
            if (newDoctor != null) {
                populateScheduleGrid(newDoctor);
            }
        });
        setupGridPane();
    }

    private void setupGridPane() {
        scheduleGrid.getChildren().clear();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE, MMM d");

        // Membuat header hari (Kolom)
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            Label dayLabel = new Label(date.format(dayFormatter));
            dayLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            dayLabel.setPadding(new Insets(5));
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setStyle("-fx-alignment: center;");
            scheduleGrid.add(dayLabel, i + 1, 0);
        }

        // Membuat header jam (Baris)
        for (int i = 9; i <= 17; i++) {
            LocalTime time = LocalTime.of(i, 0);
            Label timeLabel = new Label(time.toString());
            timeLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            timeLabel.setPadding(new Insets(5));
            scheduleGrid.add(timeLabel, 0, i - 8);
        }

        for (int day = 0; day < 7; day++) {
            for (int hour = 9; hour <= 17; hour++) {
                LocalDateTime slotDateTime = today.plusDays(day).atTime(hour, 0);
                Appointment appointment = appointmentFactory.getAppointmentAt(slotDateTime);

                VBox cellBox = new VBox();
                cellBox.setPadding(new Insets(4));
                GridPane.setVgrow(cellBox, Priority.SOMETIMES);
                GridPane.setHgrow(cellBox, Priority.SOMETIMES);
                scheduleGrid.add(cellBox, day + 1, hour - 8);
            }
        }
    }

    private void populateScheduleGrid(User selectedDoctor) {
        scheduleGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) > 0 && GridPane.getColumnIndex(node) > 0);

        LocalDate today = LocalDate.now();

        for (int day = 0; day < 7; day++) {
            for (int hour = 9; hour <= 17; hour++) {
                LocalDateTime slotDateTime = today.plusDays(day).atTime(hour, 0);
                Appointment appointment = appointmentFactory.getAppointmentAt(slotDateTime);

                VBox cellBox = new VBox();
                cellBox.setPadding(new Insets(4));
                GridPane.setVgrow(cellBox, Priority.SOMETIMES);
                GridPane.setHgrow(cellBox, Priority.SOMETIMES);
                
                if (appointment != null && appointment.getDoctorId().equals(selectedDoctor.getId())) {
                    Patient patient = patientFactory.getPatientById(appointment.getPatientId());
                    
                    cellBox.setStyle("-fx-background-color: #FFD2D2; -fx-border-color: #E0E0E0;");
                    Label doctorLabel = new Label("Booked");
                    Label patientLabel = new Label("Patient: " + (patient != null ? patient.getName() : "N/A"));
                    doctorLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
                    patientLabel.setFont(Font.font("System", 11));
                    cellBox.getChildren().addAll(doctorLabel, patientLabel);
                } else {
                    cellBox.setStyle("-fx-background-color: #D4F4DD; -fx-border-color: #E0E0E0;");
                    Label availableLabel = new Label("Available");
                    availableLabel.setStyle("-fx-text-fill: #006400;");
                    cellBox.getChildren().add(availableLabel);
                }
                scheduleGrid.add(cellBox, day + 1, hour - 8);
            }
        }
    }
}