package sample;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    public static void renderWindowBattleShips(Stage primaryStage, Integer type, GridPane gridPane, int[][] map) throws IOException {
        gridPaneBot = new GridPane();
        gridPaneBot.setGridLinesVisible(true);
        gridPaneBot.setPadding(new Insets(15));
        for (int i = 0; i < 10; i++) {
            gridPaneBot.getColumnConstraints().add(new ColumnConstraints(32));
            gridPaneBot.getRowConstraints().add(new RowConstraints(32));
        }

        if (type == 0) {
            mapBot = bot.generateMap();
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
                button.setOnAction(event -> {
                    shootingPlayer(finalJ, finalI, gridPane, finalMap);
                });
            }
        }

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(gridPane);
        borderPane.setRight(gridPaneBot);
        Scene scene = new Scene(borderPane, 700, 400);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void shootingPlayer(int x, int y, GridPane gridPane, int[][] map) {
        Rectangle rectangle = new Rectangle(32, 32);
        if (map[x + 1][y + 1] == 1) {
            rectangle.setFill(Color.RED);
            map[x + 1][y + 1] = 0;
            gridPaneBot.add(rectangle, y, x);
            checkCleanShip(x+1, y+1, gridPane, map);
        } else {
            gridPaneBot.add(rectangle, y, x);
            bot.shootingBot(gridPane, map);
        }
    }

    private static void checkCleanShip(int x, int y, GridPane gridPane, int[][] map) {
//        if (map[x][y+1] == 0 && map[x][y-1] == 0 && map[x+1][y] == 0 && map[x-1][y] == 0 && map[x+1][y+1] == 0 && map[x+1][y-1] == 0 && map[x-1][y+1] == 0 && map[x-1][y-1] == 0) {
//            gridPane.add();
//        }
    }
}
