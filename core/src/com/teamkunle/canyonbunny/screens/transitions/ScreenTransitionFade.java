package com.teamkunle.canyonbunny.screens.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by EngineerKunle on 28/05/2017.
 */

public class ScreenTransitionFade implements ScreenTransition {

    private static final ScreenTransitionFade instance = new ScreenTransitionFade();

    private float duration;

    public static ScreenTransitionFade init(float duration) {
        instance.duration = duration;
        return instance;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture currentScreen, Texture nextScreen, float alpha) {

    }
}
