module org.example.frauddetectionsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.frauddetector to javafx.fxml;
    exports org.example.frauddetector;
}