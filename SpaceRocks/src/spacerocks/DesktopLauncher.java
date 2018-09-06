package spacerocks;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
        @SuppressWarnings("ResultOfObjectAllocationIgnored")
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// change configuration settings
		config.width = 800;
		config.height = 600;
		config.title = "Space Rocks!";
                config.forceExit = false; // Prevent default behavior of LWJGL 2 backend calling System.exit(-1).

		new LwjglApplication(new SpaceRocksGame(), config);
	}
}
