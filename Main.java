package application;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            LogManager.getLogManager().reset();
            Logger globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
            globalLogger.setLevel(Level.SEVERE);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login2.fxml"));
            Scene scene = new Scene(loader.load());

            primaryStage.setScene(scene);
            primaryStage.setTitle("Al-Maher Mobile");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
