package src.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import src.User;
import src.utils.FXMLUtils;
import src.utils.SessionManager;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessageLabel;

    private UserManagementController userManagementController;

    public LoginController() {
        this.userManagementController = new UserManagementController();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorMessageLabel.setText("Username and password cannot be empty.");
            return;
        }

        try {
            User user = userManagementController.signIn(username, password);

            if (user != null) {
                SessionManager.setCurrentUser(user);
                errorMessageLabel.setText("");

                String dashboardFxmlPath;
                String role = user.getRole();

                switch (role) {
                    case "System Manager":
                        dashboardFxmlPath = "/src/views/SystemManagerDashboardPage.fxml";
                        break;
                   
                    case "Administrator":
                        dashboardFxmlPath = "/src/views/AdminDashboardPage.fxml";
                        break;

                    case "Apothecary":
                        dashboardFxmlPath = "/src/views/ApothecaryDashboardPage.fxml";
                        break;

                    case "Dokter":
                        dashboardFxmlPath = "/src/views/DoctorDashboardPage.fxml";
                        break;
                    
                    default:
                        errorMessageLabel.setText("Dashboard untuk role '" + role + "' tidak ditemukan.");
                        return;
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource(dashboardFxmlPath));
                Parent dashboardPage = loader.load();

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Scene dashboardScene = new Scene(dashboardPage, 1280, 720);

                stage.setScene(dashboardScene);
                stage.setTitle("Womentcare - " + role + " Dashboard");
                stage.setResizable(true);
                stage.centerOnScreen();
                stage.show();

            } else {
                errorMessageLabel.setText("Username atau password salah!");
            }
        } catch (IllegalArgumentException e) {
            errorMessageLabel.setText(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            errorMessageLabel.setText("Error: Gagal memuat halaman dashboard wek.");
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Load Error", "Gagal Memuat Halaman", "File FXML untuk dashboard tidak dapat ditemukan atau rusak.");
        }
    }
}