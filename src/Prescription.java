package src;

import java.util.Date;

public class Prescription {
    private String id;
    private String patientId;
    private String doctorId;
    private String medicineName;
    private String dosage;
    private String quantity;
    private String notes;
    private Date issueDate;
    private String status;
    public Prescription(String id, String patientId, String doctorId, String medicineName, String dosage, String quantity, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.quantity = quantity;
        this.notes = notes;
        this.issueDate = new Date();
        this.status = "Pending";
    }
    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getMedicineName() { return medicineName; }
    public String getDosage() { return dosage; }
    public String getQuantity() { return quantity; }
    public String getNotes() { return notes; }
    public Date getIssueDate() { return issueDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}