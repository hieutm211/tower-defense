package com.ligrim.tower_defense;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.ligrim.tower_defense.base.GameIOFile;
import com.ligrim.tower_defense.base.Map;
import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.base.Route;
import com.ligrim.tower_defense.enemy.Enemy;
import com.ligrim.tower_defense.tile.GameTile;
import com.ligrim.tower_defense.tower.MachineGunTower;
import com.ligrim.tower_defense.tower.Tower;

public class GameStage {

    public static final int INITIAL_GOLD = 50;
    public static int UNIT_WIDTH = 70;
    public static int UNIT_HEIGHT = 70;
    public static int WIDTH;
    public static int HEIGHT;
    private int currentRound;
    private List<Round> roundList; // list of all rounds in the game
    private List<Route> routeList; // routeList here
    private Map map;

    public GameStage(InputStream mapFile, InputStream EnemyFile) {
        map = GameIOFile.initMapData(mapFile);
        currentRound = 0;
        this.WIDTH = map.WIDTH;
        this.HEIGHT = map.HEIGHT;
        routeList = map.convertToPositionListOfRoute();
        roundList = GameIOFile.createRoundList(EnemyFile, routeList);
        setTileSize();
    }

    public int getCurrentRound() {
        return currentRound;
    }


    public boolean hasNextEnemy() { return roundList.get(currentRound).hasNext(); }

    public Enemy nextEnemy() { return roundList.get(currentRound).nextEnemy(); }

    public void nextRound() {
        if (!hasNextEnemy()) ++currentRound;
    }

    public List<List<GameTile>> getTileList() {
        return map.toTileList(UNIT_WIDTH, UNIT_HEIGHT);
    }

    public boolean hasNextRound() {
        return currentRound < roundList.size() - 1;
    }

    public int totalRound() {
        return roundList.size();
    }

    public boolean isMapTowerOverlap(Tower tower) {
        int topLeftX = (int)tower.getPosition().getX();
        int topLeftY = (int)tower.getPosition().getY();
        int rightMostX = topLeftX + tower.getWidth();
        int downMostY = topLeftY + tower.getHeight();
        return (map.isOverlap( (topLeftY + UNIT_HEIGHT / 8)/UNIT_HEIGHT, (topLeftX + UNIT_WIDTH / 8) /UNIT_WIDTH) ||
                map.isOverlap( (downMostY - UNIT_HEIGHT / 8) /UNIT_HEIGHT, (topLeftX + UNIT_WIDTH / 8) /UNIT_WIDTH) ||
                map.isOverlap( (topLeftY + UNIT_HEIGHT / 8) /UNIT_HEIGHT, (rightMostX - UNIT_WIDTH / 8) /UNIT_WIDTH) ||
                map.isOverlap( (downMostY - UNIT_HEIGHT / 8) /UNIT_HEIGHT, (rightMostX - UNIT_WIDTH / 8) /UNIT_WIDTH));
    }

    public boolean jumpToRound(int i) {
        if (i < roundList.size()) {
            currentRound = i;
        }
        return false;
    }

    public void restart() {
        for (Round round : roundList) round.reset();
        currentRound = 0;
    }

    /***************************************************************************
     * Helper functions.
     ***************************************************************************/

    private void setTileSize() {
        float fitUnitWidth = GameGraphic.getScreenWidthPixels() / WIDTH;
        float fitUnitHeight = GameGraphic.getScreenHeightPixels() / HEIGHT;

        if (UNIT_WIDTH < fitUnitWidth || UNIT_HEIGHT < fitUnitHeight) {
            float scaleFactor = Math.max(fitUnitWidth / UNIT_WIDTH, fitUnitHeight / UNIT_HEIGHT);
            UNIT_WIDTH = (int) (UNIT_WIDTH * scaleFactor);
            UNIT_HEIGHT = (int) (UNIT_HEIGHT * scaleFactor);
        }
    }

    public static void main(String[] args) throws Exception {

        List<Tower> towers = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            towers.add(new MachineGunTower(new Position(i * 20, i * 20)));
        }
        GameIOFile.saveToFile(towers, 1, 2000, 9999999, "app/src/main/assets/map/map_3/saveFile.xml");
    }
}
