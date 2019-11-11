package com.ligrim.tower_defense;

import android.graphics.Canvas;

import java.util.List;

public class TankerEnemy extends Enemy {

    public TankerEnemy(List<Position> route) {
        super(route);
        this.health = 6;
        this.speed = 150f / 60;
        this.armor = 3;
        this.prize = 5;
        width = 32;
        height = 32;
        angle = (float) (Math.PI / 2);
        faded = false;

    }

    @Override
    public String getId() {
        return "tanker";
    }
}
