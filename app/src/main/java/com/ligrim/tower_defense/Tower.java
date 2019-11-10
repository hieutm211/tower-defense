package com.ligrim.tower_defense;

import java.util.List;
import java.util.Queue;

public interface Tower extends GameTile {

    float getRateOfFire();
    float getRange();
    int getDamage();
    double getTickOfLastShot();
    void setTickOfLastShot(double shotTime);
    void setDirection(float dx, float dy);
    void addEnemyTarget(Enemy enemy);
    Enemy chooseEnemyTarget();
    void deleteTarget();
    Queue<Enemy> getEnemyTarget();
    int getLevel();
    String toString();
}
