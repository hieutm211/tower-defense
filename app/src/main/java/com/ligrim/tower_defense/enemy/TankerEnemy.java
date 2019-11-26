package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.GameField;
import com.ligrim.tower_defense.base.Route;

public class TankerEnemy extends Enemy {

    public TankerEnemy(Route route) {
        super("enemy_tanker", route);
        this.health = 3000;
        this.maxHealth = health;
        this.speed = (60f / 60f) / 64f * GameField.UNIT_HEIGHT;
        this.armor = 20;
        this.prize = 10;
        faded = false;
    }
}
