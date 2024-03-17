module org.example.labs {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.labs to javafx.fxml;
    exports org.example.labs.model;
    exports org.example.labs.controller;
    exports org.example.labs.buttons;
    opens org.example.labs.controller to javafx.fxml;
    opens org.example.labs.buttons to javafx.fxml;
}