package com.ligrim.tower_defense.tower;

import com.ligrim.tower_defense.base.Position;

public class NormalTower extends Tower {

    public NormalTower(Position position) {
        super("tower_normal", position);
        rateOfFire = 60f / 60;
        range = 250;
        damage = 150;
        lastShotTick = 0;
        bulletSpeed = 10f;
        price = 5;
    }
}
