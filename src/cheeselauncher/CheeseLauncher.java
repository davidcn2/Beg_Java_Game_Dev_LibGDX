/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cheeselauncher;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/**
 *
 * @author dcohn
 */
public class CheeseLauncher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        // Create the initial object.
        CheeseGame myProgram = new CheeseGame();
        
        // Create and launch the Lwjgl application.
        // Use properties from the myProgram object.
        LwjglApplication launcher = new LwjglApplication( myProgram );
        
    }
    
}