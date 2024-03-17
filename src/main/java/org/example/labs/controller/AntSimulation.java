package org.example.labs.controller;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.labs.buttons.button;
import org.example.labs.model.Habitat;

import java.io.IOException;

public class AntSimulation extends Application {
    private long simulationStartTime; // Время начала симуляции
    public boolean startFlag = false; // Флаг для проверки работы симуляции
    private Text times; // Текст для информации о времени
    private boolean timerVisible = true; // Флаг видимости таймера
    private StackPane AntList = new StackPane();
    private StackPane root;
    private Habitat habitat;
    private Text descriptionText;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        root = new StackPane();
        Image Backgroundimg = new Image(getClass().getResourceAsStream("/org/example/labs/Background/soil2.png"));
        BackgroundSize bsize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true);
        BackgroundImage bImg = new BackgroundImage(Backgroundimg, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, bsize);
        Background bGround = new Background(bImg);
        root.setBackground(bGround);
        //root.setStyle("-fx-background-image: url('/org/example/labs/Background/soil2.png'); -fx-background-size: cover;"); // Установка фона окна


        root.getChildren().add(AntList);

        habitat = new Habitat(root, AntList);

        descriptionText = new Text("Press 'B' to start simulation, 'E' to stop simulation");
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
        Rectangle rectangleManagement = new Rectangle(400,900,Color.WHITE);
        StackPane.setAlignment(rectangleManagement, Pos.TOP_LEFT); // Отменяем центрирование
        rectangleManagement.setTranslateX(1200); // Устанавливаем координату X
        rectangleManagement.setTranslateY(0); // Устанавливаем координату Y
        rectangleManagement.setFill(Color.GRAY);// Устанавливаем цвет заливки прямоугольника

        rectangleManagement.setStroke(Color.BLACK);  // Установка цвета обводки
        rectangleManagement.setStrokeWidth(5); // Установка ширины обводки
        rectangleManagement.setStrokeType(javafx.scene.shape.StrokeType.INSIDE); // Установка типа обводки внутри

        root.getChildren().add(rectangleManagement);// Добавляем прямоугольник на сцену

        StackPane FXMLstackPane = new StackPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/labs/hello-view.fxml"));
        Node buttonNode = loader.load();
        button controller = loader.getController();
        controller.setHabitat(habitat, this);

        FXMLstackPane.getChildren().add(buttonNode);
        root.getChildren().add(FXMLstackPane);

        times = new Text("Time: 00:00:00"); // Текст для информации о времени
        times.setFont(Font.font("Arial Rounded MT", 35)); // устанавливаем шрифт Arial Rounded MT размером 20
        times.setFill(Color.BLACK); // устанавливаем цвет текста
        StackPane.setAlignment(times, Pos.TOP_LEFT);
        times.setTranslateX(1300); // Устанавливаем координату X
        times.setTranslateY(800); // Устанавливаем координату Y
        root.getChildren().add(times);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.B && !startFlag) {
                startSimulation();
            } else if (event.getCode() == KeyCode.E && startFlag) {
                stopSimulation();
            } else if (event.getCode() == KeyCode.T && startFlag) {
                if (timerVisible) {
                    root.getChildren().remove(times);
                    timerVisible = false;
                } else {
                    root.getChildren().add(times);
                    timerVisible = true;
                }
            }
        });

        // Добавление иконки
        primaryStage.getIcons().add(new Image(getClass().getResource("/org/example/labs/icon/icon_ant.png").toExternalForm())); // Установка иконки

        primaryStage.setTitle("Ant Simulation"); // Установка названия программы
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // Метод для запуска таймера обновления времени
    private void startTimer() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long currentTime = System.currentTimeMillis();
                long simulationTime = (currentTime - simulationStartTime) / 1000;
                long hours = simulationTime / 3600;
                long minutes = (simulationTime % 3600) / 60;
                long seconds = simulationTime % 60;
                times.setText(String.format("Time: %02d:%02d:%02d", hours, minutes, seconds));
            }
        };
        timer.start();
    }

    // Метод для остановки таймера
    private void stopTimer() {
        // Останавливаем таймер, просто прерывая его выполнение
    }

    public void startSimulation() {
        startFlag = true;
        root.getChildren().remove(descriptionText);
        simulationStartTime = System.currentTimeMillis();
        habitat.startSimulation();
        startTimer(); // Запускаем таймер
    }
    public void stopSimulation() {
        startFlag = false;
        root.getChildren().remove(times);
        habitat.stopSimulation();
        stopTimer(); // Останавливаем таймер
    }
}