package com.teamkunle.canyonbunny.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.teamkunle.canyonbunny.utils.ConstantUtils;

/**
 * Created by EngineerKunle on 17/04/2017.
 */

public class MenuScreen extends AbstractGameScreen {

    private static final String TAG = MenuScreen.class.getSimpleName();
    private Stage stage;
    private Skin skinCanyonBunny;

    private Image imgBackground, imgLogo, imgInfo, imgCoins, imgBunny, imgCharSkin;

    private Button btnMenu, btnOptions;

    private Window winOptions;

    private TextButton btnWinOptSave, btnWinOptCancel;

    private CheckBox chkSound, chkShowFpsCounter;

    private Slider sldSound;

//    private SelectBox<CharacterSkin> selCharSkin;

    //debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private boolean debugEnabled = false;
    private float debugRebuildStage;


    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (debugEnabled) {
            debugRebuildStage -= dt;
            if (debugRebuildStage <= 0) {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                reBuildStage();
            }
        }

        stage.act(dt);
        stage.draw();
        stage.setDebugAll(true);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(ConstantUtils.VIEWPORT_WIDTH,
                ConstantUtils.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        reBuildStage();
    }

    @Override
    public void hide() {
        stage.dispose();
        skinCanyonBunny.dispose();
    }

    @Override
    public void pause() {
    }

    private void reBuildStage() {
        skinCanyonBunny = new Skin(Gdx.files.internal(ConstantUtils.SKIN_CANYONBUNNY_UI),
                new TextureAtlas(ConstantUtils.TEXTURE_ATLAS_LIBGDX_UI));

        //build table layers
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerLogos = buildLogoLayers();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();

        //assemble stage for menu
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(ConstantUtils.VIEWPORT_GUI_WIDTH,
                ConstantUtils.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerLogos);
        stack.add(layerControls);
        stack.add(layerOptionsWindow);
    }

    private Table buildOptionsWindowLayer() {
        Table layer = new Table();
        return layer;
    }

    private Table buildControlsLayer() {
        Table layer = new Table();
        layer.right().bottom();

        //player bottom
        btnMenu = new Button(skinCanyonBunny, "play");
        layer.add(btnMenu);
        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        layer.row();
        btnOptions = new Button(skinCanyonBunny, "options");
        btnOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOptionsClicked();
            }
        });
        if (debugEnabled) layer.debug();
        return layer;
    }

    private void onOptionsClicked() {
    }

    private void onPlayClicked() {
        game.setScreen(new GameScreen(game));
    }

    private Table buildLogoLayers() {
        Table layer = new Table();
        layer.left().top();
        // Game logo
        imgLogo = new Image(skinCanyonBunny, "logo");
        layer.add(imgLogo);
        layer.row().expandY();
        //info logo
        imgInfo = new Image(skinCanyonBunny, "info");
        layer.add(imgInfo).bottom();
        if (debugEnabled) layer.debug();
        return layer;
    }

    private Table buildObjectsLayer() {
        Table layer = new Table();
        //coins
        imgCoins = new Image(skinCanyonBunny, "coins");
        layer.addActor(imgCoins);
        imgCoins.setPosition(135, 80);
        //bunny
        imgBunny = new Image(skinCanyonBunny, "bunny");
        layer.addActor(imgBunny);
        imgBunny.setPosition(355, 40);
        return layer;
    }

    private Table buildBackgroundLayer() {
        Table layer = new Table();
        imgBackground = new Image(skinCanyonBunny, "background");
        layer.add(imgBackground);
        return layer;
    }

}
