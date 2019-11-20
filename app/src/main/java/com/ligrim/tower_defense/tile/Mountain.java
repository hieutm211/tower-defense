package com.ligrim.tower_defense.tile;

import com.ligrim.tower_defense.GameEntity;
import com.ligrim.tower_defense.base.Position;

public class Mountain extends GameTile {

    public Mountain(int code, Position position, int width, int height) {
        super("tile_" + String.format("%03d", code), position, width, height);
    }

    public Mountain(int code, float x, float y, int width, int height) {
        this(code, new Position(x, y), width, height);
    }

    public boolean collision(GameEntity gameEntity) {
        return false;
    }

    public void update() {}
}
