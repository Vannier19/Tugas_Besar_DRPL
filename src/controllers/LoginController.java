package src.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import src.User;
import src.utils.FXMLUtils;
import src.utils.SessionManager;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorMessageLabel;

    private UserManagementController userManagementController;

    public LoginController() {
        this.userManagementController = new UserManagementController();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = userManagementController.signIn(username, password);

            if (user != null) {
                SessionManager.setCurrentUser(user);
                errorMessageLabel.setText("");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/views/DashboardPage.fxml"));
                Parent dashboardPage = loader.load();

                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene dashboardScene = new Scene(dashboardPage, 1000, 700);

                stage.setScene(dashboardScene);
                stage.setTitle("Sistem Manajemen Klinik Sehat Medika - Dashboard");
                stage.sizeToScene(); // Adjusts the stage to the size of the scene content if preferred sizes are set in FXML
                stage.setResizable(true); // Ensure the window is resizable
                stage.show();
            } else {
                errorMessageLabel.setText("Username atau password salah!");
            }
        } catch (IllegalArgumentException e) {
            errorMessageLabel.setText(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Gagal Memuat Halaman", "Tidak dapat memuat halaman dashboard.");
        }
    }
}