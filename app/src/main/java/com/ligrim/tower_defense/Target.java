package com.ligrim.tower_defense;

public class Target extends Road {

    public Target(int code ,Position position) {
        super(code, position);
        this.type = TYPE_TARGET;
    }

    public Target(int code, float x, float y) {
        this(code, new Position(x, y));
        this.type = TYPE_TARGET;
    }
}
