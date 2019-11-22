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
        OptionPane.init(unitWidth, unitHeight);
        SettingPane.init();
    }

    public static String getButtonId(float x, float y) {
        buttonList.clear();
        if (SettingPane.isActive()) {
            buttonList.addAll(SettingPane.getButtonList());
        } else {
            buttonList.addAll(OptionPane.getButtonList());
        }

        for (GameButton button: buttonList) {
            if (button.containsPoint(x, y)) {
                return button.getId();
            }
        }
        return null;
    }

    public static String getTowerId(float x, float y) {
        if (SettingPane.isActive()) return null;

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
        OptionPane.draw(canvas);

        if (SettingPane.isActive()) {
            SettingPane.draw(canvas);
        }
    }

}

class TowerPane {
    private static int margin = 2;
    private static List<ImageButton> buttonList = new ArrayList<>();
    private static Rect background;

    public static void setTowerAvailableList(List<Tower> towerAvailableList, int width, int height) {
        int renderX = margin;
        int renderY = GameGraphic.getScreenHeightPixels() - margin - height;

        for (Tower tower: towerAvailableList) {
            ImageButton button = new ImageButton(tower.getId(), renderX, renderY, width, height);
            buttonList.add(button);
            renderX += width + margin;
        }

        background = new Rect(0, renderY, renderX, renderY + height + margin);
    }

    public static List<ImageButton> getButtonList() {
        return buttonList;
    }

    public static void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(background, paint);
        for (ImageButton button: buttonList) {
            button.draw(canvas);
        }
    }
}

class InformationPane {
    private static int margin = 2;
    private static GameField gameField;
    private static List<ImageButton> buttonList = new ArrayList<>();
    private static int unitWidth;
    private static int unitHeight;

    public static void setGameField(GameField gameField, int width, int height) {
        InformationPane.gameField = gameField;
        unitWidth = width;
        unitHeight = height;
    }

    public static List<ImageButton> getButtonList() {
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

class OptionPane {
    private static int margin = 10;
    private static List<ImageButton> buttonList = new ArrayList<>();
    private static final int numButton = 2;

    public static void init(int width, int height) {
        int renderX = GameGraphic.getScreenWidthPixels() - numButton * (width + margin);
        int renderY = margin;

        ImageButton button;

        //pause button
        button = new ImageButton("pause", renderX, renderY, width, height);
        buttonList.add(button);
        renderX += width + margin;

        //settings button
        button = new ImageButton("settings", renderX, renderY, width, height);
        buttonList.add(button);
        renderX += width + margin;
    }

    public static List<ImageButton> getButtonList() {
        return buttonList;
    }

    public static void draw(Canvas canvas) {
        for (ImageButton button: buttonList) {
            button.draw(canvas);
        }
    }
}

class SettingPane {
    private static boolean active = false;
    private static int margin = 150;
    private static List<TextButton> buttonList = new ArrayList<>();
    private static Rect background;
    private static int width = 500;
    private static int buttonWidth = 400;
    private static int buttonHeight = 80;
    private static float textSize = 64f;

    public static void init() {
        active = false;

        int x = GameGraphic.getCenterScreenX() - width/2;
        int y = 0;
        int u = GameGraphic.getCenterScreenX() + width/2;
        int v = GameGraphic.getScreenHeightPixels();

        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        background = new Rect(x, y, u, v);

        x = GameGraphic.getCenterScreenX() - buttonWidth/2;
        y = buttonHeight;

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAlpha(200);
        textPaint.setTextSize(50f);

        TextButton button;

        button = new TextButton("back_to_game", x, y, buttonWidth, buttonHeight);
        button.setText("Back to game");
        button.setTextPaint(textPaint);
        button.setBackgroundPaint(backgroundPaint);
        buttonList.add(button);

        y += margin;

        button = new TextButton("restart", x, y, buttonWidth, buttonHeight);
        button.setText("Restart");
        button.setTextPaint(textPaint);
        button.setBackgroundPaint(backgroundPaint);
        buttonList.add(button);

        y += margin;

        button = new TextButton("toggle_mute", x, y, buttonWidth, buttonHeight);
        button.setText("Mute / Unmute");
        button.setTextPaint(textPaint);
        button.setBackgroundPaint(backgroundPaint);
        buttonList.add(button);

        y = GameGraphic.getScreenHeightPixels() - margin;

        button = new TextButton("exit", x, y, buttonWidth, buttonHeight);
        button.setText("Exit game");
        button.setTextPaint(textPaint);
        button.setBackgroundPaint(backgroundPaint);
        buttonList.add(button);
    }

    public static void setActive(boolean _active) {
        active = _active;
    }

    public static boolean isActive() {
        return active;
    }

    public static List<TextButton> getButtonList() {
        return buttonList;
    }

    public static void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setAlpha(180);

        canvas.drawRect(background, paint);
        for (TextButton button: buttonList) {
            button.draw(canvas);
        }
    }
}