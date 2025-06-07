package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory untuk mengelola pembuatan dan penyimpanan objek Appointment.
 * Pola ini konsisten dengan factory lain yang ada di dalam proyek.
 */
public class AppointmentFactory {
    private Map<String, Appointment> appointments;

    public AppointmentFactory() {
        this.appointments = new HashMap<>();
    }

    /**
     * Menambahkan janji temu baru ke dalam sistem.
     * @param appointment Objek Appointment yang akan ditambahkan.
     */
    public void addAppointment(Appointment appointment) {
        appointments.put(appointment.getId(), appointment);
    }

    /**
     * Mengambil semua janji temu yang ada.
     * @return Daftar semua janji temu.
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }

    /**
     * Mencari janji temu berdasarkan ID.
     * @param id ID janji temu.
     * @return Objek Appointment jika ditemukan, null jika tidak.
     */
    public Appointment getAppointmentById(String id) {
        return appointments.get(id);
    }
}