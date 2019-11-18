package com.ligrim.tower_defense;

import android.graphics.Canvas;

public abstract class GameTile extends GameEntity {

    GameTile(String id, Position position) {
        super(id, position);
    }

    GameTile(String id, Position position, int width, int height) {
        super(id, position, width, height);
    }

    @Override
    public boolean collision(GameEntity other) {
        return false;
    }

    @Override
    public void update() {

    }
}
