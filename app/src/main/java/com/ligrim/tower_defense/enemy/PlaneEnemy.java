package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.GameField;

public class PlaneEnemy extends FlyingEnemy {

    public PlaneEnemy() {
        super("enemy_plane");
        this.health = 2000;
        this.maxHealth = health;
        this.speed = (100f / 60f ) / 64f * GameField.UNIT_HEIGHT;
        this.armor = 25;
        this.prize = 50;
        faded = false;
    }

}
