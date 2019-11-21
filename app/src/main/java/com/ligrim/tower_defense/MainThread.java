package com.ligrim.tower_defense;

import android.graphics.Color;
import android.view.SurfaceHolder;
import android.graphics.Canvas;

public class MainThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private GameField gameField;
    private boolean running;
    private boolean pause;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GameField gameField) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameField = gameField;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        int targetFPS = 60;
        long targetTime = 1000 / targetFPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                GameGraphic.setCanvas(canvas);

                synchronized(surfaceHolder) {

                    if (!pause) {
                        this.gameField.update();
                    }

                    canvas.drawColor(Color.rgb(255, 193, 0));

                    this.gameField.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            } catch (Exception e) {}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == targetFPS)        {
                long averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }

    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    public void togglePause() {
        pause = !pause;
    }
}