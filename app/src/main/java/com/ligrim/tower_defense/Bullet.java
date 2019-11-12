package com.ligrim.tower_defense;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bullet implements GameEntity {

    private static final float COLLIDE_DISTANCE = 25f;
    private final float speed;
    private final float range;
    private final int damage;
    private Position position;
    private final int width = GameField.UNIT_WIDTH;
    private final int height = GameField.UNIT_HEIGHT;
    private float directionX;
    private float directionY;
    private Enemy enemyTarget;
    private Tower owner;
    private float angle;

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
        setAngle(directionX, directionY);
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

    public float getAngle() {
        return angle;
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
        canvas.save();
        canvas.rotate(angle, position.getX() + width/2 - 1, position.getY() + height/2 - 1);
        canvas.drawBitmap(bitmap, position.getX(), position.getY(), null);
        canvas.restore();
    }
}
