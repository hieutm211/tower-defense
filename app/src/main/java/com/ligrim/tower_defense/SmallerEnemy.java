package com.ligrim.tower_defense;

import android.graphics.Canvas;

import java.util.List;

public class SmallerEnemy extends Enemy {

    public SmallerEnemy(List<Position> route) {
        super(route);
        this.health = 150;
        this.maxHealth = health;
        this.speed = 180f / 60;
        this.armor = 0;
        this.prize = 1;
        faded = false;
    }

    @Override
    public String getId() {
        return "smaller";
    }

}
