package com.ligrim.tower_defense;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        try {
            InputStream mapFile = getAssets().open("map/map_1/sample_map1.tmx");
            InputStream enemyFile = getAssets().open("map/map_1/enemy_info.txt");
            InputStream routeFile = getAssets().open("map/map_1/route_info.txt");
            GameStage gameStage = new GameStage(mapFile, enemyFile, routeFile);
            setContentView(new GameField(this, gameStage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
