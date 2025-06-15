// Simpan di: src/test/java/tests/PatientManagementTest.java
package tests;

import org.junit.jupiter.api.*;
import main.java.controllers.*;
import main.java.moduls.*;
import main.java.utils.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pengujian Use Case Manajemen Data Pasien (UC-02)")
class PatientManagementTest {

    private PatientManagementController controller;

    @BeforeEach
    void setUp() {
        controller = new PatientManagementController();
    }

    @Test
    @DisplayName("[U-2-01] Skenario Normal: Menambah data pasien baru dengan data valid")
    void addPatient_Success() {
        assertDoesNotThrow(() -> {
            controller.addNewPatient("Rina Sari", 28);
        }, "Penambahan pasien baru dengan data valid seharusnya berhasil.");
        
        List<Patient> patients = PatientFactory.getAllPatients();
        assertTrue(patients.stream().anyMatch(p -> p.getName().equals("Rina Sari")));
    }

    @Test
    @DisplayName("[U-2-02] Skenario Alternatif: Gagal menambah pasien karena input tidak valid")
    void addPatient_FailsWithInvalidAge() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            controller.addNewPatient("Budi", -5); // Usia negatif
        }, "Seharusnya melempar error untuk usia yang tidak valid.");
        
        assertEquals("Usia pasien tidak valid!", e.getMessage());
    }

    @Test
    @DisplayName("[U-2-03] Skenario Alternatif: Gagal menghapus pasien yang tidak ada")
    void deletePatient_FailsForNonExistentPatient() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            controller.deletePatient("P999"); // ID tidak ada
        });
        assertTrue(e.getMessage().contains("tidak ditemukan"));
    }
}