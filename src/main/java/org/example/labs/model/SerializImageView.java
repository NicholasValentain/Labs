package org.example.labs.model;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;

public class SerializImageView implements Serializable {

    private static final long serialVersionUID = 1L; // Пример значения serialVersionUID
    transient ImageView image;
    transient String path;

    public SerializImageView(Image image, String path) {
        this.image = new ImageView(image);
        this.path = path;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        out.writeObject(path);
        out.writeObject(image.getX());
        out.writeObject(image.getY());
        out.writeObject(image.getFitHeight());
        out.writeObject(image.getFitWidth());
        out.writeObject(image.isPreserveRatio());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.path = (String) in.readObject();
        image = new ImageView();
        this.image.setImage(new Image(new FileInputStream(path)));
        this.image.setX((Double) in.readObject());
        this.image.setY((Double) in.readObject());
        this.image.setFitHeight((Double) in.readObject());
        this.image.setFitWidth((Double) in.readObject());
        this.image.setPreserveRatio((Boolean) in.readObject());
    }
}
