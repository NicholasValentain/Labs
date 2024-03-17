package org.example.labs.buttons;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.labs.model.Habitat; // Импортируем класс Habitat

public class button {
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    private Habitat habitat; // Добавляем поле для хранения ссылки на habitat

    // Метод для установки ссылки на habitat
    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
    }

    @FXML
    private void click(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        switch (clickedButton.getId()) {
            case "btnStart":
                if (habitat != null) {
                    habitat.startSimulation(); // Запускаем симуляцию при нажатии на кнопку старта
                }
                break;
            case "btnStop":
                if (habitat != null) {
                    habitat.stopSimulation(); // Останавливаем симуляцию при нажатии на кнопку стопа
                }
                break;
        }
    }

}
