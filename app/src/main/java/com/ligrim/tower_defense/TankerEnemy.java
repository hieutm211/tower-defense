package com.ligrim.tower_defense;

import android.graphics.Canvas;

import java.util.List;

public class TankerEnemy extends Enemy {

    public TankerEnemy(List<Position> route) {
        super(route);
        this.health = 2000;
        this.maxHealth = health;
        this.speed = 80f / 60;
        this.armor = 35;
        this.prize = 5;
        angle = (float) (Math.PI / 2);
        faded = false;

    }

    @Override
    public String getId() {
        return "tanker";
    }
}
