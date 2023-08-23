package gui.components.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PredictionApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Prediction Application");
        Parent load = FXMLLoader.load(getClass().getResource("application.fxml"));
        primaryStage.getIcons().add(
                new Image(
                        PredictionApplication.class.getResourceAsStream( "prediction-icon.png" )));
        Scene scene = new Scene(load);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}