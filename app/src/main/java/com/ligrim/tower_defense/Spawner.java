package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class Spawner extends Road {

    private double spawnerTick;

    public Spawner(int code, Position position) {
        super(code, position);
        this.type = TYPE_SPAWNER;
        spawnerTick = -1.0;
    }

    public Spawner(int code, float x, float y) {
        this(code, new Position(x, y));
    }

    public double getSpawnerTick() {
        return spawnerTick;
    }

    public void setSpawnerTick(double spawnerTick) {
        this.spawnerTick = spawnerTick;
    }

}
