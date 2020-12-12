package ru.itis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Roman Leontev
 * 10:26 07.12.2020
 * group 11-905
 */

public class Client {

    Socket client;
    DataInputStream in;
    DataOutputStream out;

    public Client(Socket client) {

        this.client = client;

        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
