package rectangledestroyer;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Launcher
{
    
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
	config.title = "Rectangle Destroyer!"; // Set title of application.
        
        // Launch game using configuration settings.
	new LwjglApplication(new RectangleDestroyerGame(), config);
        
    }
    
}