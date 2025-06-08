package tests;

import src.Appointment;
import src.controllers.AppointmentController;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Kelas untuk menguji fitur penjadwalan janji temu secara interaktif.
 * Kelas ini mensimulasikan interaksi Administrator dengan sistem.
 */
public class ManageAppointmentTest {
    public static void main(String[] args) {
        AppointmentController controller = new AppointmentController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Tes Interaktif Penjadwalan Janji Temu ===");

        // Skenario 1: Menambahkan janji temu baru
        System.out.println("\n--- Membuat Janji Temu Baru ---");
        System.out.print("Masukkan ID Pasien (e.g., P001): ");
        String patientId = scanner.nextLine();
        System.out.print("Masukkan ID Dokter (e.g., U001): ");
        String doctorId = scanner.nextLine();

        try {
            String result = controller.createAppointment(patientId, doctorId, new Date());
            System.out.println("Hasil: " + result);
        } catch (IllegalArgumentException e) {
            System.err.println("Gagal: " + e.getMessage());
        }

        // Skenario 2: Melihat semua janji temu yang ada
        System.out.println("\n--- Melihat Semua Janji Temu ---");
        List<Appointment> appointments = controller.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("Belum ada janji temu yang dijadwalkan.");
        } else {
            System.out.println("Daftar semua janji temu:");
            for (Appointment app : appointments) {
                System.out.println(" - " + app.toString());
            }
        }

        scanner.close();
    }
}