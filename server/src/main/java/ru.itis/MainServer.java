package ru.itis;

/**
 * @author Roman Leontev
 * 11:15 07.12.2020
 * group 11-905
 */

public class MainServer {
    public static void main(String[] args) {
        TcpServer server = new TcpServer(7777);
        server.start();
    }
}
