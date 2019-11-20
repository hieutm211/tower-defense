package com.ligrim.tower_defense;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends GameEntity {
    protected int health;
    protected int maxHealth;
    protected float speed;
    protected int armor;
    protected int prize;
    protected float directionX;
    protected float directionY;
    protected Position prevPosition;
//    protected Position position;
//    protected int width = GameField.UNIT_WIDTH;
//    protected int height = GameField.UNIT_HEIGHT;
    protected boolean faded;
    protected List<Position> route;
    protected int checkpoint;

    public Enemy(String id, List<Position> route) {
        super(id, route.get(0)); // set position

        this.route = route;
        directionX = 0;
        directionY = 0;

        assert (route.size() > 1);
        checkpoint = 1;
        updateDirection(checkpoint);
        this.setAngle();

        this.prevPosition = position;
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

//    public void setPosition(Position position) {
//        this.position = position;
//    }
//
//    public Position getPosition() {
//        return this.position;
//    }

    public boolean collision(GameEntity other) {
        if (other instanceof Enemy){
            Enemy enemyOther = (Enemy) other;
            if (!(this.getClass().equals(enemyOther.getClass()))) return false;
            return Position.distance(this.position, enemyOther.getPosition()) <= this.speed;
        }
        return false;
    }

    public void update() {
        prevPosition = position;
        if (isReachCheckpoint()) nextDestination();
        float newEnemyPositionX = this.getX() + this.getDirectionX() * this.getSpeed();
        float newEnemyPositionY = this.getY() + this.getDirectionY() * this.getSpeed();
        this.setPosition(new Position(newEnemyPositionX, newEnemyPositionY));
    }

    public void unupdate() {
        this.setPosition(prevPosition);
        checkpoint--;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setAngle() {
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

    public float getAngle() {
        return angle;
    }
    public boolean isFaded() {
        return faded;
    }

    public void disappear() {
        faded = true;
    }

    // check if checkpoint is target, then update checkpoint and direction
    public void nextDestination() {
        // assert isReachCheckpoint();
        if (checkpoint < route.size() - 1) {
            ++checkpoint;
            updateDirection(this.checkpoint);
            this.setAngle();
        }
    }

    public boolean isReachCheckpoint() {
        return Position.distance(this.getPosition(), route.get(checkpoint)) <= this.getSpeed();
    }

    // check if the distance between this enemy and target is lower than speed
    public boolean isReachTarget() {
        return checkpoint == route.size() - 1 && Position.distance(this.position, route.get(route.size() - 1)) <= this.speed;
    }

    // check if the given enemy is the same type of this enemy, then check the distance between them
//    public boolean isCollideTo(Enemy other) {
//        if (!(this.getClass().equals(other.getClass()))) return false;
//        return Position.distance(this.position, other.getPosition()) <= this.speed;
//    }

    private void updateDirection(int check) {
        float dx = route.get(check).getX() - this.getX();
        float dy = route.get(check).getY() - this.getY();
        // turn dx and dy to unit vector
        dx /= Position.distance(this.getPosition(), route.get(check));
        dy /= Position.distance(this.getPosition(), route.get(check));
        this.setDirection(dx, dy);
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
}
