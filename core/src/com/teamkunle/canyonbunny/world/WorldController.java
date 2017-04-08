package com.teamkunle.canyonbunny.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.teamkunle.canyonbunny.helper.CameraHelper;
import com.teamkunle.canyonbunny.utils.ConstantUtils;


public class WorldController extends InputAdapter {
	private static final String TAG = WorldController.class.getSimpleName();
	public CameraHelper cameraHelper;
	
	public int lives;
	public int score;
	public Level level;
	
	public WorldController() {
		init();
	}
	
	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = ConstantUtils.LIVES_START;
		initLevel();
	}

	public void update(float time){
		handleDebugInput(time);
		cameraHelper.update(time);
		level.update(time);
	}
	
	private void handleDebugInput(float time) {
	}
	
	private void initLevel(){
		score = 0;
		level = new Level(ConstantUtils.LEVEL_01);
	}
	
	private void moveCamera(float x, float y){
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	
	//inherited from class
	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.R:
			init();
			Gdx.app.debug(TAG, "Game world resetted");
			break;	
		default:
			Gdx.app.debug(TAG, "nothing pressed here");
			break;
		}
		return false;
	}
}
