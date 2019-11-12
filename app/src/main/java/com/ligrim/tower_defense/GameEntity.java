package com.ligrim.tower_defense;

import android.graphics.Canvas;

public abstract class GameEntity {
    protected Position position;
    protected int width = GameField.UNIT_WIDTH;
    protected int height = GameField.UNIT_HEIGHT;
    public GameEntity(){}
    public void setPosition(Position position) {
        this.position = position;
    }
    public Position getPosition() {
        return this.position;
    }
    public abstract boolean collision(GameEntity other);
    public abstract void update();
    public abstract void draw(Canvas canvas);
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public float getX() {
        return this.position.getX();
    }
    public float getY() {
        return this.position.getY();
    }
    public float getCenterX() {
        return this.position.getX() + width / 2;
    }
    public float getCenterY() {
        return this.position.getY() + height / 2;
    }
}
