package src.controllers;
import src.MedicalRecordFactory;
import src.PatientFactory;
import src.MedicalRecord;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MedicalRecordController {
    private MedicalRecordFactory recordFactory;
    private PatientFactory patientFactory;
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    public MedicalRecordController() {
        this.recordFactory = new MedicalRecordFactory();
        this.patientFactory = new PatientFactory();
    }

    /**
     * Menambahkan riwayat medis baru, sesuai dengan sequence diagram.
     * @param patientId ID pasien.
     * @param diagnosis Diagnosis dokter.
     * @param date Tanggal pemeriksaan.
     * @return Pesan konfirmasi.
     */
    public String addMedicalRecord(String patientId, String diagnosis, Date date) {
        // Validasi: Pastikan pasien ada
        if (patientFactory.getAllPatients().stream().noneMatch(p -> p.getId().equals(patientId))) {
            throw new IllegalArgumentException("Patient with ID " + patientId + " not found.");
        }

        String newId = "MR" + String.format("%03d", idCounter.getAndIncrement());
        MedicalRecord record = new MedicalRecord(newId, patientId, diagnosis, date);
        recordFactory.addMedicalRecord(record);
        return "Medical record added successfully for patient " + patientId;
    }

    /**
     * Mengambil riwayat medis untuk pasien tertentu, sesuai sequence diagram.
     * @param patientId ID pasien.
     * @return Daftar riwayat medis.
     */
    public List<MedicalRecord> getMedicalRecords(String patientId) {
        if (patientFactory.getAllPatients().stream().noneMatch(p -> p.getId().equals(patientId))) {
            throw new IllegalArgumentException("Patient with ID " + patientId + " not found.");
        }
        return recordFactory.getRecordsByPatientId(patientId);
    }
}