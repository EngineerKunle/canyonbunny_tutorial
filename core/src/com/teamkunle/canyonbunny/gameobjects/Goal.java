package com.teamkunle.canyonbunny.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.helper.AbstractGameObject;

/**
 * Created by EngineerKunle on 01/07/2017.
 */

public class Goal extends AbstractGameObject {

    //TODO: PAGE 341

    private TextureRegion regGoal;

    public Goal() {
        init();
    }

    private void init() {
        dimension.set(3.0f, 3.0f);
        regGoal = Assets.instance.levelDecoration.goal;

        // Set bounding for collision detection

    }

    @Override
    public void render(SpriteBatch sb) {

    }
}
