package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.GameField;
import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Route;

import java.util.List;

public class NormalEnemy extends Enemy {

    public NormalEnemy(Route route) {
        super("enemy_normal", route);
        this.health = 450;
        this.maxHealth = health;
        this.speed = (80f / 60f) / 64f * GameField.UNIT_HEIGHT;
        this.armor = 4;
        this.prize = 1;
        faded = false;
    }
}
