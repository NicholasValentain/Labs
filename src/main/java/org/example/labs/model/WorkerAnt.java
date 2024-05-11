package org.example.labs.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Random;

import javafx.scene.image.Image;

class WorkerAnt extends Ant {
    private static final long serialVersionUID = 1L; // Пример значения serialVersionUID
    private static final double MOVEMENT_SPEED = 5.0;

    private static String path = "C:/Users/User/Downloads/Lab5_FINALL/Lab5_S_V/Lab5_S_V/Lab5/Labs/Labs/src/main/resources/org/example/labs/AntGIF/ant1.gif";
    static final Image image;

    private double routeX;
    private double routeY;

    public double getRouteX() {
        return routeX;
    }

    public void setRouteX(double routeX) {
        this.routeX = routeX;
    }

    public double getRouteY() {
        return routeY;
    }

    public void setRouteY(double routeY) {
        this.routeY = routeY;
    }

    static {
        try {
            image = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int spawnCount = 0;

    public WorkerAnt(int _x, int _y, long birthTime, long lifeTime) throws FileNotFoundException {
        super(_x, _y, image, path, "work", birthTime, lifeTime);

    }

}
