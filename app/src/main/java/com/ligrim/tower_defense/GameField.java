package com.ligrim.tower_defense;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;

import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Timer;
import com.ligrim.tower_defense.enemy.Enemy;
import com.ligrim.tower_defense.tile.GameTile;
import com.ligrim.tower_defense.tower.MachineGunTower;
import com.ligrim.tower_defense.tower.NormalTower;
import com.ligrim.tower_defense.tower.SniperTower;
import com.ligrim.tower_defense.tower.Tower;

import java.util.ArrayList;
import java.util.List;

public class GameField extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;

    public static int WIDTH;
    public static int HEIGHT;
    public static int UNIT_WIDTH;
    public static int UNIT_HEIGHT;

    private GameStage stage;
    private List<Enemy> enemyList; // enemy da duoc tao ra va con song
    private List<Tower> towerList; // tower da duoc xay dung.
    private List<List<GameTile>> tileList;

    private List<Bullet> bulletList; // nhung vien dan dang bay

    private Tower temporaryTower = null;

    private int gold;
    private int health;

    private Timer gameTick;
    private double lastAddEnemyTick;
    private final double dt = 1d / 60; // amount increased by game Tick after an update
    private final double timeToAddEnemy = .25;
    /*private final double shootTime = 0.5;*/

    public GameField(Context context, GameStage gameStage) {
        //android code here
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);

        setOnTouchListener(new TouchEventListener());
        //initiate gameField using gameStage here

        this.stage = gameStage;

        WIDTH = gameStage.WIDTH;
        HEIGHT = gameStage.HEIGHT;
        UNIT_WIDTH = gameStage.UNIT_WIDTH;
        UNIT_HEIGHT = gameStage.UNIT_HEIGHT;

        this.enemyList = new ArrayList<>();
        this.towerList = new ArrayList<>();
        this.tileList = gameStage.getTileList();
        this.bulletList = new ArrayList<>();
        this.gold = gameStage.INITIAL_GOLD;
        gameTick = new Timer(dt);
        lastAddEnemyTick = -1.0;
        this.health = 200;
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
        requestTogglePause();
