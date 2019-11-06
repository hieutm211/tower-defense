package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class BossEnemy implements Enemy {
    private int health;
    private float speed;
    private int armor;
    private int prize;
    private float directionX;
    private float directionY;
    private Position position;
    private int width;
    private int height;
    private float angle;
    private boolean faded;

    public BossEnemy(Position position) {
        this.health = 3;
        this.speed = 1f / 60;
        this.armor = 3;
        this.prize = 10;
        this.position = position;
        width = 64;
        height = 64;
        directionX = 0;
        directionY = 0;
        angle = (float) (Math.PI / 2);
        faded = false;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public int getArmor() {
        return armor;
    }

    @Override
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

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public boolean collision(GameEntity other) {
        return false;
    }

    @Override
    public void update() {
        float newEnemyPositionX = this.getPosition().getX() + this.getDirectionX() * this.getSpeed();
        float newEnemyPositionY = this.getPosition().getY() + this.getDirectionY() * this.getSpeed();
        this.setPosition(new Position(newEnemyPositionX, newEnemyPositionY));
        this.setAngle();
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
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

}
