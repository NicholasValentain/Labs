package org.example.labs;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class AntSimulation extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-image: url('soil.png'); -fx-background-size: cover;");
        Habitat habitat = new Habitat(root);

        Text descriptionLabel = new Text("Press 'B' to start simulation, 'E' to stop simulation");
        descriptionLabel.setFont(Font.font("Arial Rounded MT", 35)); // устанавливаем шрифт Arial Rounded MT размером 35
        Color customColor = Color.rgb(215,125,49); // создаем свой собственный цвет
        descriptionLabel.setFill(customColor); // устанавливаем цвет текста на наш свой собственный цвет
        descriptionLabel.setStroke(Color.BLACK);
        descriptionLabel.setStrokeWidth(2.0);
        descriptionLabel.setStrokeType(StrokeType.OUTSIDE);
        root.getChildren().add(descriptionLabel);



        Scene scene = new Scene(root, 1200, 900);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.B) {
                habitat.startSimulation();
            } else if (event.getCode() == KeyCode.E) {
                habitat.stopSimulation();
            }
            root.getChildren().remove(descriptionLabel);
        });

        primaryStage.setTitle("Ant Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}