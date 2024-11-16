package com.frauddetector.frontend;
import com.frauddetector.backend.FraudDetectionJob;
import com.frauddetector.backend.TransactionSource;
import com.frauddetector.frontend.models.Transaction;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.flink.streaming.api.datastream.DataStream;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new
                FXMLLoader(getClass().getResource("/frontend/TransactionView.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Real-Time Fraud Detection System");
        primaryStage.setScene(scene);
        primaryStage.show();
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
