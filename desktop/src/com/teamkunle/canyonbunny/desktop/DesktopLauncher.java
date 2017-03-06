package com.teamkunle.canyonbunny.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.teamkunle.canyonbunny.Main;
import com.teamkunle.canyonbunny.utils.ConstantUtils;

public class DesktopLauncher {
	private static boolean reBuildAtlas = false;
	private static boolean drawDebugOutline = false;
	
	public static void main (String[] arg) {
		if (reBuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = ConstantUtils.SETTINGS_WIDTH;
			settings.maxHeight = ConstantUtils.SETTINGS_HEIGHT;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "/Users/EngineerKunle/Documents/libgdx/canyonbunny/desktop/assets-raw/images"
					,"../assets/images","canyonbunny.pack");
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = ConstantUtils.GAME_TITLE;
		config.width = ConstantUtils.GLOBAL_WIDTH;
		config.height = ConstantUtils.GLOBAL_HEIGHT;
		new LwjglApplication(new Main(), config);
	}
}
