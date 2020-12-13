package ru.itis.window;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ru.itis.controller.CreateGameController;
import ru.itis.model.Ship;
import ru.itis.model.ShipMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateGame {

    private static ShipMap shipMap = new ShipMap();
    private static int countShips;

    public static void renderWindowCreateGame(Stage primaryStage, Integer typeGame) throws IOException {
        GridPane gridPane = new GridPane();
        FXMLLoader loader = new FXMLLoader();
        loader.load(CreateGame.class.getResourceAsStream("/fxml/createGame.fxml"));
        CreateGameController controller = loader.getController();

        Rectangle[] rectanglesHor = createHorShips(controller);

        boolean flag = addSetOnMouseClicked(rectanglesHor);
        if (!flag)
            throw new IllegalStateException();

        Rectangle[] rectanglesVert = createVertShips(controller);

        flag = addSetOnMouseClicked(rectanglesVert);
        if (!flag)
            throw new IllegalStateException();

        flag = addConstraints(gridPane);
        if (!flag)
            throw new IllegalStateException();

        addActionOnButton(gridPane, rectanglesHor, rectanglesVert);

        gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(15));
        Label label = new Label("Выберите корабль и \n" +
                "кликните на поле:");

        List<HBox> listHor = createHBoxWithRectHor(rectanglesHor);

        List<HBox> listVert = createHBoxWithRectVert(rectanglesVert);

        Button button = new Button("Поворот");
        button.setOnAction(event -> {
            if (shipMap.getShips()[0].getPosition().equals("horizontally")) {
                setVisAndMan(listHor, false);
                setVisAndMan(listVert, true);
                setVisAndManRectHor(rectanglesHor, rectanglesVert);
                for (int j = 0; j < 4; j++)
                    shipMap.getShips()[j].setPosition("vertically");
            } else {
                setVisAndMan(listHor, true);
                setVisAndMan(listVert, false);
                setVisAndManRectHor(rectanglesVert, rectanglesHor);
                for (int j = 0; j < 4; j++)
                    shipMap.getShips()[j].setPosition("horizontally");
            }
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(label, button);

        VBox vBox = new VBox(hBox, listHor.get(0), listHor.get(1), listHor.get(2), listHor.get(3), listVert.get(0), listVert.get(1), listVert.get(2));
        vBox.setPadding(new Insets(15, 100, 15, 15));
        vBox.setStyle("-fx-spacing: 10");
        HBox hBox4 = new HBox();
        VBox vBox1 = new VBox();
        if (typeGame == 0) {
            vBox1.getChildren().addAll(gridPane, controller.button);
            hBox4.getChildren().addAll(vBox, vBox1);
            Button button1 = controller.button;
            button1.setOnAction(event -> {
                try {
                    if (countShips == 10)
                        BattleShips.renderWindowBattleShipsWithBot(Main.primaryStage, gridPane, shipMap.getMap());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            Button button1 = new Button("Создать игру");
            Button button2 = new Button("Присоединиться по коду");
            button1.setOnAction(event -> {
                try {
                    if (countShips == 10)
                        BattleShips.renderWindowBattleShipsWithPlayerCreatePass(Main.primaryStage, gridPane, shipMap.getMap());
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            });
            button2.setOnAction(event -> {
                try {
                    if (countShips == 10)
                        BattleShips.renderWindowBattleShipsWithPlayerWritePass(Main.primaryStage, gridPane, shipMap.getMap());
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            });
            HBox hBox9 = new HBox();
            hBox9.setSpacing(10);
            hBox9.getChildren().addAll(button1, button2);
            vBox1.getChildren().addAll(gridPane, hBox9);
            hBox4.getChildren().addAll(vBox, vBox1);
        }
        Scene scene = new Scene(hBox4, 700, 400);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void setVisAndManRectHor(Rectangle[] rectanglesVert, Rectangle[] rectanglesHor) {
        int i = 0;
        for (Rectangle rectangle : rectanglesVert) {
            if (GridPane.getRowIndex(rectangle) != null) {
                rectanglesHor[i].setVisible(false);
                rectanglesHor[i].setManaged(false);
            }
            i++;
        }
    }

    private static void setVisAndMan(List<HBox> list, boolean flag) {
        list.forEach(hBox -> {
            hBox.setVisible(flag);
            hBox.setManaged(flag);
        });
    }

    private static List<HBox> createHBoxWithRectVert(Rectangle[] rectanglesVert) {
        HBox hBox1 = new HBox();
        hBox1.setSpacing(10);
        hBox1.getChildren().addAll(rectanglesVert[9], rectanglesVert[8], rectanglesVert[7]);

        hBox1.setVisible(false);
        hBox1.setManaged(false);

        HBox hBox2 = new HBox();
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(rectanglesVert[6], rectanglesVert[5], rectanglesVert[4]);

        hBox2.setVisible(false);
        hBox2.setManaged(false);

        HBox hBox3 = new HBox();
        hBox3.setSpacing(10);
        hBox3.getChildren().addAll(rectanglesVert[3], rectanglesVert[2], rectanglesVert[1], rectanglesVert[0]);

        hBox3.setVisible(false);
        hBox3.setManaged(false);
        List<HBox> list = new ArrayList<>();
        list.add(hBox1);
        list.add(hBox2);
        list.add(hBox3);
        return list;
    }

    private static List<HBox> createHBoxWithRectHor(Rectangle[] rectanglesHor) {
        HBox hBox1 = new HBox();
        hBox1.getChildren().add(rectanglesHor[9]);

        HBox hBox2 = new HBox();
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(rectanglesHor[8], rectanglesHor[7]);

        HBox hBox3 = new HBox();
        hBox3.setSpacing(10);
        hBox3.getChildren().addAll(rectanglesHor[6], rectanglesHor[5], rectanglesHor[4]);

        HBox hBox4 = new HBox();
        hBox4.setSpacing(10);
        hBox4.getChildren().addAll(rectanglesHor[3], rectanglesHor[2], rectanglesHor[1], rectanglesHor[0]);

        List<HBox> list = new ArrayList<>();
        list.add(hBox1);
        list.add(hBox2);
        list.add(hBox3);
        list.add(hBox4);
        return list;
    }

    private static boolean addActionOnButton(GridPane gridPane,Rectangle[] rectanglesHor, Rectangle[] rectanglesVert) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button button = createButton(gridPane, i, j);
                int finalI = i;
                int finalJ = j;
                button.setOnAction(event -> {

                            for (int k = 0; k < 4; k++) {
                                Ship ship = shipMap.getShips()[k];
                                if (ship.getActive() != null && ship.getActive()) {
                                    int n = 0;
                                    int m = 0;
                                    if (k == 0) {
                                        m = 4;
                                    }
                                    if (k == 1) {
                                        n = 4;
                                        m = 7;
                                    }
                                    if (k == 2) {
                                        n = 7;
                                        m = 9;
                                    }
                                    if (k == 3) {
                                        n = 9;
                                        m = 10;
                                    }

                                    while (n < m && (GridPane.getRowIndex(rectanglesHor[n]) != null || GridPane.getRowIndex(rectanglesVert[n]) != null)) {
                                        n++;
                                    }

                                    ship.setCoordinates(new Integer[]{finalJ, finalI});
                                    if (ship.getPosition().equals("horizontally")) {
                                        try {
                                            if (shipMap.canUseShip(ship, k + 1)) {
                                                gridPane.add(rectanglesHor[n], finalI, finalJ, k + 1, 1);
                                                countShips++;
                                            }
                                        } catch (IllegalArgumentException e) {
                                            //ignore
                                        }
                                    } else {
                                        try {
                                            if (shipMap.canUseShip(ship, k + 1)) {
                                                gridPane.add(rectanglesVert[n], finalI, finalJ, 1, k + 1);
                                                countShips++;
                                            }
                                        } catch (IllegalArgumentException e) {
                                            //ignore
                                        }
                                    }
                                }
                            }
                        }
                );
            }
        }
        return true;
    }

    public static Button createButton(GridPane gridPane, int i, int j) {
        Button button = new Button();
        button.setId(String.valueOf(button));
        button.setPrefHeight(32);
        button.setPrefWidth(32);

        gridPane.getChildren().add(button);
        GridPane.setConstraints(button, i, j);
        return button;
    }

    public static boolean addConstraints(GridPane gridPane) {
        for (int i = 0; i < 10; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(32));
            gridPane.getRowConstraints().add(new RowConstraints(32));
        }
        return true;
    }

    public static Rectangle[] createVertShips(CreateGameController controller) {
        Rectangle[] rectanglesVert = new Rectangle[10];
        rectanglesVert[0] = controller.ship111;
        rectanglesVert[1] = controller.ship112;
        rectanglesVert[2] = controller.ship113;
        rectanglesVert[3] = controller.ship114;
        rectanglesVert[4] = controller.ship211;
        rectanglesVert[5] = controller.ship212;
        rectanglesVert[6] = controller.ship213;
        rectanglesVert[7] = controller.ship311;
        rectanglesVert[8] = controller.ship312;
        rectanglesVert[9] = controller.ship41;
        return rectanglesVert;
    }

    public static Rectangle[] createHorShips(CreateGameController controller) {
        Rectangle[] rectanglesHor = new Rectangle[10];
        rectanglesHor[0] = controller.ship11;
        rectanglesHor[1] = controller.ship12;
        rectanglesHor[2] = controller.ship13;
        rectanglesHor[3] = controller.ship14;
        rectanglesHor[4] = controller.ship21;
        rectanglesHor[5] = controller.ship22;
        rectanglesHor[6] = controller.ship23;
        rectanglesHor[7] = controller.ship31;
        rectanglesHor[8] = controller.ship32;
        rectanglesHor[9] = controller.ship4;
        return rectanglesHor;
    }

    public static boolean addSetOnMouseClicked(Rectangle[] rectangles) {
        rectangles[0].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++) {
                if (shipMap.getShips()[i].getActive())
                    shipMap.getShips()[i].setActive(false);
            }
            shipMap.getShips()[0].setActive(true);
        });
        rectangles[1].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++) {
                if (shipMap.getShips()[i].getActive())
                    shipMap.getShips()[i].setActive(false);
            }
            shipMap.getShips()[0].setActive(true);
        });
        rectangles[2].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++) {
                if (shipMap.getShips()[i].getActive())
                    shipMap.getShips()[i].setActive(false);
            }
            shipMap.getShips()[0].setActive(true);
        });
        rectangles[3].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++) {
                if (shipMap.getShips()[i].getActive())
                    shipMap.getShips()[i].setActive(false);
            }
            shipMap.getShips()[0].setActive(true);
        });
        rectangles[4].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++)
                if (shipMap.getShips()[i].getActive())
                    shipMap.getShips()[i].setActive(false);
            shipMap.getShips()[1].setActive(true);
        });
        rectangles[5].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++)
                if (shipMap.getShips()[i].getActive())
                    shipMap.getShips()[i].setActive(false);
            shipMap.getShips()[1].setActive(true);
        });
        rectangles[6].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++)
                if (shipMap.getShips()[i].getActive())
                    shipMap.getShips()[i].setActive(false);
            shipMap.getShips()[1].setActive(true);
        });
        rectangles[7].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++)
                if (shipMap.getShips()[i].getActive())
                    shipMap.getShips()[i].setActive(false);
            shipMap.getShips()[2].setActive(true);
        });
        rectangles[8].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++)
                if (shipMap.getShips()[i].getActive())
                    shipMap.getShips()[i].setActive(false);
            shipMap.getShips()[2].setActive(true);
        });
        rectangles[9].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++)
                if (shipMap.getShips()[i].getActive())
                    shipMap.getShips()[i].setActive(false);
            shipMap.getShips()[3].setActive(true);
        });
        return true;
    }
}
