package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlLocation = getClass().getResource("/src/views/PatientManagementPage.fxml");
        System.out.println("FXML Location: " + fxmlLocation); // <--- Add this line

        if (fxmlLocation == null) {
            System.err.println("ERROR: FXML file not found at the specified path!");
            // You might want to throw a more specific exception or show an alert here
            return; // Prevent NullPointerException
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Sistem Manajemen Klinik Sehat Medika - Manajemen Pasien");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}