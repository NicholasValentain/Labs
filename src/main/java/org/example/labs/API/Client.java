package org.example.labs.API;

import dto.AntsDTO;
import dto.RequestDTO;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import server.Server;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.Random;

import org.example.labs.controllres.Controller;
import org.example.labs.main.AntSimulation;
import org.example.labs.model.Ant;
import org.example.labs.model.Habitat;
import org.example.labs.model.WarriorAnt;
import org.example.labs.model.WorkerAnt;

import dto.UserDTO;

public class Client {
    private static String userName;
    private static String serverIP;
    private static int serverPort;
    private static Socket socket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static int labelConcurrentThreads = 0;

    public static void askUserName(Label mode) {
        TextField fieldName = createTextField("Имя");
        TextField fieldIP = createTextField("IP-адрес сервера");
        TextField fieldPort = createTextField("Порт");

        fieldIP.setText("127.0.0.1");
        fieldPort.setText("8030");

        Button button = new Button("Войти");
        button.setFont(new Font("Consolas", 15));

        VBox root = new VBox(10, fieldName, fieldIP, fieldPort, button);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 215);
        Stage stage = new Stage();
        stage.setScene(scene);

        // Устанавливаем фокус на кнопку после показа сцены
        stage.setOnShown(event -> button.requestFocus());

        button.setOnAction(event -> {
            if (!fieldName.getText().isEmpty() && !fieldIP.getText().isEmpty() && !fieldPort.getText().isEmpty()) {
                userName = fieldName.getText();
                serverIP = fieldIP.getText();
                serverPort = Integer.parseInt(fieldPort.getText());
                stage.close();
                mode.setText("launch");
            }
        });
        stage.setOnCloseRequest(event -> mode.setText("exit"));
        stage.setTitle("Подключение к серверу");
        stage.showAndWait();
    }

    private static TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setFont(new Font("System", 20));
        textField.setPromptText(prompt);
        return textField;
    }

    private static void setPromptText(TextField textField, String prompt) {
        textField.setFont(new Font("System", 20));
        textField.setPromptText(prompt);
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && textField.getText().isEmpty()) {
                textField.setPromptText(prompt);
            } else {
                textField.setPromptText("");
            }
        });
    }

    public static void createUserList(String[] info) {
        Controller ct = Habitat.getInstance().getController();
        if (ct != null && ct.syncSettingsWithBox != null) {
            Platform.runLater(() -> {
                ct.syncSettingsWithBox.getItems().add("Никто");
                ct.listView.getItems().clear();
                ct.listView.getItems().add(userName + " (Вы)");
                for (var user : info) {
                    if (!user.equals(Client.userName)) {
                        ct.listView.getItems().add(user);
                        ct.syncSettingsWithBox.getItems().add(user);
                    }
                }
                ct.syncSettingsWithBox.setValue("Никто");
            });
        } else {
            System.out.println("Controller or syncSettingsWithBox is null");
        }
    }

    public static void addUserToList(String userName) {
        Controller ct = Habitat.getInstance().getController();
        Platform.runLater(() -> {
            ct.listView.getItems().add(userName);
            ct.syncSettingsWithBox.getItems().add(userName);
        });
    }

    public static void removeUserFromList(String userName) {
        Controller ct = Habitat.getInstance().getController();
        Platform.runLater(() -> {
            ct.listView.getItems().remove(userName);
            if (ct.syncSettingsWithBox.getValue().equals(userName)) {
                ct.syncSettingsWithBox.setValue("Никто");
            }
            ct.syncSettingsWithBox.getItems().remove(userName);
        });
    }

    public static void connectToServer() {
        try {
            socket = new Socket(serverIP, serverPort);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            ServerList listener = new ServerList(ois);
            listener.start();

            UserDTO output = new UserDTO(userName);
            oos.writeObject(output);

            // Убедитесь, что controller не равен null перед установкой текста
            Controller controller = Habitat.getInstance().getController();
            if (controller != null && controller.labelConnectionInfo != null) {
                controller.labelConnectionInfo.setText("IP: " + serverIP + " Порт: " + serverPort);
            } else {
                System.out.println("Controller or labelConnectionInfo is null");
            }

        } catch (ConnectException e) {
            // Убедитесь, что controller не равен null перед установкой текста
            if (Habitat.getInstance().getController() != null) {
                Habitat.getInstance().getController().labelConnectionInfo.setText("---Нет соединения---");
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void disconnectFromServer() throws IOException {
        ois.close();
        oos.close();
        socket.close();
    }

    public static void setAnts(String targetUserName, int antCount) {
        try {
            Vector<Ant> antsList = Habitat.getInstance().getAntsList();

            // Перемешивание списка муравьев
            Collections.shuffle(antsList);

            if (antCount > antsList.size()) {
                antCount = antsList.size();
            }

            double[] xCoordinates = new double[antCount];
            double[] yCoordinates = new double[antCount];
            boolean[] type = new boolean[antCount];

            // Выбор случайных муравьев для сериализации и сбор координат
            Ant ant = null;
            for (int i = 0; i < antCount; i++) {
                ant = antsList.get(i);
                xCoordinates[i] = ant.getX();
                yCoordinates[i] = ant.getY();
                if (ant instanceof WarriorAnt)
                    type[i] = true;
                else if (ant instanceof WorkerAnt)
                    type[i] = false;
            }

            // Создание DTO объекта для передачи
            AntsDTO antsDTO = new AntsDTO(targetUserName, xCoordinates, yCoordinates, type);

            // Передача DTO на сервер
            oos.writeObject(antsDTO);

        } catch (IOException e) {
            System.err.println("Error: IOException while serializing");
            e.printStackTrace();
        }
    }

    public static void setReqAnts(String targetUserName, int antCount) {
        try {
            Vector<Ant> antsList = Habitat.getInstance().getAntsList();
            List<Ant> selectedAnts = new ArrayList<>();

            // Создание DTO объекта для передачи
            RequestDTO RequestDTO = new RequestDTO(targetUserName, antCount);
            System.out.println(RequestDTO.getAntCount() + "" + RequestDTO.getUserName());
            oos.writeObject(RequestDTO);

        } catch (IOException e) {
            System.err.println("Error: IOException while serializing");
            e.printStackTrace();
        }
    }

    public static void setNewAnts(AntsDTO dto) {
        Random rand = new Random();
        int count = dto.getXCoordinates().length;
        Platform.runLater(() -> {
            for (int i = 0; i < count; i++) {
                if (dto.getType()[i]) {
                    try {
                        Habitat.getInstance().addAnt(new WorkerAnt(rand.nextInt(0, 1150), rand.nextInt(0, 850), 1, 100),
                                1000, dto.getXCoordinates()[i], dto.getYCoordinates()[i]);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Habitat.getInstance().addAnt(
                                new WarriorAnt(rand.nextInt(0, 1150), rand.nextInt(0, 850), 1, 100),
                                1000, dto.getXCoordinates()[i], dto.getYCoordinates()[i]);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

}
