package src;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppointmentFactory {
    private static final Map<String, Appointment> appointments = new HashMap<>();

    static {
        String doctorId1 = "U001";
        String patientId1 = "P001";
        String patientId2 = "P002";

        LocalDate today = LocalDate.now();

        appointments.put("APP001", new Appointment("APP001", patientId1, doctorId1, today.atTime(9, 0)));
        appointments.put("APP002", new Appointment("APP002", patientId2, doctorId1, today.plusDays(1).atTime(11, 0)));
        appointments.put("APP003", new Appointment("APP003", patientId1, doctorId1, today.plusDays(2).atTime(14, 0)));

    }

    public AppointmentFactory() {}

    public void addAppointment(Appointment appointment) {
        appointments.put(appointment.getId(), appointment);
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }

    public Appointment getAppointmentAt(LocalDateTime dateTime) {
        for (Appointment app : appointments.values()) {
            if (app.getAppointmentDateTime().isEqual(dateTime)) {
                return app;
            }
        }
        return null;
    }

    public List<Appointment> getAppointmentsInRange(LocalDateTime start, LocalDateTime end) {
        return appointments.values().stream()
                .filter(app -> !app.getAppointmentDateTime().isBefore(start) && !app.getAppointmentDateTime().isAfter(end))
                .collect(Collectors.toList());
    }

    public Appointment getAppointmentById(String id) {
        return appointments.get(id);
    }

    public void deleteAppointment(String appointmentId) {
        appointments.remove(appointmentId);
    }

    public void updateAppointment(String appointmentId, LocalDateTime newDateTime) {
        Appointment appointmentToUpdate = appointments.get(appointmentId);
        if (appointmentToUpdate != null) {
            appointmentToUpdate.setAppointmentDate(newDateTime);
        }
    }


}