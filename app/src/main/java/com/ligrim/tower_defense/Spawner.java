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

    @Override
    public void setPosition(Position position) {
        super.setPosition(position);
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public boolean collision(GameEntity gameEntity) {
        return super.collision(gameEntity);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }
}
