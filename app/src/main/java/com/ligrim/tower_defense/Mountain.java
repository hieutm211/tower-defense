package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class Mountain extends GameTile {

    public Mountain(int code, Position position) {
        super("tile_" + String.format("%03d", code), position);
    }

    public Mountain(int code, float x, float y) {
        this(code, new Position(x, y));
    }

    public boolean collision(GameEntity gameEntity) {
        return false;
    }

    public void update() {}
}
