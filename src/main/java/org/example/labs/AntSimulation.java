package org.example.labs;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
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
    private long simulationStartTime; // Время начала симуляции
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

        Rectangle rectangle = new Rectangle();
        Text times = new Text("Тime: ");

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.B) {
                simulationStartTime = System.currentTimeMillis();
                habitat.startSimulation();
            }
            else if (event.getCode() == KeyCode.E) {
                habitat.stopSimulation();
            }
            else if (event.getCode() == KeyCode.T) {
                boolean isRectangleShown = root.getChildren().contains(rectangle);

                if (!isRectangleShown) {
                    StackPane.setAlignment(rectangle, Pos.TOP_LEFT);
                    rectangle.setTranslateX(1000); // Устанавливаем координату X
                    rectangle.setTranslateY(0); // Устанавливаем координату Y
                    rectangle.setWidth(200); // Устанавливаем ширину
                    rectangle.setHeight(50); // Устанавливаем высоту
                    rectangle.setFill(Color.WHITE);// Устанавливаем цвет заливки прямоугольника
                    root.getChildren().add(rectangle);// Добавляем прямоугольник на сцену или другой контейнер

                    long simulationEndTime = System.currentTimeMillis();
                    long simulationTime = (simulationEndTime - simulationStartTime) / 1000;


                    StackPane.setAlignment(times, Pos.TOP_LEFT);
                    times.setFont(Font.font("Arial Rounded MT", 20)); // устанавливаем шрифт Arial Rounded MT размером 35
                    times.setFill(Color.BLACK); // устанавливаем цвет текста на наш свой собственный цвет
                    times.setTranslateX(1005); // Устанавливаем координату X
                    times.setTranslateY(10); // Устанавливаем координату Y
                    root.getChildren().add(times);
                    // Определяем таймер для обновления времени
                    AnimationTimer timer = new AnimationTimer() {
                        @Override
                        public void handle(long now) {
                            long currentTime = System.currentTimeMillis();
                            long simulationTime = (currentTime - simulationStartTime) / 1000;
                            times.setText("Time: " + simulationTime); // Обновляем текст с текущим временем
                        }
                    };
                    // Начинаем таймер
                    timer.start();
                }
                else {
                    // Если прямоугольник уже отображен, скрываем его
                    root.getChildren().remove(rectangle);
                    // Удаляем текст
                    root.getChildren().remove(times);
                }

            }
            root.getChildren().remove(descriptionLabel);
        });

        primaryStage.setTitle("Ant Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}