package com.kit10.csci448.catastrophe.model;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccollier on 5/3/2017.
 */

public class SoundBox {

    private static final String TAG = "SoundBox";
    private static final String SOUNDS_FOLDER = "cat_sounds";
    private static final String SHORT_HAPPY_SOUNDS_FOLDER = "short_happy_sounds";
    private static final String PURR_SOUNDS_FOLDER = "purr_sounds";
    private List<Sound> mSounds;
    private List<Sound> mPurrSounds;
    private List<Sound> mShortHappySounds;
    private Sound startSound;
    private Sound backgroundMusic;
    private static final int MAX_SOUNDS = 10;    //Maybe make is so every visible cat can have a sound.
    private SoundPool mSoundPool;
    private AssetManager mAssets;
    int backgroundID;

    public SoundBox(Context context) {
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 0.25f, 0.25f, 1, 0, 1.0f);
    }

    public void playLoop(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        backgroundID = mSoundPool.play(soundId, 0.05f, 0.05f, 1, -1, 1.0f);

    }

    public void release() {
        mSoundPool.release();
    }

    public void stop() { mSoundPool.stop(backgroundID);}


    public List<Sound> getSounds() {
        return mSounds;
    }

    public Sound getStartSound() {
        return startSound;
    }

    public Sound getBackgroundMusic() { return backgroundMusic;}

    public List<Sound> getPurrSounds() {
        return mPurrSounds;
    }

    public List<Sound> getShortHappySounds() {
        return mShortHappySounds;
    }

    //Load all sounds and load different types of sounds into different lists.
    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        mSounds = new ArrayList<Sound>();
        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }

        }

        mShortHappySounds = new ArrayList<Sound>();
        for (String filename : soundNames) {
            try {
                String assetPath = SHORT_HAPPY_SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mShortHappySounds.add(sound);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }

        }

        mPurrSounds = new ArrayList<Sound>();
        for (String filename : soundNames) {
            try {
                String assetPath = PURR_SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mPurrSounds.add(sound);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }

        }

        try {
            startSound = new Sound(SOUNDS_FOLDER + "/pissy_cat.wav");
            load(startSound);
        } catch (IOException ioe) {
            Log.e(TAG, "Could not load sound startSound", ioe);
        }

        try {
            backgroundMusic = new Sound("background_music.mp3");
            load(backgroundMusic);
        } catch (IOException ioe) {
            Log.e(TAG, "Could not load sound backgroundMusic", ioe);
        }


    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

}
