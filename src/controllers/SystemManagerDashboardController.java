package src.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import src.User;
import src.utils.FXMLUtils;
import src.utils.SessionManager;

import java.io.IOException;

/**
 * Controller untuk dashboard utama System Manager.
 * Mengelola navigasi sidebar dan pemuatan konten halaman.
 */
public class SystemManagerDashboardController {
    @FXML
    private Label userInfoText;
    @FXML
    private Button userManagementButton;
    @FXML
    private Button patientManagementButton;
    @FXML
    private Button accountButton;
    @FXML
    private VBox pageContainer;
    @FXML
    private Button activeButton;
    @FXML
    public void initialize() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            userInfoText.setText(currentUser.getRole() + " " + currentUser.getUsername());
        }
        navigateToUserManagement();
    }
    
    public void navigateToUserManagement() {
        setActiveButton(userManagementButton);
        loadPageIntoContainer("/src/views/UserManagementPage.fxml");
    }

    public void navigateToPatientManagement() {
        setActiveButton(patientManagementButton);
        loadPageIntoContainer("/src/views/PatientManagementPage.fxml"); 
    }

    @FXML
    void handleUserManagement(ActionEvent event) {
        navigateToUserManagement();
    }

    @FXML
    void handlePatientManagement(ActionEvent event) {
        navigateToPatientManagement();
    }
    
    @FXML
    void handleAccount(ActionEvent event) {
        setActiveButton(accountButton);
        System.out.println("Account Page Clicked");
        // Ganti dengan path FXML untuk halaman Akun jika sudah dibuat
        // loadPageIntoContainer("/src/views/AccountPage.fxml");
    }

    private void loadPageIntoContainer(String fxmlPath) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));
            pageContainer.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman: " + fxmlPath);
        }
    }

    private void setActiveButton(Button button) {
        if (activeButton != null) {
            activeButton.getStyleClass().remove("sidebar-button-active");
        }
        
        activeButton = button;
        if (activeButton != null) {
            activeButton.getStyleClass().add("sidebar-button-active");
        }
    }

    // @FXML
    // private void handleLogout(ActionEvent event) {
    //     // Hapus sesi pengguna saat ini
    //     SessionManager.clearSession();
    //     // Gunakan FXMLUtils untuk kembali ke halaman login
    //     FXMLUtils.loadFXML(event, "/src/views/LoginPage.fxml");
    // }
}