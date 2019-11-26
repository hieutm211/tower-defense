package com.ligrim.tower_defense.tower;

import com.ligrim.tower_defense.GameField;
import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Timer;

public class SniperTower extends Tower {

    public SniperTower(Position position) {
        super("tower_sniper", position);
        rateOfFire = 150f / 60;
        range = 350 / 64 * GameField.UNIT_HEIGHT;
        damage = 1000;
        timer = new Timer(rateOfFire);
        bulletSpeed = 18f / 64 * GameField.UNIT_HEIGHT;
        price = 40;
        currentLevel = 0;
        MAX_LEVEL = 3;
    }
}
