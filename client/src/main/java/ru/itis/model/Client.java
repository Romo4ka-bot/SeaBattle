package ru.itis.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.itis.window.BattleShips;
import ru.itis.window.Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Roman Leontev
 * 14:32 12.12.2020
 * group 11-905
 */

@Getter
@Setter
public class Client {

    private DataInputStream in;
    private DataOutputStream out;
    private int[][] mapEnemy;
    private ObjectMapper objectMapper = new ObjectMapper();
    private boolean active = true;
    private Label labelDown = new Label();

    public Client(String host, int port, Label label, int[][] map, GridPane gridPane) {
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
                                Platform.runLater(() -> label.setText("Неверный код"));
                            } else active = false;
                        } else if (message.startsWith("/startGame")) {
                            String json = objectMapper.writeValueAsString(map);
                            out.writeUTF("/myMap " + json);

                        } else if (message.startsWith("/enemyMap")) {
                            System.out.println(message);
                            String[] mass = message.split(" ");
                            mapEnemy = objectMapper.readValue(mass[1], int[][].class);
                            System.out.println(Arrays.deepToString(mapEnemy));
                            if (active) {
                                labelDown.setText("Твой ход");
                            }else
                                labelDown.setText("Ход противника");
                            Platform.runLater(() -> BattleShips.createGame(Main.primaryStage, gridPane, mapEnemy, this, labelDown));

                        } else if (message.startsWith("/successCreatePass")) {
                            Platform.runLater(() -> label.setText(message.substring(18)));

                        } else if (message.startsWith("/enemyFire")) {
                            String[] mass = message.split(" ");
                            Platform.runLater(() -> {
                                try {
                                    BattleShips.enemyShoots(Integer.parseInt(mass[1]), Integer.parseInt(mass[2]), gridPane, map, this);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }else if (message.startsWith("/end")) {
                            Platform.runLater(() -> {Optional<ButtonType> result = BattleShips.alert("Игра закончилась", "Вы проиграли!");
                            if (result.isPresent()) {
                                Main main = new Main();
                                try {
                                    main.start(Main.primaryStage);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        }
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            };

            new Thread(readMsg).start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void createPass() {
        try {
            out.writeUTF("/createPass");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void checkPass(String pass) {
        try {
            out.writeUTF("/checkPass " + pass);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void fire(int x, int y) {
        try {
            out.writeUTF("/fire " + x + " " + y);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void endGame() {
        try {
            out.writeUTF("/endGame");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
