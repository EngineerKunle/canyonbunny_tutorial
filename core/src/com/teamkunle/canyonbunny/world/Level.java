package com.teamkunle.canyonbunny.world;

public class Level {
	public static final String TAG = Level.class.getSimpleName();
	
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
		
	}
	public Level() {
		// TODO Auto-generated constructor stub
	}

}
