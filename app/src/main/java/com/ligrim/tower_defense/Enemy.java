package com.ligrim.tower_defense;

import android.graphics.Canvas;

import java.util.List;

public abstract class Enemy implements GameEntity {
    protected int health;
    protected float speed;
    protected int armor;
    protected int prize;
    protected float directionX;
    protected float directionY;
    protected Position position;
    protected int width;
    protected int height;
    protected float angle;
    protected boolean faded;
    protected List<Position> route;
    protected int checkpoint;

    public Enemy(){}

    public Enemy(List<Position> route) {
        this.route = route;
        assert (route.size() > 1);
        checkpoint = 1;
    }

    public int getHealth() {
        return health;
    }

    public float getSpeed() {
        return speed;
    }

    public int getArmor() {
        return armor;
    }

    public int getPrize() {
        return prize;
    }

    public void reduceHealth(int damage) {
        if (armor - damage < 0)
            health -= damage - armor;
    }

    public void setDirection(float dx, float dy) {
        directionX = dx;
        directionY = dy;
    }

    public float getDirectionX() {
        return directionX;
    }

    public float getDirectionY() {
        return directionY;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return this.position;
    }

    public boolean collision(GameEntity other) {
        return false;
    }

    public void update() {
        if (isReachCheckpoint()) nextDestination();
        float newEnemyPositionX = this.getPosition().getX() + this.getDirectionX() * this.getSpeed();
        float newEnemyPositionY = this.getPosition().getY() + this.getDirectionY() * this.getSpeed();
        this.setPosition(new Position(newEnemyPositionX, newEnemyPositionY));
        this.setAngle();
    }

    public void draw(Canvas canvas) {

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setAngle() {
        if (directionX == 0 && directionY == 0) {
            angle = 0;
            return;
        }
        float newAngle = (float) Math.atan(Math.abs(directionY/directionX)) * 180 / (float) Math.PI;

        if (directionX == 0 && directionY > 0) angle = 0;
        else if (directionX == 0  && directionY < 0) angle = -90;
        else if (directionX > 0 && directionY == 0) angle = 0;
        else if (directionX < 0 && directionY == 0) angle = 180;
        else if (directionX > 0 && directionY > 0) angle = newAngle;
        else if (directionX > 0 && directionY < 0) angle = -newAngle;
        else if (directionX < 0 && directionY > 0) angle = 180 - newAngle;
        else if (directionX < 0 && directionY < 0) angle = angle - 180;
    }

    public float getAngle() {
        return angle;
    }
    public boolean isFaded() {
        return faded;
    }

    public void disappear() {
        faded = true;
    }

    // check if checkpoint is target, then update checkpoint and direction
    public void nextDestination() {
        assert isReachCheckpoint();
        if (checkpoint < route.size() - 1) {
            ++checkpoint;
            updateDirection();
        }
    }

    public boolean isReachCheckpoint() {
        return Position.distance(this.getPosition(), route.get(checkpoint)) <= this.getSpeed();
    }

    // check if the distance between this enemy and target is lower than speed
    public boolean isReachTarget() {
        return checkpoint == route.size() - 1 && Position.distance(this.position, route.get(route.size() - 1)) <= this.speed;
    }

    // check if the given enemy is the same type of this enemy, then check the distance between them
    public boolean isCollideTo(Enemy other) {
        if (!(this.getClass().equals(other.getClass()))) return false;
        return Position.distance(this.position, other.getPosition()) <= this.speed;
    }

    private void updateDirection() {
        float dx = route.get(checkpoint).getX() - this.getPosition().getX();
        float dy = route.get(checkpoint).getY() - this.getPosition().getY();
        // turn dx and dy to unit vector
        dx /= Position.distance(this.getPosition(), route.get(checkpoint));
        dy /= Position.distance(this.getPosition(), route.get(checkpoint));
        this.setDirection(dx, dy);
    }

    public abstract String getId();
}
