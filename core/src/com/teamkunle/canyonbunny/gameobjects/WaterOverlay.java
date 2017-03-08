package com.teamkunle.canyonbunny.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teamkunle.canyonbunny.helper.AbstractGameObject;

import assets.Assets;

public class WaterOverlay extends AbstractGameObject{

	private TextureRegion regWaterOverlay;
	private float length;
	
	public WaterOverlay(float length) {
		this.length = length;
		init();
	}

	@Override
	public void render(SpriteBatch sb) {
		TextureRegion reg = null;
		reg = regWaterOverlay;
		sb.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, 
				origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(), false,
				false);
	}
	
	private void init(){
		dimension.set(length * 10, 3);
		regWaterOverlay = Assets.instance.levelDecoration.waterOverlay;
		
		origin.x = -dimension.x / 2;
	}
}
