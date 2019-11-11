package com.ligrim.tower_defense;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;

public class GameGraphic {

    private static AssetManager assetManager;
    private static HashMap<String, Bitmap> map = new HashMap<>();
    private static GameStage stage;

    public static void setAssetManager(AssetManager asset) {
        assetManager = asset;
    }

    public static void setGameStage(GameStage gameStage) {
        stage = gameStage;
    }

    public static Bitmap getBitmapById(String id) {
        if (!map.containsKey(id)) {
            try {
                Bitmap originBitmap = BitmapFactory.decodeStream(assetManager.open("map/map_1/images/" + id));
                map.put(id, Bitmap.createScaledBitmap(originBitmap, stage.UNIT_WIDTH, stage.UNIT_HEIGHT, false));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map.get(id);
    }

    public static Bitmap getTileById(int Id) {

        String id = String.format("%03d", Id);

        return getBitmapById("tile" + id + ".png");
    }

    public static Bitmap getTileById(String id) {
        while (id.length() < 3) {
            id = "0" + id;
        }

        return getBitmapById("tile" + id + ".png");
    }

    public static Bitmap getEnemyById(String id) {
        return getBitmapById("enemy_" + id + ".png");
    }

    public static Bitmap getTowerById(String id) {
        return getBitmapById("tower_" + id + ".png");
    }
}
