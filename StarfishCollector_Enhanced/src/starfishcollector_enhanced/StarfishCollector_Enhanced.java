package starfishcollector_enhanced;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class StarfishCollector_Enhanced {
    
    public static void main(String[] args) {
        
        // The function launches the application starting with the main game.
        
        // Create the initial object.
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        
        // Change configuration settings.
        config.width = 800; // Set width of application window.
        config.height = 600; // Set height of application window.
        config.title = "Starfish Collector"; // Set title of application.
        config.forceExit = false; // Prevent default behavior of LWJGL 2 backend calling System.exit(-1).
        
        // Create the initial object.
        TurtleGame myProgram = new TurtleGame();
        
        // Create and launch the Lwjgl application.
        // Use properties from the myProgram object.
        LwjglApplication launcher = new LwjglApplication( myProgram, config );
        
    }
    
}
