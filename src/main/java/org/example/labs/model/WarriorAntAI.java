package org.example.labs.model;

import java.util.Vector;
import javafx.application.Platform;

public class WarriorAntAI extends BaseAI {

    private double centerX; // X-координата центра окружности
    private double centerY; // Y-координата центра окружности
    private double radius; // Радиус окружности
    private double angle; // Угол для перемещения по окружности
    public static Habitat habitat;

    private static WarriorAntAI instance;

    public static WarriorAntAI getInstance(Habitat habitat) {
        if (instance == null) {
            instance = new WarriorAntAI(habitat, "Warri");
        }
        return instance;
    }

    public static WarriorAntAI getInstance() {
        if (instance == null) {
            instance = new WarriorAntAI(habitat, "Warri");
        }
        return instance;
    }

    public WarriorAntAI(Habitat habitat, String name) {
        super(name);
        this.habitat = habitat;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.angle = 0; // Начальный угол
    }

    public void run() {
        Vector<Ant> ants = habitat.ants;
        double angle = 0.0; // Инициализируем угол
        double angularSpeed = 0.08; // Скорость изменения угла

        while (true) {
            synchronized (ants) {
                for (int i = 0; i < ants.size() && isActive; i++) {
                    // System.out.println(WarriorAntAI.getInstance().getPriority());
                    Ant ant = ants.get(i);
                    // Каждый поток работает только со своим типом объекта
                    if (ant instanceof WarriorAnt) {
                        // Радиус окружности
                        double radius = 100.0;
                        // Вычисляем координаты объекта на окружности
                        double x = ant.posX + radius * Math.cos(angle);
                        double y = ant.posY + radius * Math.sin(angle);

                        Platform.runLater(() -> {
                            ant.getImageView().setTranslateX(x);
                            ant.getImageView().setTranslateY(y);
                        });
                    }
                }
            }

            // Увеличиваем угол на скорость изменения
            angle += angularSpeed;

            try {
                Thread.sleep(30); // Время задержки остается тем же
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}