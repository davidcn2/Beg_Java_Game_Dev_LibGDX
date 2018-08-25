/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balloonbuster;

import com.badlogic.gdx.Game;

public class BalloonGame extends Game { // Extends the Game class from LibGDX.
    
    // The class extends the basic functionality of a Game class and creates a new Screen object
    // with the balloon game.  The balloon game launches when the application starts.
    
    // Methods include:
    
    // create:  Creates a new BalloonLevel object used to set the screen to the balloon game when the
    //          application starts.
    
    @Override
    public void create()
            
    {
        // The function creates a new BalloonLevel object used to set the screen to the balloon game
        // when the application starts.
        
        BalloonLevel z; // BalloonLevel object used to start game.
        
        z = new BalloonLevel(this); // Creates a new CheeseMenu object to start game with main menu.
        setScreen( z ); // Sets the screen to the main menu.
        
    }
    
}
