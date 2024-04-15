package org.example.labs.model;

import javafx.application.Platform;

import java.util.List;
import java.util.Vector;

import static java.lang.Math.*;
import static java.lang.Math.pow;

public class WorkerAntAI extends BaseAI {
    public Habitat habitat;
    public WorkerAntAI(Habitat habitat, String name) {
        super(name);
        this.habitat = habitat;
        monitor = "monitor wor";
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
                        if (ant instanceof WorkerAnt) {
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
                                Platform.runLater(()-> {
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

//        // Движение к конечной точке (верхний левый угол экрана)
//        while (ant.getImageView().getTranslateX() != destinationX || ant.getImageView().getTranslateY() != destinationY) {
//            double deltaX = destinationX - ant.getImageView().getTranslateX();
//            double deltaY = destinationY - ant.getImageView().getTranslateY();
//            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
//            double moveX = deltaX / distance * speed;
//            double moveY = deltaY / distance * speed;
//            ant.getImageView().setTranslateX(ant.getImageView().getTranslateX() + moveX);
//            ant.getImageView().setTranslateY(ant.getImageView().getTranslateY() + moveY);
//            try {
//                Thread.sleep(50); // задержка для эффекта анимации
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        // Возврат в точку рождения
//        while (ant.getImageView().getTranslateX() != initialX || ant.getImageView().getTranslateY() != initialY) {
//            double deltaX = initialX - ant.getImageView().getTranslateX();
//            double deltaY = initialY - ant.getImageView().getTranslateY();
//            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
//            double moveX = deltaX / distance * speed;
//            double moveY = deltaY / distance * speed;
//            ant.getImageView().setTranslateX(ant.getImageView().getTranslateX() + moveX);
//            ant.getImageView().setTranslateY(ant.getImageView().getTranslateY() + moveY);
//            try {
//                Thread.sleep(50); // задержка для эффекта анимации
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
