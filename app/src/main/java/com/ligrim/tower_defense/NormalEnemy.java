package com.ligrim.tower_defense;

import android.graphics.Canvas;

import java.util.Iterator;
import java.util.List;

public class NormalEnemy extends Enemy {

    public NormalEnemy(List<Position> route) {
        super(route);
        this.health = 6;
        this.speed = 70f / 60;
        this.armor = 2;
        this.prize = 5;
        angle = (float) (Math.PI / 2);
        faded = false;

    }

    @Override
    public String getId() {
        return "normal";
    }

}
