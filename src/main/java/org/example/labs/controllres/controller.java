package org.example.labs.controllres;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.labs.model.Habitat; // Импортируем класс Habitat
import org.example.labs.main.AntSimulation;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public class controller {
    @FXML
    public Button btnStart, btnStop;
    @FXML
    public RadioButton ShowTime, HideTime;
    @FXML
    public CheckBox cbShowInfo;
    @FXML
    public TextField N1, N2, L1, L2;
    @FXML
    public ComboBox P1, P2;
    @FXML
    public TableView<Map.Entry<Integer, Long>> tableView;
    public TreeMap<Integer, Long> treeMap;
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
        L1.setText("1");
        L2.setText("1");
        ShowTime.setSelected(true);
        btnStop.setDisable(true);
        // Установка списка элементов в ComboBox
        P1.setItems(options);
        P2.setItems(options);
        P1.setValue("100");
        P2.setValue("100");


        tableView.setVisible(false);
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
    private void showTableView() {

        // Очистить таблицу перед добавлением новых данных
        tableView.getItems().clear();
        tableView.getColumns().clear();
        treeMap = habitat.spawnTimes;

        //tableView = new TableView<Map.Entry<Integer, Long>>();

        //ObservableList<Map.Entry<Integer, Long>> teamMembers = FXCollections.observableArrayList(treeMap.entrySet());


//        TableColumn<Map.Entry<Integer, Long>, Integer> IDColumn = (TableColumn<Map.Entry<Integer, Long>, Integer>) tableView.getColumns().get(0);
//        IDColumn.setCellValueFactory(new PropertyValueFactory<Map.Entry<Integer, Long>, Integer>("key"));
//        tableView.getColumns().add(IDColumn);
//
//        TableColumn<Map.Entry<Integer, Long>, Long> valueColumn = (TableColumn<Map.Entry<Integer, Long>, Long>) tableView.getColumns().get(1);
//        valueColumn.setCellValueFactory(new PropertyValueFactory<Map.Entry<Integer, Long>, Long>("value"));
//        tableView.getColumns().add(valueColumn);



//        tableView.setItems(teamMembers);
//
//        TableColumn<Map.Entry<Integer, Long>, Integer> IDColumn = new TableColumn<Map.Entry<Integer, Long>, Integer>("key");
//        IDColumn.setCellValueFactory(new PropertyValueFactory<Map.Entry<Integer, Long>, Integer>("key"));
//        tableView.getColumns().add(IDColumn);
//
//        TableColumn<Map.Entry<Integer, Long>, Long> valueColumn = new TableColumn<Map.Entry<Integer, Long>, Long>("value");
//        valueColumn.setCellValueFactory(new PropertyValueFactory<Map.Entry<Integer, Long>, Long>("value"));
//        tableView.getColumns().add(valueColumn);


        // Добавляем данные в колонки таблицы
        TableColumn<Map.Entry<Integer, Long>, Integer> keyColumn = new TableColumn<>("Key");
        keyColumn.setCellValueFactory(cellData -> {
            Integer key = cellData.getValue().getKey();
            return new javafx.beans.value.ObservableValueBase<Integer>() {
                @Override
                public Integer getValue() {
                    return key;
                }
            };
        });

        TableColumn<Map.Entry<Integer, Long>, Long> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(cellData -> {
            Long value = cellData.getValue().getValue();
            return new javafx.beans.value.ObservableValueBase<Long>() {
                @Override
                public Long getValue() {
                    return value;
                }
            };
        });

        tableView.getColumns().addAll(keyColumn, valueColumn);

        // Добавляем данные в таблицу
        for (Map.Entry<Integer, Long> entry : treeMap.entrySet()) {
            tableView.getItems().add(entry);
        }

        // Показываем TableView после создания
        tableView.setVisible(true);
    }

}
