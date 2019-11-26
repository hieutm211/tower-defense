package com.ligrim.tower_defense.base;

import com.ligrim.tower_defense.tower.Tower;

import java.util.LinkedList;
import java.util.List;

public class bufferedSavedGame {
    List<Tower> towerList;
    int currentRound;
    int gold;
    int health;

    public bufferedSavedGame(){
        towerList = new LinkedList<>();
    }

    public boolean hasSavedGame() { return towerList.size() > 0; }

    public int getGold() { return gold; }

    public int getCurrentRound() {
        return currentRound;
    }

    public List<Tower> getTowerList() {
        return towerList;
    }

    public int getHealth() {
        return health;
    }
}
