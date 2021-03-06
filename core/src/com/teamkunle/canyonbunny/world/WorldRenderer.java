package com.teamkunle.canyonbunny.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.utils.ConstantUtils;
import com.teamkunle.canyonbunny.utils.GamePreferencesUtils;

public class WorldRenderer implements Disposable {
	//page 189
	private static final String TAG = WorldRenderer.class.getSimpleName();
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch spriteBatch;
	private WorldController wc;
	private Box2DDebugRenderer box2DDebugRenderer;
	private ShaderProgram shaderMonoschrome;

	public WorldRenderer(WorldController wc){
		this.wc = wc;
		init();
	}

	private void init(){
		spriteBatch = new SpriteBatch();
		camera = new OrthographicCamera(ConstantUtils.VIEWPORT_WIDTH, 
				ConstantUtils.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
		
		cameraGUI = new OrthographicCamera(ConstantUtils.VIEWPORT_GUI_WIDTH, ConstantUtils.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true);
		cameraGUI.update();

        box2DDebugRenderer = new Box2DDebugRenderer();

        shaderMonoschrome = new ShaderProgram(Gdx.files.internal(ConstantUtils.SHADER_MONO_CHROME_VETEX),
                Gdx.files.internal(ConstantUtils.SHADER_MONO_CHROME_FRAGEMENT));
        if (!shaderMonoschrome.isCompiled()) {
            throw new GdxRuntimeException("Could not compile shader program: " + shaderMonoschrome.getLog());
        }
	}

	public void render(){
		renderWorld(spriteBatch);
		renderGUI(spriteBatch);
	}

	private void renderWorld(SpriteBatch sb){
		wc.cameraHelper.applyTo(camera);
		sb.setProjectionMatrix(camera.combined);
		sb.begin();

        if (GamePreferencesUtils.instance.useMonochromeShader) {
            sb.setShader(shaderMonoschrome);
            shaderMonoschrome.setUniformf("u_amount", 1.0f);
        }

		wc.level.render(sb);
        sb.setShader(null);
		sb.end();

        if (ConstantUtils.DEBUG_DRAW_BOX2D_WORLD) {
            box2DDebugRenderer.render(wc.b2World, camera.combined);
        }
	}

	public void resize(int width, int height){
		camera.viewportWidth = (ConstantUtils.VIEWPORT_HEIGHT / height) * width;
		camera.update();

		cameraGUI.viewportHeight = ConstantUtils.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (ConstantUtils.VIEWPORT_GUI_HEIGHT / (float)height) * (float)width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
	}
	
	private void renderGuiScore(SpriteBatch batch){
		float x = -15;
		float y = -15;
		float offSetX = 50;
		float offSetY = 50;

		if (wc.scoreVisual < wc.score) {
			long shakeAlpha = System.currentTimeMillis() % 360;
			float shakeDist = 1.5f;
			offSetX += MathUtils.sinDeg(shakeAlpha * 2.2f) * shakeDist;
			offSetY += MathUtils.sinDeg(shakeAlpha * 2.9f) * shakeDist;
		}

		batch.draw(Assets.instance.goldCoin.goldCoin, x, y, offSetX, offSetY, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.assetsFonts.defaultBig.draw(batch, "" + (int) wc.scoreVisual, x + 75, y + 37);
	}
	
	private void renderGuiExtraLive(SpriteBatch batch){
		float x = cameraGUI.viewportWidth - 50 - ConstantUtils.LIVES_START * 50;
		float y = -15;
		
		for(int i = 0; i< ConstantUtils.LIVES_START; i++){
			if (wc.lives <= i)
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			batch.draw(Assets.instance.bunny.head, x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
		}
		if (wc.lives >= 0 && wc.livesVisual > wc.lives) {
			int i = wc.lives;
			float alphaColor = Math.max(0, wc.livesVisual - wc.lives - 0.5f);
			float alphaScale = 0.35f * (2 + wc.lives - wc.livesVisual) * 2;
			float alphaRotate = -45 * alphaColor;
			batch.setColor(1.0f, 0.7f, 0.7f, alphaColor);
			batch.draw(Assets.instance.bunny.head, x + i * 50, y, 50, 50, 120, 100, alphaScale, -alphaScale, alphaRotate);
			batch.setColor(1, 1, 1, 1);
		}
	}
	
	private void renderGuiFpsCounter(SpriteBatch batch){
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		
		BitmapFont fpsFont = Assets.instance.assetsFonts.defaultNormal;
		if (fps >= 45) {
			// 45 or more FPS show up in green
			fpsFont.setColor(0, 1, 0, 1);
		} else if (fps >= 30) {
			// 30 or more FPS show up in yellow
			fpsFont.setColor(1, 1, 0, 1);
		} else {
			// less than 30 FPS show up in red
			fpsFont.setColor(1, 0, 0, 1);
		}

		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1, 1, 1, 1); // white
	}
	
	private void renderGUI(SpriteBatch batch){
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();

		// draw collected gold coins icon + text (anchored to top left edge)
		renderGuiScore(batch);
		// draw extra lives icon + text (anchored to top right edge)
		renderGUIFeatherPowerUp(batch);

		renderGuiExtraLive(batch);
		// draw FPS text (anchored to bottom right edge)

		if (GamePreferencesUtils.instance.showFpsCounter)
		renderGuiFpsCounter(batch);

		//draw game over screen
		renderGUIGameOverMessage(batch);

		batch.end();
	}

	private void renderGUIGameOverMessage(SpriteBatch sb) {
		float x = cameraGUI.viewportWidth / 2;
		float y = cameraGUI.viewportHeight / 2;
		if (wc.isGameOver()) {
			BitmapFont fontGameOver = Assets.instance.assetsFonts.defaultBig;
			fontGameOver.setColor(1, 0.75f, 0.25f, 1);
			fontGameOver.draw(sb, "Game Over", x, y);
			fontGameOver.setColor(1, 1, 1, 1);
		}
	}

	private void renderGUIFeatherPowerUp(SpriteBatch sb){
		float x = -15;
		float y = 100;

		float timeLeftFeatherPowerUp = wc.level.bunnyHead.timeLeftFeatherPowerup;
		if (timeLeftFeatherPowerUp > 0 ) {
			if (timeLeftFeatherPowerUp < 4) {
				if (((int)(timeLeftFeatherPowerUp * 5) % 2) != 0) {
					sb.setColor(1, 1, 1, 0.5f);
				}
			}
			sb.draw(Assets.instance.feather.feather, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
    			sb.setColor(1, 1, 1, 0.5f);
			Assets.instance.assetsFonts.defaultSmall.draw(sb, "" + (int)timeLeftFeatherPowerUp, x + 60, y + 57);
		}
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
        shaderMonoschrome.dispose();
	}

}
