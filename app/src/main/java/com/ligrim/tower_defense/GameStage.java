package com.ligrim.tower_defense;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.text.LoginFilter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class GameStage {

    // this class is for encapsulating type, amount and order of enemy to be generated
    public static class Round {
        private final int roundNumber;
        private List<EnemyType> enemy;
        private List<Integer> amount;
        private int enemyID;
        private int countEnemy;

        public Enemy nextEnemy() {
            if (enemyID > enemy.size()) return null;

            if (countEnemy >= amount.get(enemyID)) {
                countEnemy = 0;
                enemyID++;
            }
            ++countEnemy;
            return enemyFactory(enemy.get(enemyID));
        }

        public int getRoundNumber() {
            return roundNumber;
        }

        // unchecked
        public boolean hasNext() {
            return enemyID < enemy.size() - 1 || (enemyID == enemy.size() - 1 && countEnemy < amount.get(amount.size() - 1));
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
        }

        private String EnemyBatch(int i) {
            return EnemyType.toString(enemy.get(i)) + ": " + amount.get(i);
        }

        // this method has not been completed yet
        // TODO: set return enemy reference corresponding to enemy type
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

    enum EnemyType {
        NONE,
        TANKER_ENEMY,
        NORMAL_ENEMY,
        SMALLER_ENEMY,
        BOSS_ENEMY;

        public static String toString(EnemyType e) {
            switch (e) {
                case NONE:
                    return "none";
                case TANKER_ENEMY:
                    return "tanker";
                case NORMAL_ENEMY:
                    return "normal";
                case SMALLER_ENEMY:
                    return "small";
                case BOSS_ENEMY:
                    return "boss";
            }
            return "";
        }
    }

    private static String NEWLINE = Character.toString((char) 10);
    private static int[] roadID = {0, 1, 2, 5, 6, 7, 10, 11, 12, 23, 25, 28, 30, 33, 35, 46, 47, 48, 51, 52, 53, 56, 57, 58};

    public  int UNIT_WIDTH;
    public  int UNIT_HEIGHT;
    public  int WIDTH;
    public  int HEIGHT;
    public  int INITIAL_GOLD;
    private int[][] mapData;
    private Bitmap demoImg;
    // list of all rounds in the game
    private List<Round> roundList;
    private int currentRound;
    // route here
    private List<Position> route;

    public GameStage(String mapFile, String EnemyFile, String RouteFile) {
        roundList = new ArrayList<>();
        currentRound = 0;
        route = new ArrayList<>();

        readMapData(mapFile);
        readEnemyInfo(EnemyFile, RouteFile);

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
        return !(isRoad(topLeftX/UNIT_WIDTH, topLeftY/UNIT_HEIGHT) &&
                isRoad(topLeftX/UNIT_WIDTH, downMostY/UNIT_HEIGHT) &&
                isRoad(rightMostX/UNIT_WIDTH, topLeftY/UNIT_HEIGHT) &&
                isRoad(rightMostX/UNIT_WIDTH, downMostY/UNIT_HEIGHT));
    }

    // this method is for junping immediately to a specific round
    public Round getRound(int i) {
        return roundList.get(i);
    }

    // read map data from txm file, do not modify
    private void readMapData(String filename) {
        String folder = filename;
        try {
            File inputFile = new File(folder);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("map");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);


                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    // read by attribute

                    WIDTH = Integer.parseInt(eElement.getAttribute("width"));


                    HEIGHT = Integer.parseInt(eElement.getAttribute("height"));


                    mapData = new int[HEIGHT][WIDTH];

                    UNIT_HEIGHT = Integer.parseInt(eElement.getAttribute("tileheight"));
                    UNIT_WIDTH = Integer.parseInt(eElement.getAttribute("tilewidth"));


                    String buffer = eElement
                            .getElementsByTagName("data")
                            .item(0)
                            .getTextContent();

                    // convert from String input to integer array


                    String[] splitLine = buffer.split(NEWLINE);
                    String[][] splitString = new String[HEIGHT][];


                    for (int i = 1; i < splitLine.length; ++i) {
                        splitString[i - 1] = splitLine[i].split(",");
                    }

                    for (int j = 0; j < HEIGHT; ++j) {
                        for (int k = 0; k < WIDTH; ++k) {
                            mapData[j][k] = Integer.parseInt(splitString[j][k]);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // read enemy Information, i.e the order enemy appear
    // initial all rounds in the game
    private void readEnemyInfo(String Enemy_info, String Route_info) {
        try {
            // declare file directory here
            File EnemyFile = new File(Enemy_info);
            BufferedReader Enemy = new BufferedReader(new FileReader(EnemyFile));
            File RouteFile = new File(Route_info);
            BufferedReader Route = new BufferedReader(new FileReader(RouteFile));

            // read enemy info
            String st;
            int roundNumber = 0;
            while ((st = Enemy.readLine()) != null) { // read each line from enemy_Info file
                // initial round according to round number
                Round round = new Round(roundNumber++);

                // split each line by comma sign
                String[] splitComma = st.split(",");

                for (int i = 0; i < splitComma.length; ++i) {
                    // split data into two fields, enemy type and amount
                    String[] cell = splitComma[i].split("-");
                    // here are these two type to be processed
                    String c1 = cell[0], c2 = cell[1];
                    // handling according to its type
                    switch (c1) {
                        case "A":
                            round.add(EnemyType.SMALLER_ENEMY, Integer.parseInt(c2));
                            break;
                        case "B":
                            round.add(EnemyType.NORMAL_ENEMY, Integer.parseInt(c2));
                            break;
                        case "C":
                            round.add(EnemyType.TANKER_ENEMY, Integer.parseInt(c2));
                            break;
                        case "D":
                            round.add(EnemyType.BOSS_ENEMY, Integer.parseInt(c2));
                            break;
                        case "N":
                            round.add(EnemyType.NONE, Integer.parseInt(c2));
                            break;
                    }
                }

                // add round to rounds list
                roundList.add(round);

                // read route info
                while ((st = Route.readLine()) != null) {
                    String[] splitColon = st.split(";");

                    for (int i = 0; i < splitColon.length; ++i) {
                        String[] coordinate = splitColon[i].split(",");
                        Position pos = new Position(Float.parseFloat(coordinate[0]), Float.parseFloat(coordinate[1]));
                        route.add(pos);
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isRoad(int i, int j) {
        if (i >= mapData.length || j > mapData[0].length) return false;
        for (int k = 0; k < roadID.length; ++k) {
            if (mapData[i][j] == roadID[k]) return true;
        }
        return false;
    }

    private void printMapInfo() {
        System.out.println("width: " + WIDTH + ", height: " + HEIGHT);

        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                System.out.print(mapData[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void printRoundInfo() {
        for (int i = 0; i < roundList.size(); ++i) {
            Round round = roundList.get(i);
            System.out.println(NEWLINE + "round " + i);
            System.out.print("\t");
            for (int j = 0; j < round.enemy.size(); ++j) System.out.print(round.EnemyBatch(j) + ", ");
        }
        System.out.println();
    }

    private void printRouteInfo() {
        System.out.println("route info:");
        System.out.print("\t");
        for (int i = 0; i < route.size(); ++i) {
            System.out.print("(" + route.get(i).getX() + ", " + route.get(i).getY() + ") ->");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        String folder1 = "app/map/Map1/sample_map1.tmx";
        String folder2 = "app/map/Map1/enemyInfo.txt";
        String folder3 = "app/map/Map1/routeInfo.txt";
        GameStage game = new GameStage(folder1, folder2, folder3);
        game.printMapInfo();
        game.printRoundInfo();
        System.out.println("total round: " + game.totalRound());
        game.printRouteInfo();

    }
}
