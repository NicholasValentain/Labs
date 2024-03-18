package org.example.labs.buttons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.example.labs.model.Habitat; // Импортируем класс Habitat
import org.example.labs.controller.AntSimulation;

import java.util.ArrayList;
import java.util.Random;


public class button {
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private RadioButton btnShowInfo, btnShowTime, btnHideTime;
    @FXML
    private CheckBox cbShowInfo;
    @FXML
    private TextField N1, N2;
    @FXML
    private ComboBox P1, P2;
    private int plusOne = 0;
    private int minusOne = 0;
    private Habitat habitat; // Добавляем поле для хранения ссылки на habitat
    private AntSimulation antSimulation;

    // Создание списка элементов для ComboBox
    ObservableList<String> options = FXCollections.observableArrayList(
            "10", "20", "30", "40", "50", "60", "70", "80", "90", "100" );

    // Метод для установки ссылки на habitat
    public void setHabitat(Habitat habitat, AntSimulation antSimulation) {
        this.habitat = habitat;
        this.antSimulation = antSimulation;
        N1.setText("1");
        N2.setText("1");
        // Установка списка элементов в ComboBox
        P1.setItems(options);
        P2.setItems(options);
        P1.setValue("10");
        P2.setValue("100");
    }


    @FXML
    private void click(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        switch (clickedButton.getId()) {
            case "btnStart":
                if (habitat != null && !antSimulation.startFlag) {
                    btnStart.setDisable(true);
                    btnStop.setDisable(false);

                    N1.setDisable(true);
                    N2.setDisable(true);
                    P1.setDisable(true);
                    P2.setDisable(true);
                    checkError();
                    antSimulation.startSimulation();
                }
                break;
            case "btnStop":
                if (habitat != null && antSimulation.startFlag) {
                    btnStart.setDisable(false);
                    btnStop.setDisable(true);

                    N1.setDisable(false);
                    N2.setDisable(false);
                    P1.setDisable(false);
                    P2.setDisable(false);
                    antSimulation.stopSimulation();
                }
                break;
        }
    }

    @FXML
    private void check() { habitat.moreInfo = cbShowInfo.isSelected(); }

    @FXML
    private void checkError() {
        try {
            if(Integer.parseInt(N1.getText()) < 1 || Integer.parseInt(N2.getText()) < 1) {
                throw new NumberFormatException("Число меньше 1");
            }

            habitat.N1 = Integer.parseInt(N1.getText());
            habitat.N2 = Integer.parseInt(N2.getText());

            habitat.P1 = Double.parseDouble((String) P1.getValue()) / 100;
            habitat.P2 = Double.parseDouble((String) P2.getValue()) / 100;

        }
        catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Некорректный период рождения");
            alert.setContentText("Требуется целое положительное число!");
            alert.showAndWait();
        }
    }

}
