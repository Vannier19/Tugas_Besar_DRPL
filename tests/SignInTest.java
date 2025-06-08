package tests;

import src.controllers.UserManagementController;

/**
 * Kelas untuk menguji fitur sign-in pengguna.
 */
public class SignInTest {
    public static void main(String[] args) {
        UserManagementController controller = new UserManagementController();

        // Test 1: Sign-in sukses
        try {
            String result = controller.signIn("dr.john", "password123");
            System.out.println("Test 1 - Sign-in sukses: " + result);
        } catch (Exception e) {
            System.out.println("Test 1 gagal: " + e.getMessage());
        }

        // Test 2: Sign-in gagal - Password salah
        try {
            String result = controller.signIn("dr.john", "wrongpass");
            System.out.println("Test 2 - Sign-in gagal (harusnya exception): " + result);
        } catch (SecurityException e) {
            System.out.println("Test 2 berhasil gagal: " + e.getMessage());
        }

        // Test 3: Sign-in gagal - Username tidak ada
        try {
            String result = controller.signIn("unknown", "password123");
            System.out.println("Test 3 - Sign-in gagal (harusnya exception): " + result);
        } catch (SecurityException e) {
            System.out.println("Test 3 berhasil gagal: " + e.getMessage());
        }

        // Test 4: Sign-in gagal - Username kosong
        try {
            String result = controller.signIn("", "password123");
            System.out.println("Test 4 - Sign-in gagal (harusnya exception): " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Test 4 berhasil gagal: " + e.getMessage());
        }

        // Test 5: Sign-in gagal - Password null
        try {
            String result = controller.signIn("dr.john", null);
            System.out.println("Test 5 - Sign-in gagal (harusnya exception): " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Test 5 berhasil gagal: " + e.getMessage());
        }
    }
}