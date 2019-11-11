package com.ligrim.tower_defense;

import android.graphics.Canvas;

import java.util.List;

public class SmallerEnemy extends Enemy {

    public SmallerEnemy(List<Position> route) {
        super(route);
        this.health = 1;
        this.speed = 5f / 60;
        this.armor = 1;
        this.prize = 1;
        this.position = route.get(0);
        width = 64;
        height = 64;
        directionX = 0;
        directionY = 0;
        angle = (float) (Math.PI / 2);
        faded = false;
    }

    public String getId() {
        return "smaller";
    }

}
