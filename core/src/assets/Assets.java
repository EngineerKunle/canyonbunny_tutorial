package assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.teamkunle.canyonbunny.utils.ConstantUtils;

public class Assets implements Disposable, AssetErrorListener{
	private AssetManager assetmanager;
	public static final String TAG = Assets.class.getSimpleName();
	public static final Assets instance = new Assets();
	public AssetBunny bunny;
	public AssetRock rock;
	public AssetGoldCoin goldCoin;
	public AssetFeather feather;
	public AssetLevelDecoration levelDecoration;
	
	//singleton, avoid class instance
	private Assets() {}
	
	public void init(AssetManager assetmanager){
		this.assetmanager = assetmanager;
		assetmanager.setErrorListener(this);
		assetmanager.load(ConstantUtils.TEXTURE_ATLAS_OBJECTS, Texture.class);
		assetmanager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: "
		         + assetmanager.getAssetNames().size);
		
		for (String a : assetmanager.getAssetNames())
	         Gdx.app.debug(TAG, "asset: " + a);
		
		TextureAtlas atlas = assetmanager.get(ConstantUtils.TEXTURE_ATLAS_OBJECTS);
		
		//enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		//creating game resources
		bunny = new AssetBunny(atlas);
		rock = new AssetRock(atlas);
		goldCoin = new AssetGoldCoin(atlas);
		feather = new AssetFeather(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		
	}

	@Override
	public void dispose() {
		assetmanager.dispose();
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" +
				   asset.fileName + "'", (Exception)throwable);
	}
	
	public class AssetBunny {
		public final AtlasRegion head;
		
		public AssetBunny(TextureAtlas ta) {
			head = ta.findRegion("bunny_head");
		}
	}
 
	public class AssetRock {
		public final AtlasRegion edge;
		public final AtlasRegion middle;
		
		public AssetRock(TextureAtlas ta) {
			edge = ta.findRegion("rock_edge");
			middle = ta.findRegion("rock_middle");
		}
	}
	
	public class AssetGoldCoin{
		public final AtlasRegion goldCoin;
		
		public AssetGoldCoin(TextureAtlas ta) {
			goldCoin = ta.findRegion("item_gold_coin");
		}
	}
	
	public class AssetFeather{
		public final AtlasRegion feather;
		public AssetFeather(TextureAtlas ta) {
			feather = ta.findRegion("item_feather");
		}
	}
	
	public class AssetLevelDecoration{
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;
		
		public AssetLevelDecoration(TextureAtlas ta) {
			cloud01 = ta.findRegion("cloud01");
			cloud02 = ta.findRegion("cloud02");
			cloud03 = ta.findRegion("cloud03");
			mountainLeft = ta.findRegion("mountain_left");
			mountainRight = ta.findRegion("mountain_right");
			waterOverlay = ta.findRegion("water_overlay");
		}
		
	}
}