/*
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
*/
    }

    //update game state here
    public void update() {

        //update Enemy status
        for (Enemy enemy: enemyList) {
            if (enemy.isHalting() ) {
                boolean canMove = true;
                if (!enemy.getTimer().alarm(gameTick.getTime())) {
                    canMove = false;
                }
                else for (Enemy e : enemyList) {
                    if (enemy != e && enemy.collision(e)) {
                        canMove = false;
                    }
                }
                if (canMove) enemy.go();
            }
            else for (Enemy enemy1: enemyList) {
                if (enemy != enemy1 && enemy.collision(enemy1)) {
                    enemy.halt();
                    enemy.getTimer().alarm(gameTick.getTime());
                }
            }
            enemy.move();
        }
        addEnemy();
        checkEnemyReachTarget();

        // battlefield update
        updateTowersTarget();
        for (Tower tower: towerList) {
            tower.update();
        }
        updateNextShoot();
        for (Bullet bullet: bulletList) {
            bullet.move();
        }
        updateCollisionBulletEnemy();
        updateBulletList();
        updatePrize();
        updateListEnemy();

        if (isDead()) return;
        else if (!stage.hasNextEnemy() && enemyList.isEmpty()) {
            if (!stage.hasNextRound()) return;
            else {
                stage.nextRound();
            }
        }

        gameTick.tick();
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    public int getCurrentRound() { return stage.getCurrentRound(); }

    public int getTotalRound() { return stage.totalRound(); }

    public List<Tower> getTowerList() {
        return towerList;
    }

    public List<List<GameTile>> getTileList() {
        return tileList;
    }

    public List<Bullet> getBulletList() {
        return bulletList;
    }

    public int getGold() {
        return gold;
    }

    public int getHealth() {
        return health;
    }

    public void setTemporaryTower(Tower temporaryTower) {
        this.temporaryTower = temporaryTower;
    }

    // check if bullet out of range
    private void updateBulletList() {
        for (int i = 0; i < bulletList.size(); i++) {
            if (Position.distance(bulletList.get(i).getPosition(), bulletList.get(i).getOwner().getPosition()) > bulletList.get(i).getRange()) {
                bulletList.remove(i);
                --i;
            }
        }
    }

    //update list of Enemy
    public void addEnemy() {
        if (gameTick.getTime() - lastAddEnemyTick >= timeToAddEnemy) {
            if (stage.hasNextEnemy()) {
                Enemy enemy = stage.nextEnemy();
                if (enemy != null) {
                    enemyList.add(enemy);
                }
                lastAddEnemyTick = gameTick.getTime();
            }
        }
    }

    public void checkEnemyReachTarget() {
        for (int i = 0; i < enemyList.size(); i++) {
            // if distance of enemy and target smaller than the enemy speed, then delete enemy
            if (enemyList.get(i).isReachTarget()) {
                enemyList.get(i).disappear();
                enemyList.remove(i);
                i--;
                health--;
            }
        }
    }

    //set up Tower
    public boolean canSetTower(String towerId, Position pos) {
        Tower tower = new NormalTower(pos);
        switch(towerId) {
            case "normal": tower = new NormalTower(pos); break;
            case "sniper": tower = new SniperTower(pos); break;
            case "machine_gun": tower = new MachineGunTower(pos); break;
        }
        return !isTowerTowerOverlap(tower) && !stage.isRoadTowerOverlap(tower);
    }
    public boolean isTowerTowerOverlap(Tower experimentalTower) {
        if (towerList.size() == 0) return false;
        for (Tower tower: towerList) {
            if(tower.collision(experimentalTower)) return true;
        }
        return false;
    }

    public void addTower(String towerId, Position pos) {
        switch(towerId) {
            case "tower_normal": towerList.add(new NormalTower(pos)); break;
            case "tower_sniper": towerList.add(new SniperTower(pos)); break;
            case "tower_machine_gun": towerList.add(new MachineGunTower(pos)); break;
        }
    }


    // operation with Health
    public boolean isDead() {
        return health <= 0;
    }

    //add more targets for tower
    public void updateTowersTarget() {
        for(Tower tower: towerList) {
            for (Enemy enemy: enemyList) {
                if (Position.distance(tower.getPosition(), enemy.getPosition()) <= tower.getRange() && !enemy.isDead()) {
                    tower.addEnemyTarget(enemy);
                }
            }
        }
    }

    //update Next Shoot of each tower
    public void updateNextShoot() {
        for (Tower tower: towerList) {
            if (tower.getTimer().alarm(gameTick.getTime())) {
                Bullet b = tower.attack();
                if (b != null) bulletList.add(b);
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
                    break;
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
            if (enemyList.get(i).isDead() || enemyList.get(i).isFaded()) {
                enemyList.remove(i);
                i--;
            }
        }
    }

    //check if Game is Over yet
    public boolean canContinue() {
        return !isDead() || stage.hasNextEnemy() || stage.hasNextRound() || !enemyList.isEmpty();
    }

    //get all available tower
    public List<Tower> getAvailableTower() {
        List<Tower> tow = new ArrayList<>();
        tow.add(new NormalTower(new Position(0,0)));
        tow.add(new SniperTower(new Position(0,0)));
        tow.add(new MachineGunTower(new Position(0,0)));
        return tow;
    }

    public void requestTogglePause() {
        thread.togglePause();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.save();
        canvas.translate(-GameGraphic.getScreenX(), -GameGraphic.getScreenY());

        GameGraphic.draw(tileList.get(0));
        GameGraphic.draw(enemyList);
        GameGraphic.draw(towerList);
        GameGraphic.draw(tileList.get(1));
        GameGraphic.draw(bulletList);

        if (temporaryTower != null) {
            GameGraphic.draw(temporaryTower);
        }

        canvas.restore();

        GamePane.draw(canvas);
    }
}
