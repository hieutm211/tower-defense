package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.base.Position;

import java.util.List;

public class NormalEnemy extends Enemy {

    public NormalEnemy(List<Position> route) {
        super("enemy_normal", route);
        this.health = 450;
        this.maxHealth = health;
        this.speed = 70f / 60;
        this.armor = 20;
        this.prize = 5;
        faded = false;
    }
}
