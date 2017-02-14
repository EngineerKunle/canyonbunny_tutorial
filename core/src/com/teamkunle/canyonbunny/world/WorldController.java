package com.teamkunle.canyonbunny.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

public class WorldController {
	private static final String TAG = WorldController.class.getSimpleName();
	public Sprite[] testsprites;
	public int selectedSprite;
	
	
	public WorldController() {
		init();
	}
	
	private void init() {
		initTestObjects();
	}
	
	private void initTestObjects() {
		testsprites = new Sprite[5];
		int width = 32;
		int height = 32;
		Pixmap pixmap = createProceduralPixmap(width, height);
		Texture texture = new Texture(pixmap);
	
		for (int i = 0; i < testsprites.length; i++) {
	         Sprite spr = new Sprite(texture);
	         spr.setSize(1, 1);
	         spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
	         float randomX = MathUtils.random(-2.0f, 2.0f);
	         float randomY = MathUtils.random(-2.0f, 2.0f);
	         spr.setPosition(randomX, randomY);
	         testsprites[i] = spr;
	       }
		
		selectedSprite = 0;
	}

	private Pixmap createProceduralPixmap(int width, int height) {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		  // Draw a yellow-colored X shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		
		//random circle
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawCircle(0, 0, 10);
		return pixmap;
	}

	public void update(float time){
		handleDebugInput(time);
		updateTestObjects(time);
	}
	
	private void handleDebugInput(float time) {
		if (Gdx.app.getType() != ApplicationType.Desktop)
			return;
		
		//selected sprite control
		float sprMovespeed = 5 * time;
		 if (Gdx.input.isKeyPressed(Keys.A)) 
			 moveSelectedSprite(-sprMovespeed, 0);
		       
		 if (Gdx.input.isKeyPressed(Keys.D))
		         moveSelectedSprite(sprMovespeed, 0);
		       
		 if (Gdx.input.isKeyPressed(Keys.W)) 
			 moveSelectedSprite(0, sprMovespeed);
		       
		 if (Gdx.input.isKeyPressed(Keys.S)) 
			 moveSelectedSprite(0, -sprMovespeed);
		
	}


	private void moveSelectedSprite(float x, float y) {
		testsprites[selectedSprite].translate(x, y);;
	}

	private void updateTestObjects(float deltaTime) {
	    // Get current rotation from selected sprite
	    float rotation = testsprites[selectedSprite].getRotation();
	    // Rotate sprite by 90 degrees per second
	    rotation += 90 * deltaTime;
	    // Wrap around at 360 degrees
	    rotation %= 360;
	    // Set new rotation value to selected sprite
	    testsprites[selectedSprite].setRotation(rotation);
	}
}
