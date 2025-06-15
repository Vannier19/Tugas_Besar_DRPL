package tests;

import org.junit.jupiter.api.*;
import main.java.controllers.*;
import main.java.moduls.*;
import main.java.utils.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pengujian Use Case Manajemen Data User (UC-01)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserManagementTest {

    private static UserManagementController controller;

    @BeforeAll
    static void setUp() {
        // Inisialisasi controller sekali saja untuk semua tes di kelas ini
        controller = new UserManagementController();
    }

    @Test
    @Order(1)
    @DisplayName("[U-1-01] Skenario Normal: Menambah user baru")
    void addUser_Success() {
        assertDoesNotThrow(() -> {
            controller.addUser("testuser", "password123", "Dokter");
        }, "Seharusnya berhasil menambah user baru yang valid.");

        // Verifikasi bahwa user benar-benar ada
        assertNotNull(UserFactory.getUserByUsername("testuser"), "User yang baru ditambahkan seharusnya ditemukan.");
    }

    @Test
    @Order(2)
    @DisplayName("[U-1-01] Skenario Normal: Mengubah data user")
    void updateUser_Success() {
        User user = UserFactory.getUserByUsername("testuser");
        assertNotNull(user, "User untuk di-update seharusnya ada.");

        assertDoesNotThrow(() -> {
            controller.updateUser(user.getId(), "testuser_updated", "password456", "Apoteker");
        }, "Seharusnya berhasil mengubah data user.");

        // Verifikasi perubahan
        User updatedUser = UserFactory.getUserById(user.getId());
        assertEquals("testuser_updated", updatedUser.getUsername());
        assertEquals("Apoteker", updatedUser.getRole());
    }

    @Test
    @Order(3)
    @DisplayName("[U-1-01] Skenario Normal: Menghapus user")
    void deleteUser_Success() {
        User user = UserFactory.getUserByUsername("testuser_updated");
        assertNotNull(user, "User untuk dihapus seharusnya ada.");

        assertDoesNotThrow(() -> {
            controller.deleteUser(user.getId());
        }, "Seharusnya berhasil menghapus user.");

        // Verifikasi bahwa user telah dihapus
        assertNull(UserFactory.getUserById(user.getId()), "User seharusnya sudah tidak ada setelah dihapus.");
    }

    @Test
    @Order(4)
    @DisplayName("[U-1-02] Skenario Alternatif: Gagal menambah user karena data tidak lengkap")
    void addUser_FailsWithIncompleteData() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            controller.addUser("", "password123", "Dokter"); // Username kosong
        });
        assertEquals("Username, password, dan role tidak boleh kosong!", e.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("[U-1-03] Skenario Alternatif: Gagal mengubah user yang tidak ada")
    void updateUser_FailsForNonExistentUser() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            controller.updateUser("U999", "nouser", "nopass", "norole"); // ID tidak ada
        });
        assertTrue(e.getMessage().contains("tidak ditemukan"));
    }
}