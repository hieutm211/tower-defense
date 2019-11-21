package com.ligrim.tower_defense.base;

import com.ligrim.tower_defense.tile.GameTile;
import com.ligrim.tower_defense.tile.Mountain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Map {
    private static final int[] roadID = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 23, 25,
            26, 27, 28, 30, 31, 32, 33, 35, 36, 37, 46, 47, 48, 50, 51, 52, 53, 55, 56, 57, 58, 60};
    private static final int[] spawnerID = {118, 123, 128};
    private static final int[] targetID = {49, 54, 59};
    private static final int[] conjunctionID = {0, 2, 5, 7, 10, 12, 46, 48, 51, 53, 56, 58};
    private static final int[] treeID = {130, 131, 132, 133, 134};
    private static final int[] rockID = {135, 136, 137};
    private static final int[] otherID = {19, 20, 21};

    public int[][] mapData;
    public int[][] mapLayer;
    public final int UNIT_WIDTH;
    public final int UNIT_HEIGHT;
    public final int WIDTH;
    public final int HEIGHT;

    public Map(int width, int height, int unitWidth, int unitHeight) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.UNIT_WIDTH = unitWidth;
        this.UNIT_HEIGHT = unitHeight;
        this.mapData = new int[HEIGHT][WIDTH];
        this.mapLayer = new int[HEIGHT][WIDTH];
    }

    public List<List<GameTile>> toTileList(int unitwidth, int unitheight) {

        List<List<GameTile>> tileList = new LinkedList<>();

        LinkedList<GameTile> layer1 = new LinkedList<>();
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                float x = (float) j * unitwidth, y = (float) i * unitheight;
                layer1.add(new Mountain(mapData[i][j], new Position(x, y), unitwidth, unitheight));
            }
        }
        LinkedList<GameTile> layer2 = new LinkedList<>();
        for (int i  = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                if (mapLayer[i][j] > 0) {
                    float x = (float) j * unitwidth, y = (float) i * unitheight;
                    if (isOther(i, j) || isRock(i, j)) {
                        layer1.addLast(new Mountain(mapLayer[i][j], new Position(x, y), unitwidth, unitheight));
                    }
                    if (isTree(i, j)) {
                        layer2.add(new Mountain(mapLayer[i][j], new Position(x, y), unitwidth, unitheight));
                    }
                }
            }
        }

        tileList.add(layer1);
        tileList.add(layer2);
        return tileList;
    }

    public List<Route> convertToPositionListOfRoute() {
        List<int[][]> matrix = this.convertToMapMatrix();
        List<Route> route = new ArrayList<>();
        for (int i = 0; i < matrix.size(); ++i) {
            route.add(new Route(BFS.getRouteFromMatrix(matrix.get(i))));
        }
        return route;
    }

    private List<int[][]> convertToMapMatrix() {
        List<int[][]> result = new LinkedList<>();
        int n = countSpawner();
        List<List<Integer>> allSpawner = getAllSpawner();
        for (int i = 0; i < allSpawner.size(); ++i) {

            int iSpawner = allSpawner.get(i).get(0);
            int jSpawner = allSpawner.get(i).get(1);
            int roadType = 0;

            for (int m = 0; m < BFS.dx.length; ++m) {
                if (isInBound(iSpawner + BFS.dy[m], jSpawner + BFS.dx[m])) {
                    if (isRoad(iSpawner + BFS.dy[m], jSpawner + BFS.dx[m])) {
                        roadType = getTypeRoad(mapData[iSpawner + BFS.dy[m]][jSpawner + BFS.dx[m]]);
                        break;
                    }
                }
            }

            int[][] MapOfASpawner = new int[HEIGHT][WIDTH];

            for (int j = 0; j < MapOfASpawner.length; ++j) {
                for (int k = 0; k < MapOfASpawner[0].length; ++k) {
                    if (j == iSpawner && k == jSpawner) MapOfASpawner[j][k] = BFS.START;
                    else if (isTarget(j, k)) {
                        if (isTargetOfSpawner(j, k, iSpawner, jSpawner)) MapOfASpawner[j][k] = BFS.END;
                        else MapOfASpawner[j][k] = BFS.ROAD;
                    }
                    else if (isRoad(j, k)) {
                        if (isRoadOfType(roadType, mapData[j][k])) MapOfASpawner[j][k] = BFS.ROAD;
                        else MapOfASpawner[j][k] = 1;
                        if (isConjunction(j, k)) MapOfASpawner[j][k] = BFS.ROAD;
                    }
                    else MapOfASpawner[j][k] = 1;
                }
            }
            result.add(MapOfASpawner);
        }
        return result;
    }

    public boolean isConjunction(int i, int j) {
        if (!isInBound(i, j)) return false;
        for (int k = 0; k < conjunctionID.length; ++k) {
            if (conjunctionID[k] == mapData[i][j] || (conjunctionID[k] + 2 * 69) == mapData[i][j] ||
                    (conjunctionID[k] + 3 * 69) == mapData[i][j]) return true;
        }
        return false;
    }

    public boolean isRoadOfType(int type, int value) {
        return type == getTypeRoad(value);
    }

    private boolean isInBound(int i, int j) {
        return i >= 0 && i < HEIGHT &&
                j >= 0 && j < WIDTH;
    }

    public int getTypeRoad(int value) {
        if (value <= 60) return 0;
        if (value <= 60 + 69 * 2) return 2;
        else if (value <= 60 + 69 * 3) return 3;
        return -1;
    }

    public int countSpawner() {
        int count = 0;
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                if (isSpawner(i, j)) ++count;
            }
        }
        return count;
    }

    public boolean isSpawner(int i, int j) {
        if (!isInBound(i, j)) return false;
        for (int k = 0; k < spawnerID.length; ++k) {
            if (mapData[i][j] == spawnerID[k]) return true;
        }
        return false;
    }

    public boolean isTarget(int i, int j) {
        if (!isInBound(i, j)) return false;
        for (int k = 0; k < targetID.length; ++k) {
            if (mapData[i][j] == targetID[k]) return true;
        }
        return false;
    }

    public boolean isTree(int i, int j) {
        if (!isInBound(i, j)) return false;
        for (int k = 0; k < treeID.length; ++k) {
            if (mapLayer[i][j] == treeID[k]) return true;
        }
        return false;
    }

    public boolean isRock(int i, int j) {
        if (!isInBound(i, j)) return false;
        for (int k = 0; k < rockID.length; ++k) {
            if (mapLayer[i][k] == rockID[k]) return true;
        }
        return false;
    }

    public boolean isOther(int i, int j) {
        if (!isInBound(i, j)) return false;
        for (int k = 0; k < otherID.length; ++k) {
            if (mapLayer[i][j] == otherID[k] ) return true;
        }
        return false;
    }

    public boolean isTargetOfSpawner(int iTarget, int jTarget, int iSpawner, int jSpawner) {
        return getTargetID(iTarget, jTarget) == getSpawnerID(iSpawner, jSpawner);
    }

    public int getSpawnerID(int i, int j) {

        for (int k = 0; k < spawnerID.length; ++k) {
            if (mapData[i][j] == spawnerID[k]) return k;
        }
        return -1;
    }

    public int getTargetID(int i, int j) {
        for (int k = 0; k < targetID.length; ++k) {
            if (mapData[i][j] == targetID[k]) return k;
        }
        return -1;
    }

    public boolean isRoad(int i, int j) {
        if (!isInBound(i, j)) return false;
        for (int k = 0; k < roadID.length; ++k) {
            if (mapData[i][j] == roadID[k] || mapData[i][j] == (roadID[k] + 2 * 69) ||
                    mapData[i][j] == (roadID[k] + 3 * 69)) return true;
        }
        return false;
    }

    public void printMapInfo() {
        System.out.println("---------- Map data -------------------");
        System.out.println("width: " + WIDTH + ", height: " + HEIGHT);

        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                System.out.print(mapData[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printLayerInfo() {
        System.out.println("---------- Layer -----------------------");
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                System.out.print(mapLayer[i][j] + " ");
            }
            System.out.println();
        }
    }

    private List<List<Integer>> getAllSpawner() {
        List<List<Integer>> res = new LinkedList<>();

        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                if (isSpawner(i, j)) {
                    List<Integer> coordinate = new LinkedList<>();
                    coordinate.add(i); coordinate.add(j);
                    res.add(coordinate);
                }
            }
        }
        return res;
    }

}
