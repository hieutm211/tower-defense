package com.ligrim.tower_defense.tower;

import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Timer;

public class NormalTower extends Tower {

    public NormalTower(Position position) {
        super("tower_normal", position);
        rateOfFire = 60f / 60;
        range = 250;
        damage = 150;
        timer = new Timer(rateOfFire);
        bulletSpeed = 10f;
        price = 5;
    }
}
