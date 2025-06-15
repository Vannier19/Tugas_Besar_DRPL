package main.java.moduls;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Factory untuk mengelola objek Appointment menggunakan method static.
 */
public class AppointmentFactory {
    private static final Map<String, Appointment> appointments = new HashMap<>();
    private static int idCounter = 1;

    static {
        // Data awal bisa ditambahkan di sini jika perlu
    }

    public AppointmentFactory() {} // Constructor private

    public static Appointment createAppointment(String patientId, String doctorId, LocalDateTime dateTime) {
        String id = "APP" + String.format("%03d", idCounter++);
        Appointment appointment = new Appointment(id, patientId, doctorId, dateTime);
        appointments.put(id, appointment);
        return appointment;
    }
    
    public static void addAppointment(Appointment appointment) {
        if (!appointments.containsKey(appointment.getId())) {
            appointments.put(appointment.getId(), appointment);
        }
    }

    public static List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }
    
    public static List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointments.values().stream()
                .filter(app -> app.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    public static Appointment getAppointmentById(String id) {
        return appointments.get(id);
    }

    public static void deleteAppointment(String appointmentId) {
        appointments.remove(appointmentId);
    }

    public static void updateAppointment(String appointmentId, LocalDateTime newDateTime) {
        Appointment appointmentToUpdate = appointments.get(appointmentId);
        if (appointmentToUpdate != null) {
            appointmentToUpdate.setAppointmentDate(newDateTime);
        }
    }
}