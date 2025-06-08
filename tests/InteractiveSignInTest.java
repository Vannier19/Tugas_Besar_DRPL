package tests;

import src.controllers.UserManagementController;

import java.util.Scanner;

/**
 * Kelas untuk menguji fitur sign-in secara interaktif.
 */
public class InteractiveSignInTest {
    public static void main(String[] args) {
        UserManagementController controller = new UserManagementController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Tes Interaktif Sign-in ===");
        System.out.print("Masukkan username: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan password: ");
        String password = scanner.nextLine();

        try {
            String result = controller.signIn(username, password);
            System.out.println("Hasil: " + result);
        } catch (IllegalArgumentException | SecurityException e) {
            System.out.println("Hasil: Gagal - " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}