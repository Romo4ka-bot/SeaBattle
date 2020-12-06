package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.IOException;

public class MenuController {
    public Button mainStart;
    public Button mainNet;

    public void renderWithBot(ActionEvent actionEvent) throws IOException {
        CreateGame.renderWindowCreateGame(Main.primaryStage, 0);
    }

    public void renderWithUser(ActionEvent actionEvent) throws IOException {
        CreateGame.renderWindowCreateGame(Main.primaryStage, 0);
    }
}
