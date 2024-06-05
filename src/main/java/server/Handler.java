package server;

import dto.AntsDTO;
import dto.DTO;
import dto.RequestDTO;
import dto.UserDTO;
import org.example.labs.main.AntSimulation;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Handler extends Thread {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String userName;

    public Handler(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
    }

    public void run() {
        try {
            while (true) {
                // Слушаем сообщения от клиента
                DTO input = (DTO) ois.readObject();
                if (input instanceof UserDTO dto) {
                    // Клиент прислал имя
                    userName = dto.getUserName();
                    System.out.println(userName + " присоединился");
                    // Добавить клиента в список сервера
                    Server.addUserToList(userName, oos);
                    // Отправить новому клиенту список клиентов
                    Server.userlistUnicast(oos);
                    // Оповестить всех клиентов о подключении клиента
                    Server.userJoinedOrLeftBroadcast(userName, true);
                } else if (input instanceof AntsDTO dto) {
                    // Клиент прислал координаты муравьёв
                    Server.antsCast(dto);
                    for (var x : dto.getXCoordinates()) {
                        System.out.println(x);
                    }

                    for (var y : dto.getYCoordinates()) {
                        System.out.println(y);
                    }

                    for (var tepy : dto.getType()) {
                        System.out.println(tepy);
                    }

                } else if (input instanceof RequestDTO dto) {
                    Server.antsReqCast(dto, userName);
                    System.out.println(dto.getAntCount() + "" + dto.getUserName());
                }
            }

        } catch (EOFException e) {
            // Клиент отключился
            System.out.println(userName + " вышел");
            Server.removeUserFromList(userName, oos);
            try {
                // Оповестить всех клиентов об отключении клиента
                Server.userJoinedOrLeftBroadcast(userName, false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                oos.close();
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
