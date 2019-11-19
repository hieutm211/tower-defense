package com.ligrim.tower_defense.tower;

import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Timer;

public class SniperTower extends Tower {

    public SniperTower(Position position) {
        super("tower_sniper", position);
        rateOfFire = 150f / 60;
        range = 600;
        damage = 1000;
        timer = new Timer(rateOfFire);
        bulletSpeed = 15f;
        price = 15;
    }
}
