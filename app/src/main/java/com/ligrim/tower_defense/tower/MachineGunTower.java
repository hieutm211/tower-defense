package com.ligrim.tower_defense.tower;

import com.ligrim.tower_defense.GameField;
import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Timer;

public class MachineGunTower extends Tower {

    public MachineGunTower(Position position) {
        super("tower_machine_gun", position);
        rateOfFire = 5f / 60f;
        range = 200 / 64 * GameField.UNIT_HEIGHT;
        damage = 22;
        timer = new Timer(rateOfFire);
        bulletSpeed = 15f / 64 * GameField.UNIT_HEIGHT;
        price = 20;
        MAX_LEVEL = 6;
    }
}
