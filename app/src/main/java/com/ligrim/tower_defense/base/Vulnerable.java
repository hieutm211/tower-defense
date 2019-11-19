package com.ligrim.tower_defense.base;

public interface Vulnerable {

    void reduceHealth(int damage);

    boolean isDead();
}
