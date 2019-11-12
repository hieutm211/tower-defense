package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class Mountain extends GameTile {

    public Mountain(Position position) {
        this.position = position;
    }

    public Mountain(int x, int y) {}

    public boolean collision(GameEntity gameEntity) {
        return false;
    }

    public void update() {}

    public void draw(Canvas canvas) {}
}
