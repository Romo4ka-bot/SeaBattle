package ru.itis;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @author Roman Leontev
 * 10:51 07.12.2020
 * group 11-905
 */

public class TcpServer extends Thread {
    private List<ClientHandler> clients;
    private List<String> passwords;
    private List<Room> rooms;
    private ServerSocket server;

    TcpServer(int port) {
        clients = new ArrayList<>();
        passwords = new ArrayList<>();
        rooms = new ArrayList<>();
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            Socket client;
            try {
                client = server.accept();
                System.out.println("Accept new client");
                ClientHandler clientHandler = new ClientHandler(client, this);
                clientHandler.start();
                clients.add(clientHandler);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public void addPass(String uniqPass, ClientHandler clientHandler) {
        passwords.add(uniqPass);
        Room room = new Room(uniqPass, clientHandler);
        rooms.add(room);
    }

    public boolean checkPass(String uniqPass, ClientHandler clientHandler) {

        Optional<String> room1 = passwords.stream().filter(pass -> {
            if (pass.equals(uniqPass)) {
                Optional<Room> currRoom = rooms.stream().filter(room -> !room.isPlay()).filter(room -> room.getPassword().equals(uniqPass)).findFirst();
                currRoom.ifPresent(room -> {
                    room.setClientHandler2(clientHandler);
                    room.getClientHandler1().sendMsg("/startGame");
                    room.getClientHandler2().sendMsg("/startGame");
                    room.setPlay(true);
                });
                return true;
            }
            return false;
        }).findFirst();

        return room1.isPresent();
    }

    public String generateUniqPass() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        return String.format("%06d", number);
    }

    public void sendRoomMsg(String msg, String pass, ClientHandler clientHandler) {
        Optional<Room> currRoom = rooms.stream().filter(room -> room.getPassword().equals(pass)).findFirst();
        if (currRoom.isPresent()) {
            if (currRoom.get().getClientHandler1().equals(clientHandler))
                currRoom.get().getClientHandler2().sendMsg(msg);
            else
                currRoom.get().getClientHandler1().sendMsg(msg);
        }
    }

    public void deleteRoom(String passRoom) {
        Optional<Room> currRoom = rooms.stream().filter(room -> room.getPassword().equals(passRoom)).findFirst();
        currRoom.ifPresent(room -> rooms.remove(room));
        passwords.remove(passRoom);
    }
}
