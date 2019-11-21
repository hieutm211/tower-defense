package com.ligrim.tower_defense.tower;

import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Timer;

public class MachineGunTower extends Tower {

    public MachineGunTower(Position position) {
        super("tower_machine_gun", position);
        rateOfFire = 5f / 60f;
        range = 200;
        damage = 38;
        timer = new Timer(rateOfFire);
        bulletSpeed = 12f;
        price = 25;
    }
}
