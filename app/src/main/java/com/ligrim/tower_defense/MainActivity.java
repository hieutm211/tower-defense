package com.ligrim.tower_defense;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

        setContentView(R.layout.activity_main);
    }

    public void pickGame(View view) {
        TextView taptoplayText = findViewById(R.id.taptoplayText);
        taptoplayText.animate().alpha(0f).setDuration(1000);

        ImageView startScreen = findViewById(R.id.startScreen);
        startScreen.animate().alpha(0.5f).setDuration(1000);

        Button map_1 = findViewById(R.id.map_1);
        map_1.setVisibility(View.VISIBLE);
        map_1.animate().alpha(1f).setDuration(2000);

        Button map_2 = findViewById(R.id.map_2);
        map_2.setVisibility(View.VISIBLE);
        map_2.animate().alpha(1f).setDuration(2000);

        Button map_3 = findViewById(R.id.map_3);
        map_3.setVisibility(View.VISIBLE);
        map_3.animate().alpha(1f).setDuration(2000);
    }

    public void enterGame(View view) {
        String mapId = null;
        switch (view.getId()) {
            case R.id.map_1:
                mapId = "map_1";
                break;
            case R.id.map_2:
                mapId = "map_2";
                break;
            case R.id.map_3:
                mapId = "map_3";
                break;
        }

        try {
            InputStream mapFile = getAssets().open("map/" + mapId + "/tilemap_info.tmx");
            InputStream enemyFile = getAssets().open("map/" + mapId + "/enemy_info.txt");
            InputStream saveFile = getAssets().open("map/" + mapId + "/saveFile.xml");
            GameStage gameStage = new GameStage(mapFile, enemyFile, saveFile);
            GameField gameField = new GameField(this, gameStage);
            setContentView(gameField);
            GameGraphic.setAssetManager(this.getAssets());
            GameGraphic.configMap(gameStage.WIDTH, gameStage.HEIGHT, gameStage.UNIT_WIDTH, gameStage.UNIT_HEIGHT);
            GamePane.setGameField(gameField);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
