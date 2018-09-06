package starscape;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Starscape {

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String[] args) 
    {
        
        // The function configures and launches the application.
        
        LwjglApplicationConfiguration config; // Application configuration object.
        
        // Create application configuration object.
        config = new LwjglApplicationConfiguration();

	// Change configuration settings.
	config.width = 800; // Set width of application window.
	config.height = 600; // Set height of application window.
	config.title = "Starscape!"; // Set title of application.
        config.forceExit = false; // Prevent default behavior of LWJGL 2 backend calling System.exit(-1).
        
        // Launch game using configuration settings.
	new LwjglApplication(new StarscapeGame(), config);
        
    }
    
}
