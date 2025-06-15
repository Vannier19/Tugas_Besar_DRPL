// Simpan di: src/test/java/tests/SignInTest.java
package tests;

import org.junit.jupiter.api.*;
import main.java.controllers.*;
import main.java.moduls.*;
import main.java.utils.*;
import main.resources.views.*;
import main.resources.assets.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pengujian Use Case Sign-in (UC-03)")
class SignInTest {

    private UserManagementController controller;

    @BeforeAll
    static void setupUsers() {
        // Menambahkan data user awal untuk keperluan tes login
        UserFactory.addUser(new User("U001", "hans", "123", "Dokter"));
        UserFactory.addUser(new User("U002", "mike", "456", "Apoteker"));
    }

    @BeforeEach
    void setUp() {
        controller = new UserManagementController();
    }

    @Test
    @DisplayName("[U-3-01] Skenario Normal: User berhasil login dengan username dan password valid")
    void signIn_Success() {
        User user = controller.signIn("hans", "123");
        assertNotNull(user, "Login seharusnya berhasil dan mengembalikan objek User.");
        assertEquals("hans", user.getUsername());
        assertEquals("Dokter", user.getRole());
    }

    @Test
    @DisplayName("[U-3-02] Skenario Alternatif: Gagal login karena password salah")
    void signIn_FailsWithWrongPassword() {
        Exception e = assertThrows(SecurityException.class, () -> {
            controller.signIn("hans", "password_salah");
        });
        assertEquals("Username atau password salah.", e.getMessage());
    }

    @Test
    @DisplayName("[U-3-03] Skenario Alternatif: Gagal login karena username tidak terdaftar")
    void signIn_FailsWithUnknownUsername() {
        Exception e = assertThrows(SecurityException.class, () -> {
            controller.signIn("user_tidak_ada", "123");
        });
        assertEquals("Username atau password salah.", e.getMessage());
    }

    @Test
    @DisplayName("[U-3-04] Skenario Alternatif: Gagal login karena input kosong")
    void signIn_FailsWithEmptyInput() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            controller.signIn("", "123"); // Username kosong
        });
        assertEquals("Username dan password tidak boleh kosong.", e.getMessage());
    }
}