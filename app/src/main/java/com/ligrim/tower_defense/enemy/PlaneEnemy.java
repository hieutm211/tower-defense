package com.ligrim.tower_defense.enemy;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.ligrim.tower_defense.GameField;
import com.ligrim.tower_defense.GameGraphic;

public class PlaneEnemy extends AirborneEnemy {

    public PlaneEnemy() {
        super("enemy_plane");
        this.health = 2000;
        this.maxHealth = health;
        this.speed = (100f / 60f ) / 64f * GameField.UNIT_HEIGHT;
        this.armor = 500;
        this.prize = 75;
        faded = false;
    }

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
        System.out.println(width);
        canvas.restore();

        int hp = healthPercent();
        if (hp == 100) return;

        Bitmap health = null;
        health = GameGraphic.getBitmapById("health_" + hp, width, height);

        GameGraphic.draw(health, x, y - height/3f);
    }

}
