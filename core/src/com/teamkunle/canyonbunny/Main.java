package com.teamkunle.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.gameobjects.Feather;
import com.teamkunle.canyonbunny.gameobjects.GoldCoin;
import com.teamkunle.canyonbunny.gameobjects.Rock;
import com.teamkunle.canyonbunny.utils.ICalledBackLibgdx;
import com.teamkunle.canyonbunny.world.WorldController;
import com.teamkunle.canyonbunny.world.WorldRenderer;

public class Main extends ApplicationAdapter implements ICalledBackLibgdx {

	//page 171
	private static final String TAG = Main.class.getSimpleName();
	private WorldController worldcontroller;
	private WorldRenderer worldrenderer;
	private boolean paused;

	//Rectangles for collisions
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();


	@Override
	public void create() {
		// make sure to always change log info for release
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		Assets.instance.init(new AssetManager());
		worldcontroller = new WorldController();
		worldrenderer = new WorldRenderer(worldcontroller, this);
		paused = false;
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		Assets.instance.init(new AssetManager());
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
		Assets.instance.dispose();
	}

	@Override
	public void resize(int width, int height) {
		worldrenderer.resize(width, height);
	}

	@Override
	public void calledMeBack() {
		Gdx.app.debug(TAG, "he called me");
	}

	//Test collisions
	private void onCollisonBunnyHeadWithRock(Rock rock) {}
	private void onCollisonBunnyHeadWithGoldCoin(GoldCoin goldCoin) {}
	private void onCollisonBunnyHeadWithFeather(Feather feather) {}
}
