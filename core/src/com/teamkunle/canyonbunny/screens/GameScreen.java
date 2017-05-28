package com.teamkunle.canyonbunny.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.teamkunle.canyonbunny.utils.GamePreferencesUtils;
import com.teamkunle.canyonbunny.world.WorldController;
import com.teamkunle.canyonbunny.world.WorldRenderer;

/**
 * Created by EngineerKunle on 17/04/2017.
 */

public class GameScreen extends AbstractGameScreen {

    private static final String TAG = GameScreen.class.getSimpleName();
    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private DirectedGame game;

    private boolean paused;

    public GameScreen(DirectedGame game) {
        super(game);
        this.game = game;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return worldController;
    }

    @Override
    public void render(float dt) {
        if (!paused) {
            worldController.update(dt);
        }

        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override
    public void show() {
        GamePreferencesUtils.instance.load();
        worldController = new WorldController(game);
        worldRenderer = new WorldRenderer(worldController);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide() {
        worldRenderer.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        super.resume();
        paused = false;
    }
}
