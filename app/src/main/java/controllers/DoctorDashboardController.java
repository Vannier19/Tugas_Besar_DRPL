package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class DoctorDashboardController {

    @FXML private Label userInfoText;
    @FXML private VBox pageContainer;
    @FXML private Button myAppointmentsButton;
    @FXML private Button patientMedicalRecordButton;
    @FXML private Button givePrescriptionButton;
    @FXML private Button accountButton;
    
    private Button activeButton;

    @FXML
    public void initialize() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            userInfoText.setText("Dokter " + currentUser.getUsername());
        }
        handleMyAppointment(null);
    }

    @FXML
    void handleMyAppointment(ActionEvent event) {
        setActiveButton(myAppointmentsButton);
        loadPageIntoContainer("/views/MyAppointmentView.fxml");
    }

    @FXML
    void handlePatientMedicalRecord(ActionEvent event) {
        setActiveButton(patientMedicalRecordButton);
        loadPageIntoContainer("/views/PatientMedicalRecordPage.fxml");
    }

    @FXML
    void handleGivePrescription(ActionEvent event) {
        setActiveButton(givePrescriptionButton);
        loadPageIntoContainer("/views/GivePrescriptionPage.fxml");
    }

    @FXML
    void handleAccount(ActionEvent event) {
        setActiveButton(accountButton);
        loadPageIntoContainer("/views/AccountPage.fxml");
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
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadPageIntoContainer(String fxmlPath) {
        try {
            URL resourceUrl = getClass().getResource(fxmlPath);
            if (resourceUrl == null) {
                throw new IOException("Cannot find FXML file at " + fxmlPath);
            }
            Parent page = FXMLLoader.load(resourceUrl);
            pageContainer.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Failed to Load Page", e.getMessage());
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
}