package starfishcollector_enhanced;

import com.badlogic.gdx.Game;

public class TurtleGame extends Game // Extends the Game class.
{
    
    // The class extends the basic functionality of a Game class and creates a new level object
    // with the turtle game.  The turtle game launches when the application starts.
    
    // Methods include:
    
    // create:  Creates a new StarfishMenu object used to set the screen to the turtle game when the
    //          application starts.
    
    @Override
    public void create() 
    {  
        
        // The function creates a new StarfishMenu object used to set the screen to the turtle game
        // when the application starts.
        
        StarfishMenu z; // StarfishMenu object used to start game.
        
        z = new StarfishMenu(this); // Creates a new StarfishMenu object to start game.
        setScreen( z ); // Sets the screen to the game.
        
    }
}