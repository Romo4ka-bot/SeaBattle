package ru.itis.window;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.itis.controller.CheckPassController;
import ru.itis.controller.CreatePassController;
import ru.itis.model.Bot;
import ru.itis.model.Client;

import java.io.IOException;
import java.util.Optional;

public class BattleShips {

    private static GridPane gridPaneBot;
    private static Bot bot = new Bot();
    private static int countShips;

    public static void renderWindowBattleShipsWithBot(Stage primaryStage, GridPane gridPane, int[][] map) throws IOException {

        BorderPane borderPane = new BorderPane();
        gridPaneBot = new GridPane();
        gridPaneBot.setGridLinesVisible(true);
        gridPaneBot.setPadding(new Insets(15));

        CreateGame.addConstraints(gridPaneBot);

        int[][] mapBot = bot.generateMap();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button button = CreateGame.createButton(gridPaneBot, i, j);
                int finalI = i;
                int finalJ = j;
                button.setOnAction(event -> shootingWithBot(finalJ, finalI, gridPane, mapBot, map));
            }
        }
        borderPane.setLeft(gridPane);
        borderPane.setRight(gridPaneBot);


        Scene scene = new Scene(borderPane, 700, 400);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void renderWindowBattleShipsWithPlayerCreatePass(Stage primaryStage, GridPane gridPane, int[][] map) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(BattleShips.class.getResourceAsStream("/fxml/createPass.fxml"));
        CreatePassController controller = loader.getController();
        Label label = controller.label;

        Client client = new Client("localhost", 7777, label, map, gridPane);
        client.createPass();

        Scene scene = new Scene(root, 700, 400);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void createGame(Stage primaryStage, GridPane gridPane, int[][] map, Client client, Label labelDown) {
        BorderPane borderPane = new BorderPane();

        VBox vBox = new VBox();

        Label labelUp = new Label("Нажмите на клетку для выстрела");

        labelUp.setFont(new Font("Arial", 20));
        labelDown.setFont(new Font("Arial", 20));

//        labelUp.setMaxWidth(200);
//        labelUp.setMaxHeight(100);
//
//        labelDown.setMaxWidth(200);
//        labelDown.setMaxHeight(100);

        GridPane gridPane2 = new GridPane();
        gridPane2.setGridLinesVisible(true);
        gridPane2.setPadding(new Insets(15));

        countShips = 10;

        CreateGame.addConstraints(gridPane2);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button button = CreateGame.createButton(gridPane2, i, j);
                int finalI = i;
                int finalJ = j;
                button.setOnAction(event -> {
                    try {
                        myShoots(finalJ, finalI, gridPane2, map, client);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        HBox hBox = new HBox();
        hBox.getChildren().addAll(gridPane, gridPane2);

        vBox.getChildren().add(labelUp);

        VBox vBox1 = new VBox();
        vBox1.getChildren().add(labelDown);

        borderPane.setTop(vBox);
        borderPane.setBottom(vBox1);

        borderPane.setCenter(hBox);

        Scene scene = new Scene(borderPane, 700, 400);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void renderWindowBattleShipsWithPlayerWritePass(Stage primaryStage, GridPane gridPane, int[][] map) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(BattleShips.class.getResourceAsStream("/fxml/checkPass.fxml"));
        CheckPassController controller = loader.getController();
        Button button = controller.button;
        TextField text = controller.text;
        Label label = controller.label;
        Client client = new Client("localhost", 7777, label, map, gridPane);
        button.setOnAction(event -> {
            if (text.toString().equals("Код") || text.toString().length() != 6) {
                client.checkPass(text.getText());
            } else
                label.setText("Некорректный пароль");
        });
        Scene scene = new Scene(root, 700, 400);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void myShoots(int x, int y, GridPane gridPane, int[][] map, Client client) throws Exception {
        Rectangle rectangle = new Rectangle(32, 32);
        if (client.isActive()) {
            if (map[x + 1][y + 1] == 1) {
                rectangle.setFill(Color.RED);
                map[x + 1][y + 1] = 0;
                gridPane.add(rectangle, y, x);
                client.fire(x, y);
                checkShip(x + 1, y + 1, map, client);
            } else {
                client.getLabelDown().setText("Ход противника");
                client.setActive(false);
                gridPane.add(rectangle, y, x);
                client.fire(x, y);
            }
        }
    }

    public static void enemyShoots(int x, int y, GridPane gridPane, int[][] map, Client client) {
        Rectangle rectangle = new Rectangle(32, 32);
        if (map[x + 1][y + 1] == 1) {
            rectangle.setFill(Color.RED);
            map[x + 1][y + 1] = 0;
        } else {
            client.getLabelDown().setText("Ваш ход");
            client.setActive(true);
        }
        gridPane.add(rectangle, y, x);
    }

    private static void checkShip(int x, int y, int[][] map, Client client) throws Exception {
        if (map[x][y + 1] == 0 && map[x][y - 1] == 0 && map[x - 1][y] == 0 && map[x - 1][y + 1] == 0 && map[x - 1][y - 1] == 0 && map[x + 1][y] == 0 && map[x + 1][y - 1] == 0 && map[x + 1][y + 1] == 0) {
            countShips--;
        }
        if (countShips == 0) {
            client.endGame();
            Optional<ButtonType> result = alert("Игра закончилась", "Вы выиграли!");
            if (result.isPresent()) {
                Main main = new Main();
                main.start(Main.primaryStage);
            }
        }
    }

    public static Optional<ButtonType> alert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(msg);
        alert.setContentText("Можете попробовать сыграть еще!");
        return alert.showAndWait();
    }

    private static void shootingWithBot(int x, int y, GridPane gridPane, int[][] mapBot, int[][] map) {
        Rectangle rectangle = new Rectangle(32, 32);
        if (mapBot[x + 1][y + 1] == 1) {
            rectangle.setFill(Color.RED);
            mapBot[x + 1][y + 1] = 0;
            gridPaneBot.add(rectangle, y, x);
        } else {
            gridPaneBot.add(rectangle, y, x);
            bot.shootingBot(gridPane, map);
        }
    }
}
