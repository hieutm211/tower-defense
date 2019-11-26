package com.ligrim.tower_defense.enemy;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.ligrim.tower_defense.GameEntity;
import com.ligrim.tower_defense.GameField;
import com.ligrim.tower_defense.GameGraphic;
import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Route;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class FlyingEnemy extends Enemy {
    private static Random randomNumber = new Random();

    public FlyingEnemy(String id) {
        super(id, getRandomAirwayRoute());
    }

    private static Route getRandomAirwayRoute() {
        float xStart = randomNumber.nextFloat() * 4 * GameField.UNIT_WIDTH + GameField.UNIT_WIDTH;
        float yStart = randomNumber.nextFloat() * 4 * GameField.UNIT_HEIGHT + GameField.UNIT_HEIGHT;
        float xEnd = (float) GameField.UNIT_WIDTH * GameField.WIDTH;
        float yEnd = (float) GameField.UNIT_HEIGHT * GameField.HEIGHT;
        List<Position> route = new LinkedList<>();
        route.add(new Position(-xStart, -yStart)); route.add(new Position(xEnd, yEnd));
        return new Route(route);
    }

    // air enemy generally dont collide to each other
    @Override
    public boolean collision(GameEntity other) { return false; }

    @Override
    public void draw(Canvas canvas) {
        float x = getX();
        float y = getY();
        float centerX = getCenterX();
        float centerY = getCenterY();

        Bitmap enemy = GameGraphic.getBitmapById(getId(), width, height);
        Bitmap shadow = GameGraphic.getBitmapById(getId() + "_shadow", width, height);

        //draw shadow
        canvas.save();
        canvas.rotate(angle, centerX - width/1.2f, centerY + height/3f);
        canvas.drawBitmap(shadow, x - width/1.2f, y + height/3f, null);
        canvas.restore();

        //draw plane
        canvas.save();
        canvas.rotate(angle, centerX, centerY);
        canvas.drawBitmap(enemy, x, y, null);
        canvas.restore();

        int hp = healthPercent();
        if (hp == 100) return;

        Bitmap health = null;
        health = GameGraphic.getBitmapById(Integer.toString(hp), width, height);

        GameGraphic.draw(health, x, y - height/3f);
    }

}
