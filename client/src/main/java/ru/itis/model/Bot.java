package ru.itis.model;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Random;

@Data
@Setter
@Getter
public class Bot {

    private Random random;
    private int xFirstHit, yFirstHit, xLastHit, yLastHit;
    private boolean activeAttack;
    //    private static String up = "not use", down = "not use", left = "not use", right = "not use";
    private static boolean up, down, left, right;
    private boolean isShooting = true;

    public Bot() {
        random = new Random();
    }

    public int[][] generateMap() throws IOException {

        ShipMap shipMap = new ShipMap();

        for (int i = 3; i > -1; i--) {

            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Ship ship = shipMap.getShips()[i];
            ship.setCoordinates(new Integer[]{y, x});

            int position = random.nextInt(2);
            System.out.println(position);

            if (i == 3) {
                checkRotate(position, ship);
                if (!shipMap.canUseShip(ship, 4))
                    i++;
            }

            if (i == 2) {
                for (int j = 7; j < 9; j++) {

                    checkRotate(position, ship);

                    if (!shipMap.canUseShip(ship, 3))
                        j--;

                    x = random.nextInt(10);
                    y = random.nextInt(10);
                    ship.setCoordinates(new Integer[]{y, x});
                    position = random.nextInt(2);
                }
            }
            if (i == 1) {
                for (int j = 4; j < 7; j++) {

                    checkRotate(position, ship);

                    if (!shipMap.canUseShip(ship, 2))
                        j--;

                    x = random.nextInt(10);
                    y = random.nextInt(10);
                    ship.setCoordinates(new Integer[]{y, x});
                    position = random.nextInt(2);
                }
            }

            if (i == 0) {
                for (int j = 0; j < 4; j++) {

                    checkRotate(position, ship);

                    if (!shipMap.canUseShip(ship, 1))
                        j--;

                    x = random.nextInt(10);
                    y = random.nextInt(10);
                    ship.setCoordinates(new Integer[]{y, x});
                }
            }
        }
        return shipMap.getMap();
    }

    private void checkRotate(Integer position, Ship ship) {
        if (position == 0) {
            if (ship.getPosition().equals("vertically")) {
                ship.setPosition("horizontally");
            }
        } else {
            if ((ship.getPosition().equals("horizontally"))) {
                ship.setPosition("vertically");
            }
        }
    }

    public void shootingBot(GridPane gridPane, int[][] map) {

        if (activeAttack) {
            finishingOff(gridPane, map);
        } else {

            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (isHitting(x, y, map)) {
                initializeAttack(x, y, gridPane, map);
                finishingOff(gridPane, map);
            } else {
                Rectangle rectangle = new Rectangle(32, 32);
                gridPane.add(rectangle, x, y);
            }
        }
    }

