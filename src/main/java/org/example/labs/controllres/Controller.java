package org.example.labs.controllres;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.labs.model.Habitat; // Импортируем класс Habitat
import org.example.labs.main.AntSimulation;
import org.example.labs.model.WarriorAntAI;
import org.example.labs.model.WorkerAntAI;

import java.io.IOException;
import java.util.TreeMap;


public class Controller {
    @FXML
    public Button btnStart, btnStop, btnList, btnStopWorkerAI, btnStopWarriorAI;
    @FXML
    public RadioButton ShowTime, HideTime;
    @FXML
    public CheckBox cbShowInfo;
    @FXML
    public TextField N1, N2, L1, L2;
    @FXML
    public ComboBox P1, P2, WorkerPriority, WarriorPriority;

    //@FXML
    //public TableView<Map.Entry<Integer, Long>> tableView;

    public TreeMap<Integer, Long> treeMap;
    private Habitat habitat; // Добавляем поле для хранения ссылки на habitat
    private AntSimulation antSimulation;

    // Создание списка элементов для ComboBox
    ObservableList<String> options = FXCollections.observableArrayList(
            "10", "20", "30", "40", "50", "60", "70", "80", "90", "100" );
    ObservableList<String> prioritys = FXCollections.observableArrayList(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" );

    // Метод для установки ссылки на habitat
    public void setHabitat(Habitat habitat, AntSimulation antSimulation) {
        this.habitat = habitat;
        this.antSimulation = antSimulation;
        N1.setText("1");
        N2.setText("1");
        L1.setText("1");
        L2.setText("1");
        ShowTime.setSelected(true);
        btnStop.setDisable(true);
        // Установка списка элементов в ComboBox
        P1.setItems(options);
        P2.setItems(options);
        P1.setValue("100");
        P2.setValue("100");
        WorkerPriority.setItems(prioritys);
        WarriorPriority.setItems(prioritys);
        WorkerPriority.setValue("5");
        WarriorPriority.setValue("5");


        //tableView.setVisible(false);
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
                    L1.setDisable(true);
                    L2.setDisable(true);
                    P1.setDisable(true);
                    P2.setDisable(true);
                    WorkerPriority.setDisable(true);
                    WarriorPriority.setDisable(true);
                    cbShowInfo.setDisable(true);



                    //WorkerAntAI.getInstance().isActive = true;
                    //WorkerAntAI.getInstance().monitor.notify();
                    //WarriorAntAI.getInstance().isActive = true;
                   //WarriorAntAI.getInstance().monitor.notify();

                    WorkerAntAI Workerth = WorkerAntAI.getInstance();
                    WarriorAntAI Warriorth = WarriorAntAI.getInstance();


                    if (btnStopWorkerAI.getText().equals("Рабочих: ON")) {
                        Workerth.isActive = true;
                        String monitor = Workerth.monitor;
                        synchronized (monitor) {
                            monitor.notify();
                        }
                    }

                    if (btnStopWarriorAI.getText().equals("Солдат: ON")) {
                        Warriorth.isActive = true;
                        String monitor = Warriorth.monitor;
                        synchronized (monitor) {
                            monitor.notify();
                        }
                    }


                    antSimulation.startSimulation();
                }
                break;
            case "btnStop":
                if (habitat != null && antSimulation.startFlag) {
                    if(cbShowInfo.isSelected()) {
                        btnStart.setDisable(true);
                        btnStop.setDisable(true);
                    }
                    if(!cbShowInfo.isSelected()) {
                        N1.setDisable(false);
                        N2.setDisable(false);
                        L1.setDisable(false);
                        L2.setDisable(false);
                        P1.setDisable(false);
                        P2.setDisable(false);
                        WorkerPriority.setDisable(false);
                        WarriorPriority.setDisable(false);
                        cbShowInfo.setDisable(false);
                    }

                    WorkerAntAI.getInstance().isActive = false;
                    WarriorAntAI.getInstance().isActive = false;

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
            //HideTime.setSelected(false);
        }
        else if (HideTime.isSelected() && antSimulation.timerVisible) {
            antSimulation.timerVisible = false;
            antSimulation.root.getChildren().remove(antSimulation.times);
            //ShowTime.setSelected(false);
        }
    }

    @FXML
    private void check() { habitat.moreInfo = cbShowInfo.isSelected(); }

    @FXML
    public boolean checkError() {
        int n1 = 1;
        int n2 = 1;
        int l1 = 1;
        int l2 = 1;

        try {
            n1 = Integer.parseInt(N1.getText());
            n2 = Integer.parseInt(N2.getText());
            l1 = Integer.parseInt(L1.getText());
            l2 = Integer.parseInt(L2.getText());

            if (n1 < 1 || n2 < 1 || l1 < 1 || l2 < 1) throw new NumberFormatException("Число меньше 1");


            habitat.N1 = n1;
            habitat.N2 = n2;
            habitat.L1 = l1;
            habitat.L2 = l2;

            habitat.P1 = Double.parseDouble((String) P1.getValue()) / 100;
            habitat.P2 = Double.parseDouble((String) P2.getValue()) / 100;
            WorkerAntAI.getInstance().setPriority(Integer.parseInt((String) WorkerPriority.getValue()));
            WarriorAntAI.getInstance().setPriority(Integer.parseInt((String) WarriorPriority.getValue()));


        }
        catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText("Некорректный период рождения");
            alert.setContentText("Требуется целое положительное число!");

            // Проверяем, на некорректный ввод
            if (!N1.getText().matches("\\d+") || n1 < 1) N1.setText("1");
            if (!N2.getText().matches("\\d+") || n2 < 1) N2.setText("1");
            if (!L1.getText().matches("\\d+") || l1 < 1) L1.setText("1");
            if (!L2.getText().matches("\\d+") || l2 < 1) L2.setText("1");

            // Get the Stage.
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            // Add a custom icon.
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/labs/icon/error.png")));
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
                    L1.setDisable(true);
                    L2.setDisable(true);
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
                    L1.setDisable(false);
                    L2.setDisable(false);
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
    @FXML
    private void openDialog() {
        try {
            treeMap = habitat.spawnTimes;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/labs/dialogList.fxml"));
            Parent root = fxmlLoader.load();
            DialogListController dialogController = fxmlLoader.getController();
            dialogController.setTreeMap(treeMap); // Устанавливаем TreeMap

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Текущие объекты");
            stage.getIcons().add(new Image(getClass().getResource("/org/example/labs/icon/table.png").toExternalForm()));
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Показываем окно и ждем, пока его закроют
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void controlWorkerAI(){
        String monitor = WorkerAntAI.getInstance().monitor;
        if (btnStopWorkerAI.getText().equals("Рабочих: ON")) {
            WorkerAntAI.getInstance().isActive = false;
            btnStopWorkerAI.setText("Рабочих: OFF");
        }
        else {
            WorkerAntAI.getInstance().isActive = true;
            synchronized (monitor) {
                monitor.notify();
            }
            btnStopWorkerAI.setText("Рабочих: ON");
        }
    }

    @FXML
    private void controlWarriorAI(){
        String monitor = WarriorAntAI.getInstance().monitor;
        if (btnStopWarriorAI.getText().equals("Солдат: ON")) {
            WarriorAntAI.getInstance().isActive = false;
            btnStopWarriorAI.setText("Солдат: OFF");
        }
        else {
            WarriorAntAI.getInstance().isActive = true;
            synchronized (monitor) {
                monitor.notify();
            }
            btnStopWarriorAI.setText("Солдат: ON");
        }
    }
}
