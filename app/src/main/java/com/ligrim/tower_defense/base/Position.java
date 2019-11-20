package com.ligrim.tower_defense.base;

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

    public static Position add(Position first, Position second) {
        return new Position(first.getX() + second.getX(), first.getY() + second.getY());
    }

    public static float distanceSquared(Position first, Position second) {
        float dx = first.getX() - second.getX();
        float dy = first.getY() - second.getY();
        return dx * dx + dy * dy;
    }

    public static Position midPoint(Position first, Position second) {
        return new Position((first.x + second.x) / 2, (first.y + second.y) / 2);
    }

    public float getY() {
        return y;
    }

    public static float cross(Position u, Position v) {
        return u.x * v.y - v.x * u.y;
    }

    // return absolute angle in radian, always a minimum positive float
    public static float getAbsoluteAngle(Position vector) {

        double tan = vector.y / vector.x;
        float arctan = (float) Math.atan(tan);
        float pi = (float) Math.PI;

        if (Math.abs(vector.x) < 0.0001f) {
            if (vector.y > 0f) return pi / 2;
            return 3 * pi / 2;
        }
        else if (Math.abs(vector.y) < 0.0001f ) {
            if (vector.x > 0f) return 0f;
            else return pi;
        }
        else if (vector.y > 0f && vector.x > 0f) return Math.abs(arctan);
        else if (vector.y > 0f && vector.x < 0f) return arctan + pi;
        else if (vector.y < 0f && vector.x < 0f) return arctan + pi;
        else return arctan + 2 * pi;
    }

    public static float getAngleBetween(Position a, Position b) {
        float da = distance(new Position(0, 0), a);
        float db = distance(new Position(0, 0), b);
        float angle = (float) Math.acos(da*db/dot(a, b));
        return  angle;
    }

    public static float dot(Position a, Position b) {
        return a.x * b.x + a.y * b.y;
    }

    public static float distance(Position first, Position second) {
        float xSquareDistance = (first.getX() - second.getX()) * (first.getX() - second.getX());
        float ySquareDistance = (first.getY() - second.getY()) * (first.getY() - second.getY());
        return (float) Math.sqrt(xSquareDistance + ySquareDistance);
    }
}
