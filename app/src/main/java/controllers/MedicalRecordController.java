package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import java.util.Date;
import java.util.List;

public class MedicalRecordController {
    
    private final MedicalRecordFactory recordFactory;
    private final PatientFactory patientFactory;

    public MedicalRecordController() {
        this.recordFactory = new MedicalRecordFactory();
        this.patientFactory = new PatientFactory();
    }
    public void addMedicalRecord(String patientId, String diagnosis, Date date) {
        if (patientFactory.getPatientById(patientId) == null) {
            throw new IllegalArgumentException("Pasien dengan ID " + patientId + " tidak ditemukan.");
        }
        MedicalRecordFactory.addMedicalRecord(patientId, diagnosis, date);
    }
    public List<MedicalRecord> getMedicalRecords(String patientId) {
        if (patientFactory.getPatientById(patientId) == null) {
            throw new IllegalArgumentException("Pasien dengan ID " + patientId + " tidak ditemukan.");
        }
        return recordFactory.getRecordsByPatientId(patientId);
    }
}