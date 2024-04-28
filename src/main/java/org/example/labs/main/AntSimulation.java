package org.example.labs.main;

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
import java.io.IOException;

import org.example.labs.model.Habitat;
import org.example.labs.controllres.Controller;
import org.example.labs.model.WarriorAntAI;
import org.example.labs.model.WorkerAntAI;

public class AntSimulation extends Application {
    private long simulationStartTime; // Время начала симуляции
    public boolean startFlag = false; // Флаг для проверки работы симуляции
    public Text times; // Текст для информации о времени
    public boolean timerVisible = true; // Флаг видимости таймера
    private StackPane AntList = new StackPane();
    public StackPane root;
    private Habitat habitat;
    private Controller controller;
    private Text descriptionText;
    private AnimationTimer timer;
    private long waitTime = 0;
    private long currentTime = 0;
    private long simulationTime = 0;

    public boolean isTimerVisible() {
        return timerVisible;
    } // Геттер для переменной timerVisible
    public void setTimerVisible(boolean visible) { this.timerVisible = visible; } // Метод для установки переменной timerVisible
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

        root.getChildren().add(AntList);
        habitat = Habitat.getInstance(root, AntList);
        descriptionText = new Text("Нажмите 'B' чтобы начать симуляцию, 'E' чтобы остановить");
        descriptionText.setFont(Font.font("Arial Rounded MT", 35)); // Устанавливаем шрифт Arial Rounded MT размером 35
        StackPane.setAlignment(descriptionText, Pos.TOP_LEFT); // Отменяем центрирование
        descriptionText.setTranslateX(110); // Устанавливаем координату X
        descriptionText.setTranslateY(420); // Устанавливаем координату Y
        Color customColor = Color.rgb(215,125,49); // Создаем свой собственный цвет
        descriptionText.setFill(customColor); // Устанавливаем цвет текста на наш
        descriptionText.setStroke(Color.BLACK); // Устанавливаем чёрный контур
        descriptionText.setStrokeWidth(2.0); // Устанавливаем толщину обводки
        descriptionText.setStrokeType(StrokeType.OUTSIDE); // Устанавливаем тип обводки
        root.getChildren().add(descriptionText);

        Scene scene = new Scene(root, 1600, 900); // Основное окно

        Rectangle rectangleManagement = new Rectangle(400,900,Color.WHITE); // Окно для кнопок
        StackPane.setAlignment(rectangleManagement, Pos.TOP_LEFT); // Отменяем центрирование
        rectangleManagement.setTranslateX(1200); // Устанавливаем координату X
        rectangleManagement.setTranslateY(0); // Устанавливаем координату Y
        rectangleManagement.setFill(Color.GRAY);// Устанавливаем цвет заливки прямоугольника
        rectangleManagement.setStroke(Color.BLACK);  // Установка цвета обводки
        rectangleManagement.setStrokeWidth(5); // Установка ширины обводки
        rectangleManagement.setStrokeType(StrokeType.INSIDE); // Установка типа обводки внутри
        root.getChildren().add(rectangleManagement);// Добавляем прямоугольник на сцену

        StackPane FXMLstackPane = new StackPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/labs/control.fxml"));
        Node buttonNode = loader.load();
        controller = loader.getController();
        controller.setHabitat(habitat, this);

        WorkerAntAI.getInstance(habitat).start();
        WarriorAntAI.getInstance(habitat).start();


        //controller.setConfig();

        habitat.setController(controller);
        FXMLstackPane.getChildren().add(buttonNode);
        root.getChildren().add(FXMLstackPane);

        //controller.getConfig();

