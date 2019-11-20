package com.ligrim.tower_defense.tile;

import com.ligrim.tower_defense.GameEntity;
import com.ligrim.tower_defense.base.Position;

public abstract class GameTile extends GameEntity {

    public GameTile(String id, Position position) {
        super(id, position);
    }

    public GameTile(String id, Position position, int width, int height) {
        super(id, position, width, height);
    }

    @Override
    public boolean collision(GameEntity other) {
        return false;
    }

}
