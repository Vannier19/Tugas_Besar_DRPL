package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;


import java.io.IOException;

public class AccountPageController {

    @FXML private Label userIdLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private UserManagementController userManagementController;

    @FXML
    public void initialize() {
        userManagementController = new UserManagementController();
        loadCurrentUserData();
    }

    private void loadCurrentUserData() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            userIdLabel.setText(currentUser.getId());
            usernameField.setText(currentUser.getUsername());
            passwordField.clear();
        } else {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "User Session", "No user is currently logged in.");
        }
    }

    @FXML
    private void handleUpdateAccount() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser == null) {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Update Failed", "No user logged in to update.");
            return;
        }

        String newUsername = usernameField.getText().trim();
        String newPassword = passwordField.getText().trim(); 

        String currentRole = currentUser.getRole();

        try {
            String passwordToUse = newPassword.isEmpty() ? currentUser.getPassword() : newPassword;

            String message = userManagementController.updateUser(
                    currentUser.getId(),
                    newUsername,
                    passwordToUse,
                    currentRole 
            );

            User updatedUser = userManagementController.getUserById(currentUser.getId());
            SessionManager.setCurrentUser(updatedUser);

            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Success", "Profile Updated", message);
            loadCurrentUserData(); 
        } catch (IllegalArgumentException | IllegalStateException e) {
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Update Failed", e.getMessage());
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.clearSession();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginPage.fxml"));
            Parent loginPage = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow(); 
            Scene loginScene = new Scene(loginPage); 

            stage.setScene(loginScene);
            stage.setWidth(1280);    
            stage.setHeight(720);   
            stage.setResizable(false); 
            stage.centerOnScreen(); 
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Gagal Logout", "Tidak dapat kembali ke halaman login.");
        }
    }
}
