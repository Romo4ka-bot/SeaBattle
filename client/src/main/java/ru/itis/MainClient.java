package ru.itis;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Roman Leontev
 * 13:39 07.12.2020
 * group 11-905
 */

public class MainClient {
    public static void main(String[] args) throws IOException {
        new Client(new Socket("localhost", 7777));
    }
}
