package com.ligrim.tower_defense;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GameField extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;

    private GameStage stage;
    private List<Enemy> enemyList; // enemy da duoc tao ra va con song
    private List<Tower> towerList; // tower da duoc xay dung.
    private List<GameTile> tileList;

    private Spawner spawner;
    private List<Bullet> bulletList; // nhung vien dan dang bay

    private int gold;
    private int health;

    private double gameTick;
    private final double dt = 1d / 60; // amount increased by game Tick after an update
    private final double timeToAddEnemy = 1.0;
    private final double shootTime = 0.5;



    public GameField(Context context, GameStage gameStage) {
        //android code here
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);

        //initiate gameField using gameStage here

        this.stage = gameStage;
        this.enemyList = new ArrayList<>();
        this.towerList = new ArrayList<>();
        this.tileList = new ArrayList<>();
        this.bulletList = new ArrayList<>();
        this.gold = gameStage.INITIAL_GOLD;
        gameTick = 0.0;
        this.health = 10;

        //get Spawner
        for (GameTile tile: tileList) {
            if (tile instanceof Spawner) {
                spawner = (Spawner) tile;
                break;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.rgb(255, 176, 242));
    }

    //update game state here
    public void update() {

        //update Enemy status
        for (Enemy enemy: enemyList) {
            enemy.update();
        }
        addEnemy();
        checkEnemyReachTarget();

        //check if can put tower here canPutTower(tower)
            // if there is no problem addTower()
        // end of build Tower code


        // battlefield update
        updateTowersTarget();
        for (Tower tower: towerList) {
            tower.update();
        }
        updateNextShoot();
        for (Bullet bullet: bulletList) {
            bullet.update();
        }
        updateCollisionBulletEnemy();
        updatePrize();
        updateListEnemy();

        if (isDead()) return;
        else if (!stage.hasNextEnemy() && enemyList.isEmpty()) {
            if (!stage.hasNextRound()) return;
            else {
                stage.nextRound();
            }
        }


        gameTick += dt;
    }

    //update list of Enemy
    public void addEnemy() {
        if (gameTick - spawner.getSpawnerTick() >= timeToAddEnemy) {
            if (stage.hasNextEnemy()) {
                Enemy enemy = stage.nextEnemy();
                if (enemy != null) {
                    enemyList.add(enemy);
                }
                spawner.setSpawnerTick(gameTick);
            }
        }
    }

    public void checkEnemyReachTarget() {
        for (int i = 0; i < enemyList.size(); i++) {
            // if distance of enemy and target smaller than the enemy speed, then delete enemy
            if (enemyList.get(i).isReachCheckpoint()) {
                enemyList.get(i).disappear();
                enemyList.remove(i);
                i--;
                health--;
            }
        }
    }

    //set up Tower
    public boolean canSetTower(Tower tower) {
        return !isTowerTowerOverlap(tower) && !stage.isRoadTowerOverlap(tower);
    }
    public boolean isTowerTowerOverlap(Tower experimentalTower) {
        if (towerList.size() == 0) return false;
        for (Tower tower: towerList) {
            if(tower.collision(experimentalTower)) return true;
        }
        return false;
    }

    public void addTower(Tower tower) {
        towerList.add(tower);
    }


    // operation with Health
    public boolean isDead() {
        return health == 0;
    }

    //add more targets for tower
    public void updateTowersTarget() {
        for(Tower tower: towerList) {
            for (Enemy enemy: enemyList) {
                if (Position.distance(tower.getPosition(), enemy.getPosition()) <= tower.getRange() && !enemy.isFaded()) {
                    tower.addEnemyTarget(enemy);
                }
            }
        }
    }

    //update Next Shoot of each tower
    public void updateNextShoot() {
        for (Tower tower: towerList) {
            if (gameTick - tower.getTickOfLastShot() > shootTime) {
                bulletList.add(new Bullet(tower, tower.chooseEnemyTarget()));
                tower.setTickOfLastShot(gameTick);
            }
        }
    }

    //update collision between bullet and enemy
    public void updateCollisionBulletEnemy() {
        for (int i = 0; i < bulletList.size(); i++) {
            for (Enemy enemy: enemyList) {
                if (bulletList.get(i).collision(enemy)) {
                    enemy.reduceHealth(bulletList.get(i).getDamage());
                    bulletList.remove(i);
                    i--;
                }
            }
        }
    }

    // update Prize
    public void updatePrize() {
        for (Enemy enemy: enemyList) {
            if (enemy.getHealth() <= 0) {
                this.gold += enemy.getPrize();
                enemy.disappear();
            }
        }
    }

    //update the List of Enemy
    public void updateListEnemy() {
        for (int i = 0; i < enemyList.size(); i++) {
            if (enemyList.get(i).isFaded() || enemyList.get(i).getHealth() <= 0) {
                enemyList.remove(i);
                i--;
            }
        }
    }

    //check if Game is Over yet
    public boolean canContinue() {
        return !isDead() || stage.hasNextEnemy() || stage.hasNextRound() || !enemyList.isEmpty();
    }


}
