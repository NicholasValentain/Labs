package org.example.labs.model;

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.util.ArrayList; // Добавим импорт для ArrayList
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Optional;
import java.util.Random;

public class Habitat {
    private static Habitat instance; // Статическое поле для хранения единственного экземпляра
    private StackPane root;
    private StackPane AntList;
    private Random random;
    private AnimationTimer simulationTimer;
    private List<Ant> ants; // Добавим объявление переменной ants
    private long simulationStartTime = 0; // Время начала симуляции
    private Label statisticsLabel; // Label для вывода статистики
    private Rectangle statisticsRectangle; // Rectangle для вывода статистики
    public boolean moreInfo;
    public volatile boolean paused = false;
    public  volatile  boolean isExit = false;
    private boolean startFlag; // Флаг для проверки работы симуляции
    public int N1; // Интервал для рабочих муравьев (в секундах)
    public double P1; // Вероятность для рабочих муравьев
    public int N2; // интервал для муравьев-воинов (в секундах)
    public double P2; // вероятность для муравьев-воинов

    private long waitTime = 0;
    private long simulationTimes = 0;
    private long currentTime = 0;

    private Habitat(StackPane root, StackPane AntList) {
        this.root = root;
        this.AntList = AntList;
        this.random = new Random();
        this.simulationTimer = createSimulationTimer();
        this.ants = new ArrayList<>(); // Инициализируем переменную ants
        this.statisticsLabel = new Label();
        this.statisticsRectangle = new Rectangle();
    }
    public static Habitat getInstance(StackPane root, StackPane AntList) {
        if (instance == null) {
            instance = new Habitat(root, AntList);
        }
        return instance;
    }

    public void startSimulation() {
        isExit = false;
        paused = false;
        waitTime = 0;
        simulationStartTime = System.currentTimeMillis();
        AntList.getChildren().clear();
        ants.clear(); // Очищаем список муравьев
        statisticsLabel.setText("");
//        root.getChildren().remove(statisticsRectangle);
//        root.getChildren().remove(statisticsLabel);
        simulationTimer.start();
    }

    public void stopSimulation() {
        //AntList.getChildren().clear();
        paused = true;
        if(moreInfo) updateStatistics();
        else {
            simulationTimer.stop();
            AntList.getChildren().clear();
            isExit = true;
        }
    }


    private AnimationTimer createSimulationTimer() {
        long startTime = System.nanoTime();
        final long[] lastWorkerTime = {0};
        final long[] lastWarriorTime = {0};

        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (paused) {
                    currentTime = ((System.currentTimeMillis() - simulationStartTime) / 1000);
                    waitTime = (currentTime - simulationTimes);
                    //System.out.println(waitTime + " " + currentTime + " " + simulationTimes);
                    //return;
                }
                else {
                    currentTime = System.currentTimeMillis();
                    simulationTimes = ((currentTime - simulationStartTime) / 1000) - waitTime;
                    long elapsedTime = (now - startTime) / 1_000_000_000;

                    // Проверяем, прошло ли достаточно времени с момента последнего выполнения условия для рабочего муравья
                    if (elapsedTime - lastWorkerTime[0] >= N1) {
                        if (random.nextDouble() <= P1) {
                            spawnAnt(new WorkerAnt());
                        }
                        lastWorkerTime[0] = elapsedTime;
                    }

                    // Проверяем, прошло ли достаточно времени с момента последнего выполнения условия для воинственного муравья
                    if (elapsedTime - lastWarriorTime[0] >= N2) {
                        if (random.nextDouble() <= P2) {
                            spawnAnt(new WarriorAnt());
                        }
                        lastWarriorTime[0] = elapsedTime;
                    }
                }
            }
        };
    }

    private void spawnAnt(Ant ant) {
        // Отменяем центрирование только для добавленных муравьев
        StackPane.setAlignment(ant.getImageView(), Pos.TOP_LEFT);
        ant.getImageView().setTranslateX(random.nextDouble() * 1150);
        ant.getImageView().setTranslateY(random.nextDouble() * 850);
        AntList.getChildren().add(ant.getImageView());
        ants.add(ant);
    }

    private void updateStatistics() {
        long simulationTime = simulationTimes;

        int workerAntsCount = 0;
        int warriorAntsCount = 0;

        for (Ant ant : ants) {
            if (ant instanceof WorkerAnt) {
                workerAntsCount++;
            } else if (ant instanceof WarriorAnt) {
                warriorAntsCount++;
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Статистика");
        alert.setHeaderText("OK - прекратить симуляцию\nОтмена - продолжить симуляцию");

        String simulationTimeString;
        long hours = simulationTime / 3600;
        long minutes = (simulationTime % 3600) / 60;
        long seconds = simulationTime % 60;
        simulationTimeString = String.format("Время: %02d:%02d:%02d\n", hours, minutes, seconds);

        String statistics = String.format("%sРабочих: %d\nСолдат Ants: %d",
                simulationTimeString, workerAntsCount, warriorAntsCount);

        TextArea textArea = new TextArea(statistics);
        textArea.setPrefColumnCount(20);
        textArea.setPrefRowCount(5);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        alert.getDialogPane().setContent(textArea);
        //alert.showAndWait();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            simulationTimer.stop();
            isExit = true;
            AntList.getChildren().clear();
        }
        else {
            paused = false;
        }
    }
}