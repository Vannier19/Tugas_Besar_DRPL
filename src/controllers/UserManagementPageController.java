// File: src/controllers/UserManagementPageController.java (Corrected UI Controller)
package src.controllers;

import src.User; // Import the User model
import src.utils.FXMLUtils; // Import FXMLUtils for alerts

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.PasswordField;

public class UserManagementPageController {

    @FXML private TextField idField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> idColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> passwordColumn;
    @FXML private TableColumn<User, String> roleColumn;

    private src.controllers.UserManagementController userLogicController;
    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        userLogicController = new src.controllers.UserManagementController();
        userList = FXCollections.observableArrayList();

        // Populate the role ComboBox
        roleComboBox.getItems().addAll("Dokter", "Apoteker", "System Manager", "Administrator");

        // Set up table columns to bind to User properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        loadUserData();

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
            idField.setText(user.getId());
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            roleComboBox.setValue(user.getRole());
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

            String message = userLogicController.addUser(username, password, role); 
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Tambah Pengguna", message);
            loadUserData();
            clearForm();
        } catch (IllegalArgumentException | IllegalStateException e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Tambah Pengguna Gagal", e.getMessage());
        }
    }

    @FXML
    private void handleUpdateUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            FXMLUtils.showAlert(Alert.AlertType.WARNING, "Pilih Pengguna", "Tidak Ada Pengguna Terpilih", "Pilih pengguna dari tabel untuk diperbarui.");
            return;
        }

        try {
            String id = idField.getText().trim(); 
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = roleComboBox.getValue();

            if (role == null || role.isEmpty()) {
                FXMLUtils.showAlert(Alert.AlertType.WARNING, "Input Diperlukan", "Role Kosong", "Role pengguna harus dipilih.");
                return;
            }

            String message = userLogicController.updateUser(id, username, password, role);
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Perbarui Pengguna", message);
            loadUserData();
            clearForm();
        } catch (IllegalArgumentException | IllegalStateException e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Perbarui Pengguna Gagal", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            FXMLUtils.showAlert(Alert.AlertType.WARNING, "Pilih Pengguna", "Tidak Ada Pengguna Terpilih", "Pilih pengguna dari tabel untuk dihapus.");
            return;
        }

        try {
            String message = userLogicController.deleteUser(selectedUser.getId()); 
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Hapus Pengguna", message);
            loadUserData();
            clearForm();
        } catch (IllegalArgumentException e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Hapus Pengguna Gagal", e.getMessage());
        }
    }

    @FXML
    private void handleClearForm() {
        clearForm();
        userTable.getSelectionModel().clearSelection();
    }

    private void clearForm() {
        idField.clear();
        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }
}