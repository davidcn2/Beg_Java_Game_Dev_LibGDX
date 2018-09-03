package project3d;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Project3D
{

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String[] args) 
    {
        
        // The function configures and launches the application.
        
        LwjglApplicationConfiguration config; // Application configuration object.
        LwjglApplication launcher; // Application object.
        TheTest myProgram; // Implementation of ApplicationListener.  Main class / loop.
        
        // Create application configuration object.
        config = new LwjglApplicationConfiguration();

	// Change configuration settings.
	config.width = 800; // Set width of application window.
	config.height = 600; // Set height of application window.
	config.title = "The Test!"; // Set title of application.
        
        // Initialize instance of main class.
        myProgram = new TheTest();
        
        // Launch main class.
        launcher = new LwjglApplication( myProgram, config );
        
    }
    
}
