package com.ligrim.tower_defense;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MachineGunTower implements Tower {
    private float rateOfFire;
    private float range;
    private int damage;
    private double lastShotTick;
    private int width;
    private int height;
    private Position position;
    private float angle;
    private float directionX;
    private float directionY;
    private Queue<Enemy> enemyTarget;


    public MachineGunTower(Position position) {
        rateOfFire = 5f / 60;
        range = 100;
        damage = 3;
        width = 128;
        height = 128;
        this.position = position;
        lastShotTick = 0;
        directionX = 0;
        directionY = 0;
        enemyTarget = new LinkedList<>();

    }

    @Override
    public float getRateOfFire() {
        return rateOfFire;
    }

    @Override
    public float getRange() {
        return range;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public double getTickOfLastShot() {
        return lastShotTick;
    }

    @Override
    public void setTickOfLastShot(double shotTime) {
        lastShotTick = shotTime;
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
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean collision(GameEntity other) {
        if (other instanceof Tower) {
            Tower experimentalTower = (Tower) other;
            float widthExperimentalTower = experimentalTower.getWidth();
            float lengthExperimentalTower = experimentalTower.getHeight();
            // get Position of 4 vertex
            Position point1 = experimentalTower.getPosition();
            Position point2 = new Position(point1.getX() + widthExperimentalTower, point1.getY());
            Position point3 = new Position(point1.getX(), point1.getY() + lengthExperimentalTower);
            Position point4 = new Position(point3.getX() + widthExperimentalTower, point3.getY());
            if (inside(point1, this) || inside(point2, this) || inside(point3, this) || inside(point4, this)) return true;
        }
        return false;
    }

    private boolean inside(Position position, Tower tower) {
        double x = tower.getPosition().getX();
        double y = tower.getPosition().getY();
        double width = tower.getWidth();
        double height = tower.getHeight();
        return (position.getX() >= x && position.getY() >= y && position.getX() <= x + width && position.getY() <= y + height);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setDirection(float dx, float dy) {
        directionX = dx;
        directionY = dy;
    }

    public Queue<Enemy> getEnemyTarget() {
        return enemyTarget;
    }

    public void addEnemyTarget(Enemy enemy) {
        if (!enemyTarget.contains(enemy)) enemyTarget.add(enemy);
    }

    public Enemy chooseEnemyTarget() {
        while(enemyTarget.peek().isFaded() || distance(this, enemyTarget.peek()) > this.getRange()) {
            deleteTarget();
        }
        return enemyTarget.peek();
    }

    public void deleteTarget() {
        enemyTarget.remove();
    }

    private float distance(Tower tower, Enemy enemy) {
        Position towPos = tower.getPosition();
        Position enPos = enemy.getPosition();
        return (float) Math.sqrt((towPos.getX() - enPos.getX()) * (towPos.getX() - enPos.getX())
                + (towPos.getY() - enPos.getY()) * (towPos.getY() - enPos.getY()));
    }

    @Override
    public void update() {
        Enemy finalTarget = chooseEnemyTarget();
        directionX = finalTarget.getDirectionX() - this.getPosition().getX();
//        directionY = finalTarget.getDirectionY() - this.
        if (directionX == 0 && directionY == 0) angle = 0;
        else {
            float tempAngle = (float) Math.atan(Math.abs(directionY / directionX)) * 180 / (float) Math.PI;
            if (directionX == 0 && directionY > 0) angle = 180;
            else if (directionX == 0 && directionY < 0) angle = 0;
            else if (directionX > 0 && directionY > 0) angle = 90 + tempAngle;
            else if (directionX > 0 && directionY < 0) angle = 90 - tempAngle;
            else if (directionX > 0 && directionY == 0) angle = 90;
            else if (directionX < 0 && directionY > 0) angle = -90 - tempAngle;
            else if (directionX < 0 && directionY < 0) angle = -90 + tempAngle;
            else angle = -90;
        }
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
