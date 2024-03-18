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



public class button {
    @FXML
    public Button btnStart, btnStop;
    @FXML
    public RadioButton ShowTime, HideTime;
    @FXML
    public CheckBox cbShowInfo;
    @FXML
    public TextField N1, N2;
    @FXML
    public ComboBox P1, P2;
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
        ShowTime.setSelected(true);
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
                if (habitat != null && !antSimulation.startFlag && checkError()) {
                    btnStart.setDisable(true);
                    btnStop.setDisable(false);

                    N1.setDisable(true);
                    N2.setDisable(true);
                    P1.setDisable(true);
                    P2.setDisable(true);
                    cbShowInfo.setDisable(true);
                    antSimulation.startSimulation();
                }
                break;
            case "btnStop":
                if (habitat != null && antSimulation.startFlag) {
                    if(cbShowInfo.isSelected()) {
                        btnStart.setDisable(true);
                        btnStop.setDisable(true);
                    }

                    N1.setDisable(false);
                    N2.setDisable(false);
                    P1.setDisable(false);
                    P2.setDisable(false);
                    cbShowInfo.setDisable(false);
                    antSimulation.stopSimulation();
                }
                break;
        }
    }
    @FXML
    private void clickTimeSwitch() {
        if (ShowTime.isSelected() && !antSimulation.timerVisible) {
            antSimulation.timerVisible = true;
            antSimulation.root.getChildren().add(antSimulation.times);
            HideTime.setSelected(false);
        }
        else if (HideTime.isSelected() && antSimulation.timerVisible) {
            antSimulation.timerVisible = false;
            antSimulation.root.getChildren().remove(antSimulation.times);
            ShowTime.setSelected(false);
        }
    }

    @FXML
    private void check() { habitat.moreInfo = cbShowInfo.isSelected(); }

    @FXML
    public boolean checkError() {
        try {
            if(Integer.parseInt(N1.getText()) < 1 || Integer.parseInt(N2.getText()) < 1) {
                if (Integer.parseInt(N1.getText()) < 1) N1.setText("1");
                if (Integer.parseInt(N2.getText()) < 1) N2.setText("1");
                throw new NumberFormatException("Число меньше 1");
            }

            habitat.N1 = Integer.parseInt(N1.getText());
            habitat.N2 = Integer.parseInt(N2.getText());

            habitat.P1 = Double.parseDouble((String) P1.getValue()) / 100;
            habitat.P2 = Double.parseDouble((String) P2.getValue()) / 100;

        }
        catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText("Некорректный период рождения");
            alert.setContentText("Требуется целое положительное число!");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    @FXML
    private void handleMenuAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        switch (menuItem.getText()) {
            case "Выход":
                System.exit(0);
                break;
            case "Старт (B)":
                if (habitat != null && !antSimulation.startFlag) {
                    btnStart.setDisable(true);
                    btnStop.setDisable(false);

                    N1.setDisable(true);
                    N2.setDisable(true);
                    P1.setDisable(true);
                    P2.setDisable(true);
                    cbShowInfo.setDisable(true);
                    checkError();
                    antSimulation.startSimulation();
                }
                break;
            case "Стоп (E)":
                if (habitat != null && antSimulation.startFlag) {
                    if(cbShowInfo.isSelected()) {
                        btnStart.setDisable(true);
                        btnStop.setDisable(true);
                    }

                    N1.setDisable(false);
                    N2.setDisable(false);
                    P1.setDisable(false);
                    P2.setDisable(false);
                    cbShowInfo.setDisable(false);
                    antSimulation.stopSimulation();
                }
                break;
        }
    }

    @FXML
    private void clickTime() {
        // Изменяем состояние видимости таймера и обновляем чекбоксы
        if (antSimulation.timerVisible) {
            antSimulation.root.getChildren().remove(antSimulation.times);
            antSimulation.timerVisible = false;
            HideTime.setSelected(true);
            ShowTime.setSelected(false);
        } else {
            antSimulation.root.getChildren().add(antSimulation.times);
            antSimulation.timerVisible = true;
            ShowTime.setSelected(true);
            HideTime.setSelected(false);
        }
    }

    @FXML
    private void handleTimerMenuAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        if ("Показать/Скрыть (T)".equals(menuItem.getText())) {
            if (antSimulation != null) {
                // Изменяем состояние видимости таймера и обновляем чекбоксы
                antSimulation.setTimerVisible(!antSimulation.isTimerVisible());
                ShowTime.setSelected(antSimulation.isTimerVisible());
                HideTime.setSelected(!antSimulation.isTimerVisible());
            }
        }
    }

    @FXML
    private void clickInf() {
        if(!cbShowInfo.isDisable()) {
            if(!cbShowInfo.isSelected()) {
                cbShowInfo.setSelected(true);
                habitat.moreInfo = cbShowInfo.isSelected();
            }
            else {
                cbShowInfo.setSelected(false);
                habitat.moreInfo = cbShowInfo.isSelected();
            }
        }
    }
}
