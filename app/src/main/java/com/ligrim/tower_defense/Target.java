package com.ligrim.tower_defense;

public class Target extends Road {

    public Target(Position position) {
        super(position);
        this.type = TYPE_TARGET;
    }

    public Target(float x, float y) {
        super(x, y);
        this.type = TYPE_TARGET;
    }
}
