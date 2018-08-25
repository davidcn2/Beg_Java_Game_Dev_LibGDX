package balloonbuster_enhanced;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class BalloonBuster_Enhanced {

    public static void main(String[] args) {
    
        // The function launches the application starting with the main menu.
        
        // Create the initial object.
        BalloonGame myProgram = new BalloonGame();
        
        // Create and launch the Lwjgl application.
        // Use properties from the myProgram object.
        LwjglApplication launcher = new LwjglApplication( myProgram );
        
    }
    
}
