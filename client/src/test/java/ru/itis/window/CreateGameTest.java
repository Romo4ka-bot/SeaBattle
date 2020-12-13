package ru.itis.window;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CreateGameTest {

    @Disabled
    @Test
    void createButton() {
        GridPane gridPane = new GridPane();
        CreateGame.addConstraints(gridPane);
        int i = 0;
        int j = 0;
        Button button = CreateGame.createButton(gridPane, i, j);
        Assertions.assertAll(
                () -> Assertions.assertEquals(button.getPrefHeight(), 32),
                () -> Assertions.assertEquals(button.getPrefWidth(), 32),
                () -> Assertions.assertEquals(button.getId(), button.toString()),
                () -> Assertions.assertEquals(gridPane.getChildren().get(0), button)
        );
    }

    @Test
    void createVertShips() {
    }

    @Test
    void createHorShips() {
    }

    @Test
    void addConstraints() {
        GridPane gridPane = new GridPane();
        boolean flag = CreateGame.addConstraints(gridPane);
        Assertions.assertTrue(flag);
    }

    @Test
    void addSetOnMouseClicked() {
        Rectangle[] rectangles = {new Rectangle(),new Rectangle(),new Rectangle(),new Rectangle(),new Rectangle(),new Rectangle(),new Rectangle(),new Rectangle(),new Rectangle(),new Rectangle()};
        boolean flag = CreateGame.addSetOnMouseClicked(rectangles);
        Assertions.assertTrue(flag);
    }
}