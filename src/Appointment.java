package src;

import java.util.Date;

/**
 * Kelas entity untuk merepresentasikan data janji temu (Appointment).
 * Kelas ini menyimpan informasi terkait ID pasien, ID dokter, waktu, dan status janji temu.
 * Sesuai dengan perancangan kelas pada dokumen DPPLOO-02.
 */
public class Appointment {
    private String id;
    private String patientId;
    private String doctorId;
    private Date appointmentDate;
    private String status; // Contoh: "Scheduled", "Completed", "Cancelled"

    public Appointment(String id, String patientId, String doctorId, Date appointmentDate) {
        if (id == null || id.isEmpty() || patientId == null || patientId.isEmpty() || doctorId == null || doctorId.isEmpty() || appointmentDate == null) {
            throw new IllegalArgumentException("ID, Patient ID, Doctor ID, dan tanggal tidak boleh kosong.");
        }
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.status = "Scheduled"; // Status default saat janji temu dibuat
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    // Setter methods
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", patientId='" + patientId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", status='" + status + '\'' +
                '}';
    }
}