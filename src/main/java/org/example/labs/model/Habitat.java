package org.example.labs.model;

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.util.ArrayList; // Добавим импорт для ArrayList
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Random;

public class Habitat {
    private StackPane root;
    private StackPane AntList;
    private Random random;
    private AnimationTimer simulationTimer;
    private List<Ant> ants; // Добавим объявление переменной ants
    private long simulationStartTime; // Время начала симуляции
    private Label statisticsLabel; // Label для вывода статистики
    private Rectangle statisticsRectangle; // Rectangle для вывода статистики
    private static final int N1 = 5; // Интервал для рабочих муравьев (в секундах)
    private static final double P1 = 0.3; // Вероятность для рабочих муравьев
    private static final int N2 = 3; // интервал для муравьев-воинов (в секундах)
    private static final double P2 = 0.5; // вероятность для муравьев-воинов

    public Habitat(StackPane root, StackPane AntList) {
        this.root = root;
        this.AntList = AntList;
        this.random = new Random();
        this.simulationTimer = createSimulationTimer();
        this.ants = new ArrayList<>(); // Инициализируем переменную ants
        this.statisticsLabel = new Label();
        this.statisticsRectangle = new Rectangle();
    }

    public void startSimulation() {
        simulationStartTime = System.currentTimeMillis();
        AntList.getChildren().clear();
        ants.clear(); // Очищаем список муравьев
        statisticsLabel.setText("");
        root.getChildren().remove(statisticsRectangle);
        root.getChildren().remove(statisticsLabel);
        simulationTimer.start();
    }

    public void stopSimulation() {
        simulationTimer.stop();
        //root.getChildren().clear();
        AntList.getChildren().clear();
        updateStatistics();
    }

    private AnimationTimer createSimulationTimer() {
        long[] lastSpawnTimeWorker = { System.nanoTime() };
        long[] lastSpawnTimeWarrior = { System.nanoTime() };

        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTimeWorker = (now - lastSpawnTimeWorker[0]) / 1_000_000_000;
                long elapsedTimeWarrior = (now - lastSpawnTimeWarrior[0]) / 1_000_000_000;

                if (elapsedTimeWorker >= N1 && random.nextDouble() <= P1) {
                    spawnAnt(new WorkerAnt());
                    lastSpawnTimeWorker[0] = now;
                }

                if (elapsedTimeWarrior >= N2 && random.nextDouble() <= P2) {
                    spawnAnt(new WarriorAnt());
                    lastSpawnTimeWarrior[0] = now;
                }
            }
        };
    }

    private void spawnAnt(Ant ant) {
        // Отменяем центрирование только для добавленных муравьев
        StackPane.setAlignment(ant.getImageView(), Pos.TOP_LEFT);
        ant.getImageView().setTranslateX(random.nextDouble() * 1150);
        ant.getImageView().setTranslateY(random.nextDouble() * 850);
        //root.getChildren().add(ant.getImageView());
        AntList.getChildren().add(ant.getImageView());
        ants.add(ant);
    }

    private void updateStatistics() {
        long simulationEndTime = System.currentTimeMillis();
        long simulationTime = (simulationEndTime - simulationStartTime) / 1000;

        int workerAntsCount = 0;
        int warriorAntsCount = 0;

        for (Ant ant : ants) {
            if (ant instanceof WorkerAnt) {
                workerAntsCount++;
            } else if (ant instanceof WarriorAnt) {
                warriorAntsCount++;
            }
        }

        // Очищаем root от всех элементов, включая статистику
        //root.getChildren().clear();

        String statistics = String.format("Simulation Time: %d seconds\nWorker Ants: %d\nWarrior Ants: %d",
                simulationTime, workerAntsCount, warriorAntsCount);

        // Выводим статистику
        statisticsLabel.setText(statistics);
        statisticsLabel.setFont(Font.font("Arial Rounded MT", 25)); // устанавливаем шрифт Arial Rounded MT размером 35

        statisticsRectangle.setWidth(500); // Устанавливаем ширину
        statisticsRectangle.setHeight(200); // Устанавливаем высоту
        statisticsRectangle.setFill(Color.WHITE);// Устанавливаем цвет заливки прямоугольника

        root.getChildren().add(statisticsRectangle);// Добавляем прямоугольник на сцену или другой контейнер
        root.getChildren().add(statisticsLabel);
    }

    // Метод для поиска муравья по его ImageView
    private Ant findAntByImageView(ImageView imageView) {
        for (Ant ant : ants) {
            if (ant.getImageView() == imageView) {
                return ant;
            }
        }
        return null;
    }
}