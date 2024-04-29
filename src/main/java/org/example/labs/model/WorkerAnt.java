package org.example.labs.model;

import java.util.Random;

class WorkerAnt extends Ant {
    private static final double MOVEMENT_SPEED = 5.0;
    public static int spawnCount = 0;
    private Random random;

    public WorkerAnt(long birthTime, long  lifeTime) {
        super("/org/example/labs/AntGIF/ant1.gif", birthTime, lifeTime);
        this.random = new Random();
    }

    public void createImageView(double x, double y) {
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(80); // Делаем картинку квадратной
        imageView.setFitHeight(80); // Делаем картинку квадратной
        imageView.setPreserveRatio(false); // Исходное фото НЕ квадратное, поэтому начальное соотношение сторон не сохраняем
    }
}
