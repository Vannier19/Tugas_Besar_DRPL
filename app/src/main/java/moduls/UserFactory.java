package main.java.moduls;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory untuk mengelola objek User menggunakan method static.
 */
public class UserFactory {
    private static final Map<String, User> users = new HashMap<>();
    private static int idCounter = 1;

    // Static Initializer untuk data awal
    static {
        addUser(new User("U" + String.format("%03d", idCounter++), "Hans", "1", "Dokter"));
        addUser(new User("U" + String.format("%03d", idCounter++), "Stevan", "2", "Apothecary"));
        addUser(new User("U" + String.format("%03d", idCounter++), "Mike", "3", "System Manager"));
        addUser(new User("U" + String.format("%03d", idCounter++), "Jacob", "4", "Administrator"));
    }

    public UserFactory() {} // Constructor private

    // Method untuk menambah user baru dengan ID otomatis
    public static User createUser(String id, String username, String password, String role) {
        id = "U" + String.format("%03d", idCounter++);
        User user = new User(id, username, password, role);
        users.put(id, user);
        return user;
    }

    // Method helper untuk menambahkan user (berguna untuk tes)
    public static void addUser(User user) {
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public static User getUserById(String id) {
        return users.get(id);
    }

    public static User signIn(String username, String password) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static boolean deleteUser(String id) {
        return users.remove(id) != null;
    }

    public static User getUserByUsername(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

}