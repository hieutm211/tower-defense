package App;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class GameStage {

    public static class Round {
        private final int roundNumber;
        private List<EnemyType> enemy;
        private List<Integer> amount;
        private int enemyID;
        private int countEnemy;
        private List<Position> route;

        public List<Position> getRoute () {
            return route;
        }

        private void addPosition(Position p) {
            route.add(p);
        }

//      enemyId && amount has same size
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
            route = new ArrayList<>();
        }

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
        BOSS_ENEMY
    }


    private static String NEWLINE = Character.toString((char) 10);
    public  int UNIT_WIDTH;
    public  int UNIT_HEIGHT;
    public  int WIDTH;
    public  int HEIGHT;
    public  int INITIAL_GOLD;
    private int[][] mapData;

    private List<Round> roundList;

    public GameStage(String mapFile, String EnemyFile) {

        readMapData(mapFile);
        readEnemyInfo(EnemyFile);

    }

    private void readEnemyInfo(String folder) {
        try {
            File inputFile = new File(folder);


        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void printMapInfo() {
        System.out.println(WIDTH + " " + HEIGHT);

        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                System.out.print(mapData[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args ) {
        GameStage test = new GameStage(args[0], args[1]);
        test.printMapInfo();
    }
}
