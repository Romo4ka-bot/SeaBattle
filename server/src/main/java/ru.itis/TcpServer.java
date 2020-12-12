package ru.itis;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Roman Leontev
 * 10:51 07.12.2020
 * group 11-905
 */

public class TcpServer {
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
            while (true) {
                Socket client = server.accept();
                System.out.println("Accept new client");
                ClientHandler clientHandler = new ClientHandler(client, this);
                clientHandler.start();
                clients.add(clientHandler);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void addPass(String uniqPass, ClientHandler clientHandler) {
        passwords.add(uniqPass);
        Room room = new Room(uniqPass, clientHandler);
        rooms.add(room);
    }

    public boolean checkPass(String uniqPass, ClientHandler clientHandler) {

        AtomicBoolean flag = (AtomicBoolean) passwords.stream().filter(pass -> {
            if (pass.equals(uniqPass)) {
                Optional<Room> currRoom = rooms.stream().filter(room -> !room.isPlay()).filter(room -> room.getPassword().equals(uniqPass)).findFirst();
                currRoom.ifPresent(room -> {
                    room.setClientHandler2(clientHandler);
                    room.start();
                });
                return true;
            }

            return false;
        });

        return flag.get();
    }

    public String generateUniqPass() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        return String.format("%06d", number);
    }
}
