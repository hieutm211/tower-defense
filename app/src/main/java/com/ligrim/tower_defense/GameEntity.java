package com.ligrim.tower_defense;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.ligrim.tower_defense.base.Position;

public abstract class GameEntity implements DrawableObject {
    protected Position position;
    protected String id;
    protected int width;
    protected int height;
    protected float angle = 0;

    public GameEntity(String id, Position position) {
        this.id = id;
        this.position = position;
        this.width = GameField.UNIT_WIDTH;
        this.height = GameField.UNIT_HEIGHT;
    }

    public GameEntity(String id, Position position, int width, int height) {
        this(id, position);
        this.width = width;
        this.height = height;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    public void setPosition(float x, float y) {
        setPosition(new Position(x, y));
    }

    public Position getPosition() {
        return this.position;
    }
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
    public String getId() {
        return id;
    }

    public abstract boolean collision(GameEntity other);

    @Override
    public void draw(Canvas canvas) {
        float x = getX();
        float y = getY();

        Bitmap bitmap = GameGraphic.getBitmapById(getId(), width, height);

        canvas.save();
        canvas.rotate(angle, getCenterX(), getCenterY());
        canvas.drawBitmap(bitmap, x, y, null);
        canvas.restore();
    }
}
