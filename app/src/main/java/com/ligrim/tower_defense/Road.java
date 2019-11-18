package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class Road extends GameTile {
    public static final int TYPE_ROAD = 0;
    public static final int TYPE_SPAWNER = 1;
    public static final int TYPE_TARGET = 2;

    protected int type;

    public Road(int code, Position position, int width, int height) {
        super("tile_" + String.format("%03d", code), position, width, height);
        this.type = TYPE_ROAD;
    }

    public Road(int code, float x, float y, int width, int height) {
        this(code, new Position(x, y), width, height);
    }

    public boolean collision(GameEntity gameEntity) {
        return false;
    }

    public void update() {}


}
