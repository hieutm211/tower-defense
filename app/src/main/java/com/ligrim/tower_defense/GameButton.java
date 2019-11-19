package com.ligrim.tower_defense;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.ligrim.tower_defense.base.Position;

public class GameButton extends GameEntity {

    private Bitmap bitmap;

    public GameButton(String id, Position position, int width, int height) {
        super(id, position, width, height);
        this.bitmap = GameGraphic.getBitmapById(id, width, height);
    }

    public GameButton(String id, int x, int y, int width, int height) {
        this(id, new Position(x, y), width, height);
    }

    public boolean containsPoint(float x, float y) {
        if (x < getX() || getX() + getWidth() <= x) return false;
        if (y < getY() || getY() + getHeight() <= y) return false;
        return true;
    }

    @Override
    public boolean collision(GameEntity other) {
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, getX(), getY(), null);
    }
}
