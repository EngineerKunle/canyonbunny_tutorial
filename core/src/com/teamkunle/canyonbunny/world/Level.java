package com.teamkunle.canyonbunny.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.teamkunle.canyonbunny.gameobjects.BunnyHead;
import com.teamkunle.canyonbunny.gameobjects.Carrot;
import com.teamkunle.canyonbunny.gameobjects.Clouds;
import com.teamkunle.canyonbunny.gameobjects.Feather;
import com.teamkunle.canyonbunny.gameobjects.Goal;
import com.teamkunle.canyonbunny.gameobjects.GoldCoin;
import com.teamkunle.canyonbunny.gameobjects.Mountains;
import com.teamkunle.canyonbunny.gameobjects.Rock;
import com.teamkunle.canyonbunny.gameobjects.WaterOverlay;
import com.teamkunle.canyonbunny.helper.AbstractGameObject;

public class Level {
	//page 179
	
	public static final String TAG = Level.class.getSimpleName();
	
	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), //BLACK
		GOAL(255, 0, 0), //RED
		ROCK(0, 255, 0), //GREEN
		PLAYER_SPAWNPOINT(255, 255, 255), //white
		ITEM_FEATHER(255, 0, 255), // purple
		ITEM_GOLD_COIN(255, 255, 0); //YELLOW
		
		private int color;
		
		BLOCK_TYPE(int r, int g, int b){
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}
		
		public boolean sameColor (int color) {
			return this.color == color;
		}
		
		public int getColor(){
			return color;
		}
	}

    //objects
    public Array<Rock> rocks;
    public Array<Carrot> carrots;

    public Goal goal;
    public Clouds clouds;
    public Mountains mountains;
    public WaterOverlay waterOverlay;

    //player character
    public BunnyHead bunnyHead;

    public Array<GoldCoin> goldCoins;
    public Array<Feather> feathers;
	
	public Level(String filename) {
		init(filename);
	}

	private void init(String filename){
		//player character
		bunnyHead = null;

		//objects
		rocks = new Array<Rock>();
		goldCoins = new Array<GoldCoin>();
		feathers = new Array<Feather>();
		carrots = new Array<Carrot>();

		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		int lastPixel = -1;
		for(int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++){
			for(int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++){
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				float baseHeight = pixmap.getHeight() - pixelY;
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				if (BLOCK_TYPE.EMPTY.equals(currentPixel)){
					//do nothing for now
				}
				
				// rock
				else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
					if (lastPixel != currentPixel) {
						obj = new Rock();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
						rocks.add((Rock)obj);
					} else {
						rocks.get(rocks.size - 1).increaseLength(1);
					}
				}
				// player spawn point
				else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
					obj = new BunnyHead();
					offsetHeight = -3.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					bunnyHead = (BunnyHead) obj;
				}
				// feather
				else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) {
					obj = new Feather();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					feathers.add((Feather) obj);
				}
				// gold coin
				else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
					obj = new GoldCoin();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					goldCoins.add((GoldCoin) obj);
				}

                //goal
                else if (BLOCK_TYPE.GOAL.sameColor(currentPixel)) {
                    obj = new Goal();
                    offsetHeight = -7.0f;
                    obj.position.set(pixelX, baseHeight + offsetHeight);
                    goal = (Goal) obj;
                }

				// unknown object/pixel color
				else {
					// red color channel
					int r = 0xff & (currentPixel >>> 24);
					// green color channel
					int g = 0xff & (currentPixel >>> 16);
					// blue color channel
					int b = 0xff & (currentPixel >>> 8);
					// alpha channel
					int a = 0xff & currentPixel;
				}
				lastPixel = currentPixel;
			}
		}
		
		//populate 
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0, 2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1,-1);
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		waterOverlay.position.set(0, -3.75f);
		
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}
	
	//draws each object
	public void render(SpriteBatch sb){
		
		mountains.render(sb);
        goal.render(sb);
		for (Rock rock : rocks){
			rock.render(sb);
		}
		for (GoldCoin goldCoin : goldCoins) {
			goldCoin.render(sb);
		}
		for (Feather feather : feathers) {
			feather.render(sb);
		}
        for (Carrot carrot : carrots) {
            carrot.render(sb);
        }
		bunnyHead.render(sb);
		waterOverlay.render(sb);
		clouds.render(sb);
	}

	public void update(float deltaTime) {
		bunnyHead.update(deltaTime);
		clouds.update(deltaTime);

		for (Rock rock : rocks){
			rock.update(deltaTime);
		}
		for (GoldCoin goldCoin : goldCoins) {
			goldCoin.update(deltaTime);
		}
		for (Feather feather : feathers) {
			feather.update(deltaTime);
		}

		for (Carrot carrot : carrots) {
            carrot.update(deltaTime);
        }
	}
	
}
