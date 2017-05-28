package com.teamkunle.canyonbunny.screens.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

/**
 * Created by EngineerKunle on 28/05/2017.
 */

public class ScreenTransitionSlide implements ScreenTransition {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;

    private static final ScreenTransitionSlide instance = new ScreenTransitionSlide();

    private float duration;
    private int direction;
    private boolean slideOut;
    private Interpolation easing;

    public static ScreenTransitionSlide init(float duration, int direction, boolean slideOut, Interpolation easing){
        instance.duration = duration;
        instance.direction = direction;
        instance.slideOut = slideOut;
        instance.easing = easing;
        return instance;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture currentScreen, Texture nextScreen, float alpha) {

        float w = currentScreen.getWidth();
        float h = currentScreen.getHeight();
        float x = 0;
        float y = 0;
        if (easing != null) alpha = easing.apply(alpha);
    }
}
