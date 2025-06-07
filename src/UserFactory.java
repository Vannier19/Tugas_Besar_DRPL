package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory untuk mengelola pembuatan dan penyimpanan objek User.
 */
public class UserFactory {
    private Map<String, User> users;

    public UserFactory() {
        users = new HashMap<>();
        // Data awal (bisa diisi dari database atau file di masa depan)
        users.put("U001", new User("U001", "dr.john", "password123", "Dokter"));
        users.put("U002", new User("U002", "apt.smith", "password123", "Apoteker"));
    }

    /**
     * Membuat atau mengambil User berdasarkan ID.
     * @param id ID pengguna
     * @param username Nama pengguna
     * @param password Kata sandi
     * @param role Peran pengguna
     * @return Objek User
     */
    public User createUser(String id, String username, String password, String role) {
        User user = new User(id, username, password, role);
        users.put(id, user);
        return user;
    }

    /**
     * Mendapatkan semua pengguna.
     * @return Daftar semua pengguna
     */
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Memeriksa autentikasi pengguna.
     * @param username Nama pengguna
     * @param password Kata sandi
     * @return User jika berhasil, null jika gagal
     */
    public User signIn(String username, String password) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}