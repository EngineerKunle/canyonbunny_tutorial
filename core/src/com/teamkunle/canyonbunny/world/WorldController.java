package com.teamkunle.canyonbunny.world;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.audio.AudioManager;
import com.teamkunle.canyonbunny.gameobjects.BunnyHead;
import com.teamkunle.canyonbunny.gameobjects.Carrot;
import com.teamkunle.canyonbunny.gameobjects.Feather;
import com.teamkunle.canyonbunny.gameobjects.GoldCoin;
import com.teamkunle.canyonbunny.gameobjects.Rock;
import com.teamkunle.canyonbunny.helper.CameraHelper;
import com.teamkunle.canyonbunny.screens.DirectedGame;
import com.teamkunle.canyonbunny.screens.MenuScreen;
import com.teamkunle.canyonbunny.screens.transitions.ScreenTransition;
import com.teamkunle.canyonbunny.screens.transitions.ScreenTransitionSlide;
import com.teamkunle.canyonbunny.utils.ConstantUtils;


public class WorldController extends InputAdapter implements Disposable {
	private static final String TAG = WorldController.class.getSimpleName();

	private DirectedGame game;
	public CameraHelper cameraHelper;
	public Level level;

	public int lives;
	public int score;
	public float livesVisual;
	public float scoreVisual;

	public World b2World;

	private boolean goalReached;
	private float timeLeftGameOverDelay;

	//Rectangles for collisions
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
	public WorldController(DirectedGame game) {
		this.game = game;
		init();
	}

