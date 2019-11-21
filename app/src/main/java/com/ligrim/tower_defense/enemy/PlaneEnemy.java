package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.base.Route;

public class PlaneEnemy extends AirborneEnemy {

    public PlaneEnemy() {
        super("enemy_plane");
        this.health = 2000;
        this.maxHealth = health;
        this.speed = 100f / 60f;
        this.armor = 500;
        this.prize = 75;
        faded = false;
    }
}
