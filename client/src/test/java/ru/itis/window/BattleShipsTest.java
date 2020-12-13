package ru.itis.window;

import javafx.scene.control.ButtonType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BattleShipsTest {

    @Test
    void createGame() {
    }

    @Test
    void myShoots() {
    }

    @Test
    void enemyShoots() {
    }

    @Test
    void alert() {
        String title = "Привет";
        String msg = "Пользователь, как дела?";
        Optional<ButtonType> buttonType = BattleShips.alert(title, msg);
        buttonType.ifPresent(type -> Assertions.assertEquals(type.getText(), title));
    }
}