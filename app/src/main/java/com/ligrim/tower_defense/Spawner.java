package com.ligrim.tower_defense;

import android.graphics.Canvas;

public class Spawner extends Road {

    private double spawnerTick;

    public Spawner(Position position) {
        super(position);
        this.type = TYPE_SPAWNER;
        spawnerTick = -1.0;
    }

    public double getSpawnerTick() {
        return spawnerTick;
    }

    public void setSpawnerTick(double spawnerTick) {
        this.spawnerTick = spawnerTick;
    }

    public Spawner(float x, float y) {
        super(x, y);
    }
}
