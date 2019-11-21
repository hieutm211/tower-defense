package com.ligrim.tower_defense.enemy;

import com.ligrim.tower_defense.GameField;
import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Route;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class AirborneEnemy extends Enemy {
    private static Random randomNumber = new Random();

    public AirborneEnemy(String id) {
        super(id, getRandomAirwayRoute());
    }

    private static Route getRandomAirwayRoute() {
        float xStart = randomNumber.nextFloat() * 4 * GameField.UNIT_WIDTH + GameField.UNIT_WIDTH;
        float yStart = randomNumber.nextFloat() * 4 * GameField.UNIT_HEIGHT + GameField.UNIT_HEIGHT;
        float xEnd = (float) GameField.UNIT_WIDTH * GameField.WIDTH;
        float yEnd = (float) GameField.UNIT_HEIGHT * GameField.HEIGHT;
        List<Position> route = new LinkedList<>();
        route.add(new Position(-xStart, -yStart)); route.add(new Position(xEnd, yEnd));
        return new Route(route);
    }
}
