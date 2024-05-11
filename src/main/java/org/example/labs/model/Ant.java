package org.example.labs.model;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Ant implements IBehaviour, Serializable {
    private static final long serialVersionUID = 1L; // Пример значения serialVersionUID
    SerializImageView imageView; // Поле для изображения
    protected long birthTime; // Время рождения
    protected long lifeTime;

    public double posX;
    public double posY;

    private double startX;
    private double startY;

    public boolean stayInZero = false;
    protected String typeAnt;

    public Ant(int _x, int _y, Image image, String imagePath, String typeAnt, long birthTime, long lifeTime) {
        imageView = new SerializImageView(image, imagePath);
        imageView.image.setX(_x);
        imageView.image.setY(_y);
        imageView.image.setFitHeight(50);
        imageView.image.setFitWidth(50);
        imageView.image.setPreserveRatio(true);
        this.typeAnt = typeAnt;
        startX = _x;
        startY = _y;
        posX = _x;
        posY = _y;
        this.birthTime = birthTime;
        this.lifeTime = lifeTime;
    }

    public ImageView getImageView() {
        return imageView.image;
    }

    public long getBirthTime() {
        return birthTime;
    }

    public long getLifeTime() {
        return lifeTime;
    }

    public void setX(double x) {
        posX = x;
    }

    public void setY(double y) {
        posX = y;
    }

    public double getX() {
        return imageView.image.getX();
    }

    public double getY() {
        return imageView.image.getY();
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public void paint() {
        imageView.image.setX(posX);
        imageView.image.setY(posX);
    }
}
