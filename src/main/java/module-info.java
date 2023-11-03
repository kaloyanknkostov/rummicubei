module com.example.rumikub_demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.example.GUI to javafx.fxml;
    exports com.example.GUI;
}