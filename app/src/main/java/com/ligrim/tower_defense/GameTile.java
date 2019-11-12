package com.ligrim.tower_defense;

import android.graphics.Canvas;

public abstract class GameTile extends GameEntity {
    GameTile() {
        super();
    }

    @Override
    public boolean collision(GameEntity other) {
        return false;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {

    }
}
