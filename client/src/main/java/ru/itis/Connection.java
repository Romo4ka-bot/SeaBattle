package ru.itis;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

/**
 * @author Roman Leontev
 * 14:32 12.12.2020
 * group 11-905
 */

public class Connection {

    DataInputStream in;
    DataOutputStream out;

    public Connection(String host, int port, Label label, int[][] map) {
        try {
            Socket socket = new Socket(host, port);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Runnable readMsg = () -> {
                while (true) {
                    String message;
                    try {
                        message = in.readUTF();
                        System.out.println(message);
                        if (message.startsWith("/checkPass")) {
                            String[] mass = message.split(" ");
                            if (mass[1].equals("false")) {
                                label.setText("Неверный код");
                            }
                        } else if (message.startsWith("/startGame")) {
                            out.writeUTF("myMap " + Arrays.deepToString(map));
                        } else if (message.startsWith("/enemyMap")) {
                            System.out.println(message);
                        }
//                        else if (message.startsWith("/successCreatePass")) {
//                            label.setText(message.substring(message.length() - 1));
//                        }
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                }
            };

            Platform.runLater(readMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createPass() {
        try {
            out.writeUTF("/createPass");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkPass(String pass) {
        try {
            out.writeUTF("/createPass" + pass);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fire(int x, int y) {
        try {
            out.writeUTF("/fire " + x + " " + y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
