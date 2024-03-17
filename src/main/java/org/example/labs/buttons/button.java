package org.example.labs.buttons;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class button {
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    private int plusOne = 1;
    private int minusOne = 1;

    @FXML
    private void click(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        switch (clickedButton.getId()){
            case "btnStart":
                btnStart.setText("Ты пидор" + plusOne);
                plusOne++;
                break;
            case "btnStop":
                btnStop.setText("Ты не пидор" + minusOne);
                minusOne++;
                break;
        }
    }

}
