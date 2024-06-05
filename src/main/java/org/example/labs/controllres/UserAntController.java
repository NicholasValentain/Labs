package org.example.labs.controllres;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserAntController {

    @FXML
    private TextField antCountField;

    private int antCount;

    @FXML
    private void handleOk() {
        try {
            antCount = Integer.parseInt(antCountField.getText());
            Stage stage = (Stage) antCountField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            antCountField.setStyle("-fx-border-color: red;");
        }
    }

    public int getAntCount() {
        return antCount;
    }


}
