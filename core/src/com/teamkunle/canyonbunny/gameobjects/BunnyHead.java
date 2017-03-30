package com.teamkunle.canyonbunny.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.helper.AbstractGameObject;

/**
 * Created by EngineerKunle on 30/03/2017.
 */

public class BunnyHead extends AbstractGameObject {

    public static final String TAG = BunnyHead.class.getSimpleName();

    private final float JUMP_TIME_MAX = 0.3F;
    private final float JUMP_TIME_MIN = 0.1F;
    private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;


    public enum VIEW_DIRECTION {
        LEFT, RIGHT
    }

    public enum JUMP_STATE {
        GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
    }

    private TextureRegion regHead;

    public VIEW_DIRECTION viewDirection;

    public float timeJumping;
    public JUMP_STATE jumpState;

    public boolean hasFeatherPowerup;
    public float timeLeftFeatherPowerup;

    public BunnyHead() {
        init();
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void update(float time) {
        super.update(time);

        if (velocity.x != 0) {
            viewDirection = velocity.x < 0 ? VIEW_DIRECTION.RIGHT : VIEW_DIRECTION.LEFT;
        }

        if (timeLeftFeatherPowerup > 0) {
            timeLeftFeatherPowerup -= 0;
            if (timeLeftFeatherPowerup < 0) {
                //disable power-up
                timeLeftFeatherPowerup = 0;
                setFeatherPowerup(false);
            }
        }
    }

    private void init() {
        dimension.set(1, 1);
        regHead = Assets.instance.bunny.head;

        //center image on game object
        origin.set(dimension.x / 2, dimension.y / 2 );
        bounds.set(0, 0, dimension.x, dimension.y);

        terminalVelocity.set(3.0f, 4.0f);
        friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, -25.0f);

        //View direction
        viewDirection = VIEW_DIRECTION.RIGHT;

        jumpState = JUMP_STATE.FALLING;
        timeJumping = 0;

        hasFeatherPowerup = false;
        timeLeftFeatherPowerup = 0;
    }

    public void setFeatherPowerup(boolean pickedUp) {

    }
}
