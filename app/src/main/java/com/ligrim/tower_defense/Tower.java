package com.ligrim.tower_defense;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.List;
import java.util.Queue;

public abstract class Tower implements GameTile {

    protected float rateOfFire;
    protected float range;
    protected int damage;
    protected double lastShotTick;
    protected int width;
    protected int height;
    protected Position position;
    protected float angle;
    protected float directionX;
    protected float directionY;
    protected Queue<Enemy> enemyTarget;
    protected float bulletSpeed;

    public Tower(){}

    public float getBulletSpeed() { return bulletSpeed; }

    public Tower(Position position) {
        this.position = position;
    }

    public abstract String getId();

    public int getLevel() {
        return 0;
    }

    public float getRateOfFire() {
        return rateOfFire;
    }

    public float getRange() {
        return range;
    }

    public int getDamage() {
        return damage;
    }

    public double getTickOfLastShot() {
        return lastShotTick;
    }

    public void setTickOfLastShot(double shotTime) {
        lastShotTick = shotTime;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

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
        while(enemyTarget.peek() != null && (enemyTarget.peek().isFaded()
                || Position.distance(this.getPosition(), enemyTarget.peek().getPosition()) > this.getRange())) {
            deleteTarget();
        }
        return enemyTarget.peek();
    }

    public void deleteTarget() {
        enemyTarget.poll();
    }

    @Override
    public void update() {
        Enemy finalTarget = chooseEnemyTarget();
        if (chooseEnemyTarget() == null) return;
        directionX = finalTarget.getPosition().getX() - this.getPosition().getX();
        directionY = finalTarget.getPosition().getY() - this.getPosition().getY();
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
        float x = position.getX();
        float y = position.getY();

        Bitmap tower = GameGraphic.getTowerById(getId());
        tower = Bitmap.createScaledBitmap(tower, width, height, false);

        Bitmap tower_gun = GameGraphic.getTowerById(getId() + "_gun");
        tower_gun = Bitmap.createScaledBitmap(tower_gun, width, height, false);

        canvas.drawBitmap(tower, x, y, null);

        canvas.save();
        canvas.rotate(angle, position.getX() + width/2 - 1, position.getY() + height/2 - 1);
        canvas.drawBitmap(tower_gun, x, y, null);
        canvas.restore();
    }
}
