package com.teamkunle.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.screens.DirectedGame;
import com.teamkunle.canyonbunny.screens.MenuScreen;
import com.teamkunle.canyonbunny.screens.transitions.ScreenTransition;
import com.teamkunle.canyonbunny.screens.transitions.ScreenTransitionSlice;


public class Main extends DirectedGame {

	private ScreenTransition transition;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		transition = ScreenTransitionSlice.init(2, ScreenTransitionSlice.DOWN, 10, Interpolation.pow5Out);
		Assets.instance.init(new AssetManager());
		setScreen(new MenuScreen(this), transition);
	}
}
