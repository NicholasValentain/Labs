module org.example.labs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens org.example.labs to javafx.fxml;

    exports org.example.labs.model;
    exports org.example.labs.main;
    exports org.example.labs.controllres;
    exports server; // Добавлено
    exports dto; // Добавлено

    opens org.example.labs.main to javafx.fxml;
    opens org.example.labs.controllres to javafx.fxml;

    requires java.base;
}
