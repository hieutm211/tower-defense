package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class Road implements GameTile {
    public static final int TYPE_ROAD = 0;
    public static final int TYPE_SPAWNER = 1;
    public static final int TYPE_TARGET = 2;

    protected int type;
    protected Position position;

    private final int width = 128;
    private final int height = 128;

    public Road(Position position) {
        this.position = position;
        this.type = TYPE_ROAD;

    }

    public Road(float x, float y) {
        this.position = new Position(x, y);
        this.type = TYPE_ROAD;
    }

    public void setPosition(Position position) {}

    public Position getPosition() {
        return null;
    }

    public boolean collision(GameEntity gameEntity) {
        return false;
    }

    public void update() {}

    public void draw(Canvas canvas) {}

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
