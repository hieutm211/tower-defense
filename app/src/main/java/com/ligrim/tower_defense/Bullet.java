package com.ligrim.tower_defense;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bullet implements GameEntity {

    private static final float COLLIDE_DISTANCE = 25f;
    private final float speed;
    private final float range;
    private final int damage;
    private Position position;
    private final int width = 64;
    private final int height = 64;
    private float directionX;
    private float directionY;
    private Enemy enemyTarget;
    private Tower owner;

    public Bullet(Tower owner, Enemy targetedEnemy) {
        this.owner = owner;
        this.enemyTarget = targetedEnemy;
        this.speed = owner.getBulletSpeed();
        this.range = owner.getRange();
        this.damage = owner.getDamage();
        this.position = owner.getPosition();
        enemyTarget = targetedEnemy;
        /*directionX = targetedEnemy.getDirectionX() - targetedEnemy.getDirectionX(); // set vector
        directionY = targetedEnemy.getDirectionY() - targetedEnemy.getDirectionY(); // set vector*/
        directionX = this.enemyTarget.getPosition().getX() - this.getPosition().getX() + targetedEnemy.getDirectionX() * 4f; // set vector
        directionY = this.enemyTarget.getPosition().getY() - this.getPosition().getY() + targetedEnemy.getDirectionY() * 4f; // set vector
        float length = directionX * directionX + directionY * directionY;
        length = (float) Math.sqrt((double) length);
        directionX /= length; //set to unit length
        directionY /= length; //set to unit length;
    }

    float getSpeed() {
        return speed;
    }
    float getRange() {
        return range + 500f;
    }
    int getDamage() {
        return damage;
    }

    public Tower getOwner() {
        return owner;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean collision(GameEntity other) {
        if (other instanceof Enemy) {
            Enemy enemy = (Enemy) other;
            if (Position.distance(enemy.getPosition(), this.position) < COLLIDE_DISTANCE/*(float) Math.max(this.width + enemy.getWidth(),
                    this.height + enemy.getHeight())*/  ) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void update() {
        /*directionX = this.enemyTarget.getPosition().getX() - this.getPosition().getX(); // set vector
        directionY = this.enemyTarget.getPosition().getY() - this.getPosition().getY(); // set vector
        float length = directionX * directionX + directionY * directionY;
        directionX /= Math.sqrt(length); //set to unit length
        directionY /= Math.sqrt(length); //set to unit length;*/
        setPosition(new Position(this.getPosition().getX() + directionX * speed, this.getPosition().getY() + directionY * speed));
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = GameGraphic.getTowerById(owner.getId() + "_bullet");
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        canvas.drawBitmap(bitmap, position.getX(), position.getY(), null);
    }
}
