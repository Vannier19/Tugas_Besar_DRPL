package src;

import java.util.Date;
import java.util.UUID;

public class MedicalRecord {
    private String id;
    private String patientId;
    private String diagnosis;
    private Date date;
    
    public MedicalRecord(String id, String patientId, String diagnosis, Date date) {
        if (id == null || id.isEmpty() || patientId == null || patientId.isEmpty() || diagnosis == null || diagnosis.isEmpty() || date == null) {
            throw new IllegalArgumentException("ID, Patient ID, diagnosis, dan date tidak boleh kosong.");
        }
        this.id = id;
        this.patientId = patientId;
        this.diagnosis = diagnosis;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public Date getDate() {
        return date;
    }
}