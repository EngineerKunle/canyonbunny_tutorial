package com.teamkunle.canyonbunny.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.helper.AbstractGameObject;

public class Rock extends AbstractGameObject{
	
	private TextureRegion regEdge;
	private TextureRegion regMiddle;

	private final float FLOAT_CYCLE_TIME = 2.0f;
	private final float FLOAT_AMPLITUDE = 0.25f;

	private float floatCycleTimeLeft;

	private boolean floatingDownwards;
	private Vector2 floatTargetPosition;
	
	private int length;

	public Rock() {
		init();
	}

	private void init(){
		dimension.set(1,1.5f);
		regEdge = Assets.instance.rock.edge;
		regMiddle = Assets.instance.rock.middle;
		
		setLength(1);

		floatingDownwards = false;
		floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME / 2);
		floatTargetPosition = null;
	}
	
	public void setLength(int length) {
		this.length = length;
		// Update collision
		bounds.set(0, 0, dimension.x * length, dimension.y);
	}

	@Override
	public void update(float time) {
		super.update(time);

		floatCycleTimeLeft -= time;

		if (floatTargetPosition == null ) floatTargetPosition = new Vector2(position);

		if (floatCycleTimeLeft <= 0) {
			floatCycleTimeLeft = FLOAT_CYCLE_TIME;
			floatingDownwards = !floatingDownwards;
			floatTargetPosition.y += FLOAT_AMPLITUDE * (floatingDownwards ? -1 : 1);
		}
		position.lerp(floatTargetPosition, time);
	}

	public void increaseLength(int amount){
		setLength(length + amount);
	}

	@Override
	public void render(SpriteBatch sb) {
		TextureRegion reg= null;
		
		float relX = 0;
		float relY = 0;
		
		//draw left edge
		reg = regEdge;
		relX -= dimension.x /4;
		
		sb.draw(reg.getTexture(), position.x + relX,
				position.y + relY, origin.x, origin.y, dimension.x / 4, dimension.y, 
					scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
						reg.getRegionWidth(), reg.getRegionHeight(), false,
							false);
		// Draw middle
		relX = 0;
		reg = regMiddle;
		for (int i = 0; i < length; i++) {
			sb.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x,
					dimension.y,
					scale.x, scale.y, rotation, reg.getRegionX(), 
					reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}

		// Draw right edge
		reg = regEdge;
		sb.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x + dimension.x / 8, 
				origin.y, dimension.x / 4,
					dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
			reg.getRegionHeight(), true, false);

	}

}
