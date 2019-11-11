package com.ligrim.tower_defense;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import java.util.List;

public class BossEnemy extends Enemy {

    public BossEnemy(List<Position> route) {
        super(route);
        this.health = 3;
        this.speed = 54f / 60;
        this.armor = 3;
        this.prize = 10;

        width = 32;
        height = 32;

        angle = (float) (Math.PI / 2);
        faded = false;
    }

    @Override
    public String getId() {
        return "boss";
    }
}
