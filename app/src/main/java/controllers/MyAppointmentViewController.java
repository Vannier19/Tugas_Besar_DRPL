package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MyAppointmentViewController {

    @FXML
    private GridPane scheduleGrid;

    private AppointmentFactory appointmentFactory;
    private PatientFactory patientFactory;
    private User loggedInDoctor;

    @FXML
    public void initialize() {
        this.appointmentFactory = new AppointmentFactory();
        this.patientFactory = new PatientFactory();
        this.loggedInDoctor = SessionManager.getCurrentUser();

        if (loggedInDoctor != null) {
            setupGridHeaders();
            populateScheduleGrid(loggedInDoctor);
        } else {
            scheduleGrid.add(new Label("Tidak bisa memuat jadwal, user tidak ditemukan."), 1, 1);
        }
    }

    private void setupGridHeaders() {
        scheduleGrid.getChildren().clear();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE, MMM d");
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            Label dayLabel = new Label(date.format(dayFormatter));
            dayLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            dayLabel.setPadding(new Insets(5));
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setStyle("-fx-alignment: center;");
            scheduleGrid.add(dayLabel, i + 1, 0);
        }
        for (int i = 9; i <= 17; i++) {
            LocalTime time = LocalTime.of(i, 0);
            Label timeLabel = new Label(time.toString());
            timeLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            timeLabel.setPadding(new Insets(5));
            scheduleGrid.add(timeLabel, 0, i - 8);
        }
    }

    private void populateScheduleGrid(User currentDoctor) {
        LocalDate today = LocalDate.now();

        for (int day = 0; day < 7; day++) {
            for (int hour = 9; hour <= 17; hour++) {
                LocalDateTime slotDateTime = today.plusDays(day).atTime(hour, 0);
                Appointment appointment = appointmentFactory.getAppointmentAt(slotDateTime);

                VBox cellBox = new VBox();
                cellBox.setPadding(new Insets(4));
                cellBox.setStyle("-fx-border-color: #E0E0E0;");

                if (appointment != null && appointment.getDoctorId().equals(currentDoctor.getId())) {
                    Patient patient = patientFactory.getPatientById(appointment.getPatientId());
                    
                    cellBox.setStyle("-fx-background-color: #FFD2D2; -fx-border-color: #E0E0E0;");
                    Label statusLabel = new Label("Booked");
                    Label patientLabel = new Label("Pasien: " + (patient != null ? patient.getName() : "N/A"));
                    statusLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
                    patientLabel.setFont(Font.font("System", 11));
                    cellBox.getChildren().addAll(statusLabel, patientLabel);
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