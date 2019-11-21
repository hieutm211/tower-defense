package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.GameField;
import com.ligrim.tower_defense.base.Route;

public class BossEnemy extends Enemy {

    public BossEnemy(Route route) {
        super("enemy_boss", route);
        this.health = 10000;
        this.maxHealth = health;
        this.speed = (40f / 60f) / 64f * GameField.UNIT_HEIGHT;
        this.armor = 100;
        this.prize = 50;
        faded = false;
    }
}
