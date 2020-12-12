package ru.itis;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateGame {

    private static ShipController shipController = new ShipController();

    public static void renderWindowCreateGame(Stage primaryStage, Integer typeGame) throws IOException {
        GridPane gridPane = new GridPane();
        FXMLLoader loader = new FXMLLoader();
        loader.load(CreateGame.class.getResourceAsStream("/fxml/createGame.fxml"));
        Controller controller = loader.getController();
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

        addSetOnMouseClicked(rectanglesHor);

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

        addSetOnMouseClicked(rectanglesVert);

        for (int i = 0; i < 10; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(32));
            gridPane.getRowConstraints().add(new RowConstraints(32));
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button button = new Button();
                button.setId(String.valueOf(button));
                button.setPrefHeight(32);
                button.setPrefWidth(32);

                gridPane.getChildren().add(button);
                GridPane.setConstraints(button, i, j);
                int finalI = i;
                int finalJ = j;
                button.setOnAction(event -> {

                            for (int k = 0; k < 4; k++) {
                                Ship ship = shipController.getShips()[k];
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
                                            if (shipController.canUseShip(ship, k + 1))
                                                gridPane.add(rectanglesHor[n], finalI, finalJ, k + 1, 1);
                                        } catch (IllegalArgumentException e) {
                                            //ignore
                                        }
                                    } else {
                                        try {
                                            if (shipController.canUseShip(ship, k + 1))
                                                gridPane.add(rectanglesVert[n], finalI, finalJ, 1, k + 1);
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
        gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(15));
        Label label = new Label("Выберите корабль и \n" +
                "кликните на поле:");

        HBox hBox8 = new HBox();
        hBox8.getChildren().add(rectanglesHor[9]);

        HBox hBox1 = new HBox();
        hBox1.setSpacing(10);
        hBox1.getChildren().addAll(rectanglesHor[8], rectanglesHor[7]);

        HBox hBox2 = new HBox();
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(rectanglesHor[6], rectanglesHor[5], rectanglesHor[4]);

        HBox hBox3 = new HBox();
        hBox3.setSpacing(10);
        hBox3.getChildren().addAll(rectanglesHor[3], rectanglesHor[2], rectanglesHor[1], rectanglesHor[0]);

        HBox hBox5 = new HBox();
        hBox5.setSpacing(10);
        hBox5.getChildren().addAll(rectanglesVert[9], rectanglesVert[8], rectanglesVert[7]);

        hBox5.setVisible(false);
        hBox5.setManaged(false);

        HBox hBox6 = new HBox();
        hBox6.setSpacing(10);
        hBox6.getChildren().addAll(rectanglesVert[6], rectanglesVert[5], rectanglesVert[4]);

        hBox6.setVisible(false);
        hBox6.setManaged(false);

        HBox hBox7 = new HBox();
        hBox7.setSpacing(10);
        hBox7.getChildren().addAll(rectanglesVert[3], rectanglesVert[2], rectanglesVert[1], rectanglesVert[0]);

        hBox7.setVisible(false);
        hBox7.setManaged(false);

        Button button = new Button("Поворот");
        button.setOnAction(event -> {
            if (shipController.getShips()[0].getPosition().equals("horizontally")) {
                hBox8.setVisible(false);
                hBox8.setManaged(false);
                hBox1.setVisible(false);
                hBox1.setManaged(false);
                hBox2.setVisible(false);
                hBox2.setManaged(false);
                hBox3.setVisible(false);
                hBox3.setManaged(false);
                hBox5.setVisible(true);
                hBox5.setManaged(true);
                hBox6.setVisible(true);
                hBox6.setManaged(true);
                hBox7.setVisible(true);
                hBox7.setManaged(true);
                int i = 0;
                for (Rectangle rectangle : rectanglesHor) {
                    if (GridPane.getRowIndex(rectangle) != null) {
                        rectanglesVert[i].setVisible(false);
                        rectanglesVert[i].setManaged(false);
                    }
                    i++;
                }
                for (int j = 0; j < 4; j++)
                    shipController.getShips()[j].setPosition("vertically");
            } else {
                hBox8.setVisible(true);
                hBox8.setManaged(true);
                hBox1.setVisible(true);
                hBox1.setManaged(true);
                hBox2.setVisible(true);
                hBox2.setManaged(true);
                hBox3.setVisible(true);
                hBox3.setManaged(true);
                hBox5.setVisible(false);
                hBox5.setManaged(false);
                hBox6.setVisible(false);
                hBox6.setManaged(false);
                hBox7.setVisible(false);
                hBox7.setManaged(false);
                int i = 0;
                for (Rectangle rectangle : rectanglesVert) {
                    if (GridPane.getRowIndex(rectangle) != null) {
                        rectanglesHor[i].setVisible(false);
                        rectanglesHor[i].setManaged(false);
                    }
                    i++;
                }
                for (int j = 0; j < 4; j++)
                    shipController.getShips()[j].setPosition("horizontally");
            }
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(label, button);

        VBox vBox = new VBox(hBox, hBox8, hBox1, hBox2, hBox3, hBox5, hBox6, hBox7);
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
                    BattleShips.renderWindowBattleShipsWithBot(Main.primaryStage, gridPane, shipController.getMap());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            Button button1 = new Button("Создать игру");
            Button button2 = new Button("Присоединиться по коду");
            button1.setOnAction(event -> {
                try {
                    BattleShips.renderWindowBattleShipsWithPlayerCreatePass(Main.primaryStage, gridPane, shipController.getMap());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            button2.setOnAction(event -> {
                try {
                    BattleShips.renderWindowBattleShipsWithPlayerWritePass(Main.primaryStage, gridPane, shipController.getMap());
                } catch (IOException e) {
                    e.printStackTrace();
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

    private static void addSetOnMouseClicked(Rectangle[] rectangles) {
        rectangles[0].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++) {
                if (shipController.getShips()[i].getActive())
                    shipController.getShips()[i].setActive(false);
            }
            shipController.getShips()[0].setActive(true);
        });
        rectangles[1].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++) {
                if (shipController.getShips()[i].getActive())
                    shipController.getShips()[i].setActive(false);
            }
            shipController.getShips()[0].setActive(true);
        });
        rectangles[2].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++) {
                if (shipController.getShips()[i].getActive())
                    shipController.getShips()[i].setActive(false);
            }
            shipController.getShips()[0].setActive(true);
        });
        rectangles[3].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++) {
                if (shipController.getShips()[i].getActive())
                    shipController.getShips()[i].setActive(false);
            }
            shipController.getShips()[0].setActive(true);
        });
        rectangles[4].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++)
                if (shipController.getShips()[i].getActive())
                    shipController.getShips()[i].setActive(false);
            shipController.getShips()[1].setActive(true);
        });
        rectangles[5].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++)
                if (shipController.getShips()[i].getActive())
                    shipController.getShips()[i].setActive(false);
            shipController.getShips()[1].setActive(true);
        });
        rectangles[6].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++)
                if (shipController.getShips()[i].getActive())
                    shipController.getShips()[i].setActive(false);
            shipController.getShips()[1].setActive(true);
        });
        rectangles[7].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++)
                if (shipController.getShips()[i].getActive())
                    shipController.getShips()[i].setActive(false);
            shipController.getShips()[2].setActive(true);
        });
        rectangles[8].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++)
                if (shipController.getShips()[i].getActive())
                    shipController.getShips()[i].setActive(false);
            shipController.getShips()[2].setActive(true);
        });
        rectangles[9].setOnMouseClicked(event -> {
            for (int i = 0; i < 4; i++)
                if (shipController.getShips()[i].getActive())
                    shipController.getShips()[i].setActive(false);
            shipController.getShips()[3].setActive(true);
        });
    }
}
