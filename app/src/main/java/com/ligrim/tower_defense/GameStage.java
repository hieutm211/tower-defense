package com.ligrim.tower_defense;

import java.io.FileInputStream;
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

    public static final int INITIAL_GOLD = 100;
    public static int UNIT_WIDTH;
    public static int UNIT_HEIGHT;
    public static int WIDTH;
    public static int HEIGHT;
    private Map map;
    private int currentRound;
    private List<Round> roundList; // list of all rounds in the game
    private List<Route> route; // route here

    public GameStage(InputStream mapFile, InputStream EnemyFile, InputStream saveFile) {
        currentRound = 0;
        map = GameIOFile.initMapData(mapFile);
        this.UNIT_HEIGHT = map.UNIT_HEIGHT;
        this.UNIT_WIDTH = map.UNIT_WIDTH;
        this.WIDTH = map.WIDTH;
        this.HEIGHT = map.HEIGHT;
        route = map.convertToPositionListOfRoute();
        roundList = GameIOFile.createRoundList(EnemyFile, saveFile, route);
        setTileSize();
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public boolean hasNextEnemy() { return roundList.get(currentRound).hasNext(); }

    public Enemy nextEnemy() { return roundList.get(currentRound).nextEnemy(); }

    // need to check if there is any enemy on the field before this function is called
    public void nextRound() {
        if (!hasNextEnemy()) ++currentRound;
    }

    public List<List<GameTile>> getTileList() {
        return map.toTileList();
    }

    public boolean hasNextRound() {
        return currentRound < roundList.size() - 1;
    }

    public int totalRound() {
        return roundList.size();
    }

    // unchecked
    public boolean isRoadTowerOverlap(Tower tower) {
        int topLeftX = (int)tower.getPosition().getX();
        int topLeftY = (int)tower.getPosition().getY();
        int rightMostX = topLeftX + tower.getWidth();
        int downMostY = topLeftY + tower.getHeight();
        return (map.isRoad( topLeftY/UNIT_HEIGHT, topLeftX/UNIT_WIDTH) ||
                map.isRoad( downMostY/UNIT_HEIGHT, topLeftX/UNIT_WIDTH) ||
                map.isRoad( topLeftY/UNIT_HEIGHT, rightMostX/UNIT_WIDTH) ||
                map.isRoad( downMostY/UNIT_HEIGHT, rightMostX/UNIT_WIDTH));
    }

    // this method is for junping immediately to a specific round
    // return true if i < total round, false otherwise
    // set current round to ith round
    public boolean getRound(int i) {
        if (i < roundList.size()) {
            currentRound = i;
        }
        return false;
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
        GameStage game = new GameStage(new FileInputStream("app/src/main/assets/Map/map_2/sample_map2.tmx"),
                new FileInputStream("app/src/main/assets/Map/map_2/enemy_info.txt"),
                new FileInputStream("app/src/main/assets/Map/map_2/route_info.txt")  );
        List<Tower> towers = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            towers.add(new MachineGunTower(new Position(i * 20, i * 20)));
        }
        game.map.printMapInfo();
        for (int i = 0; i < game.route.size(); ++i) {
            System.out.println("route " + i);
            for (int j = 0; j < game.route.get(i).size(); ++j) {
                System.out.print( game.route.get(i).get(j).getX() + "," + game.route.get(i).get(j).getY() + "; ");
            }
        }
    }
}
