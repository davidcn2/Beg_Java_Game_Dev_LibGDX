package cheeseplease_gamepad;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class CheesePlease_Gamepad {

    public static void main(String[] args) {
    
        // The function launches the application starting with the main menu.
        
        // Create the initial object.
        CheeseGame myProgram = new CheeseGame();
        
        // Create and launch the Lwjgl application.
        // Use properties from the myProgram object.
        LwjglApplication launcher = new LwjglApplication( myProgram );
        
    }
    
}