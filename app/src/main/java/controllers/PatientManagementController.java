package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import java.util.List;
public class PatientManagementController {
    private PatientService patientService;

    public PatientManagementController() {
        this.patientService = new PatientService();
    }

    public List<Patient> retrieveAllPatients() {
        return patientService.getAllPatients();
    }

    public String addNewPatient(String name, int age) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Nama pasien tidak boleh kosong!");
        }
        if (age <= 0 || age > 120) {
            throw new IllegalArgumentException("Usia pasien tidak valid!");
        }

        String newId = patientService.generateNewPatientId();
        Patient newPatient = new Patient(newId, name, age);
        if (patientService.addPatient(newPatient)) {
            return "Pasien " + name + " berhasil ditambahkan dengan ID: " + newId;
        } else {
            throw new IllegalStateException("Gagal menambahkan pasien: ID sudah ada (ini seharusnya tidak terjadi dengan ID generator).");
        }
    }

    public String updateExistingPatient(String id, String name, int age) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID pasien tidak boleh kosong!");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Nama pasien tidak boleh kosong!");
        }
        if (age <= 0 || age > 120) {
            throw new IllegalArgumentException("Usia pasien tidak valid!");
        }

        Patient existingPatient = patientService.getPatientById(id);
        if (existingPatient == null) {
            throw new IllegalArgumentException("Pasien dengan ID " + id + " tidak ditemukan!");
        }

        existingPatient.setName(name);
        existingPatient.setAge(age);

        if (patientService.updatePatient(existingPatient)) {
            return "Data pasien " + name + " (ID: " + id + ") berhasil diperbarui.";
        } else {
            throw new IllegalStateException("Gagal memperbarui pasien.");
        }
    }

    public String deletePatient(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID pasien tidak boleh kosong!");
        }
        if (patientService.deletePatient(id)) {
            return "Pasien dengan ID " + id + " berhasil dihapus.";
        } else {
            throw new IllegalArgumentException("Pasien dengan ID " + id + " tidak ditemukan.");
        }
    }
}