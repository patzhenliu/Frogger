//DesktopLauncher.java
//Patricia Liu

package com.patricia.frogger.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.patricia.frogger.Frogger;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 690;
		config.height = 785;
		new LwjglApplication(new Frogger(), config);
	}
}
