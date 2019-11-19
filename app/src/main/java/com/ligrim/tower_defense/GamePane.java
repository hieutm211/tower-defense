package com.ligrim.tower_defense;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

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
    }

    public static String getButtonId(float x, float y) {
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