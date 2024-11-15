module org.example.frauddetectionsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.frauddetectionsystem to javafx.fxml;
    exports org.example.frauddetectionsystem;
}