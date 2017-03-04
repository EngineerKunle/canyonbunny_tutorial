package assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.teamkunle.canyonbunny.utils.ConstantUtils;

public class Assets implements Disposable, AssetErrorListener{
	private AssetManager assetmanager;
	public static final String TAG = Assets.class.getSimpleName();
	public static final Assets instance = new Assets();
	
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
}
