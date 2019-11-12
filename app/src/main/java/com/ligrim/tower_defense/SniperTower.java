package com.ligrim.tower_defense;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SniperTower extends Tower {

    public SniperTower(Position position) {
        super(position);
        rateOfFire = 190f / 60;
        range = 200;
        damage = 3;
        lastShotTick = 0;
        directionX = 0;
        directionY = 0;
        enemyTarget = new LinkedList<>();
        bulletSpeed = 11f;
    }

    @Override
    public String getId() {
        return "sniper";
    }
}
