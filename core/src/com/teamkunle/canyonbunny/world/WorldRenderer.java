package com.teamkunle.canyonbunny.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.teamkunle.canyonbunny.utils.ConstantUtils;
import com.teamkunle.canyonbunny.utils.ICalledBackLibgdx;

public class WorldRenderer implements Disposable {
	//page 189
	private static final String TAG = WorldRenderer.class.getSimpleName();
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch spriteBatch;
	private WorldController wc;
	
	private ICalledBackLibgdx testcall;
	
	public WorldRenderer(WorldController wc, ICalledBackLibgdx tc){
		this.wc = wc;
		this.testcall = tc;
		init();
	}

	private void init(){
		spriteBatch = new SpriteBatch();
		camera = new OrthographicCamera(ConstantUtils.VIEWPORT_WIDTH, 
				ConstantUtils.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
		testcall.calledMeBack();
	}
	
	public void render(){
		renderWorld(spriteBatch);
	}
	
	private void renderWorld(SpriteBatch sb){
		wc.cameraHelper.applyTo(camera);
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		wc.level.render(sb);
		sb.end();
	}

	public void resize(int width, int height){
		camera.viewportWidth = (ConstantUtils.VIEWPORT_HEIGHT / height) * width;
		camera.update();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}

}
