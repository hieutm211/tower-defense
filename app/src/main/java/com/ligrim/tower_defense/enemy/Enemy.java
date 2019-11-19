package com.ligrim.tower_defense.enemy;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.ligrim.tower_defense.GameEntity;
import com.ligrim.tower_defense.GameGraphic;
import com.ligrim.tower_defense.base.Destroyable;
import com.ligrim.tower_defense.base.Moveable;
import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Route;
import com.ligrim.tower_defense.base.Vulnerable;
import com.ligrim.tower_defense.tower.MachineGunTower;
import com.ligrim.tower_defense.tower.NormalTower;
import com.ligrim.tower_defense.tower.SniperTower;
import com.ligrim.tower_defense.tower.Tower;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends GameEntity implements Moveable, Vulnerable, Destroyable {
    protected int health;
    protected int maxHealth;
    protected float speed;
    protected int armor;
    protected int prize;
    protected float directionX;
    protected float directionY;
    protected boolean faded;
    protected Route route;
    protected int checkpoint;

    public Enemy(String id, Route route) {
        super(id, route.getSpawner()); // set position

        this.route = route;
        directionX = 0;
        directionY = 0;

        checkpoint = 1;
        updateDirection();
        this.setAngle();
    }

    public int getHealth() {
        return health;
    }

    public float HPpercent() { return (float) health / maxHealth; }

    public float getSpeed() {
        return speed;
    }

    public int getArmor() {
        return armor;
    }

    public int getPrize() {
        return prize;
    }

    public void reduceHealth(int damage) {
        if (armor - damage < 0)
            health -= damage - armor;
    }

    public void setDirection(float dx, float dy) {
        directionX = dx;
        directionY = dy;
    }

    public float getDirectionX() {
        return directionX;
    }

    public float getDirectionY() {
        return directionY;
    }

    public boolean collision(GameEntity other) {
        if (other instanceof Enemy){
            Enemy enemyOther = (Enemy) other;
            if (!(this.getClass().equals(enemyOther.getClass()))) return false;
            return Position.distance(this.position, enemyOther.getPosition()) <= this.speed;
        }
        return false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getAngle() {
        return angle;
    }

    // check if the distance between this enemy and target is lower than speed
    public boolean isReachTarget() {
        return checkpoint == route.size() - 1 && Position.distance(this.position, route.getTarget()) <= this.speed;
    }

    //get all available tower
    public List<Tower> getAvailableTower() {
        List<Tower> tow = new ArrayList<>();
        tow.add(new NormalTower(new Position(0,0)));
        tow.add(new SniperTower(new Position(0,0)));
        tow.add(new MachineGunTower(new Position(0,0)));
        return tow;
    }

    @Override
    public boolean isFaded() {
        return faded;
    }

    @Override
    public boolean isDead() { return health <= 0; }

    @Override
    public void disappear() {
        faded = true;
    }

    @Override
    public void move() {
        if (isReachCheckpoint()) nextDestination();
        float newEnemyPositionX = this.getX() + this.getDirectionX() * this.getSpeed();
        float newEnemyPositionY = this.getY() + this.getDirectionY() * this.getSpeed();
        this.setPosition(new Position(newEnemyPositionX, newEnemyPositionY));
    }

    @Override
    public void draw(Canvas canvas) {
        float x = getX();
        float y = getY();
        float centerX = getCenterX();
        float centerY = getCenterY();

        Bitmap enemy = GameGraphic.getBitmapById(getId(), width, height);

        canvas.save();
        canvas.rotate(angle, centerX, centerY);
        canvas.drawBitmap(enemy, x, y, null);
        canvas.restore();
    }

    /***************************************************************************
     * Helper functions.
     ***************************************************************************/

    private void updateDirection() {
        float dx = route.get(checkpoint).getX() - this.getX();
        float dy = route.get(checkpoint).getY() - this.getY();
        // turn dx and dy to unit vector
        dx /= Position.distance(this.getPosition(), route.get(checkpoint));
        dy /= Position.distance(this.getPosition(), route.get(checkpoint));
        this.setDirection(dx, dy);
    }

    // check if checkpoint is target, then update checkpoint and direction
    private void nextDestination() {
        // assert isReachCheckpoint();
        if (checkpoint < route.size() - 1) {
            ++checkpoint;
            updateDirection();
            this.setAngle();
        }
    }

    private boolean isReachCheckpoint() {
        return Position.distance(this.getPosition(), route.get(checkpoint)) <= this.getSpeed();
    }

    private void setAngle() {
        if (Math.abs(directionX) < .1f  && Math.abs(directionY) < .1f) {
            angle = 0f;
            return;
        }
        // angle in degree
        float newAngle = (float) Math.atan(Math.abs(directionY/directionX)) * 180 / (float) Math.PI;

        if (Math.abs(directionX) < .1f && directionY > 0f) angle = 90f;
        else if (Math.abs(directionX) < .1f  && directionY < 0f) angle = -90f;
        else if (directionX > 0f && Math.abs(directionY) < .1f) angle = 0f;
        else if (directionX < 0f && Math.abs(directionY) < .1f) angle = 180f;
        else if (directionX > 0f && directionY > 0f) angle = newAngle;
        else if (directionX > 0f && directionY < 0f) angle = -newAngle;
        else if (directionX < 0f && directionY > 0f) angle = -(180f + newAngle);
        else if (directionX < 0f && directionY < 0f) angle = (newAngle + 180f);
    }

}