    private void finishingOff(GridPane gridPane, int[][] map) {
        Rectangle rectangle = new Rectangle(32, 32);
        if (!up && !down && !left && !right) {
            up = true;
        }
        if (up) {
            yLastHit = yLastHit + 1;
            if (yLastHit == 10) {
                yLastHit--;
                down = true;
                up = false;
                finishingOff(gridPane, map);
            } else {
                if (map[yLastHit + 1][xFirstHit + 1] == 1) {
                    isShooting = true;
                    rectangle.setFill(Color.RED);
                    gridPane.add(rectangle, xFirstHit, yLastHit);
                    map[yLastHit + 1][xFirstHit + 1] = 2;
                    finishingOff(gridPane, map);
                } else {
                    up = false;
                    down = true;
                }
            }
        } else if (down) {
            yLastHit = yLastHit - 1;
            if (yLastHit == -1) {
                yLastHit++;
                left = true;
                down = false;
                finishingOff(gridPane, map);
            } else {
                if (map[yLastHit + 1][xFirstHit + 1] == 1) {
                    isShooting = true;
                    rectangle.setFill(Color.RED);
                    gridPane.add(rectangle, xFirstHit, yLastHit);
                    map[yLastHit + 1][xFirstHit + 1] = 2;
                    finishingOff(gridPane, map);
                } else {
                    down = false;
                    left = true;
                }
            }
        } else if (isShooting) {
            if (yFirstHit <= yLastHit) {
                cleanMap(yFirstHit, yLastHit, xFirstHit, map, gridPane);
            } else {
                cleanMap(yLastHit, yFirstHit, xFirstHit, map, gridPane);
            }
            revertData();
        } else if (left) {
            xLastHit = xLastHit - 1;
            if (xLastHit == -1) {
                xLastHit++;
                left = false;
                right = true;
                finishingOff(gridPane, map);
            } else {
                if (map[yFirstHit + 1][xLastHit + 1] == 1) {
                    rectangle.setFill(Color.RED);
                    gridPane.add(rectangle, xLastHit, yFirstHit);
                    map[yFirstHit + 1][xLastHit + 1] = 2;
                    finishingOff(gridPane, map);
                } else {
                    left = false;
                    right = true;
                }
            }
        } else {
            xLastHit = xLastHit + 1;
            if (xLastHit == 10) {
                xLastHit--;
                left = false;
                right = true;
                finishingOff(gridPane, map);
            } else {
                if (map[yFirstHit + 1][xLastHit + 1] == 1) {
                    rectangle.setFill(Color.RED);
                    gridPane.add(rectangle, xLastHit, yFirstHit);
                    map[yFirstHit + 1][xLastHit + 1] = 2;
                    finishingOff(gridPane, map);
                } else {
                    if (xFirstHit <= xLastHit) {
                        cleanMap(xFirstHit, xLastHit, yFirstHit, map, gridPane);
                    } else {
                        cleanMap(xLastHit, xFirstHit, yFirstHit, map, gridPane);
                    }
                    revertData();
                }
            }
        }
//        if (!(up.equals("active") || down.equals("active") || left.equals("active") || right.equals("active"))) {
//            int num = random.nextInt(4);
//            if (num == 0) {
//                if (up.equals("not use")) {
//                    up = "use";
//                    yLastHit = yFirstHit + 1;
//                    if (yLastHit == 10) {
//                        yLastHit--;
//                        finishingOff(gridPane, map);
//                    } else if (map[yLastHit + 1][xFirstHit + 1] == 1) {
//                        gridPane.add(rectangle, xFirstHit, yLastHit);
//                        up = "active";
//                        map[yLastHit + 1][xFirstHit + 1] = 2;
//                        finishingOff(gridPane, map);
//                    } else {
//                        up = "use";
//                        down = "active";
//                    }
//                } else {
//                    finishingOff(gridPane, map);
//                }
//            } else if (num == 1) {
//                if (down.equals("not use")) {
//                    down = "use";
//                    yLastHit = yFirstHit - 1;
//                    if (yLastHit == -1) {
//                        yLastHit++;
//                        finishingOff(gridPane, map);
//                    } else if (map[yLastHit + 1][xFirstHit + 1] == 1) {
//                        gridPane.add(rectangle, xFirstHit, yLastHit);
//                        down = "active";
//                        map[yLastHit + 1][xFirstHit + 1] = 2;
//                        finishingOff(gridPane, map);
//                    } else {
//                        down = "use";
//                        up = "active";
//                    }
//                } else {
//                    finishingOff(gridPane, map);
//                }
//            } else if (num == 2) {
//                if (left.equals("not use")) {
//                    left = "use";
//                    xLastHit = xFirstHit - 1;
//                    if (xLastHit == -1) {
//                        xLastHit++;
//                        finishingOff(gridPane, map);
//                    } else if (map[yFirstHit + 1][xLastHit + 1] == 1) {
//                        gridPane.add(rectangle, xLastHit, xFirstHit);
//                        left = "active";
//                        map[yFirstHit + 1][xLastHit + 1] = 2;
//                        finishingOff(gridPane, map);
//                    } else {
//                        left = "use";
//                        right = "active";
//                    }
//                } else {
//                    finishingOff(gridPane, map);
//                }
//            } else if (num == 3) {
//                if (right.equals("not use")) {
//                    right = "use";
//                    xLastHit = xFirstHit + 1;
//                    if (xLastHit == 10) {
//                        xLastHit--;
//                        finishingOff(gridPane, map);
//                    } else if (map[yFirstHit + 1][xLastHit + 1] == 1) {
//                        gridPane.add(rectangle, xLastHit, xFirstHit);
//                        right = "active";
//                        map[yFirstHit + 1][xLastHit + 1] = 2;
//                        finishingOff(gridPane, map);
//                    } else {
//                        right = "use";
//                        left = "active";
//                    }
//                }
//            }
//        } else if (up.equals("active")) {
//            yLastHit++;
//            if (yLastHit == 10) {
//                yLastHit--;
//                up = "use";
//                finishingOff(gridPane, map);
//            } else if (map[yLastHit + 1][xFirstHit + 1] == 1) {
//                gridPane.add(rectangle, xFirstHit, yLastHit);
//                map[yLastHit + 1][xFirstHit + 1] = 2;
//                finishingOff(gridPane, map);
//            } else {
//                up = "use";
//                if (checkVert) {
//                    if (yFirstHit <= yLastHit) {
//                        cleanMap(yFirstHit, yLastHit, xFirstHit, map, gridPane);
//                    } else {
//                        cleanMap(yLastHit, yFirstHit, xFirstHit, map, gridPane);
//                    }
//                    revertData();
//                } else {
//                    down = "active";
//                    checkVert = true;
//                }
//            }
//        } else if (down.equals("active")) {
//            yLastHit--;
//            if (yLastHit == 10) {
//                yLastHit++;
//                down = "use";
//                finishingOff(gridPane, map);
//            } else if (map[yLastHit + 1][xFirstHit + 1] == 1) {
//                gridPane.add(rectangle, xFirstHit, yLastHit);
//                map[yLastHit + 1][xFirstHit + 1] = 2;
//                finishingOff(gridPane, map);
//            } else {
//                down = "use";
//                if (checkVert) {
//                    if (yFirstHit <= yLastHit) {
//                        cleanMap(yFirstHit, yLastHit, xFirstHit, map, gridPane);
//                    } else {
//                        cleanMap(yLastHit, yFirstHit, xFirstHit, map, gridPane);
//                    }
//                    revertData();
//                } else {
//                    up = "active";
//                    checkVert = true;
//                }
//            }
//        } else if (left.equals("active")) {
//            xLastHit--;
//            if (xLastHit == -1) {
//                xLastHit++;
//                left = "use";
//                finishingOff(gridPane, map);
//            } else if (map[yFirstHit + 1][xLastHit + 1] == 1) {
//                gridPane.add(rectangle, xLastHit, yFirstHit);
//                map[yFirstHit + 1][xLastHit + 1] = 2;
//                finishingOff(gridPane, map);
//            } else {
//                left = "use";
//                if (checkVert) {
//                    if (xFirstHit <= xLastHit) {
//                        cleanMap(xFirstHit, xLastHit, yFirstHit, map, gridPane);
//                    } else {
//                        cleanMap(xLastHit, xFirstHit, yFirstHit, map, gridPane);
//                    }
//                    revertData();
//                } else {
//                    right = "active";
//                    checkHor = true;
//                }
//            }
//        } else {
//            xLastHit++;
//            if (xLastHit == 10) {
//                xLastHit--;
//                right = "use";
//                finishingOff(gridPane, map);
//            } else if (map[yFirstHit + 1][xLastHit + 1] == 1) {
//                gridPane.add(rectangle, xLastHit, yFirstHit);
//                map[yFirstHit + 1][xLastHit + 1] = 2;
//                finishingOff(gridPane, map);
//            } else {
//                right = "use";
//                if (checkVert) {
//                    if (xFirstHit <= xLastHit) {
//                        cleanMap(xFirstHit, xLastHit, yFirstHit, map, gridPane);
//                    } else {
//                        cleanMap(xLastHit, xFirstHit, yFirstHit, map, gridPane);
//                    }
//                    revertData();
//                } else {
//                    left = "active";
//                    checkHor = true;
//                }
//            }
//        }
    }

