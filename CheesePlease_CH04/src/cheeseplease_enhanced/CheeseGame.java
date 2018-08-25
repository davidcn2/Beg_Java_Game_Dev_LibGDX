/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cheeseplease_enhanced;

import com.badlogic.gdx.Game;

public class CheeseGame extends Game { // Extends the Game class from LibGDX.
    
    // The class extends the basic functionality of a Game class and creates a new Screen object
    // with the main menu.  The main menu launches when the application starts.
    
    // Methods include:
    
    // create:  Creates a new CheeseMenu object used to set the screen to the main menu when the
    //          application starts.
    
    @Override
    public void create()
            
    {
        // The function creates a new CheeseMenu object used to set the screen to the main menu
        // when the application starts.
        
        CheeseMenu cm; // CheeseMenu object used to start game with main menu.
        
        cm = new CheeseMenu(this); // Creates a new CheeseMenu object to start game with main menu.
        setScreen( cm ); // Sets the screen to the main menu.
    }
    
}