package org.example.labs.controllres;

import javafx.fxml.FXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Map;
import java.util.TreeMap;
import java.time.Duration;


public class DialogListController {
    @FXML
    private TableView<Map.Entry<Integer, Long>> tableView = new TableView<>();
    private TreeMap<Integer, Long> dataMap;

    public DialogListController(TreeMap<Integer, Long> dataMap) {
        this.dataMap = dataMap;
    }
    public DialogListController() {
        dataMap = new TreeMap<Integer, Long>();
    }

    public void setTreeMap(TreeMap<Integer, Long> dataMap) {
        this.dataMap = dataMap;
        initialize(); // Вызываем метод initialize после того, как установили TreeMap
    }

    @FXML
    private void initialize() {
        //tableView.getItems().clear();
        tableView.getColumns().clear();

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

        // В вашем методе initialize() в классе DialogListController
        TableColumn<Map.Entry<Integer, Long>, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(cellData -> {
            Long seconds = cellData.getValue().getValue();
            Duration duration = Duration.ofSeconds(seconds);
            return new SimpleStringProperty(formatDuration(duration));
        });

        tableView.getColumns().addAll(keyColumn, valueColumn);

        // Добавляем данные в таблицу
        for (Map.Entry<Integer, Long> entry : dataMap.entrySet()) {
            tableView.getItems().add(entry);
        }
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
