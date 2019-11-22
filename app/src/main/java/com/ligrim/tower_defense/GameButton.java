package com.ligrim.tower_defense;

import android.graphics.Canvas;

public interface GameButton {
    boolean containsPoint(float x, float y);
    void draw(Canvas canvas);
    String getId();
}
