package com.ligrim.tower_defense;

import android.graphics.Canvas;

public interface GameEntity {

    void setPosition(Position position);
    Position getPosition();
    boolean collision(GameEntity other);
    void update();
    void draw(Canvas canvas);
    int getWidth();
    int getHeight();
}
