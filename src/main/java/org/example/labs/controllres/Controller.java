package org.example.labs.controllres;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.example.labs.model.*;

import org.example.labs.main.AntSimulation;

import java.io.*;
import java.util.Random;
import java.util.TreeMap;

import java.util.Vector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.geometry.Orientation;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseEvent;


public class Controller {
    @FXML
    public Button btnStart, btnStop, btnList, btnStopWorkerAI, btnStopWarriorAI, btnConsole, btnResume, btnSave, btnLoad;
    @FXML
    public RadioButton ShowTime, HideTime;
    @FXML
    public CheckBox cbShowInfo;
    @FXML
    public TextField N1, N2, L1, L2;
    @FXML
    public ComboBox P1, P2, WorkerPriority, WarriorPriority;

    @FXML
    public ListView<String> listView;

    public TreeMap<Integer, Long> treeMap;
    private Habitat habitat; // Добавляем поле для хранения ссылки на habitat
    private AntSimulation antSimulation;

    // Создание списка элементов для ComboBox
    ObservableList<String> options = FXCollections.observableArrayList(
            "10", "20", "30", "40", "50", "60", "70", "80", "90", "100");
    ObservableList<String> prioritys = FXCollections.observableArrayList(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    ObservableList<String> users = FXCollections.observableArrayList(
            "Nigger", "Dybkov", "Zayakin", "Pidor", "Saimon", "Belain", "Gabe", "Chmo", "Chert", "Pubertatnaya Yazva");

    // Метод для установки ссылки на habitat
    public void setHabitat(Habitat habitat, AntSimulation antSimulation) {
        this.habitat = habitat;
        this.antSimulation = antSimulation;
        N1.setText("1");
        N2.setText("1");
        L1.setText("1");
        L2.setText("1");
        ShowTime.setSelected(true);
        btnStop.setDisable(true);
        btnResume.setDisable(true);
        P1.setItems(options);
        P2.setItems(options);
        P1.setValue("100");
        P2.setValue("100");
        WorkerPriority.setItems(prioritys);
        WarriorPriority.setItems(prioritys);
        WorkerPriority.setValue("5");
        WarriorPriority.setValue("5");
        listView.setItems(users);
    }

//    // Устанавливаем обработчик события нажатия на элемент списка
//    listView.setOnMouseClicked(event -> {
//        String selectedItem = listView.getSelectionModel().getSelectedItem();
//        System.out.println("Выбран элемент: " + selectedItem);
//    });

    public void takeUser() {
        System.out.println(listView.getSelectionModel().getSelectedItem());
    }

    public void getConfig() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("config.txt"));

            WarriorAntAI Warriorth = WarriorAntAI.getInstance();
            WorkerAntAI Workerth = WorkerAntAI.getInstance();

            N1.setText(reader.readLine());
            N2.setText(reader.readLine());
            L1.setText(reader.readLine());
            L2.setText(reader.readLine());
            ShowTime.setSelected(Boolean.parseBoolean(reader.readLine()));
            HideTime.setSelected(Boolean.parseBoolean(reader.readLine()));
            if (ShowTime.isSelected()) {
                antSimulation.timerVisible = true;
                antSimulation.root.getChildren().add(antSimulation.times);
            } else {
                antSimulation.timerVisible = false;
                antSimulation.root.getChildren().remove(antSimulation.times);
            }
            cbShowInfo.setSelected(Boolean.parseBoolean(reader.readLine()));
            habitat.moreInfo = cbShowInfo.isSelected();
            P1.setValue(reader.readLine());
            P2.setValue(reader.readLine());
            WorkerPriority.setValue(reader.readLine());
            WarriorPriority.setValue(reader.readLine());
            Workerth.isActive = Boolean.parseBoolean(reader.readLine());
            Warriorth.isActive = Boolean.parseBoolean(reader.readLine());

            if (Workerth.isActive) {
                btnStopWorkerAI.setText("Рабочих: ON");
                synchronized (Workerth) {
                    Workerth.notify();
                }
            } else {
                btnStopWorkerAI.setText("Рабочих: OFF");
            }

