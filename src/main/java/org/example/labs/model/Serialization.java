package org.example.labs.model;


import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.labs.controllres.Controller;
import org.example.labs.main.AntSimulation;

import java.io.*;
import java.util.*;
import java.util.Vector;
import java.util.HashSet;
import java.util.TreeMap;

public class Serialization {
    private static final String defaultConfigFolder = System.getProperty("user.dir");
    private static String directory;
    private static FileChooser fileChooser;
    static {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(defaultConfigFolder));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файл конфигурации", "*.dat"));
    }
    public static void setDefaultDirectory() {
        directory = defaultConfigFolder + "\\config.dat";
    }
    public static void callSaveDialogWindow() {
        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) directory = null;
        else directory = file.getPath();
    }
    public static void callLoadDialogWindow() {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null) directory = null;
        else directory = file.getPath();
    }

    public static void saveToFile() throws IOException {
        if (directory == null) return;
        FileOutputStream fos = new FileOutputStream(directory, false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        Vector<Ant> objCollection = Habitat.getInstance().getObjCollection();
        HashSet<Integer> identifiers = Habitat.getInstance().getBornCollection();
        synchronized (objCollection) {
            oos.writeInt(objCollection.size()); // Количество объектов
            for (var obj : objCollection) {
                oos.writeObject(obj); // Объект
                oos.writeInt(obj.getID()); // Время рождения
                oos.writeDouble(obj.getX()); // Координата X
                oos.writeDouble(obj.getY()); // Координата Y
            }
            oos.writeInt(WorkerAnt.spawnCount); // Количество живых объектов одного типа
            oos.writeInt(WarriorAnt.spawnCount);
            // Далее надо записать параметры симуляции
            Habitat hb = Habitat.getInstance();
            oos.writeLong(hb.getTimer()); // Текущее время
            Controller ct = hb.controller;
            oos.writeBoolean(ct.cbShowInfo.isSelected()); // Показывать информацию?
            oos.writeBoolean(ct.ShowTime.isSelected()); // Показывать время?
            oos.writeUTF(ct.N1.getText()); // Периоды рождения
            oos.writeUTF(ct.N2.getText());
            oos.writeUTF((String) ct.P1.getValue()); // Вероятности рождения
            oos.writeUTF((String) ct.P2.getValue());
            oos.writeUTF(ct.L1.getText()); // Время жизни
            oos.writeUTF(ct.L2.getText());
            oos.writeUTF(ct.btnStopWorkerAI.getText()); // Интеллект объектов
            oos.writeUTF(ct.btnStopWarriorAI.getText());
            oos.writeUTF((String) ct.WorkerPriority.getValue()); // Приоритет потоков
            oos.writeUTF((String) ct.WarriorPriority.getValue());
        }
        oos.close();
        fos.close();
    }

    public static void loadFromFile() throws IOException, ClassNotFoundException {
        if (directory == null) return;
        FileInputStream fis = new FileInputStream(directory);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Habitat hab = Habitat.getInstance();
        Habitat hb = Habitat.getInstance();
        Controller ct = hb.controller;
        // Остановить текущую симуляцию и потоки
        if (hb.timer != null) hb.timer.cancel();
        hb.startFlag = false;
        WorkerAntAI.getInstance().isActive = false;
        WarriorAntAI.getInstance().isActive = false;
        // Очистить изображения
        hab.getObjCollection().forEach((tmp) -> ct.getStackPane().getChildren().remove(tmp.getImageView()));
        // Очистить старые коллекции
        Vector<Ant> objCollection = hab.getObjCollection();//objCollection!
        HashSet<Integer> identifiers = hab.getBornCollection();
        TreeMap<Integer, Long> spawnTimes = hab.getIdCollection();
        objCollection.clear();//objCollection!
        identifiers.clear();
        spawnTimes.clear();
        // Чтение всех объектов, времён рождения, координат
        int size = ois.readInt();
        for (int i = 0; i < size; i++) {
            Ant obj = (Ant) ois.readObject();
            long bornTime = ois.readInt();
            double currentX = ois.readDouble();
            double currentY = ois.readDouble();
            objCollection.add(obj);
            identifiers.add(obj.getID());
            spawnTimes.put(obj.getID(), bornTime);
            obj.createImageView(currentX, currentY);
            ct.getStackPane().getChildren().add(obj.getImageView());
        }
        WarriorAnt.spawnCount = ois.readInt();
        WorkerAnt.spawnCount = ois.readInt();
        // Чтение параметров симуляции
        int time = ois.readInt(); // Текущее время
        //hb.minutes = time / 60;
        //hb.seconds = time % 60;
        //if (hb.seconds != -1) hb.updateTimer();
        boolean showInfo = ois.readBoolean(); // Показывать информацию?
        ct.cbShowInfo.setSelected(showInfo);
        //ct.menuShowInfo.setSelected(showInfo);
        boolean showTime = ois.readBoolean(); // Показывать время?
        if (showTime) {
            ct.ShowTime.setSelected(true);
            //ct.menuShowTime.setSelected(true);
        }
        else {
            ct.HideTime.setSelected(true);
            //ct.menuHideTime.setSelected(true);
            hb.getTimer();
        }
        ct.N1.setText(ois.readUTF()); // Периоды рождения
        ct.N2.setText(ois.readUTF());
        ct.P1.setValue(ois.readUTF()); // Вероятности рождения
        ct.P2.setValue(ois.readUTF());
        ct.L1.setText(ois.readUTF()); // Время жизни
        ct.L2.setText(ois.readUTF());

        String mode = ois.readUTF(); // Интеллект объектов
        ct.btnStopWorkerAI.setText(mode);
        //phyIntellect(mode);
        mode = ois.readUTF();
        ct.btnStopWarriorAI.setText(mode);
        //jurIntellect(mode);

        int priority = ois.readInt(); // Приоритет потоков
        ct.WorkerPriority.setValue(priority);
        WorkerAntAI.getInstance().setPriority(priority);
        priority = ois.readInt();
        ct.WarriorPriority.setValue(priority);
        WarriorAntAI.getInstance().setPriority(priority);
        ois.close();
        fis.close();
    }


}