        times = new Text("Время: 00:00:00"); // Текст для информации о времени
        times.setFont(Font.font("Arial Rounded MT", 24)); // устанавливаем шрифт Arial Rounded MT размером 20
        times.setFill(Color.BLACK); // устанавливаем цвет текста
        StackPane.setAlignment(times, Pos.TOP_LEFT);
        times.setTranslateX(1310); // Устанавливаем координату X
        times.setTranslateY(865); // Устанавливаем координату Y
        //root.getChildren().add(times);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case KeyCode.B:
                    if(!startFlag) {
                        controller.btnStart.setDisable(true);
                        controller.btnStop.setDisable(false);
                        controller.N1.setDisable(true);
                        controller.N2.setDisable(true);
                        controller.L1.setDisable(true);
                        controller.L2.setDisable(true);
                        controller.P1.setDisable(true);
                        controller.P2.setDisable(true);
                        controller.WorkerPriority.setDisable(true);
                        controller.WarriorPriority.setDisable(true);
                        controller.cbShowInfo.setDisable(true);
                        controller.checkError();

                        WorkerAntAI Workerth = WorkerAntAI.getInstance();
                        WarriorAntAI Warriorth = WarriorAntAI.getInstance();
                        if (controller.btnStopWorkerAI.getText().equals("Рабочих: ON")) {
                            Workerth.isActive = true;
                            //WorkerAntAI.getInstance().notify();
                            synchronized (Workerth){
                                Workerth.notify();
                            }
                        }
                        if (controller.btnStopWarriorAI.getText().equals("Солдат: ON")) {
                            Warriorth.isActive = true;
                            //WarriorAntAI.getInstance().notify();
                            synchronized (Warriorth){
                                Warriorth.notify();
                            }
                        }

                        startSimulation();
                    }
                    break;
                case KeyCode.E:
                    if(startFlag) {
                        if(controller.cbShowInfo.isSelected()) {
                            controller.btnStart.setDisable(true);
                            controller.btnStop.setDisable(true);
                        }
                        if(!controller.cbShowInfo.isSelected()) {
                            controller.N1.setDisable(false);
                            controller.N2.setDisable(false);
                            controller.L1.setDisable(false);
                            controller.L2.setDisable(false);
                            controller.P1.setDisable(false);
                            controller.P2.setDisable(false);
                            controller.WorkerPriority.setDisable(false);
                            controller.WarriorPriority.setDisable(false);
                            controller.cbShowInfo.setDisable(false);
                        }

                        WorkerAntAI Workerth = WorkerAntAI.getInstance();
                        WarriorAntAI Warriorth = WarriorAntAI.getInstance();
                        Workerth.isActive = false;
                        synchronized (Workerth){
                            Workerth.notify();
                        }
                        Warriorth.isActive = false;
                        synchronized (Warriorth){
                            Warriorth.notify();
                        }


                        stopSimulation();
                    }
                    break;
                case KeyCode.T:
                    if (timerVisible) {
                        controller.ShowTime.setSelected(false);
                        controller.HideTime.setSelected(true);
                        root.getChildren().remove(times);
                        timerVisible = false;
                    } else {
                        controller.HideTime.setSelected(false);
                        controller.ShowTime.setSelected(true);
                        root.getChildren().add(times);
                        timerVisible = true;
                    }
                    break;
            }
        });

        controller.getConfig();
        primaryStage.getIcons().add(new Image(getClass().getResource("/org/example/labs/icon/icon_ant.png").toExternalForm())); // Добавление иконки
        primaryStage.setTitle("Ant Simulation"); // Установка названия программы
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            controller.setConfig();
            System.exit(0);
        }); // Остановка приложения по нажатию крестика


        primaryStage.show();
    }


    // Метод для запуска таймера обновления времени
    private void startTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!habitat.paused) {
                    currentTime = System.currentTimeMillis();
                    simulationTime = ((currentTime - simulationStartTime) / 1000) - waitTime;
                    long hours = simulationTime / 3600;
                    long minutes = (simulationTime % 3600) / 60;
                    long seconds = simulationTime % 60;
                    times.setText(String.format("Время: %02d:%02d:%02d", hours, minutes, seconds));
                } else {
                    currentTime = ((System.currentTimeMillis() - simulationStartTime) / 1000);
                    waitTime = (currentTime - simulationTime);
                }
            }
        };
        timer.start();
    }

    // Метод для остановки таймера
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            simulationStartTime = System.currentTimeMillis(); // Сброс начального времени симуляции до исходного значения
            times.setText("Время: 00:00:00");// Сброс текста отображаемого времени
            waitTime = 0;
        }
    }

    public void startSimulation() {
        startFlag = true;
        root.getChildren().remove(descriptionText);
        simulationStartTime = System.currentTimeMillis();
        habitat.startSimulation();
        startTimer(); // Запускаем таймер
    }
    public void stopSimulation() {
        habitat.stopSimulation();
        if(habitat.isExit) {
            controller.btnStop.setDisable(true);
            controller.btnStart.setDisable(false);
            startFlag = false;
            stopTimer();
        }
        else controller.btnStop.setDisable(false);
    }
}