package com.teamkunle.canyonbunny.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.teamkunle.canyonbunny.assets.Assets;
import com.teamkunle.canyonbunny.audio.AudioManager;
import com.teamkunle.canyonbunny.helper.CharacterSkinHelper;
import com.teamkunle.canyonbunny.screens.transitions.ScreenTransition;
import com.teamkunle.canyonbunny.screens.transitions.ScreenTransitionFade;
import com.teamkunle.canyonbunny.utils.ConstantUtils;
import com.teamkunle.canyonbunny.utils.GamePreferencesUtils;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

public class MenuScreen extends AbstractGameScreen {

    private static final String TAG = MenuScreen.class.getName();

    private Stage stage;
    private Skin skinCanyonBunny;
    private Skin skinLibgdx;

    // menu
    private Image imgBackground;
    private Image imgLogo;
    private Image imgInfo;
    private Image imgCoins;
    private Image imgBunny;
    private Button btnMenuPlay;
    private Button btnMenuOptions;

    // options
    private Window winOptions;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;
    private CheckBox chkSound;
    private Slider sldSound;
    private CheckBox chkMusic;
    private Slider sldMusic;
    private SelectBox<CharacterSkinHelper> selCharSkin;
    private Image imgCharSkin;
    private CheckBox chkShowFpsCounter;
    private CheckBox chkUseMonoChromeShader;

    // debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private boolean debugEnabled = false;
    private float debugRebuildStage;

    public MenuScreen(DirectedGame game) {
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
                rebuildStage();
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
        stage = new Stage(new StretchViewport(ConstantUtils.VIEWPORT_GUI_WIDTH, ConstantUtils.VIEWPORT_GUI_HEIGHT));
        rebuildStage();
    }

