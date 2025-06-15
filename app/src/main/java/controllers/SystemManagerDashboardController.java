package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage; 
import javafx.scene.Scene; 

import java.io.IOException;

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
        loadPageIntoContainer("/views/UserManagementPage.fxml");
    }

    public void navigateToPatientManagement() {
        setActiveButton(patientManagementButton);
        loadPageIntoContainer("/views/PatientManagementPage.fxml"); 
    }

    public void navigateToAccount() {
        setActiveButton(accountButton); 
        loadPageIntoContainer("/views/AccountPage.fxml");
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
        navigateToAccount(); 
    }

    private void loadPageIntoContainer(String fxmlPath) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));
            pageContainer.getChildren().setAll(page); 
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman: " + fxmlPath);
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Gagal Memuat Halaman", "Tidak dapat memuat " + fxmlPath + ".");
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
