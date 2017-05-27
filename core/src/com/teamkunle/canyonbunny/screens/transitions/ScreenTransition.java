package com.teamkunle.canyonbunny.screens.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by EngineerKunle on 28/05/2017.
 */

public interface ScreenTransition {
    float getDuration();
    void render(SpriteBatch batch, Texture currentScreen, Texture nextScreen, float alpha);
}
