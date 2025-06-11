package src.controllers;
import src.User;
import src.UserFactory;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller untuk mengelola operasi terkait pengguna.
 */
public class UserManagementController {
    private UserFactory userFactory;

    public UserManagementController() {
        userFactory = new UserFactory();
    }

    @FXML
    public void initialize() {
        // Initialization logic for User Management page
    }
    
    /**
     * Menambahkan pengguna baru.
     * @param id ID pengguna
     * @param username Nama pengguna
     * @param password Kata sandi
     * @param role Peran pengguna
     * @return Pesan sukses
     * @throws IllegalArgumentException Jika parameter tidak valid
     * @throws IllegalStateException Jika ID sudah ada
     */

    public String addUser(String id, String username, String password, String role) {
        if (id == null || username == null || password == null || role == null) {
            throw new IllegalArgumentException("Parameter tidak boleh null!");
        }
        if (id.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            throw new IllegalArgumentException("Parameter tidak boleh kosong!");
        }
        if (userFactory.getAllUsers().stream().anyMatch(u -> u.getId().equals(id))) {
            throw new IllegalStateException("Gagal menambahkan pengguna: ID sudah ada!");
        }
        userFactory.createUser(id, username, password, role);
        return "Pengguna " + username + " berhasil ditambahkan!";
    }

    /**
     * Mendapatkan semua pengguna.
     * @return Daftar semua pengguna
     */
    public List<User> getAllUsers() {
        return userFactory.getAllUsers();
    }

    /**
     * Melakukan sign-in pengguna.
     * @param username Nama pengguna
     * @param password Kata sandi
     * @return User object if successful, null if authentication fails.
     * @throws IllegalArgumentException Jika username atau password null/kosong.
     */
    public User signIn(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username dan password tidak boleh null!");
        }
        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username dan password tidak boleh kosong!");
        }
        return userFactory.signIn(username, password);
    }
}