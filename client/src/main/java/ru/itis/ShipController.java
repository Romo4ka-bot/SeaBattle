package ru.itis;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ShipController {

    private Ship[] ships = new Ship[4];
    private int[][] map = new int[12][12];

    public ShipController() {
        ships[0] = new Ship(4);
        ships[1] = new Ship(3);
        ships[2] = new Ship(2);
        ships[3] = new Ship(1);
    }

    public boolean canUseShip(Ship ship, Integer length) {
        Integer num = ship.getNum();
        Integer[] coordinates = ship.getCoordinates();

        if (num > 0) {
            if (checkCoordinates(ship, length)) {
                ship.setNum(num - 1);
                if (ship.getPosition().equals("horizontally")) {
                    for (int i = 0; i < length; i++)
                        map[coordinates[0] + 1][coordinates[1] + i + 1] = 1;
                } else {
                    for (int i = 0; i < length; i++)
                        map[coordinates[0] + i + 1][coordinates[1] + 1] = 1;
                }
                StringBuilder res = new StringBuilder();
                for (int i = 0; i < 12; i++) {
                    for (int j = 0; j < 12; j++) {
                        res.append(map[i][j]).append(" ");
                    }
                    res.append("\n");
                }
                System.out.println(res);
                return true;
            } else return false;
        } else return false;
    }

    public boolean checkCoordinates(Ship ship, Integer length) {

        int x = ship.getCoordinates()[0] + 1;
        int y = ship.getCoordinates()[1] + 1;

        boolean flag = false;

        if (ship.getPosition().equals("horizontally")) {
            if (y + length - 1 > 10) {
                System.err.println("Error");
                return false;
            }
            for (int i = 0; i < length; i++) {
                if (map[x + 1][y + i] == 1 || map[x - 1][y + i] == 1 || map[x][y + i] == 1 || map[x + 1][y + 1 + i] == 1 || map[x - 1][y - 1 + i] == 1 || map[x + 1][y - 1 + i] == 1 || map[x - 1][y + 1 + i] == 1 || map[x][y + 1 + i] == 1 || map[x][y - 1 + i] == 1) {
                    flag = true;
                    break;
                }
            }
        } else {
            if (x + length - 1 > 10) {
                System.err.println("Error");
                return false;
            }
            for (int i = 0; i < length; i++) {
                if (map[x + i][y + 1] == 1 || map[x + i][y - 1] == 1 || map[x + i][y] == 1 || map[x + 1 + i][y + 1] == 1 || map[x - 1 + i][y - 1] == 1 || map[x - 1 + i][y + 1] == 1 || map[x + 1 + i][y - 1] == 1 || map[x + 1 + i][y] == 1 || map[x - 1 + i][y] == 1) {
                    flag = true;
                    break;
                }
            }
        }
        return !flag;
    }

    public void returnShip(Ship ship, Integer length) {
        Integer num = ship.getNum();
        Integer[] coordinates = ship.getCoordinates();
        if (ship.getPosition().equals("horizontally")) {
            for (int i = 0; i < length; i++)
                map[coordinates[0] + i][coordinates[1]] = 0;
        } else {
            for (int i = 0; i < length; i++)
                map[coordinates[0]][coordinates[1] + i] = 0;
        }
        ship.setNum(num + 1);
    }
}
