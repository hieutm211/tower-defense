package com.ligrim.tower_defense.tower;

import com.ligrim.tower_defense.GameField;
import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Timer;

public class NormalTower extends Tower {

    public NormalTower(Position position) {
        super("tower_normal", position);
        rateOfFire = 60f / 60;
        range = 260 / 64f * GameField.UNIT_HEIGHT;
        damage = 160;
        timer = new Timer(rateOfFire);
        bulletSpeed = 14f / 64 * GameField.UNIT_HEIGHT;
        price = 25;
        MAX_LEVEL = 6;
    }
}
