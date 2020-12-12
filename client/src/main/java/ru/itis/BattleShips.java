package ru.itis;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class BattleShips {

    private static ShipController shipController = new ShipController();
    private static int[][] mapBot = new int[12][12];
    private static GridPane gridPaneBot;
    private static Bot bot = new Bot();

    public static void renderWindowBattleShipsWithBot(Stage primaryStage, GridPane gridPane, int[][] map) throws IOException {

        BorderPane borderPane = new BorderPane();
        gridPaneBot = new GridPane();
        gridPaneBot.setGridLinesVisible(true);
        gridPaneBot.setPadding(new Insets(15));
        for (int i = 0; i < 10; i++) {
            gridPaneBot.getColumnConstraints().add(new ColumnConstraints(32));
            gridPaneBot.getRowConstraints().add(new RowConstraints(32));
        }
        mapBot = bot.generateMap();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button button = new Button();
                button.setId(String.valueOf(button));
                button.setPrefHeight(32);
                button.setPrefWidth(32);

                gridPaneBot.getChildren().add(button);
                GridPane.setConstraints(button, i, j);
                int finalI = i;
                int finalJ = j;
                int[][] finalMap = mapBot;
                button.setOnAction(event -> shootingWithBot(finalJ, finalI, gridPane, finalMap, map));
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

        BorderPane borderPane = new BorderPane();

        Label label = new Label();

        Connection connection = new Connection("localhost", 7777, label, map);

        connection.createPass();

        Button button = new Button("Продолжить");

        button.setOnAction(event -> createGame(primaryStage, gridPane, map));

        borderPane.getChildren().add(label);
        Scene scene = new Scene(borderPane, 700, 400);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void createGame(Stage primaryStage, GridPane gridPane, int[][] map) {
        BorderPane borderPane = new BorderPane();

        GridPane gridPane2 = new GridPane();
        gridPane2.setGridLinesVisible(true);
        gridPane2.setPadding(new Insets(15));

        for (int i = 0; i < 10; i++) {
            gridPane2.getColumnConstraints().add(new ColumnConstraints(32));
            gridPane2.getRowConstraints().add(new RowConstraints(32));
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button button = new Button();
                button.setId(String.valueOf(button));
                button.setPrefHeight(32);
                button.setPrefWidth(32);

                gridPaneBot.getChildren().add(button);
                GridPane.setConstraints(button, i, j);
                int finalI = i;
                int finalJ = j;
                int[][] finalMap = mapBot;
                button.setOnAction(event -> shootingWithPlayer(finalJ, finalI, gridPane, finalMap));
            }
        }

        borderPane.setLeft(gridPane);
        borderPane.setRight(gridPane2);


        Scene scene = new Scene(borderPane, 700, 400);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void renderWindowBattleShipsWithPlayerWritePass(Stage primaryStage, GridPane gridPane, int[][] map) throws IOException {

        Label label = new Label("Введите код:");
        Connection connection = new Connection("localhost", 7777, label, map);

        TextArea textArea = new TextArea("Код");
        Button button = new Button("Выйти");
        button.setOnAction(event -> {
            try {
                if (textArea.toString().equals("Код") || textArea.toString().length() != 6) {
                    connection.checkPass(textArea.toString());
                    CreateGame.renderWindowCreateGame(primaryStage, 1);
                }else
                    label.setText("Некорректный пароль");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        BorderPane borderPane = new BorderPane();
        borderPane.getChildren().addAll(label, textArea);
        Scene scene = new Scene(borderPane, 700, 400);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void shootingWithPlayer(int x, int y, GridPane gridPane, int[][] map) {
        Rectangle rectangle = new Rectangle(32, 32);
        if (map[x + 1][y + 1] == 1) {
            rectangle.setFill(Color.RED);
            map[x + 1][y + 1] = 0;
        }
        gridPaneBot.add(rectangle, y, x);
    }

    private static void shootingWithBot(int x, int y, GridPane gridPane, int[][] mapBot, int[][] map) {
        Rectangle rectangle = new Rectangle(32, 32);
        if (map[x + 1][y + 1] == 1) {
            rectangle.setFill(Color.RED);
            map[x + 1][y + 1] = 0;
            gridPaneBot.add(rectangle, y, x);
        } else {
            gridPaneBot.add(rectangle, y, x);
            bot.shootingBot(gridPane, map);
        }
    }
}
