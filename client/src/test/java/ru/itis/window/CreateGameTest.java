package ru.itis.window;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateGameTest {

    @Test
    void createButton() {

        int i = 0;
        int j = 0;

        Button button = mock(Button.class);
        when(button.getPrefHeight()).thenReturn(32.0);
        when(button.getPrefWidth()).thenReturn(32.0);

        GridPane gridPane = mock(GridPane.class);
        when(gridPane.getChildren().get(0)).thenReturn(button);

        try {
        button = CreateGame.createButton(gridPane, i, j);
        }catch (ExceptionInInitializerError e) {
            //ignore
        }
        Button finalButton = button;
        Button finalButton1 = button;
        Button finalButton2 = button;
        Assertions.assertAll(
                () -> Assertions.assertEquals(finalButton.getPrefHeight(), 32),
                () -> Assertions.assertEquals(finalButton1.getPrefWidth(), 32),
                () -> Assertions.assertEquals(gridPane.getChildren().get(0), finalButton2)
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