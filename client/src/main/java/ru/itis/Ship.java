package ru.itis;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class Ship {

    private Boolean active;
    private Integer num;
    private String position;
    private Integer[] coordinates;

    public Ship(Integer num) {
        this.num = num;
        active = false;
        position = "horizontally";
        coordinates = new Integer[2];
    }
}
