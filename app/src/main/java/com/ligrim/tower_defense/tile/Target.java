package com.ligrim.tower_defense.tile;

import com.ligrim.tower_defense.base.Position;

public class Target extends Road {

    public Target(int code , Position position, int width, int height) {
        super(code, position, width, height);
        this.type = TYPE_TARGET;
    }

    public Target(int code, float x, float y, int width, int height) {
        this(code, new Position(x, y), width, height);
        this.type = TYPE_TARGET;
    }
}
