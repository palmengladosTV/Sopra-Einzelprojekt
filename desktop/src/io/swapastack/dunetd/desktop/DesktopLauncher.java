package io.swapastack.dunetd.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.swapastack.dunetd.DuneTD;

public class DesktopLauncher {

	// window settings
	private static final int width = 1600;
	private static final int height = 900;
	private static final boolean vsync = false;

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(width, height);
		config.useVsync(vsync);
		Lwjgl3Application lwjgl3Application = new Lwjgl3Application(new DuneTD(), config);
	}
}
