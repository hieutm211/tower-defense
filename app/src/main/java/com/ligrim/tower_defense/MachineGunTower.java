package com.ligrim.tower_defense;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MachineGunTower extends Tower {

    public MachineGunTower(Position position) {
        super(position);
        rateOfFire = 4f / 60;
        range = 350;
        damage = 35;
        lastShotTick = 0;
        directionX = 0;
        directionY = 0;
        enemyTarget = new LinkedList<>();
        bulletSpeed = 12f;
        price = 10;
    }

    @Override
    public String getId() {
        return "machine_gun";
    }
}
