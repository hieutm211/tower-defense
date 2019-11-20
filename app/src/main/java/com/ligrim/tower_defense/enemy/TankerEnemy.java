package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.base.Route;

public class TankerEnemy extends Enemy {

    public TankerEnemy(Route route) {
        super("enemy_tanker", route);
        this.health = 800;
        this.maxHealth = health;
        this.speed = 60f / 60;
        this.armor = 100;
        this.prize = 70;
        faded = false;
    }
}