    @Override
    public void hide() {
        stage.dispose();
        skinCanyonBunny.dispose();
        skinLibgdx.dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    private void rebuildStage() {
        skinCanyonBunny = new Skin(
                Gdx.files.internal(ConstantUtils.SKIN_CANYONBUNNY_UI),
                new TextureAtlas(ConstantUtils.TEXTURE_ATLAS_UI));
        skinLibgdx = new Skin(Gdx.files.internal(ConstantUtils.SKIN_LIBGDX_UI),
                new TextureAtlas(ConstantUtils.TEXTURE_ATLAS_LIBGDX_UI));

        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerLogos = buildLogosLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();

        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(ConstantUtils.VIEWPORT_GUI_WIDTH,
                ConstantUtils.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerLogos);
        stack.add(layerControls);
        stage.addActor(layerOptionsWindow);
    }

    private Table buildBackgroundLayer() {
        Table layer = new Table();
        // + Background
        imgBackground = new Image(skinCanyonBunny, "background");
        layer.add(imgBackground);
        return layer;
    }

    private Table buildObjectsLayer() {
        Table layer = new Table();
        // + Coins
        imgCoins = new Image(skinCanyonBunny, "coins");
        layer.addActor(imgCoins);
        imgCoins.setOrigin(imgCoins.getWidth() / 2 , imgCoins.getHeight() / 2);
        imgCoins.addAction(sequence(
                moveTo(135, -20),
                scaleTo(0, 0),
                fadeOut(0),
                delay(2.5f),
                parallel(moveBy(0, 100, 0.5f, Interpolation.swingOut),
                        scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
                        alpha(1.0f, 0.5f))));
        // + Bunny
        imgBunny = new Image(skinCanyonBunny, "bunny");
        layer.addActor(imgBunny);
        imgBunny.addAction(sequence(moveTo(655, 510), delay(4.0f),
                moveBy(-70, -100, 0.5f, Interpolation.fade),
                moveBy(-100, -50, 0.5f, Interpolation.fade),
                moveBy(-150, -300, 1.0f, Interpolation.bounceOut)));
        return layer;
    }

    private Table buildLogosLayer() {
        Table layer = new Table();
        layer.left().top();
        // + Game Logo
        imgLogo = new Image(skinCanyonBunny, "logo");
        layer.add(imgLogo);
        layer.row().expandY();
        // + Info Logos
        imgInfo = new Image(skinCanyonBunny, "info");
        layer.add(imgInfo).bottom();
        if (debugEnabled)
            layer.debug();
        return layer;
    }

    private Table buildControlsLayer() {
        Table layer = new Table();
        layer.right().bottom();
        // + Play Button
        btnMenuPlay = new Button(skinCanyonBunny, "play");
        layer.add(btnMenuPlay);
        btnMenuPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        layer.row();
        // + Options Button
        btnMenuOptions = new Button(skinCanyonBunny, "options");
        layer.add(btnMenuOptions);
        btnMenuOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOptionsClicked();
            }
        });
        if (debugEnabled)
            layer.debug();
        return layer;
    }

    private Table buildOptWinAudioSettings() {
        Table tbl = new Table();
        // + Title: "Audio"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE))
                .colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox, "Sound" label, sound volume slider
        chkSound = new CheckBox("", skinLibgdx);
        tbl.add(chkSound);
        tbl.add(new Label("Sound", skinLibgdx));
        sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldSound);
        tbl.row();
        // + Checkbox, "Music" label, music volume slider
        chkMusic = new CheckBox("", skinLibgdx);
        tbl.add(chkMusic);
        tbl.add(new Label("Music", skinLibgdx));
        sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldMusic);
        tbl.row();
        return tbl;
    }

    private Table buildOptWinSkinSelection() {
        Table tbl = new Table();
        // + Title: "Character Skin"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Character Skin", skinLibgdx, "default-font", Color.ORANGE)).colspan(2);
        tbl.row();
        // + Drop down box filled with skin items
        selCharSkin = new SelectBox<CharacterSkinHelper>(skinLibgdx);


        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            Array<CharacterSkinHelper> items = new Array<CharacterSkinHelper>();
            CharacterSkinHelper[] arr = CharacterSkinHelper.values();
            for (int i = 0; i < arr.length; i++) {
                items.add(arr[i]);
            }
            selCharSkin.setItems(items);
        } else {
            selCharSkin.setItems(CharacterSkinHelper.values());
        }
        selCharSkin.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onCharSkinSelected(((SelectBox<CharacterSkinHelper>) actor).getSelectedIndex());
            }
        });
        tbl.add(selCharSkin).width(120).padRight(20);
        // + Skin preview image
        imgCharSkin = new Image(Assets.instance.bunny.head);
        tbl.add(imgCharSkin).width(50).height(50);
        return tbl;
    }

    private Table buildOptWinDebug() {
        Table tbl = new Table();
        // + Title: "Debug"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Debug", skinLibgdx, "default-font", Color.RED))
                .colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox, "Show FPS Counter" label
        chkShowFpsCounter = new CheckBox("", skinLibgdx);
        tbl.add(new Label("Show FPS Counter", skinLibgdx));
        tbl.add(chkShowFpsCounter);
        tbl.row();

        chkUseMonoChromeShader = new CheckBox("", skinLibgdx);
        tbl.add(new Label("User Monochrome shader", skinLibgdx));
        tbl.add(chkUseMonoChromeShader);
        tbl.row();
        return tbl;
    }

    private Table buildOptWinButtons() {
        Table tbl = new Table();
        // + Separator
        Label lbl = null;
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.75f, 0.75f, 0.75f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
        tbl.row();
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.5f, 0.5f, 0.5f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
        tbl.row();
        // + Save Button with event handler
        btnWinOptSave = new TextButton("Save", skinLibgdx);
        tbl.add(btnWinOptSave).padRight(30);
        btnWinOptSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });
        // + Cancel Button with event handler
        btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
        tbl.add(btnWinOptCancel);
        btnWinOptCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onCancelClicked();
            }
        });
        return tbl;
    }

    private Table buildOptionsWindowLayer() {
        winOptions = new Window("Options", skinLibgdx);
        // + Audio Settings: Sound/Music CheckBox and Volume Slider
        winOptions.add(buildOptWinAudioSettings()).row();
        // + Character Skin: Selection Box (White, Gray, Brown)
        winOptions.add(buildOptWinSkinSelection()).row();
        // + Debug: Show FPS Counter
        winOptions.add(buildOptWinDebug()).row();
        // + Separator and Buttons (Save, Cancel)
        winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

        // Make options window slightly transparent
        winOptions.setColor(1, 1, 1, 0.8f);
        // Hide options window by default
        showOptionsWindow(false, false);
        if (debugEnabled)
            winOptions.debug();
        // Let TableLayout recalculate widget sizes and positions
        winOptions.pack();
        // Move options window to bottom right corner
        winOptions.setPosition(
                ConstantUtils.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
        return winOptions;
    }

    private void onPlayClicked() {
        ScreenTransition transition = ScreenTransitionFade.init(0.75f);
        game.setScreen(new GameScreen(game), transition);
    }

    private void onOptionsClicked() {
        loadSettings();
        showMenuButtons(false);
        showOptionsWindow(true, true);
    }

    private void onSaveClicked() {
        saveSettings();
        onCancelClicked();
        AudioManager.instance.onSettingsUpdated();
    }

    private void onCancelClicked() {
        showMenuButtons(true);
        showOptionsWindow(false, true);
        AudioManager.instance.onSettingsUpdated();
    }

    private void showMenuButtons(boolean visible) {
        float moveDuration = 1.0f;
        Interpolation moveEasing = Interpolation.swing;
        float delayOptionsButton = 0.25f;

        float moveX = 300 * (visible ? 1 : -1);
        float moveY = 0 * (visible ? -1 : 1);
        final Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;

        btnMenuPlay.addAction(moveBy(moveX, moveY, moveDuration, moveEasing));
        btnMenuOptions.addAction(sequence(delay(delayOptionsButton),
                moveBy(moveX, moveY, moveDuration, moveEasing)));

        SequenceAction seq = new SequenceAction();

        if (visible) {
            seq.addAction(delay(delayOptionsButton + moveDuration));
            seq.addAction(run(new Runnable() {
                public void run() {
                    btnMenuPlay.setTouchable(touchEnabled);
                    btnMenuOptions.setTouchable(touchEnabled);
                }
            }));
            stage.addAction(seq);
        }
    }

    private void showOptionsWindow(boolean visible, boolean animated) {
        float alphaTo = visible ? 0.8f : 0.0f;
        float duration = animated ? 1.0f : 0.0f;
        Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;

        winOptions.addAction(sequence(touchable(touchEnabled), alpha(alphaTo, duration)));
    }

    private void onCharSkinSelected(int index) {
        CharacterSkinHelper skin = CharacterSkinHelper.GRAY.values()[index];
        imgCharSkin.setColor(skin.getColor());
    }

    private void loadSettings() {
        GamePreferencesUtils prefs = GamePreferencesUtils.instance;
        prefs.load();
        chkSound.setChecked(prefs.sound);
        sldSound.setValue(prefs.volSound);
        chkMusic.setChecked(prefs.music);
        sldMusic.setValue(prefs.volMusic);
        sldMusic.setValue(prefs.volMusic);
        selCharSkin.setSelectedIndex(prefs.charSkin);
        onCharSkinSelected(prefs.charSkin);
        chkShowFpsCounter.setChecked(prefs.showFpsCounter);
        chkUseMonoChromeShader.setChecked(prefs.useMonochromeShader);
    }

    private void saveSettings() {
        GamePreferencesUtils prefs = GamePreferencesUtils.instance;
        prefs.sound = chkSound.isChecked();
        prefs.volSound = sldSound.getValue();
        prefs.music = chkMusic.isChecked();
        prefs.volMusic = sldMusic.getValue();
        prefs.charSkin = selCharSkin.getSelectedIndex();
        prefs.showFpsCounter = chkShowFpsCounter.isChecked();
//        prefs.useMonochromeShader = chkUseMonoChromeShader.isChecked(); This is to stop the grey shader when game start
        prefs.useMonochromeShader = false;
        prefs.save();
    }
}
