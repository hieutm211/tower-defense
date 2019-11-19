package com.ligrim.tower_defense.base;

import java.util.List;

public class Route {
    private final List<Position> route;

    public Route(List<Position> route) {
        this.route = route;
    }

    public Position get(int i){
        return route.get(i);
    }

    public Position getTarget() {
        return route.get(route.size() - 1);
    }

    public Position getSpawner() {
        return route.get(0);
    }

}
