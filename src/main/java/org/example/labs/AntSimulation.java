package org.example.labs;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class AntSimulation extends Application {
    private long simulationStartTime; // Время начала симуляции
    private boolean startFlag = false; // Флаг для проверки работы симуляции
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-image: url('soil2.png'); -fx-background-size: cover;"); // Установка фона окна
        Habitat habitat = new Habitat(root);

        Text descriptionText = new Text("Press 'B' to start simulation, 'E' to stop simulation");
        descriptionText.setFont(Font.font("Arial Rounded MT", 35)); // Устанавливаем шрифт Arial Rounded MT размером 35
        Color customColor = Color.rgb(215,125,49); // Создаем свой собственный цвет
        descriptionText.setFill(customColor); // Устанавливаем цвет текста на наш
        descriptionText.setStroke(Color.BLACK); // Устанавливаем чёрный контур
        descriptionText.setStrokeWidth(2.0); // Устанавливаем толщину обводки
        descriptionText.setStrokeType(StrokeType.OUTSIDE); // Устанавливаем тип обводки
        root.getChildren().add(descriptionText);

        Scene scene = new Scene(root, 1200, 900); // Основное окно

        Rectangle rectangle = new Rectangle(); // Окно для информации о времени
        Text times = new Text(); // Текст для информации о времени

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.B && !startFlag) {
                startFlag = true;
                root.getChildren().remove(descriptionText);
                simulationStartTime = System.currentTimeMillis(); // Запускаем таймер
                habitat.startSimulation(); // Запускаем симуляцию
            }
            else if (event.getCode() == KeyCode.E && startFlag) {
                startFlag = false;
                habitat.stopSimulation(); // Останавливаем симуляцию
            }
            else if (event.getCode() == KeyCode.T && startFlag) {
                boolean isRectangleShown = root.getChildren().contains(rectangle); // Ключ для открытия/закрытия инфы

                if (!isRectangleShown) {
                    StackPane.setAlignment(rectangle, Pos.TOP_LEFT); // Отменяем центрирование
                    rectangle.setTranslateX(1000); // Устанавливаем координату X
                    rectangle.setTranslateY(0); // Устанавливаем координату Y
                    rectangle.setWidth(200); // Устанавливаем ширину
                    rectangle.setHeight(50); // Устанавливаем высоту
                    rectangle.setFill(Color.WHITE);// Устанавливаем цвет заливки прямоугольника
                    root.getChildren().add(rectangle);// Добавляем прямоугольник на сцену

                    StackPane.setAlignment(times, Pos.TOP_LEFT); // Отменяем центрирование
                    times.setFont(Font.font("Arial Rounded MT", 20)); // устанавливаем шрифт Arial Rounded MT размером 20
                    times.setFill(Color.BLACK); // устанавливаем цвет текста
                    times.setTranslateX(1005); // Устанавливаем координату X
                    times.setTranslateY(10); // Устанавливаем координату Y
                    root.getChildren().add(times);
                    // Определяем таймер для обновления времени
                    AnimationTimer timer = new AnimationTimer() {
                        @Override
                        public void handle(long now) {
                            long currentTime = System.currentTimeMillis();
                            long simulationTime = (currentTime - simulationStartTime) / 1000;
                            times.setText("Time: " + simulationTime); // Обновляем текст с текущим временем
                        }
                    };
                    timer.start(); // Начинаем таймер
                }
                else {
                    root.getChildren().remove(rectangle); // Если прямоугольник уже отображен, скрываем его
                    root.getChildren().remove(times); // Удаляем текст
                }

            }
        });

        // Добавление иконки
        primaryStage.getIcons().add(new Image("icon_ant.png")); // Установка иконки
        primaryStage.setTitle("Ant Simulation"); // Установка названия программы
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}