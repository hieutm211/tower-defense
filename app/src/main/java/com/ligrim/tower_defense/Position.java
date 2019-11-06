package com.ligrim.tower_defense;

public class Position {

    private float x;
    private float y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Position position) {
        this.x = position.x;
        this.y = position.y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public static float distance(Position first, Position second) {
        float xSquareDistance = (first.getX() - second.getX()) * (first.getX() - second.getX());
        float ySquareDistance = (first.getY() - second.getY()) * (first.getY() - second.getY());
        return (float) Math.sqrt(xSquareDistance + ySquareDistance);
    }
}
