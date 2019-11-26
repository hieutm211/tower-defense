package com.ligrim.tower_defense;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;

import com.ligrim.tower_defense.base.Position;
import com.ligrim.tower_defense.tower.Tower;
import com.ligrim.tower_defense.tower.TowerFactory;

public class TouchEventListener implements View.OnTouchListener {

    private static final int STATUS_NONE = 0;
    private static final int STATUS_DRAG = 1;
    private static final int STATUS_TOWER_DRAG = 2;
    private static final int STATUS_BUTTON_CLICK = 3;
    private static final int STATUS_TOWER_CLICK = 4;

    private float primStartTouchX = -1;
    private float primStartTouchY = -1;
    private float screenX = GameGraphic.getScreenX();
    private float screenY = GameGraphic.getScreenY();
    private float moveDx = 0;
    private float moveDy = 0;
    private int currentStatus = STATUS_NONE;
    private String currentId;
    private String tempId;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (currentStatus) {
            case STATUS_TOWER_CLICK:
                return towerClickedEvent(v, event);

            case STATUS_DRAG:
                return dragEvent(v, event);

            case STATUS_TOWER_DRAG:
                return towerDragEvent(v, event);

            case STATUS_BUTTON_CLICK:
                return clickEvent(v, event);

            case STATUS_NONE:

                primStartTouchX = event.getX();
                primStartTouchY = event.getY();

                GameField gameField = (GameField) v;
                int towerIndex = gameField.getTower(primStartTouchX + GameGraphic.getScreenX(), primStartTouchY + GameGraphic.getScreenY());

                if (towerIndex != -1) { // click tower
                    currentStatus = STATUS_TOWER_CLICK;
                    currentId = Integer.toString(towerIndex);
                    tempId = currentId;
                    Tower tower = gameField.getTower(towerIndex);
                    TowerClickedPane.setTower(tower);
                    tower.setDisplayRange(true);

                } else if (GamePane.getTowerId(primStartTouchX, primStartTouchY) != null) { //click build tower
                    currentStatus = STATUS_TOWER_DRAG;
                    currentId = GamePane.getTowerId(primStartTouchX, primStartTouchY);

                } else if (GamePane.getButtonId(primStartTouchX, primStartTouchY) != null){ // click a button
                    currentStatus = STATUS_BUTTON_CLICK;
                    currentId = GamePane.getButtonId(primStartTouchX, primStartTouchY);
                } else { // click nothing
                    currentStatus = STATUS_DRAG;
                    currentId = null;
                }
        }

        return true;
    }

    public boolean towerDragEvent(View v, MotionEvent event) {
        GameField gameField = null;
        if (v instanceof GameField) {
            gameField = (GameField) v;
        }
        if (gameField == null) return true;

        int action = event.getAction();

        float x = event.getX() + GameGraphic.getScreenX();
        float y = event.getY() + GameGraphic.getScreenY();

        Position towerPosition = new Position(x, y);

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                Tower temporaryTower = TowerFactory.getInstance(currentId);
                temporaryTower.setPosition(x, y);
                gameField.setTemporaryTower(temporaryTower);

                break;
            case MotionEvent.ACTION_UP:
                if (gameField.canSetTower(currentId, towerPosition)) {
                    gameField.addTower(currentId, towerPosition);
                }

                gameField.setTemporaryTower(null);

                currentStatus = STATUS_NONE;
                break;
        }

        return true;
    }

    public boolean dragEvent(View v, MotionEvent event) {
        float x, y;

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                moveDx = x - primStartTouchX;
                moveDy = y - primStartTouchY;
                GameGraphic.setScreenX(screenX - moveDx);
                GameGraphic.setScreenY(screenY - moveDy);
                break;

            case MotionEvent.ACTION_UP:
                screenX = GameGraphic.getScreenX();
                screenY = GameGraphic.getScreenY();
                currentStatus = STATUS_NONE;
                break;
        }
        return true;
    }

    public boolean clickEvent(View v, MotionEvent event) {
        GameField gameField = (GameField) v;
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                String buttonUpId = GamePane.getButtonId(x, y);
                if (buttonUpId != null && buttonUpId.equals(currentId)) {
                    switch (currentId) {
                        case "pause":
                            gameField.requestTogglePause();
                            break;
                        case "settings":
                            SettingPane.setActive(true);
                            gameField.requestPause();
                            break;
                        case "back_to_game":
                            SettingPane.setActive(false);
                            gameField.requestUnpause();
                            break;
                        case "restart":
                            gameField.requestRestart();
                            SettingPane.setActive(false);
                            break;

                        case "toggle_mute":
                            GameSound.toggleMute();
                            break;

                        case "exit":
                            gameField.requestExit();
                            break;
                    }
                }
                currentStatus = STATUS_NONE;
                break;
        }

        return true;
    }
    public boolean towerClickedEvent(View v, MotionEvent event) {
        GameField gameField = (GameField) v;
        float x = event.getX() + GameGraphic.getScreenX();
        float y = event.getY() + GameGraphic.getScreenY();

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                gameField.getTower(Integer.parseInt(currentId)).setDisplayRange(false);

                tempId = GamePane.getButtonId(x, y);

                int towerIndex = Integer.parseInt(currentId);

                if (tempId != null) {
                    if (tempId.equals("upgrade") && gameField.canUpgradeTower(towerIndex)) {
                        gameField.setGold(gameField.getGold() - gameField.getTower(towerIndex).getUpgradePrice());
                        gameField.getTower(towerIndex).upgrade();
                    } else if (tempId.equals("sell")) {
                        gameField.requestSellTower(towerIndex);
                    }
                }

                if (!currentId.equals(tempId)) {
                    TowerClickedPane.setTower(null);
                    currentStatus = STATUS_NONE;
                }

                break;
        }
        return true;
    }

}

