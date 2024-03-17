package org.example.labs.controller;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.labs.model.Habitat;

import java.io.IOException;

public class AntSimulation extends Application {
    private long simulationStartTime; // Время начала симуляции
    private boolean startFlag = false; // Флаг для проверки работы симуляции
    private StackPane AntList = new StackPane();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        StackPane root = new StackPane();
        Image Backgroundimg = new Image(getClass().getResourceAsStream("/org/example/labs/Background/soil2.png"));
        BackgroundSize bsize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true);
        BackgroundImage bImg = new BackgroundImage(Backgroundimg, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, bsize);
        Background bGround = new Background(bImg);
        root.setBackground(bGround);
        //root.setStyle("-fx-background-image: url('/org/example/labs/Background/soil2.png'); -fx-background-size: cover;"); // Установка фона окна


        root.getChildren().add(AntList);

        Habitat habitat = new Habitat(root, AntList);

        Text descriptionText = new Text("Press 'B' to start simulation, 'E' to stop simulation");
        descriptionText.setFont(Font.font("Arial Rounded MT", 35)); // Устанавливаем шрифт Arial Rounded MT размером 35
        StackPane.setAlignment(descriptionText, Pos.TOP_LEFT); // Отменяем центрирование
        descriptionText.setTranslateX(200); // Устанавливаем координату X
        descriptionText.setTranslateY(400); // Устанавливаем координату Y
        Color customColor = Color.rgb(215,125,49); // Создаем свой собственный цвет
        descriptionText.setFill(customColor); // Устанавливаем цвет текста на наш
        descriptionText.setStroke(Color.BLACK); // Устанавливаем чёрный контур
        descriptionText.setStrokeWidth(2.0); // Устанавливаем толщину обводки
        descriptionText.setStrokeType(StrokeType.OUTSIDE); // Устанавливаем тип обводки
        root.getChildren().add(descriptionText);

        Scene scene = new Scene(root, 1600, 900); // Основное окно

        // Окно для кнопок
        Rectangle rectangleManagement = new Rectangle();
        StackPane.setAlignment(rectangleManagement, Pos.TOP_LEFT); // Отменяем центрирование
        rectangleManagement.setTranslateX(1200); // Устанавливаем координату X
        rectangleManagement.setTranslateY(0); // Устанавливаем координату Y
        rectangleManagement.setWidth(400); // Устанавливаем ширину
        rectangleManagement.setHeight(900); // Устанавливаем высоту
        rectangleManagement.setFill(Color.WHITE);// Устанавливаем цвет заливки прямоугольника
        root.getChildren().add(rectangleManagement);// Добавляем прямоугольник на сцену


        StackPane FXMLstackPane = new StackPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/labs/hello-view.fxml"));
        Node buttonNode = loader.load();
        FXMLstackPane.getChildren().add(buttonNode); // Добавление кнопки в StackPane

        root.getChildren().add(FXMLstackPane); // Добавление кнопок в основной StackPane

        Rectangle rectangleTime = new Rectangle(); // Окно для информации о времени
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
                root.getChildren().remove(rectangleTime); // Если прямоугольник уже отображен, скрываем его
                root.getChildren().remove(times); // Удаляем текст
                habitat.stopSimulation(); // Останавливаем симуляцию
            }
            else if (event.getCode() == KeyCode.T && startFlag) {
                boolean isRectangleShown = root.getChildren().contains(rectangleTime); // Ключ для открытия/закрытия информации

                if (!isRectangleShown) {
                    StackPane.setAlignment(rectangleTime, Pos.TOP_LEFT); // Отменяем центрирование
                    rectangleTime.setTranslateX(1000); // Устанавливаем координату X
                    rectangleTime.setTranslateY(0); // Устанавливаем координату Y
                    rectangleTime.setWidth(200); // Устанавливаем ширину
                    rectangleTime.setHeight(50); // Устанавливаем высоту
                    rectangleTime.setFill(Color.WHITE);// Устанавливаем цвет заливки прямоугольника
                    root.getChildren().add(rectangleTime);// Добавляем прямоугольник на сцену

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
                    root.getChildren().remove(rectangleTime); // Если прямоугольник уже отображен, скрываем его
                    root.getChildren().remove(times); // Удаляем текст
                }

            }
        });

        // Добавление иконки
        primaryStage.getIcons().add(new Image(getClass().getResource("/org/example/labs/icon/icon_ant.png").toExternalForm())); // Установка иконки

        primaryStage.setTitle("Ant Simulation"); // Установка названия программы
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}