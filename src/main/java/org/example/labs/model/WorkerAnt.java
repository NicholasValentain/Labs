package org.example.labs.model;

import java.util.Random;

class WorkerAnt extends Ant {
    private static final double MOVEMENT_SPEED = 5.0;
    private Random random;

    public WorkerAnt(long birthTime, long  lifeTime) {
        super("/org/example/labs/AntGIF/ant1.gif", birthTime, lifeTime);
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
