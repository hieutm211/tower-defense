package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class Bullet implements GameEntity {

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
        this.speed = owner.getRateOfFire();
        this.range = owner.getRange();
        this.damage = owner.getDamage();
        this.position = owner.getPosition();
        enemyTarget = targetedEnemy;
        directionX = targetedEnemy.getDirectionX() - targetedEnemy.getDirectionX(); // set vector
        directionY = targetedEnemy.getDirectionY() - targetedEnemy.getDirectionY(); // set vector
        float length = directionX * directionX + directionY * directionY;
        directionX /= length; //set to unit length
        directionY /= length; //set to unit length;
    }

    float getSpeed() {
        return speed;
    }
    float getRange() {
        return range;
    }
    int getDamage() {
        return damage;
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
            if (Position.distance(enemy.getPosition(), this.position) < (float) Math.max(this.width + enemy.getWidth(),
                    this.height + enemy.getHeight())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void update() {
        directionX = this.enemyTarget.getPosition().getX() - this.getPosition().getX(); // set vector
        directionY = this.enemyTarget.getPosition().getY() - this.getPosition().getY(); // set vector
        float length = directionX * directionX + directionY * directionY;
        directionX /= length; //set to unit length
        directionY /= length; //set to unit length;
        setPosition(new Position(this.getPosition().getX() + directionX * speed, this.getPosition().getY() + directionY * speed));
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
