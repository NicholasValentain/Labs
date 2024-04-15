package org.example.labs.model;

import javafx.application.Platform;

import java.util.List;
import java.util.Vector;

import static java.lang.Math.*;
import static java.lang.Math.pow;

public class WarriorAntAI extends BaseAI {
    private double centerX; // X-координата центра окружности
    private double centerY; // Y-координата центра окружности
    private double radius; // Радиус окружности
    private double angle; // Угол для перемещения по окружности
    public Habitat habitat;

    public WarriorAntAI(Habitat habitat, String name) {
        super(name);
        this.habitat = habitat;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.angle = 0; // Начальный угол
        monitor = "monitor war";
    }

    public void run() {
        Vector<Ant> ants = habitat.ants;
        while(true) {
            synchronized (monitor) {
                if (!isActive) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                synchronized (ants) {
                    for (int i = 0; i < ants.size() && isActive; i++) {
                        Ant ant = ants.get(i);
                        // Каждый поток работает только со своим типом объекта
                        if (ant instanceof WarriorAnt) {
                            if (ant.getImageView().getTranslateX() < 1 && ant.getImageView().getTranslateY() < 1) {
                                ant.stayInZero = true;
                            }
                            if(ant.getImageView().getTranslateX() >= ant.posX && ant.getImageView().getTranslateY() >= ant.posY) {
                                ant.stayInZero = false;
                            }
                            if(!ant.stayInZero){
                                double deltaX = 0 - ant.getImageView().getTranslateX();
                                double deltaY = 0 - ant.getImageView().getTranslateY();
                                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                                double moveX = deltaX / distance;
                                double moveY = deltaY / distance;
                                Platform.runLater(()->{
                                    ant.getImageView().setTranslateX(ant.getImageView().getTranslateX() + moveX);
                                    ant.getImageView().setTranslateY(ant.getImageView().getTranslateY() + moveY);
                                });

                            }
                        }
                    }
                }
            }
            try {
                Thread.sleep(10); // при 20 работает хорошо
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


//        // Вычисляем новые координаты для перемещения по окружности
//        double newX = centerX + radius * Math.cos(angle);
//        double newY = centerY + radius * Math.sin(angle);
//
//        // Устанавливаем новые координаты для муравья
//        ant.getImageView().setTranslateX(newX);
//        ant.getImageView().setTranslateY(newY);
//
//        // Увеличиваем угол для следующего шага
//        angle += speed / radius; // Увеличиваем угол в зависимости от скорости и радиуса
//
//        // Проверяем, чтобы угол оставался в пределах от 0 до 2π
//        if (angle > 2 * Math.PI) {
//            angle -= 2 * Math.PI;
//        }
//
//        try {
//            Thread.sleep(50); // Задержка для эффекта анимации
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
