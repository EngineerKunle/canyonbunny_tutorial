package com.teamkunle.canyonbunny.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.helper.AbstractGameObject;

public class Mountains extends AbstractGameObject{
	
	private TextureRegion regMountainLeft;
	private TextureRegion regMountainRight;
	
	private int length;
	
	public Mountains(int length) {
		this.length = length;
		init();
	}
	
	@Override
	public void render(SpriteBatch sb) {
		// distant mountains (dark grey)
		drawMountain(sb, 0.5f, 0.5f, 0.5f);		
		//distant mountains (gray)
		drawMountain(sb, 0.25f, 0.25f, 0.7f);
		// distant mountains (black)
		drawMountain(sb, 0.0f, 0.0f, 0.7f);
	}
	
	private void init(){
		dimension.set(10,2);
		regMountainLeft = Assets.instance.levelDecoration.mountainLeft;
		regMountainRight = Assets.instance.levelDecoration.mountainRight;
		
		origin.x = -dimension.x * 2;
		length += dimension.x *2;
	}
	
	private void drawMountain(SpriteBatch sb, float offsetX, float offsetY, float tintColor){
		
		TextureRegion reg = null;
		sb.setColor(tintColor, tintColor, tintColor, 1);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;
		
		//mountains span the whole level
		int mountainLength = 0;
		mountainLength += MathUtils.ceil(length / (2 * dimension.x));
		mountainLength += MathUtils.ceilPositive(0.5f + offsetX);
		
		for(int i=0; i < mountainLength; i++){
			// mountain left
			reg = regMountainLeft;
			sb.draw(reg.getTexture(), origin.x + xRel, position.y + origin.y + yRel, origin.x, origin.y, dimension.x,
					dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
					reg.getRegionHeight(), false, false);
			xRel += dimension.x;

			// mountain right
			reg = regMountainRight;
			sb.draw(reg.getTexture(), origin.x + xRel, position.y + origin.y + yRel, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
			xRel += dimension.x;
		}
		
		// reset color to white
		sb.setColor(1, 1, 1, 1);	
	}
}