	//inherited from class
	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Keys.R:
				init();
				Gdx.app.debug(TAG, "Game world resetted");
				break;
			case Keys.ENTER:
				cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.bunnyHead);
				Gdx.app.debug(TAG, "Camera follow: " + cameraHelper.hasTarget());
				break;
			case Keys.BACK:
			case Keys.ESCAPE:
				backToMenu();
				break;
			default:
				Gdx.app.debug(TAG, "nothing pressed here");
				break;
		}
		return false;
	}
	
	private void init() {
		cameraHelper = new CameraHelper();
		lives = ConstantUtils.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
		initLevel();
		initPhysics();
	}

	private void initPhysics() {
		if (b2World != null) b2World.dispose();
		b2World = new World(new Vector2(0, -9.81f), true);

		//Rocks
		Vector2 origin = new Vector2();
		for (Rock rock : level.rocks) {
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyDef.BodyType.KinematicBody;
			bodyDef.position.set(rock.position);
			Body body = b2World.createBody(bodyDef);
			rock.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = rock.bounds.width / 2.0f;
			origin.y = rock.bounds.height / 2.0f;
			polygonShape.setAsBox(rock.bounds.width / 2.0f, rock.bounds.height / 2.0f, origin, 0);
			FixtureDef fixture = new FixtureDef();
			fixture.shape = polygonShape;
			body.createFixture(fixture);
			polygonShape.dispose();
		}
	}

	public void update(float time){
		handleDebugInput(time);
		if (isGameOver() || goalReached) {
			timeLeftGameOverDelay -= time;
			if (timeLeftGameOverDelay < 0 ) backToMenu();
		} else {
			handleInputGame(time);
		}
		level.update(time);
		testCollisions();
		b2World.step(time, 8, 3);
		cameraHelper.update(time);

		if (!isGameOver() && isPlayerInWater()) {
			AudioManager.instance.play(Assets.instance.assetsSounds.liveLost);
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = ConstantUtils.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
		level.mountains.updateScrollPosition(cameraHelper.getPosition());
		if (livesVisual > lives)
			livesVisual = Math.max(lives, livesVisual - 1 * time);

		if (scoreVisual < score)
			scoreVisual = Math.min(score, scoreVisual + 250 * time);
	}

	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

		if (!cameraHelper.hasTarget(level.bunnyHead)) {
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *=  camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);
		}

		//camera zoom
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.addZoom(1);
	}

	private void handleInputGame(float time) {
		if (cameraHelper.hasTarget(level.bunnyHead)) {
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				level.bunnyHead.velocity.x =- level.bunnyHead.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
			} else {
				if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
					level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
				}
			}

			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))
				level.bunnyHead.setJumping(true);
			else
				level.bunnyHead.setJumping(false);
		}
	}

	private void initLevel(){
		score = 0;
		scoreVisual = score;
		level = new Level(ConstantUtils.LEVEL_01);
		cameraHelper.setTarget(level.bunnyHead);
		initPhysics();
	}
	
	private void moveCamera(float x, float y){
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
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
			case JUMP_RISING:
				bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
				break;
		}
	}
	private void onCollisonBunnyHeadWithGoldCoin(GoldCoin goldCoin) {
		goldCoin.collected = true;
		AudioManager.instance.play(Assets.instance.assetsSounds.pickUpCoin);
		score += goldCoin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");
	}
	private void onCollisonBunnyHeadWithFeather(Feather feather) {
		feather.collected = true;
		AudioManager.instance.play(Assets.instance.assetsSounds.pickupFeather);
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerUp(true);
		Gdx.app.log(TAG, "feather collected");
	}

	private void testCollisions() {
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);

		//here we can test collisions
		for (Rock rock : level.rocks) {
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
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

		if (!goalReached) {
			r2.set(level.goal.bounds);
			r2.x = level.goal.position.x;
			r2.y = level.goal.position.y;
			if (r1.overlaps(r2)) onCollisionBunnyWithGoal();
		}
	}

	private void onCollisionBunnyWithGoal() {
		goalReached = true;
		timeLeftGameOverDelay = ConstantUtils.TIME_DELAY_GAME_FINISHED;
		Vector2 centerPosBunnyHead = new Vector2(level.bunnyHead.position);
		spawnCarrots(centerPosBunnyHead, ConstantUtils.CARROTS_SPAWN_MAX, ConstantUtils.CARROTS_SPAWN_RADIUS);
	}

	public boolean isGameOver() {
		return lives < 0;
	}

	public boolean isPlayerInWater() {
		return level.bunnyHead.position.y < -5;
	}

	private void backToMenu() {
		ScreenTransition screenTransition = ScreenTransitionSlide.init(0.75f, ScreenTransitionSlide.UP, false, Interpolation.bounce);
		game.setScreen(new MenuScreen(game), screenTransition);
	}

	private void spawnCarrots(Vector2 pos, int numCarrots, float radius){
		float carrotShapeScale = 0.5f;

		// calculate carrots with box2d boday and fixture
		for (int i = 0; i < numCarrots; i++) {
			Carrot carrot = new Carrot();
			// calculate random spawn position, rotation, and scale
			float x = MathUtils.random(-radius, radius);
			float y = MathUtils.random(5.0f, 15.0f);
			float rotation = MathUtils.random(0.0f, 360.0f) * MathUtils.degreesToRadians;
			float carrotScale = MathUtils.random(0.5f, 1.5f);
			carrot.scale.set(carrotScale, carrotScale);
			// create box2d body for carrot with start position
			// and angle of rotation
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(pos);
			bodyDef.position.add(x, y);
			bodyDef.angle = rotation;
			Body body = b2World.createBody(bodyDef);
			body.setType(BodyDef.BodyType.DynamicBody);
			carrot.body = body;
			// create rectangular shape for carrot to allow
			// interactions (collisions) with other objects
			PolygonShape polygonShape = new PolygonShape();
			float halfWidth = carrot.bounds.width / 2.0f * carrotScale;
			float halfHeight = carrot.bounds.height / 2.0f * carrotScale;
			polygonShape.setAsBox(halfWidth * carrotShapeScale, halfHeight * carrotShapeScale);
			// set physics attributes
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.density = 50;
			fixtureDef.restitution = 0.5f;
			fixtureDef.friction = 0.5f;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
			// finally, add new carrot to list for updating/rendering
			level.carrots.add(carrot);
		}
	}

	@Override
	public void dispose() {
		if (b2World != null) b2World.dispose();
	}
}
