package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class NormalEnemy implements Enemy {
    private int health;
    private double speed;
    private int armor;
    private int prize;
    private float directionX;
    private float directionY;
    private Position position;
    private int width;
    private int height;

    public NormalEnemy(Position position) {
        this.health = 3;
        this.speed = 2d / 60;
        this.armor = 1;
        this.prize = 3;
        this.position = position;
        width = 64;
        height = 64;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public double getSpeed() {
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
}
