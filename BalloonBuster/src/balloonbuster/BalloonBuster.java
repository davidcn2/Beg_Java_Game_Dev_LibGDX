/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balloonbuster;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/**
 *
 * @author dcohn
 */
public class BalloonBuster {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // The function launches the application starting with the main game.
        
        // Create the initial object.
        BalloonGame myProgram = new BalloonGame();
        
        // Create and launch the Lwjgl application.
        // Use properties from the myProgram object.
        LwjglApplication launcher = new LwjglApplication( myProgram );
        
    }
    
}
