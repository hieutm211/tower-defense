package com.ligrim.tower_defense;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import androidx.annotation.NonNull;

public abstract class GameEntity implements DrawableObject {
    protected Position position;
    protected int width ;
    protected int height ;
    protected float angle = 0;

    public GameEntity(Position position) {
        this.position = position;
        width = GameStage.UNIT_WIDTH;
        height = GameStage.UNIT_HEIGHT;
    }
    public void setPosition(Position position) {
        this.position = position;
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

    public abstract boolean collision(GameEntity other);

    public abstract void update();

    public abstract String getId();

    @Override
    public void draw(Canvas canvas) {
        float x = position.getX();
        float y = position.getY();

        Bitmap bitmap = GameGraphic.getBitmapById(getId(), width, height);

        canvas.save();
        canvas.rotate(angle, getCenterX(), getCenterY());
        canvas.drawBitmap(bitmap, x, y, null);
        canvas.restore();
    }
}
