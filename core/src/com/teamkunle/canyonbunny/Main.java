package com.teamkunle.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamkunle.canyonbunny.world.WorldController;
import com.teamkunle.canyonbunny.world.WorldRenderer;

public class Main extends ApplicationAdapter {

	//page 134 
	private static final String TAG = Main.class.getSimpleName();
	private WorldController worldcontroller;
	private WorldRenderer worldrenderer;
	private boolean paused;

	@Override
	public void create() {
		// make sure to always change log info for release
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		worldcontroller = new WorldController();
		worldrenderer = new WorldRenderer(worldcontroller);

		paused = false;
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	@Override
	public void render() {
		if (!paused) {
			worldcontroller.update(Gdx.graphics.getDeltaTime());
		}

		Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		worldrenderer.render();
	}

	@Override
	public void dispose() {
		worldrenderer.dispose();
	}

	@Override
	public void resize(int width, int height) {
		worldrenderer.resize(width, height);
	}
}
