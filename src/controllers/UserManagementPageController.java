package src.controllers;

import src.User;
import src.utils.FXMLUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserManagementPageController {

    // Kolom dari kode asli Anda
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> idColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    // @FXML private TableColumn<User, String> passwordColumn;
    @FXML private TableColumn<User, String> roleColumn;

    // Kolom baru dari referensi desain
    @FXML private TextField emailField;
    @FXML private DatePicker dobField;
    @FXML private TextField phoneField;

    private UserManagementController userLogicController;
    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        userLogicController = new UserManagementController();
        userList = FXCollections.observableArrayList();

        // Mengisi pilihan untuk role ComboBox
        roleComboBox.getItems().addAll("Dokter", "Apoteker", "System Manager", "Administrator");

        // Mengatur kolom tabel
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        // passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        loadUserData();

        // Listener untuk menampilkan detail saat baris tabel dipilih
        userTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showUserDetails(newValue));
    }

    private void loadUserData() {
        userList.clear();
        userList.addAll(userLogicController.getAllUsers()); 
        userTable.setItems(userList);
    }

    private void showUserDetails(User user) {
        if (user != null) {
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            roleComboBox.setValue(user.getRole());
            // TODO: Tambahkan getter di kelas User.java untuk email, dob, dll.
            // emailField.setText(user.getEmail()); 
        } else {
            clearForm();
        }
    }

    @FXML
    private void handleAddUser() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = roleComboBox.getValue();

            if (role == null || role.isEmpty()) {
                FXMLUtils.showAlert(Alert.AlertType.WARNING, "Input Diperlukan", "Role Kosong", "Role pengguna harus dipilih.");
                return;
            }

            // Logika addUser dari controller asli Anda tetap digunakan
            String message = userLogicController.addUser(username, password, role); 
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Tambah Pengguna", message);
            loadUserData();
            clearForm();
        } catch (IllegalArgumentException | IllegalStateException e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Tambah Pengguna Gagal", e.getMessage());
        }
    }
    
    // Metode handleUpdateUser dan handleDeleteUser dari kode asli Anda dapat ditambahkan kembali di sini jika diperlukan

    @FXML
    private void handleClearForm() {
        clearForm();
        userTable.getSelectionModel().clearSelection();
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
        phoneField.clear();
        dobField.setValue(null);
        roleComboBox.getSelectionModel().clearSelection();
    }
}