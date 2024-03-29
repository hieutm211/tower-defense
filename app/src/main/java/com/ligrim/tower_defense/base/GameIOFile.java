package com.ligrim.tower_defense.base;

import android.content.Context;
import android.util.Log;

import com.ligrim.tower_defense.enemy.EnemyType;
import com.ligrim.tower_defense.Round;
import com.ligrim.tower_defense.tower.MachineGunTower;
import com.ligrim.tower_defense.tower.NormalTower;
import com.ligrim.tower_defense.tower.SniperTower;
import com.ligrim.tower_defense.tower.Tower;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class GameIOFile {

    private static final String NEWLINE = Character.toString((char) 10);

    private GameIOFile() {}

    public static final void saveToFile(List<Tower> towerList, int currentRound, int health, int gold, String toFile, Context context) {
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

            Attr healthAtr = document.createAttribute("health");
            healthAtr.setValue(Integer.toString(health));
            root.setAttributeNode(healthAtr);

            Attr goldAtr = document.createAttribute("gold");
            goldAtr.setValue(Integer.toString(gold));
            root.setAttributeNode(goldAtr);

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

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            transformer.transform(new DOMSource(document), result);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(toFile, Context.MODE_PRIVATE));
            outputStreamWriter.write(writer.toString());
            outputStreamWriter.close();

            System.out.println("Done creating XML File");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map initMapData(InputStream filename) {
        InputStream inputFile = filename;
        Map map = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Element Tagmap = (Element) doc.getElementsByTagName("map").item(0);

            // read width and height of the Map
            int WIDTH = Integer.parseInt(Tagmap.getAttribute("width"));
            int HEIGHT = Integer.parseInt(Tagmap.getAttribute("height"));
            int UNIT_WIDTH = Integer.parseInt(Tagmap.getAttribute("tilewidth"));
            int UNIT_HEIGHT = Integer.parseInt(Tagmap.getAttribute("tileheight"));

            map = new Map(WIDTH, HEIGHT, UNIT_WIDTH, UNIT_HEIGHT);

            NodeList TagLayer = doc.getElementsByTagName("layer");
            for (int temp = 0; temp < TagLayer.getLength(); temp++)
            {
                // read tiled Map
                if (temp == 0) {
                    Node nNode = TagLayer.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
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
                                map.mapData[j][k] = Integer.parseInt(splitString[j][k]) - 1;
                            }
                        }
                    }
                }
                // read terrain
                if (temp == 1) {
                    Node nNode = TagLayer.item(temp);


                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
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
                                map.mapLayer[j][k] = Integer.parseInt(splitString[j][k]) - 1;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (map == null) System.out.println("map is null");
        return map;
    }

    public static bufferedSavedGame readSaveFile(Context in, String directory) {
        bufferedSavedGame buffer = new bufferedSavedGame();
        try
        {
            String xmlStr = readFromFile(in, directory);
            Document doc = convertStringToDocument(xmlStr);
            doc.getDocumentElement().normalize();

            NodeList round = doc.getElementsByTagName("round");
            Node roundNode = round.item(0);


            buffer.currentRound = Integer.parseInt( ((Element)roundNode).getAttribute("id") );
            buffer.gold = Integer.parseInt( ((Element)roundNode).getAttribute("gold") );
            buffer.health = Integer.parseInt( ((Element)roundNode).getAttribute("health") );

            NodeList nodeList = doc.getElementsByTagName("tower");
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                // System.out.println("\nNode Name :" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    String type = eElement.getAttribute("type");
                    String posX = eElement.getAttribute("PosX");
                    String posY = eElement.getAttribute("PosY");
                    String level = eElement.getAttribute("level");

                    Position pos = new Position(Float.parseFloat(posX), Float.parseFloat(posY));

                    switch (type) {
                        case "tower_normal":
                            buffer.towerList.add( new NormalTower(pos).jumpToLevel(Integer.parseInt(level)) );
                            break;

                        case "tower_sniper":
                            buffer.towerList.add( new SniperTower(pos).jumpToLevel(Integer.parseInt(level)) );
                            break;

                        case "tower_machine_gun":
                            buffer.towerList.add( new MachineGunTower(pos).jumpToLevel(Integer.parseInt(level)) );
                            break;

                        default:
                            System.out.println("unknown tower type: " + type);
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("no saved file!");
        }
        return buffer;
    }

    // read enemy Information, i.e the order enemy appear
    // initial all rounds in the game
    public static List<Round> createRoundList(InputStream Enemy_info, List<Route> route) {

        List<Round> roundList = new ArrayList<>();

        try {
            // declare file directory here
            InputStream EnemyFile = Enemy_info;
            BufferedReader Enemy = new BufferedReader(new InputStreamReader(EnemyFile, "UTF-8"));

            String st;

            // read enemy info
            int roundNumber = 0;
            while ((st = Enemy.readLine()) != null) { // read each line from enemy_Info file
                // initial round according to round number
                Round round = new Round(roundNumber++, route);

                // split each line by comma sign
                String[] splitComma = st.split(",");

                for (int i = 0; i < splitComma.length; ++i) {
                    // split data into two fields, enemy type and amount
                    String[] cell = splitComma[i].split("-");
                    // here are these two type to be processed
                    String c1 = cell[0];

                    // split input strings and initial basic variables of game's Map
                    String[] gc3 = cell[1].split("_");
                    String[] parseRoute = gc3[1].split(";");
                    String[] parseEnemyType = c1.split(";");

                    List<Integer> enemyInRoute = new ArrayList<>();
                    for (int k = 0; k < parseRoute.length; ++k ) {
                        enemyInRoute.add(Integer.parseInt(parseRoute[k]));
                    }
                    int amount = Integer.parseInt(gc3[0]);

                    // handling according to its type
                    List<EnemyType> enemyTypes = new ArrayList<>();
                    for (String typeOfEachEnemy : parseEnemyType) {
                        switch (typeOfEachEnemy) {
                            case "A":
                                enemyTypes.add(EnemyType.SMALLER_ENEMY);
                                break;
                            case "B":
                                enemyTypes.add(EnemyType.NORMAL_ENEMY);
                                break;
                            case "C":
                                enemyTypes.add(EnemyType.TANKER_ENEMY);
                                break;
                            case "D":
                                enemyTypes.add(EnemyType.BOSS_ENEMY);
                                break;
                            case "N":
                                enemyTypes.add(EnemyType.NONE);
                                break;
                            case "P":
                                enemyTypes.add(EnemyType.PLANE_ENEMY);
                                break;
                            case "S":
                                enemyTypes.add(EnemyType.SUPER_PLANE_ENEMY);
                                break;

                            default:
                                System.out.println("unsupported character in loading enemy info: " + c1);
                        }
                    }
                    round.add(enemyTypes, amount, enemyInRoute);
                }
                // add round to rounds list
                roundList.add(round);
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return roundList;
    }

    /***************************************************************************
     * Helper functions.
     ***************************************************************************/

    private static String readFromFile(Context context, String directory) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(directory);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
            return doc;
        } catch (Exception e) {
            System.out.println("something goes wrong in converting string to document");
        }
        return null;
    }


}
