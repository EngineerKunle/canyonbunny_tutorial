package com.teamkunle.canyonbunny.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.helper.AbstractGameObject;

/**
 * Created by EngineerKunle on 30/03/2017.
 */

public class GoldCoin extends AbstractGameObject {

    private static final int SCORE_COIN = 100;

    public boolean collected;

    public GoldCoin() {
        init();
    }

    private void init() {
        dimension.set(0.5f, 0.5f);

        setAnimation(Assets.instance.goldCoin.animGoldCoin);
        statetime = MathUtils.random(0.0f, 1.0f);

        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (collected) return;

        TextureRegion reg = null;
        reg = (TextureRegion) animation.getKeyFrame(statetime, true);

        sb.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }

    public int getScore(){
        return SCORE_COIN;
    }
}
