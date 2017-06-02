package com.teamkunle.canyonbunny.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;

/**
 * Created by EngineerKunle on 03/06/2017.
 */

public class ScreenTransitionSlice implements ScreenTransition {

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int UP_DOWN = 3;

    private static final ScreenTransitionSlice instance = new ScreenTransitionSlice();

    private float duration;
    private int direction;
    private Interpolation easing;
    private Array<Integer> sliceIndex = new Array<Integer>();

    public static ScreenTransitionSlice init(float duration, int direction, int numSlices, Interpolation easing) {

        instance.duration = duration;
        instance.direction = direction;
        instance.easing = easing;
        // create shuffled list of slice indices which determines the order of slice animation
        instance.sliceIndex.clear();
        for (int i=0; i < numSlices; i++)
            instance.sliceIndex.add(i);
        instance.sliceIndex.shuffle();
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
        int sliceWidth = (int)(w/sliceIndex.size);

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(currentScreen, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, currentScreen.getWidth(), currentScreen.getHeight(), false, true);

        if (easing != null) alpha = easing.apply(alpha);
//        for()

    }
}