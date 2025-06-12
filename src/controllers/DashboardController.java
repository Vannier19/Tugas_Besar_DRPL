package src.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane; 
import javafx.scene.layout.VBox;
import javafx.scene.text.Text; 
import javafx.stage.Stage;
import javafx.scene.layout.Region; 
import javafx.scene.layout.Priority; 

import src.User;
import src.utils.FXMLUtils;
import src.utils.SessionManager;

import java.io.IOException;

public class DashboardController {
    @FXML private Label loggedInUserLabel;
    @FXML private Button patientManagementButton;
    @FXML private Button medicineManagementButton;
    @FXML private Button schedulingButton;
    @FXML private Button accountButton; 
    @FXML private VBox contentArea;
    @FXML private Text welcomeText; 
    @FXML private Text roleText; 
    @FXML private BorderPane rootBorderPane;

    @FXML
    public void initialize() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            loggedInUserLabel.setText(currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            welcomeText.setText("Selamat Datang di Sistem Manajemen Klinik Sehat Medika!");
            roleText.setText("Anda login sebagai: " + currentUser.getRole());
            updateMenuVisibility(currentUser.getRole());
        } else {
            loggedInUserLabel.setText("Not logged in");
            welcomeText.setText("Selamat Datang!");
            roleText.setText("Peran: N/A");
            patientManagementButton.setVisible(false);
            medicineManagementButton.setVisible(false);
            schedulingButton.setVisible(false);
            accountButton.setVisible(false); 
        }
    }

    private void updateMenuVisibility(String role) {
        patientManagementButton.setVisible(false);
        medicineManagementButton.setVisible(false);
        schedulingButton.setVisible(false);
        accountButton.setVisible(true);

        switch (role) {
            case "Dokter":
                patientManagementButton.setVisible(true); 
                medicineManagementButton.setVisible(true); 
                schedulingButton.setVisible(true);
                loadContent("/src/views/PatientManagementPage.fxml", "Manajemen Pasien"); 
                break;
            case "Administrator":
                schedulingButton.setVisible(true);
                loadContent("/src/views/SchedulingPage.fxml", "Penjadwalan");
                break;
            default:
                break;
        }
    }

    // Handlers for sidebar buttons
    @FXML
    private void handlePatientManagement() {
        loadContent("/src/views/PatientManagementPage.fxml", "Manajemen Pasien");
    }

    @FXML
    private void handleMedicineManagement() {
        loadContent("/src/views/MedicineManagementPage.fxml", "Manajemen Obat");
    }

    @FXML
    private void handleScheduling() {
        loadContent("/src/views/SchedulingPage.fxml", "Penjadwalan");
    }

    @FXML
    private void handleAccount() {
        loadContent("/src/views/AccountPage.fxml", "Akun Saya");
    }

    private void loadContent(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();
            rootBorderPane.setCenter(content);
            welcomeText.setVisible(false);
            roleText.setVisible(false);
         
            if (content instanceof Region) {
                Region contentRegion = (Region) content;
                contentRegion.prefWidthProperty().bind(rootBorderPane.widthProperty().subtract(rootBorderPane.getLeft().prefWidth(-1))); // Subtract sidebar width
                contentRegion.prefHeightProperty().bind(rootBorderPane.heightProperty().subtract(rootBorderPane.getTop().prefHeight(-1))); // Subtract top bar height
            } else {
                System.err.println("Peringatan: Root FXML yang dimuat bukan Region dan mungkin tidak akan menyesuaikan ukuran secara dinamis.");
            }
            
            Stage stage = (Stage) rootBorderPane.getScene().getWindow(); 
            stage.setTitle("Sistem Manajemen Klinik Sehat Medika - " + title);
        } catch (IOException e) {
            e.printStackTrace();
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Gagal Memuat Halaman", "Tidak dapat memuat " + title + ".");
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.clearSession();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/views/LoginPage.fxml"));
            Parent loginPage = loader.load();

            Stage stage = (Stage) rootBorderPane.getScene().getWindow(); 
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
