package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Optional;

public class UserManagementPageController {
    
    @FXML private TitledPane addUserPane;
    @FXML private TitledPane manageUsersPane;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> idColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> roleColumn;

    private UserManagementController userLogicController;
    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        userLogicController = new UserManagementController();
        userList = FXCollections.observableArrayList();

        roleComboBox.getItems().addAll("Dokter", "Apothecary", "System Manager", "Administrator");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        manageUsersPane.setExpanded(false);
        addUserPane.setExpanded(false);
        addUserPane.expandedProperty().addListener((observable, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                manageUsersPane.setExpanded(false);
            }
        });

        manageUsersPane.expandedProperty().addListener((observable, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                addUserPane.setExpanded(false);
            }
        });
        
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
        roleComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleUpdateUser(ActionEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            FXMLUtils.showAlert(Alert.AlertType.WARNING, "Peringatan", "Tidak Ada Pengguna Terpilih", "Silakan pilih pengguna dari tabel untuk diubah.");
            return;
        }

        try {
            String id = selectedUser.getId();
            String newUsername = usernameField.getText().trim();
            String newPassword = passwordField.getText().trim();
            String newRole = roleComboBox.getValue();

            String message = userLogicController.updateUser(id, newUsername, newPassword, newRole);
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Update Berhasil", message);
            
            loadUserData();
            clearForm();

        } catch (Exception e) {
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Update Gagal", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            FXMLUtils.showAlert(Alert.AlertType.WARNING, "Peringatan", "Tidak Ada Pengguna Terpilih", "Silakan pilih pengguna dari tabel untuk dihapus.");
            return;
        }

        // Tampilkan dialog konfirmasi sebelum menghapus
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Hapus");
        confirmationAlert.setHeaderText("Hapus Pengguna: " + selectedUser.getUsername());
        confirmationAlert.setContentText("Apakah Anda yakin ingin menghapus pengguna ini?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String message = userLogicController.deleteUser(selectedUser.getId());
                FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Hapus Berhasil", message);
                
                loadUserData(); // Muat ulang data tabel
                clearForm();    // Kosongkan form

            } catch (Exception e) {
                FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Hapus Gagal", e.getMessage());
            }
        }
    }
}