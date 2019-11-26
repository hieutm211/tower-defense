package com.ligrim.tower_defense;

import com.ligrim.tower_defense.base.Route;
import com.ligrim.tower_defense.enemy.BossEnemy;
import com.ligrim.tower_defense.enemy.Enemy;
import com.ligrim.tower_defense.enemy.EnemyType;
import com.ligrim.tower_defense.enemy.NormalEnemy;
import com.ligrim.tower_defense.enemy.PlaneEnemy;
import com.ligrim.tower_defense.enemy.SmallerEnemy;
import com.ligrim.tower_defense.enemy.SuperPlaneEnemy;
import com.ligrim.tower_defense.enemy.TankerEnemy;

import java.util.ArrayList;
import java.util.List;

// this class is for wrapping type, amount and order of enemies to be generated
public class Round {
    private final int roundNumber;
    private List<List<EnemyType>> enemy;
    private List<Integer> amount;
    private List<Route> route;
    private List<List<Integer>> enemyInRoute;
    private int enemyID;
    private int countEnemy;

    public Round(int round, List<Route> route) {
        roundNumber = round;
        enemyID = 0;
        countEnemy = 0;
        enemy = new ArrayList<>();
        amount = new ArrayList<>();
        enemyInRoute = new ArrayList<>();
        this.route = route;
    }

    public Enemy nextEnemy() {
        if (enemyID > enemy.size()) return null;

        if (countEnemy >= amount.get(enemyID)) {
            countEnemy = 0;
            enemyID++;
        }
        ++countEnemy;
        return enemyFactory(enemy.get(enemyID).get(countEnemy % enemy.get(enemyID).size()),
                            enemyInRoute.get(enemyID).get(countEnemy % enemyInRoute.get(enemyID).size()));
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    // unchecked
    public boolean hasNext() {
        return enemyID < enemy.size() - 1 || (enemyID == enemy.size() - 1 && countEnemy < amount.get(amount.size() - 1));
    }

    public void add(List<EnemyType> e, int amount, List<Integer> enemyInRoute) {
        enemy.add(e);
        this.amount.add(amount);
        this.enemyInRoute.add(enemyInRoute);
    }

    private Enemy enemyFactory(EnemyType e, int r) {
        if (r >= this.route.size()) return null;
        switch (e) {
            case TANKER_ENEMY:
                return new TankerEnemy(route.get(r));

            case NORMAL_ENEMY:
                return new NormalEnemy(route.get(r));

            case SMALLER_ENEMY:
                return new SmallerEnemy(route.get(r));

            case BOSS_ENEMY:
                return new BossEnemy(route.get(r));

            case PLANE_ENEMY:
                return new PlaneEnemy();

            case SUPER_PLANE_ENEMY:
                return new SuperPlaneEnemy();

            default:
                return null;
        }
    }
}

