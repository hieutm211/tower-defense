package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Route;

import java.util.List;

public class SmallerEnemy extends Enemy {

    public SmallerEnemy(Route route) {
        super("enemy_smaller", route);
        this.health = 200;
        this.maxHealth = health;
        this.speed = 180f / 60f;
        this.armor = 2;
        this.prize = 1;
        faded = false;
    }
}
