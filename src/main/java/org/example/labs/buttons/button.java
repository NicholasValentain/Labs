package org.example.labs.buttons;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

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
    public RadioButton btnShowInfo, btnShowTime, btnHideTime;
    @FXML
    public CheckBox cbShowInfo;
    private int plusOne = 0;
    private int minusOne = 0;
    private Habitat habitat; // Добавляем поле для хранения ссылки на habitat
    private AntSimulation antSimulation;

    // Метод для установки ссылки на habitat
    public void setHabitat(Habitat habitat, AntSimulation antSimulation) {
        this.habitat = habitat;
        this.antSimulation = antSimulation;
    }


    @FXML
    private void click(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        switch (clickedButton.getId()) {
            case "btnStart":
                if (habitat != null && !antSimulation.startFlag) {
                    antSimulation.startSimulation();
                }
                break;
            case "btnStop":
                if (habitat != null && antSimulation.startFlag) {
                    antSimulation.stopSimulation();
                }
                break;
        }
    }

    @FXML
    private void check() { habitat.moreInfo = cbShowInfo.isSelected(); }
}
