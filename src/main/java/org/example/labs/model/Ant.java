package org.example.labs.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

abstract class Ant implements IBehaviour {
    protected ImageView imageView; // Поле для изображения

    public Ant(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath)); // Изображение объекта
        imageView = new ImageView(image); // Установка изображения
        imageView.setFitWidth(50); // Установка ширины изображения
        imageView.setFitHeight(50); // Установка высоты изображения
    }

    public ImageView getImageView() {
        return imageView;
    }
    @Override
    public abstract void move();
}