            if (Warriorth.isActive) {
                btnStopWarriorAI.setText("Солдат: ON");
                synchronized (Warriorth) {
                    Warriorth.notify();
                }
            } else {
                btnStopWarriorAI.setText("Солдат: OFF");
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Ошибка при чтении из файла: " + e.getMessage());
        }
    }

    public void setConfig() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"));

            WarriorAntAI Warriorth = WarriorAntAI.getInstance();
            WorkerAntAI Workerth = WorkerAntAI.getInstance();

            writer.write(N1.getText() + '\n');
            writer.write(N2.getText() + '\n');
            writer.write(L1.getText() + '\n');
            writer.write(L2.getText() + '\n');
            writer.write(String.valueOf(ShowTime.isSelected()) + '\n');
            writer.write(String.valueOf(HideTime.isSelected()) + '\n');
            writer.write(String.valueOf(cbShowInfo.isSelected()) + '\n');
            writer.write(String.valueOf(P1.getValue()) + '\n');
            writer.write(String.valueOf(P2.getValue()) + '\n');
            writer.write(String.valueOf(WorkerPriority.getValue()) + '\n');
            writer.write(String.valueOf(WarriorPriority.getValue()) + '\n');
            writer.write(String.valueOf(Workerth.isActive) + '\n');
            writer.write(String.valueOf(Warriorth.isActive) + '\n');
            System.out.println(Workerth.isActive + " " + Warriorth.isActive);
            writer.close();
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    @FXML
    private void click(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        switch (clickedButton.getId()) {
            case "btnStart":
                start();
                break;
            case "btnStop":
                stop();
                break;
        }
    }

    public void start() {
        if (habitat != null && !antSimulation.startFlag && checkError()) {
            btnStart.setDisable(true);
            btnStop.setDisable(false);
            btnResume.setDisable(true);

            N1.setDisable(true);
            N2.setDisable(true);
            L1.setDisable(true);
            L2.setDisable(true);
            P1.setDisable(true);
            P2.setDisable(true);
            WorkerPriority.setDisable(true);
            WarriorPriority.setDisable(true);
            cbShowInfo.setDisable(true);
            antSimulation.startSimulation();


            antSimulation.plusTime = 0;
            habitat.plusTime = 0;


            if (btnStopWorkerAI.getText().equals("Рабочих: ON")) {
                WorkerAntAI Workerth = WorkerAntAI.getInstance();
                Workerth.isActive = true;
                synchronized (Workerth) {
                    Workerth.notify();
                }
            }
            if (btnStopWarriorAI.getText().equals("Солдат: ON")) {
                WarriorAntAI Warriorth = WarriorAntAI.getInstance();
                Warriorth.isActive = true;
                synchronized (Warriorth) {
                    Warriorth.notify();
                }
            }

        }
    }

    public void stop() {
        if (habitat != null && antSimulation.startFlag) {
            if (cbShowInfo.isSelected()) {
                btnStart.setDisable(true);
                btnStop.setDisable(true);
                btnResume.setDisable(false);
            }
            if (!cbShowInfo.isSelected()) {
                N1.setDisable(false);
                N2.setDisable(false);
                L1.setDisable(false);
                L2.setDisable(false);
                P1.setDisable(false);
                P2.setDisable(false);
                WorkerPriority.setDisable(false);
                WarriorPriority.setDisable(false);
                cbShowInfo.setDisable(false);
            }
            antSimulation.stopSimulation();

            WorkerAntAI Workerth = WorkerAntAI.getInstance();
            WarriorAntAI Warriorth = WarriorAntAI.getInstance();
            Workerth.isActive = false;
            Warriorth.isActive = false;
        }
    }

    @FXML
    public void resume() {
        if (habitat != null && checkError()) {
            btnStart.setDisable(true);
            btnStop.setDisable(false);
            btnResume.setDisable(true);

            N1.setDisable(true);
            N2.setDisable(true);
            L1.setDisable(true);
            L2.setDisable(true);
            P1.setDisable(true);
            P2.setDisable(true);
            WorkerPriority.setDisable(true);
            WarriorPriority.setDisable(true);
            cbShowInfo.setDisable(true);
            antSimulation.resumeTimer();

            if (btnStopWorkerAI.getText().equals("Рабочих: ON")) {
                WorkerAntAI Workerth = WorkerAntAI.getInstance();
                Workerth.isActive = true;
                synchronized (Workerth) {
                    Workerth.notify();
                }
            }
            if (btnStopWarriorAI.getText().equals("Солдат: ON")) {
                WarriorAntAI Warriorth = WarriorAntAI.getInstance();
                Warriorth.isActive = true;
                synchronized (Warriorth) {
                    Warriorth.notify();
                }
            }

        }
    }

    @FXML
    private void clickTimeSwitch() {
        if (ShowTime.isSelected() && !antSimulation.timerVisible) {
            antSimulation.timerVisible = true;
            antSimulation.root.getChildren().add(antSimulation.times);
            // HideTime.setSelected(false);
        } else if (HideTime.isSelected() && antSimulation.timerVisible) {
            antSimulation.timerVisible = false;
            antSimulation.root.getChildren().remove(antSimulation.times);
            // ShowTime.setSelected(false);
        }
    }

    @FXML
    private void check() {
        habitat.moreInfo = cbShowInfo.isSelected();
    }

    @FXML
    public boolean checkError() {
        int n1 = 1;
        int n2 = 1;
        int l1 = 1;
        int l2 = 1;

        try {
            n1 = Integer.parseInt(N1.getText());
            n2 = Integer.parseInt(N2.getText());
            l1 = Integer.parseInt(L1.getText());
            l2 = Integer.parseInt(L2.getText());

            if (n1 < 1 || n2 < 1 || l1 < 1 || l2 < 1)
                throw new NumberFormatException("Число меньше 1");

            habitat.N1 = n1;
            habitat.N2 = n2;
            habitat.L1 = l1;
            habitat.L2 = l2;

            habitat.P1 = Double.parseDouble((String) P1.getValue()) / 100;
            habitat.P2 = Double.parseDouble((String) P2.getValue()) / 100;
            WorkerAntAI.getInstance().setPriority(Integer.parseInt((String) WorkerPriority.getValue()));
            WarriorAntAI.getInstance().setPriority(Integer.parseInt((String) WarriorPriority.getValue()));

        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText("Некорректный период рождения");
            alert.setContentText("Требуется целое положительное число!");

            // Проверяем, на некорректный ввод
            if (!N1.getText().matches("\\d+") || n1 < 1)
                N1.setText("1");
            if (!N2.getText().matches("\\d+") || n2 < 1)
                N2.setText("1");
            if (!L1.getText().matches("\\d+") || l1 < 1)
                L1.setText("1");
            if (!L2.getText().matches("\\d+") || l2 < 1)
                L2.setText("1");

            // Get the Stage.
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            // Add a custom icon.
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/labs/icon/error.png")));
            alert.showAndWait();
            return false;
        }
        return true;
    }

    @FXML
    private void handleMenuAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        switch (menuItem.getText()) {
            case "Выход":
                setConfig();
                System.exit(0);
                break;
            case "Старт (B)":
                start();
                break;
            case "Стоп (E)":
                stop();
                break;
        }
    }

    @FXML
    private void clickTime() {
        // Изменяем состояние видимости таймера и обновляем чекбоксы
        if (antSimulation.timerVisible) {
            antSimulation.root.getChildren().remove(antSimulation.times);
            antSimulation.timerVisible = false;
            HideTime.setSelected(true);
            ShowTime.setSelected(false);
        } else {
            antSimulation.root.getChildren().add(antSimulation.times);
            antSimulation.timerVisible = true;
            ShowTime.setSelected(true);
            HideTime.setSelected(false);
        }
    }

    @FXML
    private void handleTimerMenuAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        if ("Показать/Скрыть (T)".equals(menuItem.getText())) {
            if (antSimulation != null) {
                // Изменяем состояние видимости таймера и обновляем чекбоксы
                antSimulation.setTimerVisible(!antSimulation.isTimerVisible());
                ShowTime.setSelected(antSimulation.isTimerVisible());
                HideTime.setSelected(!antSimulation.isTimerVisible());
            }
        }
    }

    @FXML
    private void clickInf() {
        if (!cbShowInfo.isDisable()) {
            if (!cbShowInfo.isSelected()) {
                cbShowInfo.setSelected(true);
                habitat.moreInfo = cbShowInfo.isSelected();
            } else {
                cbShowInfo.setSelected(false);
                habitat.moreInfo = cbShowInfo.isSelected();
            }
        }
    }

    @FXML
    private void openDialog() {
        try {
            treeMap = habitat.spawnTimes;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/labs/dialogList.fxml"));
            Parent root = fxmlLoader.load();
            DialogListController dialogController = fxmlLoader.getController();
            dialogController.setTreeMap(treeMap); // Устанавливаем TreeMap

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Текущие объекты");
            stage.getIcons()
                    .add(new Image(getClass().getResource("/org/example/labs/icon/table.png").toExternalForm()));
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Показываем окно и ждем, пока его закроют
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openConsole() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/labs/console.fxml"));
            Pane root = fxmlLoader.load();
            // root.setBackground(Background.fill(Color.BLACK));
            ConsoleController consoleController = fxmlLoader.getController();
            consoleController.setCommands(this, this.antSimulation); // Устанавливаем TreeMap

            Stage stage = new Stage();
            // stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Console");
            stage.getIcons()
                    .add(new Image(getClass().getResource("/org/example/labs/icon/icon_console.png").toExternalForm()));
            Scene consoleScene = new Scene(root, 1100, 600);
            stage.setScene(consoleScene);
            stage.showAndWait(); // Показываем окно и ждем, пока его закроют
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void controlWorkerAI() {

        WorkerAntAI Workerth = WorkerAntAI.getInstance();

        if (btnStopWorkerAI.getText().equals("Рабочих: ON")) {
            Workerth.isActive = false;
            btnStopWorkerAI.setText("Рабочих: OFF");
        } else {
            Workerth.isActive = true;
            synchronized (Workerth) {
                Workerth.notify();
            }
            btnStopWorkerAI.setText("Рабочих: ON");
        }
    }

    @FXML
    private void controlWarriorAI() {
        WarriorAntAI Warriorth = WarriorAntAI.getInstance();
        if (btnStopWarriorAI.getText().equals("Солдат: ON")) {
            Warriorth.isActive = false;
            btnStopWarriorAI.setText("Солдат: OFF");
        } else {
            Warriorth.isActive = true;
            synchronized (Warriorth) {
                Warriorth.notify();
            }
            btnStopWarriorAI.setText("Солдат: ON");
        }
    }

    private Serialization serialization = new Serialization(antSimulation);

    @FXML
    void SaveConf() {
        stop();
        if (serialization != null) {
            serialization.serialize();

        } else {
            System.err.println("Serialization object is not set");
        }
    }

    private static final String defaultConfigFolder = System.getProperty("user.dir");

    @FXML
    void loadConf() {
        antSimulation.root.getChildren().remove(antSimulation.descriptionText);
        if (serialization != null) {
            FileChooser fileChooser = new FileChooser();

            fileChooser.setInitialDirectory(new File(defaultConfigFolder));
            // Показать диалог выбора файла и получить выбранный файл
            File file = fileChooser.showOpenDialog(new Stage());

            if (file != null) {
                habitat.clearObjects();
                serialization.deserialize(file, antSimulation);
            }
            btnResume.setDisable(false);
        } else {
            System.err.println("Serialization object is not set");
        }
    }

    @FXML
    void saveAnt() {
        String url = "jdbc:postgresql://localhost:5432/test1";
        String user = "postgres";
        String password = "labsdb112";

        Connection conn = null;
        PreparedStatement deleteStmt = null;
        PreparedStatement stmt = null;
        try {
            Vector<Ant> ants = Habitat.getInstance().getAntsList();

            Class.forName("org.postgresql.Driver"); // Регистрируем драйвер JDBC
            conn = DriverManager.getConnection(url, user, password); // Устанавливаем соединение с базой данных

            String deleteSql = "DELETE FROM ants"; // SQL запрос для удаления предыдущих данных
            deleteStmt = conn.prepareStatement(deleteSql); // Создаем PreparedStatement для удаления
            deleteStmt.executeUpdate(); // Выполняем запрос для удаления предыдущих данных

            // Сбрасываем значения автоинкрементируемого столбца
            String resetSeqSql = "ALTER SEQUENCE ants_id_seq RESTART WITH 1";
            PreparedStatement resetSeqStmt = conn.prepareStatement(resetSeqSql);
            resetSeqStmt.executeUpdate();

            String sql = "INSERT INTO ants (typeAnt, posX, posY, lifeTime) VALUES (?, ?, ?, ?)"; // SQL запрос для вставки данных
            stmt = conn.prepareStatement(sql); // Создаем PreparedStatement
            for (Ant ant : ants) {
                if (ant instanceof WorkerAnt) {
                    stmt.setInt(1, 0); // Для рабочих
                } else if (ant instanceof WarriorAnt) {
                    stmt.setInt(1, 1); // Для солдат
                }
                stmt.setDouble(2, ant.posX); // значения для поля posX
                stmt.setDouble(3, ant.posY); // значения для поля posY
                stmt.setLong(4, ant.getLifeTime()); // значения для поля lifeTime

                // Выполняем запрос
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Элемент успешно добавлен в таблицу!");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // Закрываем PreparedStatement и Connection
                if (stmt != null) { stmt.close(); }
                if (deleteStmt != null) { deleteStmt.close(); }
                if (conn != null) { conn.close(); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @FXML
    void saveWorkerAnt() {
        String url = "jdbc:postgresql://localhost:5432/test1";
        String user = "postgres";
        String password = "labsdb112";

        Connection conn = null;
        PreparedStatement deleteStmt = null;
        PreparedStatement stmt = null;
        try {
            Vector<Ant> ants = Habitat.getInstance().getAntsList();

            Class.forName("org.postgresql.Driver"); // Регистрируем драйвер JDBC
            conn = DriverManager.getConnection(url, user, password); // Устанавливаем соединение с базой данных

            String deleteSql = "DELETE FROM ants"; // SQL запрос для удаления предыдущих данных
            deleteStmt = conn.prepareStatement(deleteSql); // Создаем PreparedStatement для удаления
            deleteStmt.executeUpdate(); // Выполняем запрос для удаления предыдущих данных

            // Сбрасываем значения автоинкрементируемого столбца
            String resetSeqSql = "ALTER SEQUENCE ants_id_seq RESTART WITH 1";
            PreparedStatement resetSeqStmt = conn.prepareStatement(resetSeqSql);
            resetSeqStmt.executeUpdate();

            String sql = "INSERT INTO ants (typeAnt, posX, posY, lifeTime) VALUES (?, ?, ?, ?)"; // SQL запрос для вставки данных
            stmt = conn.prepareStatement(sql); // Создаем PreparedStatement
            for (Ant ant : ants) {
                if (ant instanceof WorkerAnt) {
                    stmt.setInt(1, 0); // Для рабочих
                    stmt.setDouble(2, ant.posX); // значения для поля posX
                    stmt.setDouble(3, ant.posY); // значения для поля posY
                    stmt.setLong(4, ant.getLifeTime()); // значения для поля lifeTime

                    // Выполняем запрос
                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Элемент успешно добавлен в таблицу!");
                    }
                }

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // Закрываем PreparedStatement и Connection
                if (stmt != null) { stmt.close(); }
                if (deleteStmt != null) { deleteStmt.close(); }
                if (conn != null) { conn.close(); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @FXML
    void saveWarriorAnt() {
        String url = "jdbc:postgresql://localhost:5432/test1";
        String user = "postgres";
        String password = "labsdb112";

        Connection conn = null;
        PreparedStatement deleteStmt = null;
        PreparedStatement stmt = null;
        try {
            Vector<Ant> ants = Habitat.getInstance().getAntsList();

            Class.forName("org.postgresql.Driver"); // Регистрируем драйвер JDBC
            conn = DriverManager.getConnection(url, user, password); // Устанавливаем соединение с базой данных

            String deleteSql = "DELETE FROM ants"; // SQL запрос для удаления предыдущих данных
            deleteStmt = conn.prepareStatement(deleteSql); // Создаем PreparedStatement для удаления
            deleteStmt.executeUpdate(); // Выполняем запрос для удаления предыдущих данных

            // Сбрасываем значения автоинкрементируемого столбца
            String resetSeqSql = "ALTER SEQUENCE ants_id_seq RESTART WITH 1";
            PreparedStatement resetSeqStmt = conn.prepareStatement(resetSeqSql);
            resetSeqStmt.executeUpdate();


            String sql = "INSERT INTO ants (typeAnt, posX, posY, lifeTime) VALUES (?, ?, ?, ?)"; // SQL запрос для вставки данных
            stmt = conn.prepareStatement(sql); // Создаем PreparedStatement
            for (Ant ant : ants) {
                if (ant instanceof WarriorAnt) {
                    stmt.setInt(1, 1); // Для рабочих
                    stmt.setDouble(2, ant.posX); // значения для поля posX
                    stmt.setDouble(3, ant.posY); // значения для поля posY
                    stmt.setLong(4, ant.getLifeTime()); // значения для поля lifeTime

                    // Выполняем запрос
                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Элемент успешно добавлен в таблицу!");
                    }
                }

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // Закрываем PreparedStatement и Connection
                if (stmt != null) { stmt.close(); }
                if (deleteStmt != null) { deleteStmt.close(); }
                if (conn != null) { conn.close(); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @FXML
    public void loadAnt() {
        stop();
        habitat.clearObjects();
        String url = "jdbc:postgresql://localhost:5432/test1";
        String user = "postgres";
        String password = "labsdb112";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Random rand = new Random();

            Class.forName("org.postgresql.Driver"); // Регистрируем драйвер JDBC
            conn = DriverManager.getConnection(url, user, password); // Устанавливаем соединение с базой данных
            String sql = "SELECT * FROM ants"; // SQL запрос для чтения данных
            stmt = conn.prepareStatement(sql); // Создаем PreparedStatement
            rs = stmt.executeQuery(); // Выполняем запрос

            // Обрабатываем результат запроса
            while (rs.next()) {
                int id = rs.getInt("id");
                int type = rs.getInt("typeAnt");
                double posX = rs.getDouble("posX");
                double posY = rs.getDouble("posY");
                long lifeTime = rs.getLong("lifeTime");
                if (type == 0) {
                    habitat.addAnt(new WorkerAnt(rand.nextInt(0, 1150), rand.nextInt(0, 850), habitat.simulationTimes, lifeTime), habitat.simulationTimes, posX, posY);
                } else if (type == 1) {
                    habitat.addAnt(new WarriorAnt(rand.nextInt(0, 1150), rand.nextInt(0, 850), habitat.simulationTimes, lifeTime), habitat.simulationTimes, posX, posY);
                }

                // Делаем что-то с данными (например, выводим их на экран)
                System.out.println("ID: " + id + ", Type: " + type + ", PosX: " + posX + ", PosY: " + posY + ", LifeTime: " + lifeTime);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                // Закрываем ResultSet, PreparedStatement и Connection
                if (rs != null) { rs.close(); }
                if (stmt != null) { stmt.close(); }
                if (conn != null) { conn.close(); }
            } catch (SQLException e) { e.printStackTrace(); }
        }
        antSimulation.root.getChildren().remove(antSimulation.descriptionText);
        btnResume.setDisable(false);
    }

}
