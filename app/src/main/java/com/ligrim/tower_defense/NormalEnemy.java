package com.ligrim.tower_defense;

import android.graphics.Canvas;

import java.util.Iterator;
import java.util.List;

public class NormalEnemy extends Enemy {

    public NormalEnemy(List<Position> route) {
        super(route);
        this.health = 2;
        this.speed = 3f / 60;
        this.armor = 2;
        this.prize = 5;
        this.position = route.get(0);
        width = 64;
        height = 64;
        directionX = 0;
        directionY = 0;
        angle = (float) (Math.PI / 2);
        faded = false;

    }

    @Override
    public String getId() {
        return "normal";
    }

}