    private void cleanMap(int numFrom, int numTo, int num, int[][] map, GridPane gridPane) {
        for (int i = numFrom; i < numTo; i++) {
            Rectangle rectangle = new Rectangle(32, 32);
            rectangle.setFill(Color.RED);
            gridPane.add(rectangle, i, num + 1);
            gridPane.add(rectangle, i, num - 1);
            gridPane.add(rectangle, i + 1, num);
            gridPane.add(rectangle, i + 1, num + 1);
            gridPane.add(rectangle, i + 1, num + 1);
            gridPane.add(rectangle, i - 1, num + 1);
            gridPane.add(rectangle, i - 1, num + 1);
            gridPane.add(rectangle, i - 1, num + 1);
            i++;
            num++;
            map[i][num + 1] = 2;
            map[i][num - 1] = 2;
            map[i + 1][num] = 2;
            map[i + 1][num + 1] = 2;
            map[i + 1][num - 1] = 2;
            map[i - 1][num] = 2;
            map[i - 1][num + 1] = 2;
            map[i - 1][num - 1] = 2;
            i--;
            num--;
        }
    }

    private void revertData() {
        up = false;
        down = false;
        left = false;
        right = false;
        activeAttack = false;
    }

    private void initializeAttack(int x, int y, GridPane gridPane, int[][] map) {
        xFirstHit = x;
        yFirstHit = y;
        xLastHit = x;
        yLastHit = y;
        activeAttack = true;
        map[y + 1][x + 1] = 0;
        Rectangle rectangle = new Rectangle(32, 32);
        rectangle.setFill(Color.RED);
        gridPane.add(rectangle, x, y);
    }

    private boolean isHitting(int x, int y, int[][] map) {
        return map[y + 1][x + 1] == 1;
    }
}