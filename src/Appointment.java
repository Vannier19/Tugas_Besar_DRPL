package src;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Kelas entity untuk merepresentasikan data janji temu (Appointment).
 * Kelas ini menyimpan informasi terkait ID pasien, ID dokter, waktu, dan status janji temu.
 * Menghubungkan User (Dokter) dan Patient.
 */
public class Appointment {
    private String id;
    private String patientId;
    private String doctorId;
    private LocalDateTime appointmentDateTime;
    private String status; // e.g., "Scheduled", "Completed", "Cancelled"

    public Appointment(String id, String patientId, String doctorId, LocalDateTime appointmentDateTime) {
        if (id == null || id.isEmpty() || patientId == null || patientId.isEmpty() || doctorId == null || doctorId.isEmpty() || appointmentDateTime == null) {
            throw new IllegalArgumentException("ID, Patient ID, Doctor ID, and date/time cannot be empty.");
        }
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = "Scheduled"; // Default status
    }

    // Getters
    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public String getStatus() { return status; }

    // Setters
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Appointment{" +
                "id='" + id + '\'' +
                ", patientId='" + patientId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", dateTime=" + appointmentDateTime.format(formatter) +
                ", status='" + status + '\'' +
                '}';
    }
}