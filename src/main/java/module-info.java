module org.example.labs {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.labs to javafx.fxml;
    exports org.example.labs;
}