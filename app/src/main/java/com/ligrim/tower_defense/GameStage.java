package com.ligrim.tower_defense;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.text.LoginFilter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class GameStage {

    // this class is for wrapping type, amount and order of enemy to be generated
    private static class Round {
        private final int roundNumber;
        private List<EnemyType> enemy;
        private List<Integer> amount;
        private int enemyID;
        private int countEnemy;
        private List<Position> route;

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

        public Round(int round, List<Position> route) {
            roundNumber = round;
            enemyID = 0;
            countEnemy = 0;
            enemy = new ArrayList<>();
            amount = new ArrayList<>();
            this.route = route;
        }

        private String EnemyBatch(int i) {
            return EnemyType.toString(enemy.get(i)) + ": " + amount.get(i);
        }

        // this method has not been completed yet
        // TODO: set return enemy reference corresponding to enemy type
        private Enemy enemyFactory(EnemyType e) {
            switch (e) {
                case TANKER_ENEMY:
                    return new TankerEnemy(route);

                case NORMAL_ENEMY:
                    return new NormalEnemy(route);

                case SMALLER_ENEMY:
                    return new SmallerEnemy(route);

                case BOSS_ENEMY:
                    return new BossEnemy(route);

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

    private int UNIT_WIDTH;
    private int UNIT_HEIGHT;
    private int WIDTH;
    private int HEIGHT;
    public final int INITIAL_GOLD = 100;
    private int[][] mapData;
    private Bitmap demoImg;
    // list of all rounds in the game
    private List<Round> roundList;
    private int currentRound;
    // route here
    private List<Position> route;

    public GameStage(InputStream mapFile, InputStream EnemyFile, InputStream RouteFile) {
        roundList = new ArrayList<>();
        currentRound = 0;
        route = new ArrayList<>();

        readMapData(mapFile);
        readEnemyInfo(EnemyFile, RouteFile);

    }

    public List<Position> getRoute () {
        return route;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getINITIAL_GOLD() {
        return INITIAL_GOLD;
    }

    public int getUNIT_HEIGHT() {
        return UNIT_HEIGHT;
    }

    public int getUNIT_WIDTH() {
        return UNIT_WIDTH;
    }

    public int getWIDTH() {
        return WIDTH;
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
    // return true if i < total round, false otherwise
    // set current round to ith round
    public boolean getRound(int i) {
        if (i < roundList.size()) {
            currentRound = i;
        }
        return false;
    }

    // read map data from txm file, do not modify
    private void readMapData(InputStream filename) {
        InputStream inputFile = filename;
        try {
            /*       File inputFile = new File(folder);*/
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("map");

            for (int temp = 0; temp < nList.getLength(); temp++)
            {
                //int temp = 0;
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
    private void readEnemyInfo(InputStream Enemy_info, InputStream Route_info) {
        try {
            // declare file directory here
            InputStream EnemyFile = Enemy_info;
            BufferedReader Enemy = new BufferedReader(new InputStreamReader(EnemyFile, "UTF-8"));
            InputStream RouteFile = Route_info;
            BufferedReader Route = new BufferedReader(new InputStreamReader(RouteFile, "UTF-8"));

            String st;
            // read route info
            while ((st = Route.readLine()) != null) {
                String[] splitColon = st.split(";");

                for (int i = 0; i < splitColon.length; ++i) {
                    String[] coordinate = splitColon[i].split(",");
                    Position pos = new Position(Float.parseFloat(coordinate[0]), Float.parseFloat(coordinate[1]));
                    this.route.add(pos);
                }
            }

            // read enemy info
            int roundNumber = 0;
            while ((st = Enemy.readLine()) != null) { // read each line from enemy_Info file
                // initial round according to round number
                Round round = new Round(roundNumber++, this.route);

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

            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(List<Tower> towerList, int currentRound, String toFile) {
        String xmlFilePath = toFile;
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("round");
            document.appendChild(root);

            // add round information here
            Attr roundId = document.createAttribute("id");
            roundId.setValue(Integer.toString(currentRound));
            root.setAttributeNode(roundId);

            // tower element
            for (int i = 0; i < towerList.size(); ++i) {
                Element tower = document.createElement("tower");

                root.appendChild(tower);

                // set an attribute id to element
                Attr id = document.createAttribute("id");
                id.setValue(Integer.toString(i));
                tower.setAttributeNode(id);


                // set an attribute level to element
                Attr level = document.createAttribute("level");
                level.setValue(Integer.toString(towerList.get(i).getLevel()));
                tower.setAttributeNode(level);

                //you can also use staff.setAttribute("id", "1") for this

                // add information about x pos and y pos
                // set an attribute level to element
                Attr posX = document.createAttribute("PosX");
                posX.setValue(Double.toString(towerList.get(i).getPosition().getX()));
                tower.setAttributeNode(posX);

                // set an attribute y pos to element
                Attr posY = document.createAttribute("PosY");
                posY.setValue( Double.toString(towerList.get(i).getPosition().getY()) );
                tower.setAttributeNode(posY);

                // set an attribute type to element
                Attr type = document.createAttribute("type");
                type.setValue( towerList.get(i).toString() );
                tower.setAttributeNode(type);
            }

            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    private boolean isRoad(int i, int j) {
        if (i >= mapData.length || j >= mapData[0].length) return false;
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

    /*public static void main(String[] args) {
        String folder1 = "app/map/Map1/sample_map1.tmx";
        String folder2 = "app/map/Map1/enemyInfo.txt";
        String folder3 = "app/map/Map1/routeInfo.txt";
        GameStage game = new GameStage(folder1, folder2, folder3);
        game.printMapInfo();
        game.printRoundInfo();
        System.out.println("total round: " + game.totalRound());
        game.printRouteInfo();
    }*/

    public static void main(String[] args) throws Exception {
        GameStage game = new GameStage(new FileInputStream("app/src/main/assets/map/map_1/sample_map1.tmx"),
                        new FileInputStream("app/src/main/assets/map/map_1/enemy_info.txt"),
                        new FileInputStream("app/src/main/assets/map/map_1/route_info.txt")  );
        List<Tower> towers = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            towers.add(new NormalTower(new Position(i * 20, i * 20)));
        }
        game.saveToFile(towers, 3, "app/src/main/assets/map/map_1/saveFile.xml");
    }
}
