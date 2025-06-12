package src.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox; 
import javafx.scene.layout.VBox;
import javafx.scene.text.Text; 
import javafx.stage.Stage;
import javafx.scene.layout.Region; 
import javafx.scene.layout.Priority; 
import javafx.scene.control.Alert; 
import javafx.application.Platform; 
import javafx.scene.image.Image; 
import javafx.scene.image.ImageView; 
import javafx.geometry.Pos; 
import javafx.geometry.Insets; 

import src.User;
import src.utils.FXMLUtils;
import src.utils.SessionManager;

import java.io.IOException;

public class ApothecaryDashboardController {
    @FXML
    private Label loggedInUserLabel; 
    @FXML
    private Text welcomeText;       
    @FXML
    private Label roleText;          
    @FXML
    private Button pharmacyManagementButton;
    @FXML
    private Button medicineManagementButton;
    @FXML
    private Button accountButton;
    @FXML
    private VBox contentArea; 
    @FXML
    private HBox welcomeHeaderBox; 
    @FXML
    private HBox roleHeaderBox;    
    @FXML
    private VBox dashboardCardsContainer; 

    private Button activeButton; 
    private User currentUser; 

    @FXML
    public void initialize() {
        currentUser = SessionManager.getCurrentUser();
        Platform.runLater(() -> {
            if (currentUser != null) {
                welcomeText.setText("Welcome, "); 
                loggedInUserLabel.setText(currentUser.getRole() + " " + currentUser.getUsername()); 
                roleText.setText("");
            } else {
                welcomeText.setText("Welcome, ");
                loggedInUserLabel.setText("Guest!");
                roleText.setText("Not logged in");
            }
            showApothecaryWelcomeView();
        });
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

    private void loadPageIntoContainer(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();

            Object controller = loader.getController();
            if (controller instanceof MedicineManagementPageController) {
                MedicineManagementPageController medicineController = (MedicineManagementPageController) controller;
                medicineController.setParentDashboardController(this);
            }

            if (content instanceof Region) {
                Region contentRegion = (Region) content;
                contentRegion.prefWidthProperty().bind(dashboardCardsContainer.widthProperty());
                contentRegion.prefHeightProperty().bind(dashboardCardsContainer.heightProperty());
                VBox.setVgrow(contentRegion, Priority.ALWAYS);
            } else {
                System.err.println("Peringatan: Root FXML yang dimuat bukan Region dan tidak dapat diubah ukurannya secara dinamis.");
            }

            dashboardCardsContainer.getChildren().setAll(content); 

            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setTitle("Sistem Manajemen Klinik Sehat Medika - " + title);

        } catch (IOException e) {
            e.printStackTrace();
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Gagal Memuat Halaman", "Tidak dapat memuat " + title + ".");
        }
    }

    public void showApothecaryWelcomeView() {
        if (activeButton != null) {
            activeButton.getStyleClass().remove("sidebar-button-active");
            activeButton = null; 
        }

        dashboardCardsContainer.getChildren().clear(); 

        HBox cardsHBox = new HBox(40.0); 
        cardsHBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(cardsHBox, Priority.ALWAYS);
        VBox pharmacyCard = createWelcomeCard("Prescription Management", "src/assets/prescription_icon.png", "#00D2A3", "Manage Prescriptions");
        pharmacyCard.setOnMouseClicked(event -> {
            setActiveButton(pharmacyManagementButton); 
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Informasi", "Fitur Belum Tersedia", "Manajemen Farmasi sedang dalam pengembangan.");
        }); 
        
        VBox notificationsCard = createWelcomeCard("View Notifications", "src/assets/notification_icon.png", "#FF6531", "View Alerts");
        notificationsCard.setOnMouseClicked(event -> {
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Informasi", "Fitur Belum Tersedia", "Fitur Notifikasi sedang dalam pengembangan.");
        }); 
        
        cardsHBox.getChildren().addAll(pharmacyCard, notificationsCard);
        dashboardCardsContainer.getChildren().add(cardsHBox);

        Stage stage = (Stage) contentArea.getScene().getWindow();
        if (stage != null) {
            stage.setTitle("Sistem Manajemen Klinik Sehat Medika - Dashboard Apoteker");
        }
    }

    private VBox createWelcomeCard(String title, String iconPath, String bgColor, String buttonText) {
        VBox card = new VBox(10.0); 
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 15; -fx-padding: 20;");
        card.setPrefWidth(300);

        ImageView icon = new ImageView();
        icon.setFitHeight(80.0);
        icon.setFitWidth(80.0);
        icon.setPickOnBounds(true);
        icon.setPreserveRatio(true);
        try {
            icon.setImage(new Image(getClass().getResourceAsStream(iconPath)));
        } catch (Exception e) {
            System.err.println("Error loading image: " + iconPath + " - " + e.getMessage());
            icon.setImage(new Image("https://placehold.co/80x80/cccccc/000000?text=ICON"));
        }

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Button actionButton = new Button(buttonText);
        actionButton.setStyle("-fx-background-color: white; -fx-text-fill: " + bgColor + "; -fx-background-radius: 8; -fx-padding: 8 15;");
        
        actionButton.setOnAction(event -> {
            if ("Manage Prescriptions".equals(buttonText)) {
                handlePharmacyManagement(); 
            } else if ("View Alerts".equals(buttonText)) {
                FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Informasi", "Fitur Belum Tersedia", "Fitur Notifikasi sedang dalam pengembangan.");
            }
        });

        card.getChildren().addAll(icon, titleLabel, actionButton);
        return card;
    }

    @FXML
    private void handlePharmacyManagement() {
        setActiveButton(pharmacyManagementButton);
        showApothecaryWelcomeView(); 
        FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Informasi", "Fitur Belum Tersedia", "Manajemen Farmasi sedang dalam pengembangan.");
    }

    @FXML
    private void handleMedicineManagement() {
        setActiveButton(medicineManagementButton);
        loadPageIntoContainer("/src/views/MedicineManagementPage.fxml", "Manajemen Obat");
    }

    @FXML
    private void handleAccount() {
        setActiveButton(accountButton);
        loadPageIntoContainer("/src/views/AccountPage.fxml", "Akun Saya");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.clearSession();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/views/LoginPage.fxml"));
            Parent loginPage = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow(); 
            Scene loginScene = new Scene(loginPage); 
            
            stage.setScene(loginScene);
            stage.setWidth(600);    
            stage.setHeight(450);   
            stage.setResizable(false); 
            stage.centerOnScreen(); 
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Gagal Logout", "Tidak dapat kembali ke halaman login.");
        }
    }
}
