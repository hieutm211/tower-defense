package com.ligrim.tower_defense;

import android.view.MotionEvent;
import android.view.View;

public class TouchEventListener implements View.OnTouchListener {

    private float primStartTouchX = -1;
    private float primStartTouchY = -1;
    private float screenX = GameGraphic.getScreenX();
    private float screenY = GameGraphic.getScreenY();
    private float moveDx = 0;
    private float moveDy = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x, y;

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                primStartTouchX = event.getX();
                primStartTouchY = event.getY();
                break;

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

                break;
        }

        return true;
    }

}
