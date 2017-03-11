package com.teamkunle.canyonbunny.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.teamkunle.canyonbunny.gameobjects.Clouds;
import com.teamkunle.canyonbunny.gameobjects.Mountains;
import com.teamkunle.canyonbunny.gameobjects.Rock;
import com.teamkunle.canyonbunny.helper.AbstractGameObject;

public class Level {
	public static final String TAG = Level.class.getSimpleName();
	//objects
	public Array<Rock> rocks;
	public Clouds clouds;
	public Mountains mountains;
	
	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), //BLACK
		ROCK(0, 255, 0), //GREEN
		PLAYER_SPAWNPOINT(255, 255, 255), //white
		ITEM_FEATHER(255, 0, 255), // purple
		ITEM_GOLD_COIN(255, 255, 0); //YELLOW
		
		private int color;
		
		private BLOCK_TYPE(int r, int g, int b){
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}
		
		public boolean sameColor(int color){
			return this.color == color;
		}
		
		public int getColor(){
			return color;
		}
		
	}
	
	public Level(String filename) {
		init(filename);
	}

	private void init(String filename){
		rocks = new Array<Rock>();
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		int lastPixel = -1;
		for(int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++){
			for(int pixelX = 0; pixelX< pixmap.getWidth(); pixelX++ ){
				AbstractGameObject ogj = null;
			}
		}
		
	}
	public void render(SpriteBatch sp){}
	
}
