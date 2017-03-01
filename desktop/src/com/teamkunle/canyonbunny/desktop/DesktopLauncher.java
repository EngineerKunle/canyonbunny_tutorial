package com.teamkunle.canyonbunny.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.teamkunle.canyonbunny.Main;
import com.teamkunle.canyonbunny.utils.ConstantUtils;

public class DesktopLauncher {
	private static boolean reBuildAtlas = true;
	private static boolean drawDebugOutline = true;
	
	public static void main (String[] arg) {
		if (reBuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = ConstantUtils.SETTINGS_WIDTH;
			settings.maxHeight = ConstantUtils.SETTINGS_HEIGHT;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets-raw/images",
					"../canyonbunny-android/assets/images","canyonbunny.pack");
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = ConstantUtils.GAME_TITLE;
		config.width = ConstantUtils.GLOBAL_WIDTH;
		config.height = ConstantUtils.GLOBAL_HEIGHT;
		new LwjglApplication(new Main(), config);
	}
}
