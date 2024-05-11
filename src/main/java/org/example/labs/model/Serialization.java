package org.example.labs.model;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.labs.controllres.Controller;
import org.example.labs.main.AntSimulation;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.stage.*;

import java.io.*;
import java.util.*;

public class Serialization {

    private static final String defaultConfigFolder = System.getProperty("user.dir");
    private static String directory;
    private static FileChooser fileChooser;
    static {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(defaultConfigFolder));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("DAT", "*.dat"));
    }

    private AntSimulation antSimulation;

    public Serialization(AntSimulation antSimulation) {
        this.antSimulation = antSimulation;
    }

    public void serialize() {
        try {
            File file = fileChooser.showSaveDialog(new Stage());
            if (file == null)
                return;
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            Vector<Ant> antsList = Habitat.getInstance().getAntsList();
            objectOutputStream.writeObject(antsList.size());
            for (Ant ant : antsList) {
                double x = ant.getImageView().getTranslateX();
                double y = ant.getImageView().getTranslateY();
                objectOutputStream.writeDouble(x);
                objectOutputStream.writeDouble(y);
                objectOutputStream.writeObject(ant);
            }

            TreeMap<Integer, Long> spawnTimes = Habitat.getInstance().getTreeMap();
            HashSet<Integer> antSet = Habitat.getInstance().getHashSet();
            objectOutputStream.writeObject(spawnTimes);
            objectOutputStream.writeObject(antSet);

            objectOutputStream.writeLong(Habitat.getInstance().simulationStartTime);
            objectOutputStream.writeLong(Habitat.getInstance().waitTime);
            objectOutputStream.writeLong(Habitat.getInstance().simulationTimes);
            objectOutputStream.writeLong(Habitat.getInstance().currentTime);
            objectOutputStream.writeLong(Habitat.getInstance().lastWorkerTime);
            objectOutputStream.writeLong(Habitat.getInstance().lastWarriorTime);

            System.out.println(Habitat.getInstance().simulationStartTime + "1");
            System.out.println(Habitat.getInstance().waitTime + "1");
            System.out.println(Habitat.getInstance().simulationTimes + "1");
            System.out.println(Habitat.getInstance().currentTime + "1");
            System.out.println(Habitat.getInstance().lastWorkerTime + "1");
            System.out.println(Habitat.getInstance().lastWarriorTime + "1\n");

            // objectOutputStream.writeLong(AntSimulation.getInstance().simulationStartTime);
            // objectOutputStream.writeLong(AntSimulation.getInstance().waitTime);
            // objectOutputStream.writeLong(AntSimulation.getInstance().currentTime);
            // objectOutputStream.writeLong(AntSimulation.getInstance().simulationTime);

            // System.out.println(AntSimulation.getInstance().simulationStartTime);
            // System.out.println(AntSimulation.getInstance().waitTime);
            // System.out.println(AntSimulation.getInstance().simulationTime);
            // System.out.println(AntSimulation.getInstance().currentTime);

            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (FileNotFoundException eFileNotFound) {
            System.err.println("Error: file ants.dat not found while serializing.");
        } catch (IOException eIO) {
            System.err.println("Error: IOException while serializing");
            System.out.println(eIO.getMessage());
        } catch (Exception ex) {
            System.err.println("Error: something happened while serializing");
        }
    }

    public void deserialize(File file, AntSimulation antSimulation) {
        try {
            this.antSimulation = antSimulation;
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Очистка StackPane перед загрузкой новых объектов
            Habitat.getInstance().getStackPane().getChildren().clear();

            Vector<Ant> antsList = Habitat.getInstance().getAntsList();

            int listSize = (Integer) objectInputStream.readObject();
            for (int i = 0; i < listSize; i++) {
                double x = objectInputStream.readDouble();
                double y = objectInputStream.readDouble();
                Ant ant = (Ant) objectInputStream.readObject();
                ant.getImageView().setTranslateX(x);
                ant.getImageView().setTranslateY(y);
                antsList.add(ant);
            }

            Habitat.getInstance().ID = antsList.size();

            TreeMap<Integer, Long> spawnTimes = Habitat.getInstance().getTreeMap();
            spawnTimes.putAll((TreeMap<Integer, Long>) objectInputStream.readObject());
            Habitat.getInstance().getHashSet().addAll((HashSet<Integer>) objectInputStream.readObject());
            int workerAntsCount = 0;
            int warriorAntsCount = 0;
            for (Ant ant : antsList) {
                StackPane.setAlignment(ant.getImageView(), Pos.TOP_LEFT);
                Habitat.getInstance().getStackPane().getChildren().add(ant.getImageView());
                if (ant instanceof WorkerAnt) {
                    workerAntsCount++;
                } else if (ant instanceof WarriorAnt) {
                    warriorAntsCount++;
                }
            }
            WorkerAnt.spawnCount = workerAntsCount;
            WarriorAnt.spawnCount = warriorAntsCount;

            Habitat.getInstance().simulationStartTime = objectInputStream.readLong();
            Habitat.getInstance().waitTime = objectInputStream.readLong();
            Habitat.getInstance().simulationTimes = objectInputStream.readLong();
            Habitat.getInstance().currentTime = objectInputStream.readLong();
            Habitat.getInstance().lastWorkerTime = objectInputStream.readLong();
            Habitat.getInstance().lastWarriorTime = objectInputStream.readLong();
            Habitat.getInstance().simulationStartTime = Habitat.getInstance().simulationTimes
                    + Habitat.getInstance().waitTime;

            Habitat.getInstance().plusTime = Habitat.getInstance().simulationTimes;

            System.out.println(Habitat.getInstance().simulationStartTime + "simulationStartTime");
            System.out.println(Habitat.getInstance().waitTime + "2");
            System.out.println(Habitat.getInstance().simulationTimes + "2");
            System.out.println(Habitat.getInstance().currentTime + "2");
            System.out.println(Habitat.getInstance().lastWorkerTime + "2");
            System.out.println(Habitat.getInstance().lastWarriorTime + "2\n");

            // AntSimulation.getInstance().simulationStartTime =
            // Habitat.getInstance().simulationStartTime;
            // AntSimulation.getInstance().waitTime = Habitat.getInstance().waitTime;
            // AntSimulation.getInstance().currentTime = Habitat.getInstance().currentTime;
            // AntSimulation.getInstance().simulationTime =
            // Habitat.getInstance().simulationTimes;

            antSimulation.simulationStartTime = Habitat.getInstance().simulationStartTime;
            antSimulation.waitTime = Habitat.getInstance().waitTime;
            antSimulation.currentTime = Habitat.getInstance().currentTime;
            antSimulation.simulationTime = Habitat.getInstance().simulationTimes;


            antSimulation.plusTime = Habitat.getInstance().simulationTimes;

            // AntSimulation.getInstance().simulationStartTime =
            // objectInputStream.readLong();
            // AntSimulation.getInstance().waitTime = objectInputStream.readLong();
            // AntSimulation.getInstance().currentTime = objectInputStream.readLong();
            // AntSimulation.getInstance().simulationTime = objectInputStream.readLong();
            // AntSimulation.getInstance().simulationStartTime =
            // AntSimulation.getInstance().simulationTime
            // + AntSimulation.getInstance().waitTime;

            System.out.println(antSimulation.simulationStartTime + "2");
            System.out.println(antSimulation.waitTime + "2");
            System.out.println(antSimulation.simulationTime + "2");
            System.out.println(antSimulation.currentTime + "2");
            // Habitat.getInstance().restartSimulationTimer();

            objectInputStream.close();
        } catch (FileNotFoundException eFileNotFound) {
            System.err.println("Error: file ants.dat not found while deserializing.");
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error: IOException or ClassNotFoundException while deserializing");
            ex.printStackTrace();
        }
    }
}