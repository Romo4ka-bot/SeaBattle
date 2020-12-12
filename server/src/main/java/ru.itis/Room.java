package ru.itis;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Roman Leontev
 * 14:12 07.12.2020
 * group 11-905
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class Room extends Thread {

    private String password;
    private ClientHandler clientHandler1;
    private ClientHandler clientHandler2;
    private boolean play;

    public Room(String password, ClientHandler clientHandler1) {
        this.password = password;
        this.clientHandler1 = clientHandler1;
    }

    @Override
    public void run() {
        play = true;
        DataInputStream in1 = clientHandler1.getIn();
        DataInputStream in2 = clientHandler2.getIn();
        DataOutputStream out1 = clientHandler1.getOut();
        DataOutputStream out2 = clientHandler2.getOut();

        try {
            out1.writeUTF("/startGame");
            out2.writeUTF("/startGame");
        } catch (IOException e) {
            e.printStackTrace();
        }

        readMsg(in1, out2);

        readMsg(in2, out1);
    }

    private void readMsg(DataInputStream in, DataOutputStream out) {
        Runnable readMsg = () -> {
            while (true) {
                String message;
                try {
                    message = in.readUTF();
                    if (message.startsWith("/myMap")) {
                        String map = message.substring(6);
                        out.writeUTF("/enemyMap " + map);
                    }
                    else if (message.startsWith("/fire")) {
                        String[] coordinates = message.split(" ");
                        out.writeUTF("/checkFire " + coordinates[1] + coordinates[2]);
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
        new Thread(readMsg).start();
    }
}
