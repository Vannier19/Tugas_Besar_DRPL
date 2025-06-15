package tests;

import org.junit.jupiter.api.*;
import main.java.controllers.*;
import main.java.moduls.*;
import main.java.utils.*;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pengujian Use Case Penjadwalan Janji Temu (UC-05)")
class AppointmentTest {

    private AppointmentController controller;

    @BeforeAll
    static void setupData() {
        // Menyiapkan data dokter dan pasien
        UserFactory.addUser(new User("D001", "Dr. Strange", "pass", "Dokter"));
        PatientFactory.addPatient(new Patient("P002", "Wanda Maximoff", 29));
    }

    @BeforeEach
    void setUp() {
        controller = new AppointmentController();
    }

    @Test
    @DisplayName("[U-5-01] Skenario Normal: Administrator berhasil membuat jadwal janji temu baru")
    void createAppointment_Success() {
        LocalDate today = LocalDate.now();
        assertDoesNotThrow(() -> {
            controller.createAppointment("P002", "D001", today.atTime(9, 0));
        }, "Pembuatan janji temu baru seharusnya berhasil.");

        List<Appointment> appointments = controller.getAppointmentsByDoctorId("D001");
        assertFalse(appointments.isEmpty(), "Daftar janji temu dokter seharusnya tidak kosong.");
        assertEquals("P002", appointments.get(0).getPatientId());
    }

    @Test
    @DisplayName("[U-5-02] Skenario Alternatif: Gagal membuat janji temu karena ID pasien/dokter tidak ditemukan")
    void createAppointment_FailsForInvalidIDs() {
        LocalDate today = LocalDate.now();
        // Test dengan ID Pasien tidak valid
        Exception e1 = assertThrows(IllegalArgumentException.class, () -> {
            controller.createAppointment("P999", "D001", today.atTime(11, 0));
        });
        assertTrue(e1.getMessage().contains("Pasien dengan ID P999 tidak ditemukan"));

        // Test dengan ID Dokter tidak valid
        Exception e2 = assertThrows(IllegalArgumentException.class, () -> {
            controller.createAppointment("P002", "D999", today.atTime(10, 0));
        });
        assertTrue(e2.getMessage().contains("Dokter dengan ID D999 tidak ditemukan"));
    }
}