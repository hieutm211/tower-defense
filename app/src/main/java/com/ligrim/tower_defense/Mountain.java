package com.ligrim.tower_defense;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Mountain implements GameTile {

    private static Bitmap image;
    private Position position;
    private final int width = 128;
    private final int height = 128;

    public Mountain(Position position) {
        this.position = position;
    }

    public Mountain(int x, int y) {}

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
