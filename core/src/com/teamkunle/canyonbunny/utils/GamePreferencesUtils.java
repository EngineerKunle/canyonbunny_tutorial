package com.teamkunle.canyonbunny.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;


/**
 * Created by EngineerKunle on 23/04/2017.
 */

public class GamePreferencesUtils {
    private static final String TAG = GamePreferencesUtils.class.getSimpleName();

    public static final GamePreferencesUtils instance = new GamePreferencesUtils();
    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public int charSkin;
    public boolean showFpsCounter;

    private Preferences preferences;

    //singleton
    private GamePreferencesUtils() {
        preferences = Gdx.app.getPreferences(ConstantUtils.PREFERENCES);
    }

    public void load() {
        sound = preferences.getBoolean("sound", true);
        music = preferences.getBoolean("music", true);
        volMusic = MathUtils.clamp(preferences.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
        volSound = MathUtils.clamp(preferences.getFloat("volSound", 0.5f), 0.0f, 1.0f);
        charSkin = MathUtils.clamp(preferences.getInteger("charSKin", 0), 0, 2);
        showFpsCounter = preferences.getBoolean("showFpsCounter", true);
    }
    public void save() {
        preferences.putBoolean("sound", sound);
        preferences.putBoolean("music", music);
        preferences.putFloat("volMusic",volMusic);
        preferences.putFloat("volSound",volSound);
        preferences.putInteger("charSkin", charSkin);
        preferences.putBoolean("showFpsCounter", showFpsCounter);
        preferences.flush();
    }
}
