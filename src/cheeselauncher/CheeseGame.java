/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cheeselauncher;

import com.badlogic.gdx.Game;

/**
 *
 * @author dcohn
 */
public class CheeseGame extends Game { // Extends the Game class from LibGDX.
    
    @Override
    public void create()
    {
        // The function creates a new CheeseMenu object used to set the screen to the main menu
        // when the application starts.
        
        CheeseMenu cm = new CheeseMenu(this); // Creates a new CheeseMenu object to start game with main menu.
        setScreen( cm ); // Sets the screen to the main menu.
    }
    
}