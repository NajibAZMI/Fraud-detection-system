
package com.frauddetector.frontend;
import com.frauddetector.backend.FraudDetectionJob;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new
                FXMLLoader(getClass().getResource("/frontend/TransactionView.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Real-Time Fraud Detection System");
        primaryStage.setScene(scene);
        primaryStage.show();
        // Initialize backend in a separate thread
        new Thread(() -> {
            try {
                FraudDetectionJob.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
