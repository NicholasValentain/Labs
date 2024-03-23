package org.example.labs.model;

import java.util.Random;

class WarriorAnt extends Ant {
    private static final double MOVEMENT_SPEED = 7.0;
    private Random random;

    public WarriorAnt(long currentTime) {
        super("/org/example/labs/AntGIF/ant2.gif", currentTime);
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
