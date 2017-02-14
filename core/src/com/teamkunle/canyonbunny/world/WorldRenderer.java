package com.teamkunle.canyonbunny.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.teamkunle.canyonbunny.utils.ConstantUtils;

public class WorldRenderer implements Disposable {
	private static final String TAG = WorldRenderer.class.getSimpleName();
	private OrthographicCamera camera;
	private SpriteBatch sp;
	private WorldController wc;
	
	public WorldRenderer(WorldController wc){
		this.wc = wc;
		init();
	}

	private void init(){
		sp = new SpriteBatch();
		camera = new OrthographicCamera(ConstantUtils.VIEWPORT_WIDTH, 
				ConstantUtils.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
	}
	
	public void render(){
		renderTest();
	}
	
	
	private void renderTest() {
		sp.setProjectionMatrix(camera.combined);
		sp.begin();
		for(Sprite sprite : wc.testsprites){
			sprite.draw(sp);
		}	
		sp.end();
	}

	public void resize(int width, int height){
		camera.viewportWidth = (ConstantUtils.VIEWPORT_HEIGHT / height) * width;
		camera.update();
	}

	@Override
	public void dispose() {
		sp.dispose();
	}

}
