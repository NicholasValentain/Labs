package org.example.labs;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

interface IBehaviour {
    void move();
}

abstract class Ant implements IBehaviour {
    protected ImageView imageView;

    public Ant(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
    }

    public ImageView getImageView() {
        return imageView;
    }
    @Override
    public abstract void move();
}

class WorkerAnt extends Ant {
    private static final double MOVEMENT_SPEED = 5.0;
    private Random random;

    public WorkerAnt() {
        super("/ant1.gif");
        this.random = new Random();
    }

    @Override
    public void move() {
        double deltaX = (random.nextDouble() - 0.5) * MOVEMENT_SPEED;
        double deltaY = (random.nextDouble() - 0.5) * MOVEMENT_SPEED;

        double newX = imageView.getTranslateX() + deltaX;
        double newY = imageView.getTranslateY() + deltaY;

        newX = Math.max(0, Math.min(newX, 600 - imageView.getFitWidth()));
        newY = Math.max(0, Math.min(newY, 400 - imageView.getFitHeight()));

        imageView.setTranslateX(newX);
        imageView.setTranslateY(newY);
    }
}

class WarriorAnt extends Ant {
    private static final double MOVEMENT_SPEED = 7.0;
    private Random random;

    public WarriorAnt() {
        super("/ant2.gif");
        this.random = new Random();
    }

    @Override
    public void move() {
        double centerX = 300;
        double centerY = 200;

        double directionX = centerX - imageView.getTranslateX();
        double directionY = centerY - imageView.getTranslateY();

        double length = Math.sqrt(directionX * directionX + directionY * directionY);
        directionX /= length;
        directionY /= length;

        double deltaX = directionX * MOVEMENT_SPEED;
        double deltaY = directionY * MOVEMENT_SPEED;

        double newX = imageView.getTranslateX() + deltaX;
        double newY = imageView.getTranslateY() + deltaY;

        newX = Math.max(0, Math.min(newX, 600 - imageView.getFitWidth()));
        newY = Math.max(0, Math.min(newY, 400 - imageView.getFitHeight()));

        imageView.setTranslateX(newX);
        imageView.setTranslateY(newY);
    }
}
