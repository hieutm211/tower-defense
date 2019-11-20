package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Route;

import java.util.List;

public class SmallerEnemy extends Enemy {

    public SmallerEnemy(Route route) {
        super("enemy_smaller", route);
        this.health = 150;
        this.maxHealth = health;
        this.speed = 180f / 60;
        this.armor = 0;
        this.prize = 20;
        faded = false;
    }
}
