package tests;

import org.junit.jupiter.api.*;
import main.java.controllers.*;
import main.java.moduls.*;
import main.java.utils.*;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pengujian Use Case Manajemen Riwayat Medis (UC-04)")
class MedicalRecordTest {

    private MedicalRecordController controller;

    @BeforeAll
    static void setupPatient() {
        // Menyiapkan data pasien awal
        PatientFactory.addPatient(new Patient("P001", "Budi Santoso", 30));
    }

    @BeforeEach
    void setUp() {
        controller = new MedicalRecordController();
    }

    @Test
    @DisplayName("[U-4-01] Skenario Normal: Dokter berhasil menambah dan melihat riwayat medis")
    void addAndSeeMedicalRecord_Success() {
        String patientId = "P001";
        String diagnosis = "Flu dan Batuk";
        String prescription = "Paracetamol 3x1";
        
        assertDoesNotThrow(() -> {
            controller.addMedicalRecord(patientId, diagnosis, new Date(System.currentTimeMillis() - (2L * 24 * 60 * 60 * 15000)));
        }, "Penambahan rekam medis seharusnya berhasil.");

        MedicalRecord record = controller.getMedicalRecordByPatientId(patientId);
        assertNotNull(record);
        assertEquals(diagnosis, record.getDiagnosis());
    }

    @Test
    @DisplayName("[U-4-02] Skenario Alternatif: Gagal menambah riwayat medis untuk ID pasien tidak ada")
    void addMedicalRecord_FailsForInvalidPatient() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            controller.addMedicalRecord("P999", "Sakit Kepala", new Date(System.currentTimeMillis() - (2L * 24 * 60 * 60 * 15000)));
        });
        assertEquals("Pasien dengan ID P999 tidak ditemukan.", e.getMessage());
    }
}