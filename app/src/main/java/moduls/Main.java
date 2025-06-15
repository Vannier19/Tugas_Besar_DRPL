package main.java.moduls;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlLocation = getClass().getResource("/views/LoginPage.fxml");
        if (fxmlLocation == null) {
            System.err.println("ERROR: LoginPage.fxml not found at the specified path!");
            return; 
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Klinik Sehat Medika - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    // java --module-path "C:JavaFX/javafx-sdk-24.0.1/lib" --add-modules javafx.controls,javafx.fxml src/Main.java
}