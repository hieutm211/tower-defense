package com.ligrim.tower_defense;

public interface Enemy extends GameEntity {

    int getHealth();
    double getSpeed();
    int getArmor();
    int getPrize();
    void reduceHealth(int damage);
    void setDirection(double dx, double dy); // dx and dy is unit vector
    double getDirectionX();
    double getDirectionY();


}
