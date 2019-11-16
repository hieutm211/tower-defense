package com.ligrim.tower_defense;

import java.net.MulticastSocket;
import java.util.*;

/*
 -1 -1 0 0
  0  0 0-1

 */

public class BFS {

    private static int[][] map;
    public static final int[] dx = {-1, 0, 0, 1};
    public static final int[] dy = {0, 1, -1, 0};
    public static final int ROAD = 0;
    public static final int START = -1;
    public static final int END = -2;
    private static final int curveDegree = 6; // the grater this value is, the smoothier the curves look, must be greater than 0
    private static Queue<Point> queue;

    private static class Point {
        public int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Point other) {
            return this.x == other.x && this.y == other.y;
        }
    }

    private BFS() {}

    private static void reset() {
        map = null;
        queue = null;
    }

    public static List<Position> getRouteFromMatrix(int[][] mapDat) {
        reset();
        queue = new LinkedList<>();
        Point start = new Point(0, 0);

        for (int i = 0; i < mapDat.length; ++i) {
            for (int j = 0; j < mapDat[0].length; ++j) {
                if (mapDat[i][j] == START) {
                    queue.add(new Point(j, i));
                    start = new Point(j, i);
                    break;
                }
            }
        }
        bfs(mapDat);
        // search for the nearest target
        Point nearest = findNearestTarget(mapDat);
        /*System.out.println(nearest.x + " " + nearest.y);
        print(mapDat);
        print(map);*/

        LinkedList<Position> result = new LinkedList<>();
        Stack<Point> stack = new Stack<>();
        while (map[nearest.y][nearest.x] != 0) {
            for (int i = 0; i < dx.length; ++i) {
                if (nearest.x + dx[i] >= 0 && nearest.x + dx[i] < mapDat[0].length &&
                        nearest.y + dy[i] >= 0 && nearest.y + dy[i] < mapDat.length &&
                        map[nearest.y + dy[i]][nearest.x + dx[i]] == map[nearest.y][nearest.x] - 1) {
                    stack.push(nearest);
                    nearest = new Point(nearest.x + dx[i], nearest.y + dy[i]);
                    break;
                }
            }
        }
        stack.push(nearest);
        while (!stack.empty()) {
            Point p = stack.pop();
            float x = (float) (p.x) * (float)GameStage.UNIT_WIDTH;
            float y = (float) (p.y) * (float)GameStage.UNIT_HEIGHT;
            result.add(new Position(x, y));
        }
        smoothCurve(result);
        return result;
    }

    private static Point findNearestTarget(int[][] mapDat) {
        Point nearest = new Point(0, 0) ;
        int min = -100;
        for (int i = 0; i < mapDat.length; ++i) {
            for (int j = 0; j < mapDat[0].length; ++j) {
                if (mapDat[i][j] == END && ( (min > map[i][j]) || min == -100 ) && map[i][j] >= 0 ) {
                    nearest = new Point(j, i);
                    min = map[i][j];
                }
            }
        }
        if (min == -100) throw new java.lang.ExceptionInInitializerError("there is no path from Spawner to Target");
        return nearest;
    }

    private static void bfs(int[][] mapDat) {
        boolean marked[][] = new boolean[mapDat.length][mapDat[0].length];
        map = new int[mapDat.length][mapDat[0].length];
        init();
        Point start = queue.peek();
        map[start.y][start.x] = 0;

        while (!queue.isEmpty()) {
            Point p = queue.peek();
            visit(mapDat, p.x, p.y, marked);
            queue.remove();
        }
    }

    private static void visit(int[][] mapDat, int x, int y, boolean[][] marked) {
        if (marked[y][x]) return;

        marked[y][x] = true;

        for (int i = 0; i < dx.length; ++i) {
            if (x + dx[i] >= 0 && x + dx[i] < mapDat[0].length &&
                    y + dy[i] >= 0 && y + dy[i] < mapDat.length &&
                    (mapDat[y + dy[i]][ x + dx[i]] == ROAD || mapDat[y + dy[i]][ x + dx[i]] == END) &&
                    !(marked[y + dy[i]][x + dx[i]]) &&
                    map[y + dy[i]][x + dx[i]] < map[y][x]) {
                queue.add(new Point(x + dx[i], y + dy[i]));
                map[y + dy[i]][x + dx[i]] = map[y][x] + 1;
            }
        }
    }

    public static void print(int[][] arr ) {
        System.out.println(arr.length + " " + arr[0].length);
        for (int i = 0; i < arr.length; ++i) {
            for (int j = 0; j < arr[0].length; ++j) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void smoothCurve(LinkedList<Position> list) {
        for (int i = 0; i + 2 < list.size(); ++i) {
            // find right angle
            if (Math.abs(Position.distanceSquared(list.get(i), list.get(i + 1)) +
                    Position.distanceSquared(list.get(i + 1), list.get(i + 2)) -
                    Position.distanceSquared(list.get(i), list.get(i + 2))) < .01f) {

                Position firstMid = Position.midPoint(list.get(i), list.get(i + 1));
                Position secondMid = Position.midPoint(list.get(i + 1), list.get(i + 2));
                Position origin = Position.midPoint(list.get(i), list.get(i + 2));

                boolean patch = true;
                if (Position.distance(firstMid, list.get(i + 1)) < GameStage.UNIT_HEIGHT * .325f ) {
                    firstMid = list.get(i);
                    patch = false;
                }

                Position firstVec = new Position(firstMid.getX() - origin.getX(), firstMid.getY() - origin.getY());
                Position secVec = new Position(secondMid.getX() - origin.getX(), secondMid.getY() - origin.getY());

                float firstAbsAngle = Position.getAbsoluteAngle(firstVec);

                float pi = (float) Math.PI;
                float diffAngle = Position.cross(firstVec, secVec) > 0f ? pi / 2 : - pi / 2;

                float unit_angle = diffAngle / curveDegree;
                /*System.out.println("diffangle: " + diffAngle / Math.PI * 180);*/

                list.remove(i++ + 1);
                for (int j = 0; j <= curveDegree; ++j) {
                    if (!patch && j == 0) continue;
                    double angle = (double) j * unit_angle + firstAbsAngle;
                    float x  = (float) Math.cos(angle) * GameStage.UNIT_WIDTH / 2, y = (float) Math.sin(angle) * GameStage.UNIT_HEIGHT / 2;
                    Position vectorRotate = new Position(x, y);
                    list.add(i++, Position.add(vectorRotate, origin));
                }
                i -= 2;
            }
        }
    }

    private static void init(){
        if (map == null) return;
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < map[0].length; ++j) {
                map[i][j] = -1;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int width = sc.nextInt(), height = sc.nextInt();
        int[][] map = new int[height][width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                map[i][j] = sc.nextInt();
            }
        }
        //print(map);
        //getRouteFromMatrix(map);
        //print(BFS.map);
        List<Position> p = getRouteFromMatrix(map);
        for (Position i : p) {
            System.out.print(i.getX() + " " + i.getY() + ", ");
        }

    }
}
