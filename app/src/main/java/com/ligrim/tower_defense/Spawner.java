package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class Spawner extends Road {

    private double spawnerTick;

    public Spawner(int code, Position position, int width, int height) {
        super(code, position, width, height);
        this.type = TYPE_SPAWNER;
        spawnerTick = -1.0;
    }

    public Spawner(int code, float x, float y, int width, int height) {
        this(code, new Position(x, y), width, height);
    }

    public double getSpawnerTick() {
        return spawnerTick;
    }

    public void setSpawnerTick(double spawnerTick) {
        this.spawnerTick = spawnerTick;
    }

}
