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
    private List<Position> route;

    private int gold;
    private int health;

    private double gameTick;
    private final double dt = 1d / 60; // amount increased by game Tick after an update
    private final double timeToAddEnemy = 1.0;



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
        route = gameStage.getRoute();
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
        if (canvas != null) {
            canvas.drawColor(Color.rgb(255, 176, 242));
        }
    }

    //update game state here
    public void update() {

        //update Enemy status
        addEnemy();
        updateEnemyDirection();
        for (Enemy enemy: enemyList) {
            enemy.update();
        }
        checkEnemyReachTarget();


        //check Build Tower request isTowerRequested()
        //create new Tower buildNewTower() with type and initial position depend on mouse click
        // check if position of the new created Tower overlap with any previous towers
        // check if position of the newly created Tower overlap with the Road
        // if there is no problem addTower()
        // end of build Tower code



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
            if (distance(enemyList.get(i).getPosition(), route.get(route.size() - 1)) <= enemyList.get(i).getSpeed()) {
                enemyList.get(i).disappear();
                enemyList.remove(i);
                i--;
                health--;
            }
        }
    }

    public void updateEnemyDirection() {
        if (enemyList.size() == 0) return;
        for (Enemy enemy: enemyList) {
            // if distance between enemy and checkpoint is smaller than the speed of Enemy, then change direction.
            for (int i = 0; i < route.size(); i++) {
                if (distance(enemy.getPosition(), route.get(i)) <= enemy.getSpeed() && i != route.size() - 1) {
                    float dx = route.get(i + 1).getX() - enemy.getPosition().getX();
                    float dy = route.get(i + 1).getY() - enemy.getPosition().getY();
                    // turn dx and dy to unit vector
                    dx /= distance(enemy.getPosition(), route.get(i + 1));
                    dy /= distance(enemy.getPosition(), route.get(i + 1));
                    enemy.setDirection(dx, dy);
                }
            }
        }

    }

    //set up Tower
    public boolean isTowerRequested() {
        return true;
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

    // general distance
    private double distance(Position position, Position pos) {
        double xDistance = position.getX() - pos.getX();
        double yDistance = position.getY() - pos.getY();
        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }

}
