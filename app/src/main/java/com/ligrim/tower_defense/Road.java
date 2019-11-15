package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class Road extends GameTile {
    public static final int TYPE_ROAD = 0;
    public static final int TYPE_SPAWNER = 1;
    public static final int TYPE_TARGET = 2;

    protected int type;

    public Road(String code, Position position) {
        super(code, position);
        this.position = position;
        this.type = TYPE_ROAD;

    }

    public Road(String code, float x, float y) {
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
