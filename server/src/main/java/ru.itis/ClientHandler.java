package ru.itis;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Roman Leontev
 * 10:50 07.12.2020
 * group 11-905
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientHandler extends Thread {
    private TcpServer server;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket client, TcpServer server) {
        this.server = server;
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String msg = in.readUTF();
                System.out.println("from client: " + msg);
                if (msg.startsWith("/createPass")) {
                    String pass = server.generateUniqPass();
                    System.out.println(pass);
                    server.addPass(pass, this);
                    out.writeUTF("/successCreatePass " + pass);
                } else if (msg.startsWith("/checkPass")) {
                    String[] mass = msg.split(" ");
                    boolean check = server.checkPass(mass[1], this);
                    if (!check)
                        out.writeUTF("/checkPass false");
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
