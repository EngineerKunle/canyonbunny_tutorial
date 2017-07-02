package com.teamkunle.canyonbunny.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.helper.AbstractGameObject;

/**
 * Created by EngineerKunle on 01/07/2017.
 */

public class Goal extends AbstractGameObject {

    private TextureRegion regGoal;

    public Goal() {
        init();
    }

    private void init() {
        dimension.set(3.0f, 3.0f);
        regGoal = Assets.instance.levelDecoration.goal;

        // Set bounding for collision detection
        bounds.set(1, Float.MIN_VALUE, 10, Float.MAX_VALUE);
        origin.set(dimension.x / 2.0f, 0.0f);

    }

    @Override
    public void render(SpriteBatch sb) {
        TextureRegion reg = null;

        reg = regGoal;
        sb.draw(reg.getTexture(), position.x - origin.x, position.y - origin.y, origin.x, origin.y, dimension.x, dimension.y,
                scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false,
                false);
    }
}
