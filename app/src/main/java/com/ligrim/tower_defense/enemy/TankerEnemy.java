package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.base.Route;

public class TankerEnemy extends Enemy {

    public TankerEnemy(Route route) {
        super("enemy_tanker", route);
        this.health = 2000;
        this.maxHealth = health;
        this.speed = 80f / 60;
        this.armor = 35;
        this.prize = 5;
        faded = false;
    }
}
