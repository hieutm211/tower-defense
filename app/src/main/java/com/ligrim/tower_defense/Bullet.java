package com.ligrim.tower_defense;

import com.ligrim.tower_defense.base.GameIOFile;
import com.ligrim.tower_defense.base.Moveable;
import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.enemy.Enemy;
import com.ligrim.tower_defense.tower.Tower;

public class Bullet extends GameEntity implements Moveable {

    private static final float COLLIDE_DISTANCE = .4f * GameField.UNIT_HEIGHT;
    private final float speed;
    private final float range;
    private final int damage;
    private float directionX;
    private float directionY;
    private Enemy enemyTarget;
    private Tower owner;

    public Bullet(Tower owner, Enemy targetedEnemy) {
        super(owner.getId() + "_" + "bullet", owner.getPosition()); // initiate position

        this.owner = owner;
        this.enemyTarget = targetedEnemy;
        this.speed = owner.getBulletSpeed();
        this.range = owner.getRange();
        this.damage = owner.getDamage();
        enemyTarget = targetedEnemy;

        predictDirection();
        setAngle(directionX, directionY);
    }

    float getSpeed() {
        return speed;
    }
    float getRange() {
        return range + 200f;
    }
    int getDamage() {
        return damage;
    }

    private void setAngle(float dx, float dy) {
        if (dx == 0 && dy == 0) {
            angle = 0;
            return;
        }
        float newAngle = (float) Math.atan(Math.abs(dy/dx)) * 180 / (float) Math.PI;

        if (dx == 0 && dy > 0) angle = 90 + 90;
        else if (dx == 0  && dy < 0) angle = -90 + 90;
        else if (dx > 0 && dy == 0) angle = 0 + 90;
        else if (dx < 0 && dy == 0) angle = 180 + 90;
        else if (dx > 0 && dy > 0) angle = newAngle + 90;
        else if (dx > 0 && dy < 0) angle = -newAngle + 90;
        else if (dx < 0 && dy > 0) angle = -90 - newAngle;
        else if (dx < 0 && dy < 0) angle = newAngle - 90;
    }

    private void predictDirection() {
        float distance = Position.distance(position, enemyTarget.getPosition());
        directionX = this.enemyTarget.getX() - this.getX() + this.enemyTarget.getDirectionX() *
                this.enemyTarget.getSpeed() * distance / speed ; // set vector
        directionY = this.enemyTarget.getY() - this.getY() + this.enemyTarget.getDirectionY() *
                this.enemyTarget.getSpeed() * distance / speed ; // set vector
        float length = directionX * directionX + directionY * directionY;
        length = (float) Math.sqrt((double) length);
        directionX /= length; //set to unit length
        directionY /= length; //set to unit length;
    }

    public float getAngle() {
        return angle;
    }

    public Tower getOwner() {
        return owner;
    }

    @Override
    public boolean collision(GameEntity other) {
        if (other instanceof Enemy) {
            Enemy enemy = (Enemy) other;
            if (Position.distance(enemy.getPosition(), this.position) < COLLIDE_DISTANCE) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void move() {
        setPosition(new Position(this.getX() + directionX * speed, this.getY() + directionY * speed));
    }
}
