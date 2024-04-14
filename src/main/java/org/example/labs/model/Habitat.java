package org.example.labs.model;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import javafx.stage.Stage;

public class Habitat {
    private static Habitat instance; // Статическое поле для хранения единственного экземпляра
    private StackPane root;
    private StackPane AntList;
    private Random random;
    private AnimationTimer simulationTimer;
    public Vector<Ant> ants; // Добавим объявление переменной ants



    private HashSet<Integer> identifiers;
    public TreeMap<Integer, Long> spawnTimes;
    int ID;



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
    public int L1;
    public int L2;

    private long waitTime = 0;
    private long simulationTimes = 0;
    private long currentTime = 0;

    long lastWorkerTime;
    long lastWarriorTime;

    public Habitat(StackPane root, StackPane AntList) {
        this.root = root;
        this.AntList = AntList;

        identifiers = new HashSet<>();
        spawnTimes = new TreeMap<>();

        this.random = new Random();
        this.simulationTimer = createSimulationTimer();
        this.ants = new Vector<>(); // Инициализируем переменную ants
        this.statisticsLabel = new Label();
        this.statisticsRectangle = new Rectangle();
    }
    // Статический метод для получения единственного экземпляра класса
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


        ID = 0;

        lastWorkerTime = 0;
        lastWarriorTime = 0;


        simulationStartTime = System.currentTimeMillis();
        AntList.getChildren().clear();
        synchronized (ants) {
            ants.clear(); // Очищаем список муравьев
        }
        statisticsLabel.setText("");
        simulationTimer.start();
    }

    public void stopSimulation() {
        //AntList.getChildren().clear();
        paused = true;

        if(moreInfo) updateStatistics();
        else {
            simulationTimer.stop();
            AntList.getChildren().clear();


            identifiers.clear();
            spawnTimes.clear();


            isExit = true;
        }
    }

    private AnimationTimer createSimulationTimer() {
        long startTime = System.nanoTime();


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
                    //time = currentTime - simulationStartTime;

                    simulationTimes = ((currentTime - simulationStartTime) / 1000) - waitTime;

                    //long elapsedTime = (now / 1_000_000_000) - (simulationStartTime / 1000);
                    //System.out.println("============" + simulationTimes);

                    clearDeadFish(simulationTimes);

                    // Проверяем, прошло ли достаточно времени с момента последнего выполнения условия для рабочего муравья
                    if (simulationTimes - lastWorkerTime >= N1) {
                        if (random.nextDouble() <= P1) {
                            spawnAnt(new WorkerAnt(simulationTimes, L1), simulationTimes);
                        }
                        lastWorkerTime = simulationTimes;
                    }

                    // Проверяем, прошло ли достаточно времени с момента последнего выполнения условия для воинственного муравья
                    if (simulationTimes - lastWarriorTime >= N2) {
                        if (random.nextDouble() <= P2) {
                            spawnAnt(new WarriorAnt(simulationTimes, L2), simulationTimes);
                        }
                        lastWarriorTime = simulationTimes;
                    }
                }
            }
        };
    }

    private void spawnAnt(Ant ant, long currentTime) {
        // Отменяем центрирование только для добавленных муравьев
        StackPane.setAlignment(ant.getImageView(), Pos.TOP_LEFT);

        double posX;
        double posY;

        while (true) {
            posX = random.nextDouble() * 1150;
            posY = random.nextDouble() * 850;
            if(posX != 0 && posY != 0) {
                ant.getImageView().setTranslateX(posX);
                ant.getImageView().setTranslateY(posY);
                ant.posX = posX;
                ant.posY = posY;
                break;
            }
        }

        AntList.getChildren().add(ant.getImageView());
        synchronized (ants) {
            ants.add(ant);
        }



        identifiers.add(ID);
        spawnTimes.put(ID, currentTime);
        ID++;
    }

    private void updateStatistics() {
        synchronized (ants) {
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


            // Get the Stage.
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            // Add a custom icon.
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/labs/icon/statistics.png")));

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


                identifiers.clear();
                spawnTimes.clear();
                ID = 0;

            } else {
                paused = false;
            }
        }
    }

    private void clearDeadFish(long currentTime) {
        List<AtomicReference<Ant>> foundedFishList = new ArrayList<>();
        AtomicReference<Ant> foundedFish = new AtomicReference<>(); // Временная переменная для объекта

        synchronized (ants){
            ants.forEach(tmp -> { // Перебор массива
                if (currentTime - tmp.getBirthTime() == tmp.getLifeTime()) { // Проверка на смэрть  не >=, а ==

                    AtomicInteger foundedId = new AtomicInteger(); // Временная переменная для ID
                    spawnTimes.forEach((id, birthTime) -> {
                        if (tmp.getBirthTime() == birthTime) { // Находим объект с таким же временем в treeMap
                            foundedId.set(id); // Берём ID этого элемента
                            //System.out.println(currentTime + " " + tmp.getBirthTime() + " " + tmp.getLifeTime()+ " " + id);
                        }
                    });
                    identifiers.remove(foundedId.get()); // Удаляем его из hashSet
                    spawnTimes.remove(foundedId.get()); // Удаляем его из treeMap
                    AntList.getChildren().remove(tmp.getImageView()); // Удаляем из Pane
                    foundedFish.set(tmp); // Берём этот элемент и копируем в временную переменную
                    foundedFishList.add(foundedFish);
                    //
                }
            });
            for (AtomicReference<Ant> atomicRef : foundedFishList) {
                ants.remove(atomicRef.get()); // Удаляем из листа
            }
        }
    }
}