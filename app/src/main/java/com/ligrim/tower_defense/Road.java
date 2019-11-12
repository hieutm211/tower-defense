package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class Road extends GameTile {
    public static final int TYPE_ROAD = 0;
    public static final int TYPE_SPAWNER = 1;
    public static final int TYPE_TARGET = 2;

    protected int type;

    public Road(Position position) {
        this.position = position;
        this.type = TYPE_ROAD;

    }

    public Road(float x, float y) {
        this.position = new Position(x, y);
        this.type = TYPE_ROAD;
    }

    public boolean collision(GameEntity gameEntity) {
        return false;
    }

    public void update() {}

    public void draw(Canvas canvas) {}
}
