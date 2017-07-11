package com.teamkunle.canyonbunny.utils;

public class ConstantUtils {

	/*Game title*/
	public static String GAME_TITLE = "Canyon-Bunny";
	
	/*View Port*/
	public static float VIEWPORT_WIDTH = 5.0f;
	public static float VIEWPORT_HEIGHT = 5.0f;
	
	/*Settings width and height*/
	public static int SETTINGS_WIDTH = 1024;
	public static int SETTINGS_HEIGHT = 1024;
	
	/*configuration height and width*/
	public static int GLOBAL_WIDTH = 800;
	public static int GLOBAL_HEIGHT = 480;

	// Location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "images/canyonbunny.pack";
	public static final String TEXTURE_ATLAS_UI = "images/canyonbunny-ui.pack";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";

	/*Location of description file for skins*/
	public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
	public static final String SKIN_CANYONBUNNY_UI = "images/canyonbunny-ui.json";
	
	/*levels to add*/
	public static final String LEVEL_01 = "levels/level-01.png";
	
	/*Gui width and height*/
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;

	/*Amount of lives begin*/
	public static final int LIVES_START = 3;

	/*Duration of feather power-up in seconds*/
	public static final float ITEM_FEATHER_POWERUP_DURATION = 9f;

	/*Delay after game over*/
	public static final float TIME_DELAY_GAME_OVER = 3f;

	/*Game preference file*/
	public static final String PREFERENCES = "canyonbunny.prefs";

	/*Debug box 2d feature*/
	public static final boolean DEBUG_DRAW_BOX2D_WORLD = false;

    /* Number of carrots to spawn*/
    public static final int CARROTS_SPAWN_MAX = 100;

    /* Spawn radius for carrots*/
    public static final float CARROTS_SPAWN_RADIUS = 3.5f;

    /*Delay after game finished*/
    public static final float TIME_DELAY_GAME_FINISHED = 6;

	/*Shader effect*/
    public static final String SHADER_MONO_CHROME_VETEX = "shaders/monochrome.vs";
    public static final String SHADER_MONO_CHROME_FRAGEMENT = "shaders/monochrome.fs";

	//Angle of rotation for dead zone (no movement)
    public static final float ACCEL_ANGEL_DEAD_ZONE = 5.0F;

    // Max angel of rotation needed to gain maximum movement velocity
    public static final float ACCEL_MAX_ANGLE_MAX_MOVEMENT = 20.0f;

}
