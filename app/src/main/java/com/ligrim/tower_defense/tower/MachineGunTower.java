package com.ligrim.tower_defense.tower;

import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Timer;

public class MachineGunTower extends Tower {

    public MachineGunTower(Position position) {
        super("tower_machine_gun", position);
        rateOfFire = 4f / 60;
        range = 200;
        damage = 35;
        timer = new Timer(rateOfFire);
        bulletSpeed = 12f;
        price = 10;
    }
}
