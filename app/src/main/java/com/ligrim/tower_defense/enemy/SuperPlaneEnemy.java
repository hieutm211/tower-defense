package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.GameField;

public class SuperPlaneEnemy extends FlyingEnemy {

    public SuperPlaneEnemy() {
        super("super_enemy_plane");
        this.health = 3000;
        this.maxHealth = health;
        this.speed = (80f / 60f ) / 64f * GameField.UNIT_HEIGHT;
        this.armor = 30;
        this.prize = 45;
        faded = false;
    }
}
