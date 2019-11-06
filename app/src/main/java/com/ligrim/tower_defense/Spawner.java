package com.ligrim.tower_defense;

public class Spawner extends Road {

    private double spawnerTick;

    public Spawner(Position position) {
        super(position);
        this.type = TYPE_SPAWNER;
        spawnerTick = -1.0;
    }

    public Spawner(double x, double y) {
        super(x, y);
        this.type = TYPE_SPAWNER;
    }

    public double getSpawnerTick() {
        return spawnerTick;
    }

    public void setSpawnerTick(double spawnerTick) {
        this.spawnerTick = spawnerTick;
    }
}
