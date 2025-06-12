package src.controllers;
import src.AppointmentFactory;
import src.PatientFactory;
import src.UserFactory;
import src.Appointment;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AppointmentController {
    private final AppointmentFactory appointmentFactory;
    private final PatientFactory patientFactory;
    private final UserFactory userFactory;
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    public AppointmentController() {
        this.appointmentFactory = new AppointmentFactory();
        this.patientFactory = new PatientFactory();
        this.userFactory = new UserFactory();
    }

    public String createAppointment(String patientId, String doctorId, Date date) {
        if (patientFactory.getAllPatients().stream().noneMatch(p -> p.getId().equals(patientId))) {
            throw new IllegalArgumentException("Pasien dengan ID " + patientId + " tidak ditemukan.");
        }
        if (userFactory.getAllUsers().stream().noneMatch(u -> u.getId().equals(doctorId) && u.getRole().equals("Dokter"))) {
            throw new IllegalArgumentException("Dokter dengan ID " + doctorId + " tidak ditemukan.");
        }

        String newId = "APP" + String.format("%03d", idCounter.getAndIncrement());
        Appointment appointment = new Appointment(newId, patientId, doctorId, date);
        appointmentFactory.addAppointment(appointment);

        return "Janji temu berhasil dibuat dengan ID: " + newId;
    }
    
    public List<Appointment> getAllAppointments() {
        return appointmentFactory.getAllAppointments();
    }
}