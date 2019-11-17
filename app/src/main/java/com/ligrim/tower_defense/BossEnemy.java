package com.ligrim.tower_defense;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import java.util.List;

public class BossEnemy extends Enemy {

    public BossEnemy(List<Position> route) {
        super("enemy_boss", route);
        this.health = 6000;
        this.maxHealth = health;
        this.speed = 54f / 60;
        this.armor = 200;
        this.prize = 10;
        faded = false;
    }

}
