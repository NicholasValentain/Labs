package org.example.labs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AntSimulation extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Habitat habitat = new Habitat(root);

        Scene scene = new Scene(root, 600, 400);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.B) {
                habitat.startSimulation();
            } else if (event.getCode() == KeyCode.E) {
                habitat.stopSimulation();
            }
        });

        primaryStage.setTitle("Ant Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}