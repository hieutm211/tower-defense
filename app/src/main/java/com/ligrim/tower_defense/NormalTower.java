package com.ligrim.tower_defense;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class NormalTower extends Tower {
    public NormalTower(Position position) {
        super(position);
        rateOfFire = 180f / 60;
        range = 200;
        damage = 3;
        width = 128;
        height = 128;
        lastShotTick = 0;
        directionX = 0;
        directionY = 0;
        enemyTarget = new LinkedList<>();

    }

    @Override
    public String getId() {
        return "normal";
    }


}
