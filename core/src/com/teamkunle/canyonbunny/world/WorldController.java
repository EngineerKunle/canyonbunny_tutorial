package com.teamkunle.canyonbunny.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.teamkunle.canyonbunny.gameobjects.BunnyHead;
import com.teamkunle.canyonbunny.gameobjects.Feather;
import com.teamkunle.canyonbunny.gameobjects.GoldCoin;
import com.teamkunle.canyonbunny.gameobjects.Rock;
import com.teamkunle.canyonbunny.helper.CameraHelper;
import com.teamkunle.canyonbunny.utils.ConstantUtils;


public class WorldController extends InputAdapter {
	private static final String TAG = WorldController.class.getSimpleName();
	public CameraHelper cameraHelper;
	
	public int lives;
	public int score;
	public Level level;

	//Rectangles for collisions
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
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
	//Test collisions
	private void onCollisonBunnyHeadWithRock(Rock rock) {
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));

		if(heightDifference > 0.25f) {
			boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if (hitRightEdge) {
				bunnyHead.position.x = rock.position.x + rock.bounds.width;
			} else {
				bunnyHead.position.x = rock.position.x - rock.bounds.width;
			}
			return;
		}

		switch (bunnyHead.jumpState) {
			case GROUNDED:
				break;
			case FALLING:
			case JUMP_FALLING:
				bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.x;
				bunnyHead.jumpState = BunnyHead.JUMP_STATE.GROUNDED;
				break;

		}
	}
	private void onCollisonBunnyHeadWithGoldCoin(GoldCoin goldCoin) {}
	private void onCollisonBunnyHeadWithFeather(Feather feather) {}

	private void testCollisions() {
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);

		//here we can test collisions
		for (Rock rock : level.rocks) {
			if (!r1.overlaps(r2)) continue;
			onCollisonBunnyHeadWithRock(rock);
			//IMPORTANT: must do all collisions for valid edge testing
		}

		for (Feather feather : level.feathers) {
			if (feather.collected) continue;
			r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisonBunnyHeadWithFeather(feather);
			break;
		}

		for (GoldCoin goldCoin : level.goldCoins) {
			if(goldCoin.collected) continue;
			r2.set(goldCoin.position.x, goldCoin.position.y, goldCoin.bounds.width, goldCoin.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisonBunnyHeadWithGoldCoin(goldCoin);
			break;
		}
	}
}
