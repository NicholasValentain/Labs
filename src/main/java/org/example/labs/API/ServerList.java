package org.example.labs.API;

import dto.*;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

import org.example.labs.main.AntSimulation;
import org.example.labs.model.Habitat;
import org.example.labs.controllres.Controller;

public class ServerList extends Thread {
    private ObjectInputStream ois;

    public ServerList(ObjectInputStream ois) {
        this.ois = ois;
    }

    public void run() {
        try {
            while (true) {
                // Слушаем сообщения от сервера
                DTO input = (DTO) ois.readObject();
                if (input instanceof ListUserDTO dto) {
                    // Сервер прислал список клиентов
                    Client.createUserList(dto.getUserList());
                } else if (input instanceof UserDTO dto) {
                    // Сервер прислал уведомление о подключении или отключении другого клиента
                    if (dto.getStatus()) {
                        Client.addUserToList(dto.getUserName());
                    } else {
                        Client.removeUserFromList(dto.getUserName());
                    }
                } else if (input instanceof RequestDTO dto) {
                    Client.setAnts(dto.getUserName(), dto.getAntCount());
                } else if (input instanceof AntsDTO dto) {
                    Client.setNewAnts(dto);
                }
            }
        } catch (SocketException e) {
            System.out.println("Сервер закрыл соединение");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
