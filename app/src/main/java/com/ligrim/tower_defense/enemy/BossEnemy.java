package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Route;

import java.util.List;

public class BossEnemy extends Enemy {

    public BossEnemy(Route route) {
        super("enemy_boss", route);
        this.health = 6000;
        this.maxHealth = health;
        this.speed = 54f / 60;
        this.armor = 200;
        this.prize = 10;
        faded = false;
    }

}
