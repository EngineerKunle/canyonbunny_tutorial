package com.teamkunle.canyonbunny.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.teamkunle.canyonbunny.screens.transitions.ScreenTransition;

/**
 * Created by EngineerKunle on 28/05/2017.
 */

public class DirectedGame implements ApplicationListener {

    private boolean init;
    private AbstractGameScreen currScreen;
    private AbstractGameScreen nextScreen;
    private FrameBuffer currFBO;
    private FrameBuffer nextFBO;
    private SpriteBatch batch;
    private float t;
    private ScreenTransition screenTransition;

    @Override
    public void create() {
    }

    @Override
    public void resize(int width, int height) {
        if (currScreen != null) currScreen.resize(width, height);
        if (nextScreen!= null) nextScreen.resize(width, height);
    }

    @Override
    public void render() {
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);
        if (nextScreen == null) {
            // no ongoing transition
            if (currScreen != null) currScreen.render(deltaTime);
        } else {
            // ongoing transition
            float duration = 0;
            if (screenTransition != null) duration = screenTransition.getDuration();
            t = Math.min(t + deltaTime, duration);
            if (screenTransition == null || t >= duration) {
                // no transition effect set or transition has just finished
                if (currScreen != null) currScreen.hide();
                nextScreen.resume();
                // enable input for next screen
                Gdx.input.setInputProcessor(nextScreen.getInputProcessor());
                // switch screens
                currScreen = nextScreen;
                nextScreen = null;
                screenTransition = null;
            } else {
                // render screens to FBOs
                currFBO.begin();
                if (currScreen != null) currScreen.render(deltaTime);
                currFBO.end();
                nextFBO.begin();
                nextScreen.render(deltaTime);
                nextFBO.end();
                // render transition effect to screen
                float alpha = t / duration;
                screenTransition.render(batch, currFBO.getColorBufferTexture(), nextFBO.getColorBufferTexture(), alpha);
            }
        }
    }

    @Override
    public void pause() {
        if (currScreen != null) currScreen.pause();
    }

    @Override
    public void resume() {
        if(currScreen != null) currScreen.resume();
    }

    @Override
    public void dispose() {
        if (currScreen != null) currScreen.hide();
        if (nextScreen != null) nextScreen.hide();

        if (init) {
            currFBO.dispose();
            currScreen = null;
            nextFBO.dispose();
            nextScreen = null;
            batch.dispose();
            init = false;
        }
    }

    public void setScreen(AbstractGameScreen screen) {
        setScreen(screen, null);
    }

    public void setScreen(AbstractGameScreen screen, ScreenTransition screenTransition) {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        if (!init) {
            currFBO = new FrameBuffer(Pixmap.Format.RGB888, w, h, false);
            nextFBO = new FrameBuffer(Pixmap.Format.RGB888, w, h, false);
            batch = new SpriteBatch();
            init = true;
        }
        // start new transition
        nextScreen = screen;
        nextScreen.show(); //activate next screen
        nextScreen.resize(w, h);
        nextScreen.render(0); //let the next screen render once

        if (currScreen != null) currScreen.pause();
        nextScreen.pause();
        Gdx.input.setInputProcessor(null); //disable input
        this.screenTransition = screenTransition;
        t = 0;
    }
}