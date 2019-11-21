package com.ligrim.tower_defense;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import com.ligrim.tower_defense.tower.Tower;

import java.util.ArrayList;
import java.util.List;

class GamePane {

    private static GameField gameField;
    private static int unitWidth = 100;
    private static int unitHeight = 100;
    private static List<GameButton> buttonList = new ArrayList<>();

    public static void setGameField(GameField _gameField) {
        gameField = _gameField;
        TowerPane.setTowerAvailableList(gameField.getAvailableTower(), unitWidth, unitHeight);
        InformationPane.setGameField(gameField, unitWidth/2, unitHeight/2);
        SettingPane.init(unitWidth, unitHeight);
    }

    public static String getButtonId(float x, float y) {
        for (GameButton button: SettingPane.getButtonList()) {
            if (button.containsPoint(x, y)) {
                return button.getId();
            }
        }
        return null;
    }

    public static String getTowerId(float x, float y) {
        for (GameButton button: TowerPane.getButtonList()) {
            if (button.containsPoint(x, y)) {
                return button.getId();
            }
        }
        return null;
    }

    public static void draw(Canvas canvas) {
        TowerPane.draw(canvas);
        InformationPane.draw(canvas);
        SettingPane.draw(canvas);
    }

}

class TowerPane {
    private static int margin = 2;
    private static List<GameButton> buttonList = new ArrayList<>();
    private static Rect background;

    public static void setTowerAvailableList(List<Tower> towerAvailableList, int width, int height) {
        int renderX = margin;
        int renderY = GameGraphic.getScreenHeightPixels() - margin - height;

        for (Tower tower: towerAvailableList) {
            GameButton button = new GameButton(tower.getId(), renderX, renderY, width, height);
            buttonList.add(button);
            renderX += width + margin;
        }

        background = new Rect(0, renderY, renderX, renderY + height + margin);
    }

    public static List<GameButton> getButtonList() {
        return buttonList;
    }

    public static void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(background, paint);
        for (GameButton button: buttonList) {
            button.draw(canvas);
        }
    }
}

class InformationPane {
    private static int margin = 2;
    private static GameField gameField;
    private static List<GameButton> buttonList = new ArrayList<>();
    private static int unitWidth;
    private static int unitHeight;

    public static void setGameField(GameField gameField, int width, int height) {
        InformationPane.gameField = gameField;
        unitWidth = width;
        unitHeight = height;
    }

    public static List<GameButton> getButtonList() {
        return buttonList;
    }

    public static void draw(Canvas canvas) {
        String wave = " Wave " + gameField.getCurrentRound() + "/" + gameField.getTotalRound();
        String health = "❤" + gameField.getHealth();
        String gold = "$" + gameField.getGold();

        int renderX = margin;
        int renderY = margin + unitHeight;

        //draw backround
        Paint paint = new Paint();

        paint.setColor(Color.DKGRAY);
        paint.setAlpha(200);
        canvas.drawRect(0, 0, renderX + 24*unitWidth/2f + margin, renderY + unitHeight/2f +margin, paint);

        //draw information
        TextPaint textPaint = new TextPaint();
        textPaint.reset();
        textPaint.setAlpha(200);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(unitWidth);

        canvas.drawText(wave, renderX, renderY, textPaint);
        renderX += 10 * unitWidth/2;
        canvas.drawText(health, renderX, renderY, textPaint);
        renderX += 8 * unitWidth/2;
        canvas.drawText(gold, renderX, renderY, textPaint);
    }
}

class SettingPane {
    private static int margin = 10;
    private static List<GameButton> buttonList = new ArrayList<>();
    private static final int numButton = 2;

    public static void init(int width, int height) {
        int renderX = GameGraphic.getScreenWidthPixels() - numButton * (width + margin);
        int renderY = margin;

        GameButton button;

        //pause button
        button = new GameButton("pause", renderX, renderY, width, height);
        buttonList.add(button);
        renderX += width + margin;

        //settings button
        button = new GameButton("settings", renderX, renderY, width, height);
        buttonList.add(button);
        renderX += width + margin;
    }

    public static List<GameButton> getButtonList() {
        return buttonList;
    }

    public static void draw(Canvas canvas) {
        for (GameButton button: buttonList) {
            button.draw(canvas);
        }
    }
}