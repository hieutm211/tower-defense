package com.ligrim.tower_defense;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import com.ligrim.tower_defense.base.Position;

public class TextButton extends GameEntity implements GameButton {
    public static final int TOP_LEFT = 0;
    public static final int CENTER = 1;

    private String text;
    private Paint backgroundPaint;
    private Paint textPaint;
    private int textAlign = CENTER;

    public TextButton(String id, Position position, int width, int height) {
        super(id, position, width, height);
    }

    public TextButton(String id, int x, int y, int width, int height) {
        this(id, new Position(x, y), width, height);
    }

    public void setText(String txt) {
        text = txt;
    }

    public void setBackgroundPaint(Paint paint) {
        backgroundPaint = paint;
    }

    public void setTextPaint(Paint paint) {
        textPaint = paint;
    }

    public void setTextAlign(int align) {
        textAlign = align;
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
        float x = getX();
        float y = getY();

        canvas.drawRect(x, y, x+width, y+height, backgroundPaint);

        switch (textAlign) {
            case TOP_LEFT:
                canvas.drawText(text, x, y, textPaint);
                break;
            case CENTER:
                float wordPx = textPaint.getTextSize() / 2;
                canvas.drawText(text, x+width/2 - text.length()/2*wordPx, y+height/2 + wordPx - 2, textPaint);
                break;
        }
    }
}
