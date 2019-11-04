package com.ligrim.tower_defense;

import java.util.List;

public class GameField {

    private GameStage stage;
    private List<Enemy> enemyList; // enemy da duoc tao ra va con song
    private List<Tower> towerList; // tower da duoc xay dung.
    private List<GameTile> tileList;
    private List<Bullet> bulletList; // nhung vien dan dang bay
    private int currentRound; // chi so cua round trong GameStage.RoundList;
    private int gold;
    private double gameTick;

    public GameField(GameStage gameStage) {}

}
