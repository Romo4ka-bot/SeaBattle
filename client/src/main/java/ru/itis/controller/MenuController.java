package ru.itis.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import ru.itis.window.Main;
import ru.itis.window.CreateGame;

import java.io.IOException;

public class MenuController {
    public Button mainStart;
    public Button mainNet;

    public void renderWithBot(ActionEvent actionEvent) throws IOException {
        CreateGame.renderWindowCreateGame(Main.primaryStage, 0);
    }

    public void renderWithUser(ActionEvent actionEvent) throws IOException {
        CreateGame.renderWindowCreateGame(Main.primaryStage, 1);
    }
}
