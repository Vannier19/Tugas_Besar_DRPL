package src;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Controller untuk mengelola operasi terkait penjadwalan janji temu.
 * Sesuai dengan diagram sekuens pada dokumen DPPLOO-02.
 */
public class AppointmentController {
    private final AppointmentFactory appointmentFactory;
    private final PatientFactory patientFactory;
    private final UserFactory userFactory;
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    public AppointmentController() {
        this.appointmentFactory = new AppointmentFactory();
        this.patientFactory = new PatientFactory();
        this.userFactory = new UserFactory();
    }

    /**
     * Membuat janji temu baru.
     * Logika ini mengikuti alur pada sequence diagram untuk penjadwalan.
     * @param patientId ID Pasien.
     * @param doctorId ID Dokter.
     * @param date Tanggal janji temu.
     * @return Pesan konfirmasi.
     */
    public String createAppointment(String patientId, String doctorId, Date date) {
        // Validasi: Pastikan pasien ada
        if (patientFactory.getAllPatients().stream().noneMatch(p -> p.getId().equals(patientId))) {
            throw new IllegalArgumentException("Pasien dengan ID " + patientId + " tidak ditemukan.");
        }
        // Validasi: Pastikan dokter ada
        if (userFactory.getAllUsers().stream().noneMatch(u -> u.getId().equals(doctorId) && u.getRole().equals("Dokter"))) {
            throw new IllegalArgumentException("Dokter dengan ID " + doctorId + " tidak ditemukan.");
        }

        String newId = "APP" + String.format("%03d", idCounter.getAndIncrement());
        Appointment appointment = new Appointment(newId, patientId, doctorId, date);
        appointmentFactory.addAppointment(appointment);

        return "Janji temu berhasil dibuat dengan ID: " + newId;
    }

    /**
     * Mengambil semua data janji temu.
     * @return list dari semua janji temu.
     */
    public List<Appointment> getAllAppointments() {
        return appointmentFactory.getAllAppointments();
    }
}