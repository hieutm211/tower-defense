package com.ligrim.tower_defense;

public interface Enemy extends GameEntity {

    int getHealth();
    float getSpeed();
    int getArmor();
    int getPrize();
    void reduceHealth(int damage);
    void setDirection(float dx, float dy); // dx and dy is unit vector
    float getDirectionX();
    float getDirectionY();


}
