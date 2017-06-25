package com.teamkunle.canyonbunny.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.audio.AudioManager;
import com.teamkunle.canyonbunny.helper.AbstractGameObject;
import com.teamkunle.canyonbunny.helper.CharacterSkinHelper;
import com.teamkunle.canyonbunny.utils.ConstantUtils;
import com.teamkunle.canyonbunny.utils.GamePreferencesUtils;

/**
 * Created by EngineerKunle on 30/03/2017.
 */

public class BunnyHead extends AbstractGameObject {

    public static final String TAG = BunnyHead.class.getSimpleName();
    public ParticleEffect dustParticles = new ParticleEffect();

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
        TextureRegion reg = null;

        dustParticles.draw(sb);
        sb.setColor(CharacterSkinHelper.values()[GamePreferencesUtils.instance.charSkin].getColor());

        // Set special color when game object
        if (hasFeatherPowerup) {
            sb.setColor(1.0f, 0.8f, 0.0f, 1.0f);
        }

        //Draw image
        reg = regHead;
        sb.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
                viewDirection == VIEW_DIRECTION.RIGHT, false);

        // Reset color to white
        sb.setColor(1, 1, 1, 1);
    }

    @Override
    public void update(float time) {
        super.update(time);

        if (velocity.x != 0) {
            viewDirection = velocity.x < 0 ? VIEW_DIRECTION.RIGHT : VIEW_DIRECTION.LEFT;
        }

        if (timeLeftFeatherPowerup > 0) {
            timeLeftFeatherPowerup -= time;
            if (timeLeftFeatherPowerup < 0) {
                //disable power-up
                timeLeftFeatherPowerup = 0;
                setFeatherPowerUp(false);
            }
        }

        dustParticles.update(time);
    }

    @Override
    protected void updateMotionX(float deltaTime) {
        super.updateMotionX(deltaTime);
    }

    @Override
    protected void updateMotionY(float deltaTime) {
        switch (jumpState) {
            case GROUNDED:
                jumpState = JUMP_STATE.FALLING;
                if (velocity.x != 0) {
                    dustParticles.setPosition(position.x + dimension.x / 2, position.y);
                    dustParticles.start();
                }
                break;

            case JUMP_RISING:
                // Keep track of jump time
                timeJumping += deltaTime;
                // Jump time left ?
                if (timeJumping <= JUMP_TIME_MAX) {
                    // Still jumping
                    velocity.y = terminalVelocity.y;
                }
                break;

            case FALLING:
                break;

            case JUMP_FALLING:
                // Add delta times to track jump time
                timeJumping += deltaTime;
                // Jump to minimal height if jump key was pressed too short
                if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
                    // still jumping
                    velocity.y = terminalVelocity.y;
                }
        }
        if (jumpState != JUMP_STATE.GROUNDED) {
            dustParticles.allowCompletion();
            super.updateMotionY(deltaTime);
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

        //dust particles
        dustParticles.load(Gdx.files.internal("particles/dust.pfx"), Gdx.files.internal("particles"));
    }

    public void setFeatherPowerUp(boolean pickedUp) {
        hasFeatherPowerup = pickedUp;
        if (pickedUp) {
            timeLeftFeatherPowerup = ConstantUtils.ITEM_FEATHER_POWERUP_DURATION;
        }
    }

    public boolean hasFeatherPowerUp() {
        return hasFeatherPowerup && timeLeftFeatherPowerup > 0;
    }

    public void setJumping(boolean jumpKeyPressed) {
        switch (jumpState) {
            case GROUNDED: // player standing on platform
                if (jumpKeyPressed) {
                    AudioManager.instance.play(Assets.instance.assetsSounds.jump);
                    // Start counting jump time from the beginning
                    timeJumping = 0;
                    jumpState = JUMP_STATE.JUMP_RISING;
                }
                break;
            case JUMP_RISING: //rising in the air
                if (!jumpKeyPressed) {
                    jumpState = JUMP_STATE.JUMP_FALLING;
                }
                break;
            case FALLING:// Falling down
            case JUMP_FALLING:
                if (jumpKeyPressed && hasFeatherPowerup) {
                    AudioManager.instance.play(Assets.instance.assetsSounds.jumpFeather, 1,
                            MathUtils.random(1.0f, 1.1f));
                    timeJumping = JUMP_TIME_OFFSET_FLYING;
                    jumpState = JUMP_STATE.JUMP_RISING;
                }

        }
    }
}
