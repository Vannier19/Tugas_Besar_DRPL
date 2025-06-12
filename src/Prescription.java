package src;

import java.util.Date;

public class Prescription {
    private String id;
    private String patientId;
    private String doctorId;
    private Date prescriptionDate;

    public Prescription(String id, String patientId, String doctorId, Date prescriptionDate) {
        if (id == null || id.isEmpty() || patientId == null || patientId.isEmpty() || doctorId == null || doctorId.isEmpty() || prescriptionDate == null) {
            throw new IllegalArgumentException("Semua parameter resep tidak boleh kosong.");
        }
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.prescriptionDate = prescriptionDate;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public Date getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setPrescriptionDate(Date prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    @Override
    public String toString() {
        return "Prescription{" +
               "id='" + id + '\'' +
               ", patientId='" + patientId + '\'' +
               ", doctorId='" + doctorId + '\'' +
               ", prescriptionDate=" + prescriptionDate +
               '}';
    }
}
