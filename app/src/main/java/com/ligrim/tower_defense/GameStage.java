package com.ligrim.tower_defense;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.LoginFilter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.awt.image.ImageWatched;
import sun.awt.image.IntegerComponentRaster;
import sun.security.ssl.HandshakeInStream;

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

    private static String NEWLINE = Character.toString((char) 10);
    private static final int[] roadID = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 23, 25,
            26, 27, 28, 30, 31, 32, 33, 35, 36, 37, 46, 47, 48, 50, 51, 52, 53, 55, 56, 57, 58, 60};
    private static final int[] spawnerID = {118, 123, 128};
    private static final int[] targetID = {49, 54, 59};
    private static final int[] conjuction = {0, 2, 5, 7, 10, 12, 46, 48, 51, 53, 56, 58};
    private static final int[] treeID = {130, 131, 132, 133, 134};
    private static final int[] rockID = {135, 136, 137};
    private static final int[] otherID = {19, 20, 21};

    public static final int INITIAL_GOLD = 100;
    public static int UNIT_WIDTH;
    public static int UNIT_HEIGHT;
    public static int WIDTH;
    public static int HEIGHT;
    private int[][] mapData;
    private int[][] mapLayer;
    private int currentRound;
    private List<Round> roundList; // list of all rounds in the game
    private List<List<Position>> route; // route here
    private List<List<GameTile>> tileList;
    private Bitmap demoImg;

    // this class is for wrapping type, amount and order of enemies to be generated
    private static class Round {
        private final int roundNumber;
        private List<EnemyType> enemy;
        private List<Integer> amount;
        private List<List<Integer>> enemyInRoute;
        private int enemyID;
        private int countEnemy;
        private List<List<Position>> route;

        public Enemy nextEnemy() {
            if (enemyID > enemy.size()) return null;

            if (countEnemy >= amount.get(enemyID)) {
                countEnemy = 0;
                enemyID++;
            }
            ++countEnemy;
            return enemyFactory(enemy.get(enemyID), enemyInRoute.get(enemyID).get(countEnemy % enemyInRoute.get(enemyID).size()));
        }

        public int getRoundNumber() {
            return roundNumber;
        }

        // unchecked
        public boolean hasNext() {
            return enemyID < enemy.size() - 1 || (enemyID == enemy.size() - 1 && countEnemy < amount.get(amount.size() - 1));
        }

        public void add(EnemyType e, int amount, List<Integer> enemyInRoute) {
            enemy.add(e);
            this.amount.add(amount);
            this.enemyInRoute.add(enemyInRoute);
        }

        public Round(int round, List<List<Position>> route) {
            roundNumber = round;
            enemyID = 0;
            countEnemy = 0;
            enemy = new ArrayList<>();
            amount = new ArrayList<>();
            enemyInRoute = new ArrayList<>();
            this.route = route;
        }

        private String EnemyBatch(int i) {
            return EnemyType.toString(enemy.get(i)) + ": " + amount.get(i);
        }

        private Enemy enemyFactory(EnemyType e, int r) {
            if (r >= this.route.size()) return null;
            switch (e) {
                case TANKER_ENEMY:
                    return new TankerEnemy(route.get(r));

                case NORMAL_ENEMY:
                    return new NormalEnemy(route.get(r));

                case SMALLER_ENEMY:
                    return new SmallerEnemy(route.get(r));

                case BOSS_ENEMY:
                    return new BossEnemy(route.get(r));

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

    public GameStage(InputStream mapFile, InputStream EnemyFile, InputStream saveFile) {
        roundList = new ArrayList<>();
        currentRound = 0;
        route = new ArrayList<>();

        readMapData(mapFile);
        readEnemyInfo(EnemyFile, saveFile);
        initTileList();
        //convertToPositionListOfRoute(convertToMapMatrix());
    }

    public List<Position> getRoute (int i) {
        return route.get(i);
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

    public List<Tower> readSaveFile(InputStream in) {
        List<Tower> list = new ArrayList<>();
        try
        {
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(in);
            doc.getDocumentElement().normalize();

            NodeList round = doc.getElementsByTagName("round");
            Node roundNode = round.item(0);
            currentRound = Integer.parseInt( ((Element)roundNode).getAttribute("id") );


            NodeList nodeList = doc.getElementsByTagName("tower");
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                System.out.println("\nNode Name :" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    String type = eElement.getAttribute("type");
                    String posX = eElement.getAttribute("PosX");
                    String posY = eElement.getAttribute("PosY");

                    System.out.println("tower id: "+ eElement.getAttribute("id"));
                    System.out.println("tower type: "+ type);
                    System.out.println("tower posX: "+ posX);
                    System.out.println("tower posY: "+ posY);

                    Position pos = new Position(Float.parseFloat(posX), Float.parseFloat(posY));

                    switch (type) {
                        case "normal":
                            list.add(new NormalTower(pos) );
                            break;
                        case "sniper":
                            list.add(new SniperTower(pos));
                            break;
                        case "machine_gun":
                            list.add(new MachineGunTower(pos));
                            break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("no saved file!");
        }
        return list;
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

                // add information about x pos and y pos
                // set an attribute pos X to element
                Attr posX = document.createAttribute("PosX");
                posX.setValue(Double.toString(towerList.get(i).getPosition().getX()));
                tower.setAttributeNode(posX);

                // set an attribute y pos to element
                Attr posY = document.createAttribute("PosY");
                posY.setValue( Double.toString(towerList.get(i).getPosition().getY()) );
                tower.setAttributeNode(posY);

                // set an attribute type to element
                Attr type = document.createAttribute("type");
                type.setValue( towerList.get(i).getId() );
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

    public void draw(Canvas canvas) {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                Bitmap bitmap = GameGraphic.getTileById(mapData[i][j]);
                canvas.drawBitmap(bitmap, j*UNIT_WIDTH, i*UNIT_HEIGHT, null);
            }
        }
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (mapLayer[i][j] > 0) {
                    Bitmap bitmap = GameGraphic.getTileById(mapLayer[i][j]);
                    canvas.drawBitmap(bitmap, j*UNIT_WIDTH, i*UNIT_HEIGHT, null);
                }
            }
        }
    }


    /***************************************************************************
     * Helper functions.
     ***************************************************************************/

    // read map data from txm file, do not modify
    private void readMapData(InputStream filename) {
        InputStream inputFile = filename;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            Element Tagmap = (Element) doc.getElementsByTagName("map").item(0);

            // read width and height of the map

            WIDTH = Integer.parseInt(Tagmap.getAttribute("width"));
            HEIGHT = Integer.parseInt(Tagmap.getAttribute("height"));
            UNIT_HEIGHT = Integer.parseInt(Tagmap.getAttribute("tileheight"));
            UNIT_WIDTH = Integer.parseInt(Tagmap.getAttribute("tilewidth"));


            NodeList TagLayer = doc.getElementsByTagName("layer");

            for (int temp = 0; temp < TagLayer.getLength(); temp++)
            {
                // read tiled map
                if (temp == 0) {
                    Node nNode = TagLayer.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        mapData = new int[HEIGHT][WIDTH];

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
                                mapData[j][k] = Integer.parseInt(splitString[j][k]) - 1;
                            }
                        }
                    }
                }
                // read terrain
                if (temp == 1) {
                    Node nNode = TagLayer.item(temp);


                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        mapLayer = new int[HEIGHT][WIDTH];

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
                                mapLayer[j][k] = Integer.parseInt(splitString[j][k]) - 1;
                            }
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
    private void readEnemyInfo(InputStream Enemy_info, InputStream saveFile) {
        try {
            // declare file directory here
            InputStream EnemyFile = Enemy_info;
            BufferedReader Enemy = new BufferedReader(new InputStreamReader(EnemyFile, "UTF-8"));
            InputStream RouteFile = saveFile;
            BufferedReader Route = new BufferedReader(new InputStreamReader(RouteFile, "UTF-8"));

            String st;
            // read route info

            convertToPositionListOfRoute(convertToMapMatrix());

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
                    String c1 = cell[0];

                    // split input strings and initial basic variables of game's map
                    String[] gc3 = cell[1].split("_");

                    String[] parseRoute = gc3[1].split(";");

                    List<Integer> enemyInRoute = new ArrayList<>();
                    for (int k = 0; k < parseRoute.length; ++k ) {
                        enemyInRoute.add(Integer.parseInt(parseRoute[k]));
                    }

                    int amount = Integer.parseInt(gc3[0]);

                    // handling according to its type
                    switch (c1) {
                        case "A":
                            round.add(EnemyType.SMALLER_ENEMY, amount, enemyInRoute);
                            break;
                        case "B":
                            round.add(EnemyType.NORMAL_ENEMY, amount, enemyInRoute);
                            break;
                        case "C":
                            round.add(EnemyType.TANKER_ENEMY, amount, enemyInRoute);
                            break;
                        case "D":
                            round.add(EnemyType.BOSS_ENEMY, amount, enemyInRoute);
                            break;
                        case "N":
                            round.add(EnemyType.NONE, amount, enemyInRoute);
                            break;
                        default:
                            System.out.println("unsupported character in loading enemy info: " + c1);
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

    private void convertToPositionListOfRoute(List<int[][]> matrix) {
        for (int i = 0; i < matrix.size(); ++i) {
            this.route.add(BFS.getRouteFromMatrix(matrix.get(i)));
        }
    }

    private void initTileList() {
        this.tileList = new LinkedList<>();

        LinkedList<GameTile> layer1 = new LinkedList<>();
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                float x = (float) j * UNIT_WIDTH, y = (float) i * UNIT_HEIGHT;
                layer1.add(new Mountain(Integer.toString(mapData[i][j]), new Position(x, y)));
            }
        }
        LinkedList<GameTile> layer2 = new LinkedList<>();
        for (int i  = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                if (mapLayer[i][j] > 0) {
                    float x = (float) j * UNIT_WIDTH, y = (float) i * UNIT_HEIGHT;
                    if (isOther(i, j) || isRock(i, j)) {
                        layer1.addLast(new Mountain(Integer.toString(mapLayer[i][j]), new Position(x, y)));
                    }
                    if (isTree(i, j)) {
                        layer2.add(new Mountain(Integer.toString(mapLayer[i][j]), new Position(x, y)));
                    }
                }
            }
        }

        tileList.add(layer1);
        tileList.add(layer2);
    }

    public List<List<GameTile>> getTileList() {
        return this.tileList;
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

    private boolean isConjunction(int i, int j) {
        for (int k = 0; k < conjuction.length; ++k) {
            if (conjuction[k] == mapData[i][j] || (conjuction[k] + 2 * 69) == mapData[i][j] ||
                    (conjuction[k] + 3 * 69) == mapData[i][j]) return true;
        }
        return false;
    }

    private boolean isRoadOfType(int type, int value) {
        return type == getTypeRoad(value);
    }

    private boolean isInBound(int i, int j) {
        return i >= 0 && i < mapData.length &&
                j >= 0 && j < mapData[0].length;
    }

    private int getTypeRoad(int value) {
        if (value <= 60) return 0;
        if (value <= 60 + 69 * 2) return 2;
        else if (value <= 60 + 69 * 3) return 3;
        return -1;
    }

    private int countSpawner() {
        int count = 0;
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                if (isSpawner(i, j)) ++count;
            }
        }
        return count;
    }

    private boolean isSpawner(int i, int j) {
        for (int k = 0; k < spawnerID.length; ++k) {
            if (mapData[i][j] == spawnerID[k]) return true;
        }
        return false;
    }

    private boolean isTarget(int i, int j) {
        for (int k = 0; k < targetID.length; ++k) {
            if (mapData[i][j] == targetID[k]) return true;
        }
        return false;
    }

    private boolean isTree(int i, int j) {
        for (int k = 0; k < treeID.length; ++k) {
            if (mapLayer[i][j] == treeID[k]) return true;
        }
        return false;
    }

    private boolean isRock(int i, int j) {
        for (int k = 0; k < roadID.length; ++k) {
            if (mapLayer[i][k] == rockID[k]) return true;
        }
        return false;
    }

    private boolean isOther(int i, int j) {
        for (int k = 0; k < otherID.length; ++k) {
            if (mapLayer[i][j] == otherID[k] ) return true;
        }
        return false;
    }

    private boolean isTargetOfSpawner(int iTarget, int jTarget, int iSpawner, int jSpawner) {
        return getTargetID(iTarget, jTarget) == getSpawnerID(iSpawner, jSpawner);
    }

    private int getSpawnerID(int i, int j) {
        for (int k = 0; k < spawnerID.length; ++k) {
            if (mapData[i][j] == spawnerID[k]) return k;
        }
        return -1;
    }

    private int getTargetID(int i, int j) {
        for (int k = 0; k < targetID.length; ++k) {
            if (mapData[i][j] == targetID[k]) return k;
        }
        return -1;
    }

    private boolean isRoad(int i, int j) {
        if (i >= mapData.length || j >= mapData[0].length) return false;
        for (int k = 0; k < roadID.length; ++k) {
            if (mapData[i][j] == roadID[k] || mapData[i][j] == (roadID[k] + 2 * 69) ||
                    mapData[i][j] == (roadID[k] + 3 * 69)) return true;
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

    private void printLayer() {
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                System.out.print(mapLayer[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        GameStage game = new GameStage(new FileInputStream("app/src/main/assets/map/map_2/sample_map2.tmx"),
                new FileInputStream("app/src/main/assets/map/map_2/enemy_info.txt"),
                new FileInputStream("app/src/main/assets/map/map_2/route_info.txt")  );
        List<Tower> towers = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            towers.add(new MachineGunTower(new Position(i * 20, i * 20)));
        }
        game.saveToFile(towers, 3, "app/src/main/assets/map/map_2/saveFile.xml");
        InputStream in = new FileInputStream("app/src/main/assets/map/map_2/saveFile.xml");
        game.readSaveFile(in);
        //game.printLayer();
        game.printMapInfo();
        for (int i = 0; i < game.route.size(); ++i) {
            System.out.println("route " + i);
            for (int j = 0; j < game.route.get(i).size(); ++j) {
                System.out.print( game.route.get(i).get(j).getX() + "," + game.route.get(i).get(j).getY() + "; ");
            }
        }
    }
}
