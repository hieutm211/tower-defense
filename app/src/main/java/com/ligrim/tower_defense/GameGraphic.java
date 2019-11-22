package com.ligrim.tower_defense;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.HashMap;
import java.util.List;

public class GameGraphic {

    private static Canvas canvas;

    private static AssetManager assetManager;
    private static HashMap<String, Bitmap> map;

    private static int mapWidth;
    private static int mapHeight;
    private static int unitWidthPixels;
    private static int unitHeightPixels;
    private static int mapWidthPixels;
    private static int mapHeightPixels;
    private static int screenWidthPixels;
    private static int screenHeightPixels;

    private static float screenX = 0;
    private static float screenY = 0;

    static {
        screenWidthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeightPixels = Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static void setAssetManager(AssetManager asset) {
        assetManager = asset;
    }

    public static Bitmap getBitmapById(String id, int width, int height) {
        if (!map.containsKey(id)) {
            try {
                Bitmap originBitmap = BitmapFactory.decodeStream(assetManager.open("images/" + id + ".png"));
                map.put(id, Bitmap.createScaledBitmap(originBitmap, width, height, false));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map.get(id);
    }

    public static Bitmap getBitmapById(String id) {
        return getBitmapById(id, unitWidthPixels, unitHeightPixels);
    }

    public static Bitmap getTileById(String id, int width, int height) {
        return getBitmapById("tile" + "_" + id, width, height);
    }

    public static Bitmap getTileById(int id) {
        return getTileById(String.format("%03d", id), unitWidthPixels, unitHeightPixels);
    }

    public static void configMap(int _mapWidth, int _mapHeight, int _unitWidth, int _unitHeight) {
        mapWidth = _mapWidth;
        mapHeight = _mapHeight;

        unitWidthPixels = _unitWidth;
        unitHeightPixels = _unitHeight;

        updateMapPixels(unitWidthPixels, unitHeightPixels);

        map = new HashMap<>();
        canvas = null;
        screenX = 0;
        screenY = 0;
    }

    private static void updateMapPixels(int _unitWidthPixels, int _unitHeightPixels) {
        mapWidthPixels = mapWidth * _unitWidthPixels;
        mapHeightPixels = mapHeight * _unitHeightPixels;
    }

    public static void setCanvas(Canvas canvas) {
        GameGraphic.canvas = canvas;
    }

    public static void draw(DrawableObject drawable) {
        drawable.draw(canvas);
    }

    public static void draw(List<? extends DrawableObject> drawableList) {
        for (DrawableObject drawable: drawableList) {
            GameGraphic.draw(drawable);
        }
    }

    public static void draw(Bitmap bitmap, float x, float y) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public static int getScreenWidthPixels() {
        return screenWidthPixels;
    }

    public static int getScreenHeightPixels() {
        return screenHeightPixels;
    }

    public static int getCenterScreenX() {
        return screenWidthPixels / 2;
    }

    public static int getCenterScreenY() {
        return screenHeightPixels / 2;
    }

    public static void setScreenX(float _screenX) {
        screenX = fixScreenX(_screenX);
    }

    public static float getScreenX() {
        return screenX;
    }

    public static void setScreenY(float _screenY) {
        screenY = fixScreenY(_screenY);
    }

    public static float getScreenY() {
        return screenY;
    }

    private static float fixScreenX(float screenX) {
        screenX = Math.max(screenX, 0f);
        screenX = Math.min(screenX, mapWidthPixels - screenWidthPixels + 1);
        return screenX;
    }

    private static float fixScreenY(float screenY) {
        screenY = Math.max(screenY, 0f);
        screenY = Math.min(screenY, mapHeightPixels - screenHeightPixels + 1);
        return screenY;
    }

    public static void reset() {}
}

