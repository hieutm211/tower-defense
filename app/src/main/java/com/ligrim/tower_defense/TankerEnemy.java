package com.ligrim.tower_defense;

import android.graphics.Canvas;

import java.util.List;

public class TankerEnemy extends Enemy {

    public TankerEnemy(List<Position> route) {
        super(route);
        this.health = 3;
        this.speed = 1f / 60;
        this.armor = 3;
        this.prize = 5;
        this.position = route.get(0);
        width = 64;
        height = 64;
        directionX = 0;
        directionY = 0;
        angle = (float) (Math.PI / 2);
        faded = false;

    }

    public String getId() {
        return "tanker";
    }
}
