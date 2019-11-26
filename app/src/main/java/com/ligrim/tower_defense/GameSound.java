package com.ligrim.tower_defense;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

public class GameSound {

    private static Context context;

    private static SoundPool soundPool;
    private static MediaPlayer mediaPlayer;

    private static AudioManager audioManager;

    // Maximumn sound stream.
    private static final int MAX_STREAMS = 5;

    // Stream type.
    private static final int streamType = AudioManager.STREAM_MUSIC;

    private static float volume = 0.8f;

    private static boolean loaded = true;

    private static boolean muted = false;

    private static HashMap<String, Integer> soundMap = new HashMap<>();
    private static HashMap<String, Integer> streamMap = new HashMap<>();

    public static void config(Context _context) {
        context = _context;

        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);

        // Current volumn Index of particular stream type.
        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);

        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(streamType);

        // Volumn (0 --> 1)
        volume = currentVolumeIndex / maxVolumeIndex;

        // Suggests an audio stream whose volume should be changed by
        // the hardware volume controls.
        ((Activity) context).setVolumeControlStream(streamType);

        // For Android SDK >= 21
        if (android.os.Build.VERSION.SDK_INT >= 21 ) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            soundPool = builder.build();
        }
        // for Android SDK < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        // When Sound Pool load complete.
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });


        //load sounds.
        soundMap.put("home", soundPool.load(context, R.raw.home, 1));
        soundMap.put("ingame", soundPool.load(context, R.raw.ingame, 1));
        soundMap.put("tower_machine_gun", soundPool.load(context, R.raw.tower_machine_gun, 1));
        soundMap.put("tower_normal", soundPool.load(context, R.raw.tower_normal,1));
        soundMap.put("tower_sniper", soundPool.load(context, R.raw.tower_sniper,1));
    }

    public static void play(String id) {
        if (loaded) {
            float leftVolume = volume;
            float rightVolume = volume;

            streamMap.put(id, soundPool.play(soundMap.get(id), leftVolume, rightVolume, 1, 0, 1f));
        }
    }

    public static void stop(String id) {
        if (loaded) {
            soundPool.stop(streamMap.get(id));
        }
    }

    public static void playMediaPlayer(String id) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        switch (id) {
            case "home":
                mediaPlayer = MediaPlayer.create(context, R.raw.home);
                mediaPlayer.setVolume(volume, volume);
                mediaPlayer.start();
                break;
            case "ingame":
                mediaPlayer = MediaPlayer.create(context, R.raw.ingame);
                mediaPlayer.setVolume(volume, volume);
                mediaPlayer.start();
                break;
        }
    }

    public static void toggleMute() {
        muted = !muted;
        if (muted) {
            volume = 0f;
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(volume, volume);
            }
        } else {
            volume = 0.8f;
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(volume, volume);
            }
        }
    }
}
