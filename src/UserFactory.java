package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory untuk mengelola pembuatan dan penyimpanan objek User.
 */
public class UserFactory {
    private static Map<String, User> users = new HashMap<>();

    static {
        users.put("U001", new User("U001", "Hans", "1", "Dokter"));
        users.put("U002", new User("U002", "Stevan", "2", "Apothecary"));
        users.put("U003", new User("U003", "Mike", "3", "System Manager"));
        users.put("U004", new User("U004", "Jacob", "4", "Administrator"));
    }

    public UserFactory() {}

    /**
     * Membuat atau memperbarui User berdasarkan ID.
     * Jika ID sudah ada, data pengguna diperbarui.
     * @param id ID pengguna
     * @param username Nama pengguna
     * @param password Kata sandi
     * @param role Peran pengguna
     * @return Objek User yang dibuat/diperbarui
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
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Mendapatkan pengguna berdasarkan ID.
     * @param id ID pengguna
     * @return User jika ditemukan, null jika tidak.
     */
    public User getUserById(String id) {
        return users.get(id);
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

    /**
     * Menghapus pengguna berdasarkan ID.
     * @param id ID pengguna yang akan dihapus
     * @return true jika berhasil dihapus, false jika pengguna tidak ditemukan.
     */
    public boolean deleteUser(String id) {
        return users.remove(id) != null;
    }
}