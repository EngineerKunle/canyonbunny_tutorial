package com.teamkunle.canyonbunny.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.helper.AbstractGameObject;

/**
 * Created by EngineerKunle on 30/03/2017.
 */

public class Feather extends AbstractGameObject {

    private static final int SCORE = 250;
    private TextureRegion regFeather;
    public boolean collected;

    public Feather(){
        init();
    }

    @Override
    public void render(SpriteBatch sb) {
        if(collected) return;

        TextureRegion reg = null;
        reg = regFeather;

        sb.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }

    private void init() {
        dimension.set(0.5f, 0.5f);
        regFeather = Assets.instance.feather.feather;

        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }

    public int getScore() {
        return SCORE;
    }

}
