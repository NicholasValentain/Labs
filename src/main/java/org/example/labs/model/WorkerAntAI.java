package org.example.labs.model;

import javafx.application.Platform;
import java.util.Vector;

public class WorkerAntAI extends BaseAI {
    public static Habitat habitat;
    public double speed;

    private static WorkerAntAI instance;
    public static WorkerAntAI getInstance(Habitat habitat) {
        if (instance == null) {
            instance = new WorkerAntAI(habitat, "AI Physical");
        }
        return instance;
    }

    public static WorkerAntAI getInstance() {
        if (instance == null) {
            instance = new WorkerAntAI(habitat, "AI Physical");
        }
        return instance;
    }

    public WorkerAntAI(Habitat habitat, String name) {
        super(name);
        this.habitat = habitat;
        speed = 0.15;
    }

    public void run() {
        Vector<Ant> ants = habitat.ants;
        while (true) {
            synchronized (ants) {
                for (int i = 0; i < ants.size() && isActive; i++) {
                    //System.out.println(WorkerAntAI.getInstance().getPriority());
                    Ant ant = ants.get(i);
                    // Каждый поток работает только со своим типом объекта
                    if (ant instanceof WorkerAnt) {
                        if (ant.getImageView().getTranslateX() < 1 && ant.getImageView().getTranslateY() < 1) {
                            ant.stayInZero = true;
                        }
                        if (ant.getImageView().getTranslateX() >= ant.posX
                                && ant.getImageView().getTranslateY() >= ant.posY) {
                            ant.stayInZero = false;
                        }
                        if (!ant.stayInZero) {
                            double deltaX = 0 - ant.getImageView().getTranslateX();
                            double deltaY = 0 - ant.getImageView().getTranslateY();
                            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                            double moveX = deltaX / (distance * speed);
                            double moveY = deltaY / (distance * speed);

                            Platform.runLater(() -> {
                                ant.getImageView().setTranslateX(ant.getImageView().getTranslateX() + moveX);
                                ant.getImageView().setTranslateY(ant.getImageView().getTranslateY() + moveY);
                            });

                        } else {
                            double deltaX = ant.posX - ant.getImageView().getTranslateX();
                            double deltaY = ant.posY - ant.getImageView().getTranslateY();
                            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                            double moveX = deltaX / (distance * speed);
                            double moveY = deltaY / (distance * speed);

                            Platform.runLater(() -> {
                                ant.getImageView().setTranslateX(ant.getImageView().getTranslateX() + moveX);
                                ant.getImageView().setTranslateY(ant.getImageView().getTranslateY() + moveY);
                            });

                        }
                    }
                }
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}