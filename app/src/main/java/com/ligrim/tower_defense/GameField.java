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

    private int currentRound; // chi so cua round trong GameStage.RoundList;
    private List<GameStage.Round> roundList;

    private int gold;

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
        this.currentRound = 0;
        roundList = this.stage.getRoundList();
        route = roundList.get(currentRound).getRoute();
        this.gold = gameStage.INITIAL_GOLD;
        gameTick = 0.0;

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
        updateEnemyPosition();
        addEnemy();
        checkEnemyReachTarget();
        gameTick += dt;
    }

    //update list of Enemy
    public void addEnemy() {
        if (gameTick - spawner.getSpawnerTick() >= timeToAddEnemy) {
            if (roundList.get(currentRound).hasNext()) {
                Enemy enemy = roundList.get(currentRound).nextEnemy();
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
                enemyList.remove(i);
                i--;
            }
        }
    }

    public void updateEnemyPosition() {
        if (enemyList.size() == 0) return;
        for (Enemy enemy: enemyList) {
            // if distance between enemy and checkpoint is smaller than the speed of Enemy, then change direction.
            for (int i = 0; i < route.size(); i++) {
                if (distance(enemy.getPosition(), route.get(i)) <= enemy.getSpeed() && i != route.size() - 1) {
                    double dx = route.get(i + 1).getX() - enemy.getPosition().getX();
                    double dy = route.get(i + 1).getY() - enemy.getPosition().getY();
                    // turn dx and dy to unit vector
                    dx /= distance(enemy.getPosition(), route.get(i + 1));
                    dy /= distance(enemy.getPosition(), route.get(i + 1));
                    enemy.setDirection(dx, dy);
                }
            }
            double newEnemyPositionX = enemy.getPosition().getX() + enemy.getDirectionX() * enemy.getSpeed();
            double newEnemyPositionY = enemy.getPosition().getY() + enemy.getDirectionY() * enemy.getSpeed();
            enemy.setPosition(new Position(newEnemyPositionX, newEnemyPositionY));

        }

    }

    //set up Tower
    public boolean isTowerTowerOverlap(Tower experimentalTower) {
        if (towerList.size() == 0) return false;
        double widthExperimentalTower = experimentalTower.getWidth();
        double lengthExperimentalTower = experimentalTower.getHeight();
        // get Position of 4 vertex
        Position point1 = experimentalTower.getPosition();
        Position point2 = new Position(point1.getX() + widthExperimentalTower, point1.getY());
        Position point3 = new Position(point1.getX(), point1.getY() + lengthExperimentalTower);
        Position point4 = new Position(point3.getX() + widthExperimentalTower, point3.getY());


        for (Tower tower: towerList) {
            if (inside(point1, tower) || inside(point2, tower) || inside(point3, tower) || inside(point4, tower)) return false;
        }
        return true;
    }

    private boolean inside(Position position, Tower tower) {
        double x = tower.getPosition().getX();
        double y = tower.getPosition().getY();
        double width = tower.getWidth();
        double height = tower.getHeight();
        return (position.getX() >= x && position.getY() >= y && position.getX() <= x + width && position.getY() <= y + height);
    }







    // general distance
    private double distance(Position position, Position pos) {
        double xDistance = position.getX() - pos.getX();
        double yDistance = position.getY() - pos.getY();
        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }



}
