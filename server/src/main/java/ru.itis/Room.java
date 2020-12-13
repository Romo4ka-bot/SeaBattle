package ru.itis;

import lombok.Data;

/**
 * @author Roman Leontev
 * 14:12 07.12.2020
 * group 11-905
 */

@Data
public class Room {

    private String password;
    private ClientHandler clientHandler1;
    private ClientHandler clientHandler2;
    private boolean play;

    public Room(String password, ClientHandler clientHandler1) {
        this.password = password;
        this.clientHandler1 = clientHandler1;
    }
}
