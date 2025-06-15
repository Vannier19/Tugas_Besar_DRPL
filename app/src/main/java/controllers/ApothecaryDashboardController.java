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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.Callback;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    @FXML
    private StackPane rootStackPane;

    private Button activeButton;
    private User currentUser;
    private NotificationController notificationController;
    private PrescriptionFactory prescriptionFactory;

    private ObservableList<Notification> currentDisplayedNotifications;
    private ObservableList<Prescription> currentDisplayedPrescriptions;

    @FXML
    public void initialize() {
        notificationController = new NotificationController();
        prescriptionFactory = new PrescriptionFactory();
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
            notificationController.discoverNewNotifications(); 
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
            loader.setLocation(getClass().getResource(fxmlPath)); 
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
            FXMLUtils.showAlert(Alert.AlertType.ERROR, "Error", "Gagal Memuat Halaman", "Tidak dapat memuat " + title + ". Pastikan file FXML ada di lokasi yang benar.");
        }
    }

    public void showApothecaryWelcomeView() {
        dashboardCardsContainer.getChildren().clear();

        HBox topCardsHBox = new HBox(40.0);
        topCardsHBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(topCardsHBox, Priority.NEVER);

        VBox pharmacyCard = createWelcomeCard("Prescription Management", "/src/assets/prescription_icon.png", "#00D2A3", "Manage Prescriptions");
        pharmacyCard.setOnMouseClicked(event -> {
            handleViewPrescriptions();
        });

        VBox notificationsCard = createWelcomeCard("View Notifications", "/src/assets/notification_icon.png", "#FF6531", "View Alerts");
        notificationsCard.setOnMouseClicked(event -> handleViewNotifications());

        topCardsHBox.getChildren().addAll(pharmacyCard, notificationsCard);

        VBox bottomContentVBox = new VBox(20.0);
        bottomContentVBox.setPadding(new Insets(20));
        VBox.setVgrow(bottomContentVBox, Priority.ALWAYS);

        Label dataAnalyticsTitle = new Label("Data Analytics");
        dataAnalyticsTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        VBox analyticsContent = new VBox(10);
        analyticsContent.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;");
        analyticsContent.getChildren().addAll(
            new Label("Total Drugs in Stock: 6420/10000"),
            new Label("Prescriptions to process: " + prescriptionFactory.getAllPrescriptions().stream().filter(p -> "Pending".equals(p.getStatus())).count() + " [view details]"),
            new Label("Low stock alert: " + notificationController.getCurrentNotifications().stream().filter(n -> "LOW_STOCK".equals(n.getType())).count() + " [view details]"),
            new Label("Expiring soon: " + notificationController.getCurrentNotifications().stream().filter(n -> "EXPIRY_WARNING".equals(n.getType())).count() + " [view details]")
        );

        Label recentActivitiesTitle = new Label("Recent Activities");
        recentActivitiesTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        VBox activitiesContent = new VBox(5);
        activitiesContent.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;");
        activitiesContent.getChildren().addAll(
            new Label("Received prescription for doctor hansjoseph Yesterday 20:46:42"),
            new Label("Issued Amoxicillin to Jane Smith Yesterday 20:51:27"),
            new Label("Received: 125 from apothecary stevaneiner 10:45"),
            new Label("Received alert: low stock on penicillin 11:02")
        );

        bottomContentVBox.getChildren().addAll(
            dataAnalyticsTitle, analyticsContent,
            recentActivitiesTitle, activitiesContent
        );

        dashboardCardsContainer.getChildren().addAll(topCardsHBox, bottomContentVBox);

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
                handleViewPrescriptions();
            } else if ("View Alerts".equals(buttonText)) {
                handleViewNotifications();
            }
        });

        card.getChildren().addAll(icon, titleLabel, actionButton);
        return card;
    }

    @FXML
    private void handlePharmacyManagement() { 
        setActiveButton(pharmacyManagementButton);
        showApothecaryWelcomeView(); 
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
    private void handleViewPrescriptions() {
        currentDisplayedPrescriptions = FXCollections.observableArrayList();
        applyPrescriptionFilter("Pending");

        Pane overlayBackground = new Pane();
        overlayBackground.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        overlayBackground.setPrefSize(rootStackPane.getWidth(), rootStackPane.getHeight());

        VBox prescriptionModal = new VBox(15.0);
        prescriptionModal.setAlignment(Pos.TOP_CENTER);
        prescriptionModal.setPadding(new Insets(20));
        prescriptionModal.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.0, 0, 0);");
        prescriptionModal.setMaxWidth(900); 
        prescriptionModal.setMaxHeight(500);

        Label titleLabel = new Label("Manajemen Resep");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(0, 0, 10, 0));

        Label filterLabel = new Label("Filter by Status:");
        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("All", "Pending", "Processed", "Cancelled");
        filterComboBox.setValue("Pending");

        filterComboBox.setOnAction(e -> applyPrescriptionFilter(filterComboBox.getValue()));
        filterBox.getChildren().addAll(filterLabel, filterComboBox);

        ListView<Prescription> prescriptionListView = new ListView<>(currentDisplayedPrescriptions);
        prescriptionListView.setPrefHeight(300);
        prescriptionListView.setCellFactory(new Callback<ListView<Prescription>, ListCell<Prescription>>() {
            @Override
            public ListCell<Prescription> call(ListView<Prescription> listView) {
                return new ListCell<Prescription>() {
                    private final HBox hbox = new HBox(10);
                    private final VBox textVBox = new VBox(2);
                    private final Label detailsLabel = new Label();
                    private final Button processButton = new Button("Process");

                    {
                        hbox.setAlignment(Pos.CENTER_LEFT);
                        hbox.setPadding(new Insets(5, 0, 5, 0));
                        detailsLabel.setWrapText(true);
                        detailsLabel.setMaxWidth(prescriptionModal.getMaxWidth() - 250);

                        processButton.setStyle("-fx-background-color: #00D2A3; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 5 10; -fx-font-size: 12px;");
                        processButton.setOnAction(event -> {
                            Prescription item = getItem();
                            if (item != null) {
                                prescriptionFactory.updatePrescriptionStatus(item.getId(), "Processed");
                                FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Resep Diproses", "Resep berhasil diproses.", "Resep dengan ID: " + item.getId() + " telah ditandai sebagai 'Processed'.");
                                applyPrescriptionFilter(filterComboBox.getValue());
                                showApothecaryWelcomeView();
                            }
                        });
                        HBox.setHgrow(textVBox, Priority.ALWAYS);
                        textVBox.getChildren().add(detailsLabel);
                        hbox.getChildren().addAll(textVBox, processButton);
                    }

                    @Override
                    protected void updateItem(Prescription item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            detailsLabel.setText(
                                "ID Resep: " + item.getId() + "\n" +
                                "Pasien: " + item.getPatientId() + "\n" +
                                "Dokter: " + item.getDoctorId() + "\n" +
                                "Obat: " + item.getMedicineName() + "\n" +
                                "Dosis: " + item.getDosage() + "\n" +
                                "Jumlah: " + item.getQuantity() + "\n" +
                                "Catatan: " + item.getNotes() + "\n" +
                                "Tanggal Terbit: " + item.getIssueDate() + "\n" +
                                "Status: " + item.getStatus()
                            );
                            processButton.setVisible("Pending".equals(item.getStatus()));
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });


        Button closeButton = new Button("Tutup");
        closeButton.setStyle("-fx-background-color: #503E9D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 20;");
        closeButton.setOnAction(event -> {
            rootStackPane.getChildren().remove(overlayBackground);
            rootStackPane.getChildren().remove(prescriptionModal);
        });

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(closeButton);

        prescriptionModal.getChildren().addAll(titleLabel, filterBox, prescriptionListView, buttonBox);

        rootStackPane.getChildren().addAll(overlayBackground, prescriptionModal);

        StackPane.setAlignment(prescriptionModal, Pos.CENTER);
    }

    private void applyPrescriptionFilter(String filterType) {
        currentDisplayedPrescriptions.clear();
        List<Prescription> rawPrescriptions = prescriptionFactory.getAllPrescriptions();

        List<Prescription> tempFilteredList = rawPrescriptions.stream()
            .filter(p -> {
                switch (filterType) {
                    case "Pending":
                        return "Pending".equals(p.getStatus());
                    case "Processed":
                        return "Processed".equals(p.getStatus());
                    case "Cancelled":
                        return "Cancelled".equals(p.getStatus());
                    case "All":
                    default:
                        return true;
                }
            })
            .collect(Collectors.toList());

        if (tempFilteredList.isEmpty()) {
            Prescription infoPrescription = new Prescription(
                "NO_PRES",        
                "NO_PATIENT",     
                "NO_DOCTOR",      
                "Tidak ada resep yang cocok dengan filter ini.", 
                "NO_DOSAGE",     
                "1",              
                "INFO"              
            );
            infoPrescription.setStatus("INFO");
            currentDisplayedPrescriptions.add(infoPrescription);
        } else {
            currentDisplayedPrescriptions.addAll(tempFilteredList);
        }
    }

    @FXML
    private void handleViewNotifications() {
        notificationController.discoverNewNotifications();

        currentDisplayedNotifications = FXCollections.observableArrayList();
        applyNotificationFilter("All");

        Pane overlayBackground = new Pane();
        overlayBackground.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        overlayBackground.setPrefSize(rootStackPane.getWidth(), rootStackPane.getHeight());

        VBox notificationModal = new VBox(15.0);
        notificationModal.setAlignment(Pos.TOP_CENTER);
        notificationModal.setPadding(new Insets(20));
        notificationModal.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.0, 0, 0);");
        notificationModal.setMaxWidth(800);
        notificationModal.setMaxHeight(500);

        Label titleLabel = new Label("Peringatan & Notifikasi");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(0, 0, 10, 0));

        Label filterLabel = new Label("Filter by:");
        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("All", "Low Stock", "Expiring Soon", "Last 7 Days");
        filterComboBox.setValue("All");

        filterComboBox.setOnAction(e -> applyNotificationFilter(filterComboBox.getValue()));
        filterBox.getChildren().addAll(filterLabel, filterComboBox);

        ListView<Notification> notificationListView = new ListView<>(currentDisplayedNotifications);
        notificationListView.setPrefHeight(300);
        notificationListView.setCellFactory(new Callback<ListView<Notification>, ListCell<Notification>>() {
            @Override
            public ListCell<Notification> call(ListView<Notification> listView) {
                return new ListCell<Notification>() {
                    private final HBox hbox = new HBox(10);
                    private final Label messageLabel = new Label();
                    private final Button deleteButton = new Button("X");

                    {
                        hbox.setAlignment(Pos.CENTER_LEFT);
                        hbox.setPadding(new Insets(5, 0, 5, 0));
                        messageLabel.setWrapText(true);
                        messageLabel.setMaxWidth(notificationModal.getMaxWidth() - 150);

                        deleteButton.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 2 6; -fx-font-size: 10px;");
                        deleteButton.setOnAction(event -> {
                            Notification item = getItem();
                            if (item != null) {
                                notificationController.deleteNotification(item.getId());
                                applyNotificationFilter(filterComboBox.getValue());
                            }
                        });
                        HBox.setHgrow(messageLabel, Priority.ALWAYS);
                        hbox.getChildren().addAll(messageLabel, deleteButton);
                    }

                    @Override
                    protected void updateItem(Notification item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            if (item.getId().equals("NO_NOTIF")) {
                                messageLabel.setText(item.getMessage());
                                deleteButton.setVisible(false);
                            } else {
                                messageLabel.setText(item.getMessage());
                                deleteButton.setVisible(true);
                            }
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });


        Button closeButton = new Button("Tutup");
        closeButton.setStyle("-fx-background-color: #503E9D; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 20;");
        closeButton.setOnAction(event -> {
            rootStackPane.getChildren().remove(overlayBackground);
            rootStackPane.getChildren().remove(notificationModal);
        });

        Button clearAllButton = new Button("Clear All");
        clearAllButton.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 20;");
        clearAllButton.setOnAction(event -> {
            notificationController.clearAllNotifications();
            applyNotificationFilter("All");
            FXMLUtils.showAlert(Alert.AlertType.INFORMATION, "Notifikasi", "Semua notifikasi telah dihapus.", "Daftar notifikasi kosong.");
        });

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(closeButton, clearAllButton);

        notificationModal.getChildren().addAll(titleLabel, filterBox, notificationListView, buttonBox);

        rootStackPane.getChildren().addAll(overlayBackground, notificationModal);

        StackPane.setAlignment(notificationModal, Pos.CENTER);
    }

    private void applyNotificationFilter(String filterType) {
        currentDisplayedNotifications.clear();
        Date now = new Date();
        long sevenDaysMillis = TimeUnit.DAYS.toMillis(7);

        List<Notification> rawNotifications = notificationController.getCurrentNotifications();
        List<Notification> tempFilteredList = rawNotifications.stream()
            .filter(notif -> {
                switch (filterType) {
                    case "Low Stock":
                        return "LOW_STOCK".equals(notif.getType());
                    case "Expiring Soon":
                        return "EXPIRY_WARNING".equals(notif.getType());
                    case "Last 7 Days":
                        return notif.getDate().after(new Date(now.getTime() - sevenDaysMillis)) && notif.getDate().before(new Date(now.getTime() + TimeUnit.SECONDS.toMillis(1)));
                    case "All":
                    default:
                        return true;
                }
            })
            .collect(Collectors.toList());

        if (tempFilteredList.isEmpty()) {
            if (currentDisplayedNotifications.stream().noneMatch(n -> n.getId().equals("NO_NOTIF"))) {
                currentDisplayedNotifications.add(new Notification("NO_NOTIF", "Tidak ada notifikasi yang cocok dengan filter ini.", new Date(), "INFO"));
            }
        } else {
            currentDisplayedNotifications.removeIf(n -> n.getId().equals("NO_NOTIF"));
            currentDisplayedNotifications.addAll(tempFilteredList);
        }
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