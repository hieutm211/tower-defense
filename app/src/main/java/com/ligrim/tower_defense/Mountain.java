package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class Mountain extends GameTile {

    public Mountain(String code, Position position) {
        super(code, position);
    }

    public Mountain(String code, float x, float y) {
        this(code, new Position(x, y));
    }

    public boolean collision(GameEntity gameEntity) {
        return false;
    }

    public void update() {}

    @Override
    public String getId() {
        return "tile" + "_" + code;
    }
}
