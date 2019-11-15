package com.ligrim.tower_defense;

import android.graphics.Canvas;

public abstract class GameTile extends GameEntity {
    protected String code;

    GameTile(Position position) {
        super(position);
    }

    GameTile(String code, Position position) {
        this(position);
        this.code = code;
    }

    @Override
    public boolean collision(GameEntity other) {
        return false;
    }

    @Override
    public void update() {

    }
}
