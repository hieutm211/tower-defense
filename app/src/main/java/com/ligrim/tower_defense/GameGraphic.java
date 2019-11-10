package com.ligrim.tower_defense;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GameGraphic {

    private static AssetManager assetManager;

    public static void setAssetManager(AssetManager asset) {
        assetManager = asset;
    }

    public static Bitmap getBitmapById(String id) throws Exception {
        return BitmapFactory.decodeStream(assetManager.open(id));
    }

    public static Bitmap getTileById(String id) throws Exception {
        return getBitmapById("tile" + id + ".png");
    }

    public static Bitmap getEnemyById(String id) throws Exception {
        return getBitmapById("enemy_" + id + ".png");
    }

    public static Bitmap getTowerById(String id) throws Exception {
        return getBitmapById("tower_" + id + ".png");
    }
}
