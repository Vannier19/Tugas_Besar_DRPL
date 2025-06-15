package main.java.controllers;
import main.java.moduls.*;
import main.java.utils.FXMLUtils;
import main.java.utils.SessionManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {
    @FXML private Label userInfoText;
    @FXML private VBox pageContainer;
    @FXML private Button viewAppointmentButton;
    @FXML private Button addAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button updateAppointmentButton;
    @FXML private Button accountButton;

    private Button activeButton;

    @FXML
    public void initialize() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            userInfoText.setText("Administrator " + currentUser.getUsername());
        }
        handleViewAppointment(null);
    }

    @FXML
    void handleViewAppointment(ActionEvent event) {
        setActiveButton(viewAppointmentButton);
        loadPageIntoContainer("/views/ViewAppointmentPage.fxml");
    }
    
    @FXML
    void handleAddAppointment(ActionEvent event) {
        setActiveButton(addAppointmentButton);
        loadPageIntoContainer("/views/AddAppointmentPage.fxml");
    }

    @FXML
    void handleDeleteAppointment(ActionEvent event) {
        setActiveButton(deleteAppointmentButton);
        loadPageIntoContainer("/views/DeleteAppointmentPage.fxml");
    }
    
    @FXML
    void handleUpdateAppointment(ActionEvent event) {
        setActiveButton(updateAppointmentButton);
        loadPageIntoContainer("/views/UpdateAppointmentPage.fxml");
    }
    
    @FXML
    void handleAccount(ActionEvent event) {
        setActiveButton(accountButton);
        loadPageIntoContainer("/views/AccountPage.fxml");
    }

    private void loadPageIntoContainer(String fxmlPath) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));
            pageContainer.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
            FXMLUtils.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Failed to Load Page", "Could not load " + fxmlPath);
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
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}