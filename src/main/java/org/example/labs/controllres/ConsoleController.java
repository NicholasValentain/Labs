package org.example.labs.controllres;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.example.labs.main.AntSimulation;

public class ConsoleController {
    @FXML
    private TextArea textArea = new TextArea();
    private String[] commands = new String[] { "start", "stop" };
    private Controller controller;
    private AntSimulation antSimulation;
    private int charNum;

    public void setCommands(Controller controller, AntSimulation antSimulation) {
        this.controller = controller;
        this.antSimulation = antSimulation;
        initialize(); // Вызываем метод initialize после того, как установили TreeMap
    }

    @FXML
    private void initialize() {
        textArea.setText("Shindows PowerRanger\n(C) Корпорация Говнософт (Shitsoft Corporation). У нас нет прав.\n");
        textArea.positionCaret(textArea.getText().length()); // Устанавливаем курсор в конец текста
        charNum = textArea.getText().length(); // Устанавливаем текущее колличество символов
        StringBuilder userInput = new StringBuilder(); // Инициализируем StringBuilder для хранения ввода пользователя

        // Устанавливаем обработчик события ввода текста для TextArea
        textArea.setOnKeyTyped(event -> {
            // Проверяем, была ли нажата клавиша Enter
            if (event.getCharacter().equals("\r") || event.getCharacter().equals("\n")) {
                // Делаем что-то с пользовательским вводом
                if (userInput.toString().trim().equals(commands[0])) {
                    System.out.println("start" + "==" + userInput.toString().trim());
                    if (!antSimulation.startFlag) {
                        controller.start();
                        textArea.appendText("Simulation started\n");
                    } else {
                        textArea.appendText("The simulation is already running\n");
                    }
                    textArea.positionCaret(textArea.getText().length());
                } else if (userInput.toString().trim().equals(commands[1])) {
                    System.out.println("stop" + "==" + userInput.toString().trim());
                    if (antSimulation.startFlag) {
                        controller.stop();
                        textArea.appendText("Simulation stopped\n");
                    } else {
                        textArea.appendText("Simulation not running\n");
                    }
                    textArea.positionCaret(textArea.getText().length());
                } else {
                    System.out.println("Command not found");
                    textArea.appendText("Command not found\n");
                    textArea.positionCaret(textArea.getText().length());
                }
                System.out.println("Пользователь ввел:" + userInput.toString().trim());

                userInput.setLength(0); // Очищаем StringBuilder для следующего ввода пользователя
                charNum = textArea.getText().length(); // Устанавливаем текущее колличество символов
            } else {
                if (!event.getCharacter().equals("\b") && !event.getCharacter().equals("\u007F")) {
                    userInput.append(event.getCharacter()); // Если нажата не клавиша Enter, добавляем символ в
                                                            // StringBuilder
                }
                System.out.println("else   " + userInput.toString().trim());
            }
        });

        // Обработчик для удаления
        textArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                System.out.println(userInput.length());
                if (userInput.length() > 0) {
                    userInput.setLength(userInput.length() - 1);
                }
                System.out.println(userInput.length() + " BACK_SPACE " + userInput.toString().trim());
            }
        });

        textArea.addEventFilter(MouseEvent.ANY, mouseEvent -> {
            mouseEvent.consume();
        }); // МЫШЬ НИЗЯ!
        // Нельзя перемещаться по строкам
        textArea.addEventFilter(KeyEvent.ANY, keyEvent -> {
            if (textArea.getText().length() < charNum) {
                textArea.appendText("\n");
                System.out.println("!!!!!!!!!!!!!!");
            }
            switch (keyEvent.getCode()) {
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                case PAGE_UP:
                case PAGE_DOWN:
                case HOME:
                case END:
                    keyEvent.consume();
                    break;
            }
        });
    }
}
