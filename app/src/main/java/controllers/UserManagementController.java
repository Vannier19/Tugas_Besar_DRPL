package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class UserManagementController {
    private UserFactory userFactory;
    private static final AtomicInteger idCounter = new AtomicInteger(0); 

    public UserManagementController() {
        userFactory = new UserFactory();
        int maxIdNum = userFactory.getAllUsers().stream()
                                  .map(User::getId)
                                  .filter(id -> id.startsWith("U"))
                                  .mapToInt(id -> {
                                      try {
                                          return Integer.parseInt(id.substring(1));
                                      } catch (NumberFormatException e) {
                                          return 0;
                                      }
                                  })
                                  .max().orElse(0);
        idCounter.set(maxIdNum);
    }

    public String addUser(String username, String password, String role) { 
        if (username == null || username.isEmpty() || password == null || password.isEmpty() || role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Username, password, dan role tidak boleh kosong!");
        }

        String newId = "U" + String.format("%03d", idCounter.incrementAndGet());
        if (userFactory.getUserById(newId) != null) {
            throw new IllegalStateException("Gagal menambahkan pengguna: ID sudah ada (otomatis)");
        }
        
        userFactory.createUser(newId, username, password, role);
        return "Pengguna " + username + " berhasil ditambahkan dengan ID: " + newId + ".";
    }

    public List<User> getAllUsers() {
        return userFactory.getAllUsers();
    }

    public User signIn(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username dan password tidak boleh null!");
        }
        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username dan password tidak boleh kosong!");
        }
        return userFactory.signIn(username, password); 
    }

    public String updateUser(String id, String newUsername, String newPassword, String newRole) {
        if (id == null || id.isEmpty() || newUsername == null || newUsername.isEmpty() ||
            newPassword == null || newPassword.isEmpty() || newRole == null || newRole.isEmpty()) {
            throw new IllegalArgumentException("Semua parameter untuk update tidak boleh kosong!");
        }

        User userToUpdate = userFactory.getUserById(id);
        if (userToUpdate == null) {
            throw new IllegalArgumentException("Pengguna dengan ID " + id + " tidak ditemukan!");
        }

        userToUpdate.setUsername(newUsername);
        userToUpdate.setPassword(newPassword);
        userToUpdate.setRole(newRole);

        userFactory.createUser(userToUpdate.getId(), userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getRole());
        return "Pengguna " + newUsername + " (ID: " + id + ") berhasil diperbarui.";
    }

    public String deleteUser(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID pengguna tidak boleh kosong!");
        }
        if (userFactory.deleteUser(id)) {
            return "Pengguna dengan ID " + id + " berhasil dihapus.";
        } else {
            throw new IllegalArgumentException("Pengguna dengan ID " + id + " tidak ditemukan.");
        }
    }

    public User getUserById(String id) {
        return userFactory.getUserById(id);
    }
}