package com.ligrim.tower_defense.tower;

import com.ligrim.tower_defense.base.Position;

public class MachineGunTower extends Tower {

    public MachineGunTower(Position position) {
        super("tower_machine_gun", position);
        rateOfFire = 4f / 60;
        range = 350;
        damage = 35;
        lastShotTick = 0;
        bulletSpeed = 12f;
        price = 10;
    }
}
