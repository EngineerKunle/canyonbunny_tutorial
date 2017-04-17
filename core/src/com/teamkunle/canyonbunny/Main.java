package com.teamkunle.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.screens.MenuScreen;

public class Main extends Game {

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		setScreen(new MenuScreen(this));
	}
}
