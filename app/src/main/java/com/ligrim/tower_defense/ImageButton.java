package com.ligrim.tower_defense;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.ligrim.tower_defense.base.Position;

public class ImageButton extends GameEntity implements GameButton {
    public static final int TOP_LEFT = 0;
    public static final int CENTER = 1;

    private Bitmap bitmap;
    private String text = null;
    private Paint backgroundPaint;
    private Paint textPaint;
    private int textAlign = CENTER;
    private float textSize;

    public ImageButton(String id, Position position, int width, int height) {
        super(id, position, width, height);

        if (id.contains("tower")) {
            id += "_demo";
        }

        this.bitmap = GameGraphic.getBitmapById(id, width, height);
    }

    public ImageButton(String id, int x, int y, int width, int height) {
        this(id, new Position(x, y), width, height);
    }

    public void setText(String txt) {
        text = txt;
    }

    public void setTextPaint(Paint paint) {
        textPaint = paint;
        textSize = paint.getTextSize();
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

        canvas.drawBitmap(bitmap, x, y, null);

        if (text == null) return;

        switch (textAlign) {
            case TOP_LEFT:
                canvas.drawText(text, x, y + textSize, textPaint);
                break;
            case CENTER:
                float wordPx = textSize / 2;
                canvas.drawText(text, x+width/2 - text.length()/2*wordPx, y+height/2 + wordPx - 2, textPaint);
                break;
        }
    }
}
