package com.ligrim.tower_defense;

import android.graphics.Canvas;

import java.util.List;

public class SmallerEnemy extends Enemy {

    public SmallerEnemy(List<Position> route) {
        super(route);
        this.health = 1;
        this.speed = 100f / 60;
        this.armor = 1;
        this.prize = 1;
        width = 32;
        height = 32;
        angle = (float) (Math.PI / 2);
        faded = false;
    }

    @Override
    public String getId() {
        return "smaller";
    }

}
