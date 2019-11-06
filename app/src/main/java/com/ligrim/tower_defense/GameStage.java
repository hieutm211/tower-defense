package com.ligrim.tower_defense;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;

public class GameStage {

    public static class Round {
        private final int roundNumber;
        private List<EnemyType> enemy;
        private List<Integer> amount;
        private int enemyID;
        private int countEnemy;
        private List<Position> route;

        public List<Position> getRoute () {
            return route;
        }

        private void addPosition(Position p) {
            route.add(p);
        }

//      enemyId && amount has same size
    public Enemy nextEnemy() {

        if (enemyID > enemy.size()) return null;

        if (countEnemy >= amount.get(enemyID)) {
            countEnemy = 0;
            enemyID++;
        }
        ++countEnemy;
        return enemyFactory(enemy.get(enemyID));
    }

    public boolean hasNext() {
            return true;
    }

        public int getRoundNumber() {
            return roundNumber;
        }

        public void add(EnemyType e, int n) {
            enemy.add(e);
            amount.add(n);
        }

        public Round(int round) {
            roundNumber = round;
            enemyID = 0;
            countEnemy = 0;
            enemy = new ArrayList<>();
            amount = new ArrayList<>();
            route = new ArrayList<>();
        }

        private Enemy enemyFactory(EnemyType e) {
            switch (e) {
                case TANKER_ENEMY:
                    return null;

                case NORMAL_ENEMY:
                    return null;

                case SMALLER_ENEMY:
                    return null;

                case BOSS_ENEMY:
                    return null;

                default:
                    return null;
            }
        }





    }

    enum RoadType {
        MOUNTAIN,
        ROAD
    }

    enum EnemyType {
        TANKER_ENEMY,
        NORMAL_ENEMY,
        SMALLER_ENEMY,
        BOSS_ENEMY
    }

    enum TowerType {
        NORMAL_TOWER,
        MACHINEGUN_TOWER,
        SNIPER_TOWER
    }

    public final int UNIT_WIDTH;
    public final int UNIT_HEIGHT;
    public final int WIDTH;
    public final int HEIGHT;
    public final int INITIAL_GOLD;
    private RoadType[][] mapData;
    private Bitmap demoImg;

    private List<Round> roundList;

    //getter RoundList
    public List<Round> getRoundList() {
        return roundList;
    }

    public boolean isRoadTowerOverlap(Tower tower) {
        return false;
    }

    public GameStage(String folder, String mapFile, String EnemyFile) {
        UNIT_WIDTH = 0;
        UNIT_HEIGHT = 0;
        WIDTH = 0;
        HEIGHT = 0;
        INITIAL_GOLD = 0;
    }
}
