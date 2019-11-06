package com.ligrim.tower_defense;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;

import java.util.List;

public class GameField extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;

    private GameStage stage;
    private List<Enemy> enemyList; // enemy da duoc tao ra va con song
    private List<Tower> towerList; // tower da duoc xay dung.
    private List<GameTile> tileList;
    private List<Bullet> bulletList; // nhung vien dan dang bay
    private int currentRound; // chi so cua round trong GameStage.RoundList;
    private int gold;
    private double gameTick;

    public GameField(Context context, GameStage gameStage) {
        //android code here
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);

        //initiate gameField using gameStage here
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.rgb(255, 176, 242));
        }
    }

    //update game state here
    public void update() {
    }
}
