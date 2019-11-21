package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Route;

import java.util.List;

public class NormalEnemy extends Enemy {

    public NormalEnemy(Route route) {
        super("enemy_normal", route);
        this.health = 450;
        this.maxHealth = health;
        this.speed = 80f / 60f;
        this.armor = 30;
        this.prize = 2;
        faded = false;
    }
}
