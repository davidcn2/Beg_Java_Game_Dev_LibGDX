package balloonbuster_more_enhanced;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.forceExit = false; // Prevent default behavior of LWJGL 2 backend calling System.exit(-1).
		new LwjglApplication(new BalloonGameTable(), config);
	}
}
