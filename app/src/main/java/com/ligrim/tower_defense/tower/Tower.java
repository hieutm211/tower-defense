package com.ligrim.tower_defense.tower;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ligrim.tower_defense.Bullet;
import com.ligrim.tower_defense.base.Attacker;
import com.ligrim.tower_defense.base.Timer;
import com.ligrim.tower_defense.enemy.Enemy;
import com.ligrim.tower_defense.GameEntity;
import com.ligrim.tower_defense.GameGraphic;
import com.ligrim.tower_defense.tile.GameTile;
import com.ligrim.tower_defense.base.Position;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Tower extends GameTile implements Attacker {

    protected double rateOfFire;
    protected float bulletSpeed;
    protected float range;
    protected int damage;
    protected int price;
    protected Timer timer;

    protected float directionX;
    protected float directionY;
    protected Queue<Enemy> enemyTarget;
    protected int currentLevel;
    protected int MAX_LEVEL;
    protected boolean displayRange = false;

    public void setPrice() { price += (price / 2);}
    public int getUpgradePrice() { return price / 2; }
    public int getPrice() {
        return price;
    }
    public float getBulletSpeed() { return bulletSpeed; }
    public void setDamage() {damage += (damage / 2);}

    public Tower(String id, Position position) {
        super(id, position);
        directionX = 0;
        directionY = 0;
        enemyTarget = new LinkedList<>();
    }

    public Timer getTimer() {
        return this.timer;
    }

    public int getLevel() {
        return currentLevel;
    }

    public float getRange() {
        return range;
    }

    public int getDamage() {
        return damage;
    }

    /*//public double getTickOfLastShot() {
        return lastShotTick;
    }

    public void setTickOfLastShot(double shotTime) {
        lastShotTick = shotTime;
    }*/

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

    public void setDisplayRange(boolean displayRange) {
        this.displayRange = displayRange;
    }

    public Queue<Enemy> getEnemyTarget() {
        return enemyTarget;
    }

    public void addEnemyTarget(Enemy enemy) {
        if (!enemyTarget.contains(enemy)) enemyTarget.add(enemy);
    }

    public Enemy chooseEnemyTarget() {
        while(enemyTarget.peek() != null && (enemyTarget.peek().isDead() || enemyTarget.peek().isFaded()
                || Position.distance(this.getPosition(), enemyTarget.peek().getPosition()) > this.getRange())) {
            deleteTarget();
        }
        return enemyTarget.peek();
    }

    public void update() {
        Enemy finalTarget = chooseEnemyTarget();
        if (chooseEnemyTarget() == null) return;
        directionX = finalTarget.getX() - this.getX();
        directionY = finalTarget.getY() - this.getY();
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
    public Bullet attack() {
        Enemy target = chooseEnemyTarget();
        if (target == null) return null;
        return new Bullet(this, target);
    }

    @Override
    public void draw(Canvas canvas) {
        float x = getX();
        float y = getY();
        float centerX = getCenterX();
        float centerY = getCenterY();

        if (displayRange) {
            Paint circlePaint = new Paint();

            circlePaint.setColor(Color.LTGRAY); // green

            circlePaint.setAlpha(150);

            canvas.drawCircle(centerX, centerY, range, circlePaint);
        }

        Bitmap tower = GameGraphic.getBitmapById(getId(), width, height);

        Bitmap tower_gun = GameGraphic.getBitmapById(getId() + "_gun", width, height);

        canvas.drawBitmap(tower, x, y, null);

        canvas.save();
        canvas.rotate(angle, centerX, centerY);
        canvas.drawBitmap(tower_gun, x, y, null);
        canvas.restore();

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(20f);
        canvas.drawText(Integer.toString(getLevel()), getCenterX()-5, getCenterY()+10, paint);
    }

    public void upgrade() {
        currentLevel++;
        setPrice();
        setDamage();
    }

    public boolean isLevelMax() {
        return currentLevel >= MAX_LEVEL;
    }


    /***************************************************************************
     * Helper functions.
     ***************************************************************************/

    private void deleteTarget() {
        enemyTarget.poll();
    }

    private boolean inside(Position position, Tower tower) {
        double x = tower.getX();
        double y = tower.getY();
        double width = tower.getWidth();
        double height = tower.getHeight();
        return (position.getX() >= x && position.getY() >= y && position.getX() <= x + width && position.getY() <= y + height);
    }

}
