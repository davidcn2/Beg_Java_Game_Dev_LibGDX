package pkg52pickup;

import core.BaseGame;

public class pkg52pickupGame extends BaseGame {
    
    // The class extends the basic functionality of a BaseGame class and creates a new GameScreen object
    // with the 52 Card Pickup game.  The 52 Card Pickup game launches when the application starts.
    
    // Methods include:
    
    // create:  Creates a new GameScreen object used to set to the main screen when the
    //          application starts.
    
    @Override
    public void create() 
    {  
        // The function creates a new GameScreen object used to go to the main screen
        // when the application starts.
        
        // Create new GameScreen, which will act as the level object.
        GameScreen gs = new GameScreen(this);
        
        // Set the screen to the one just created.
        setScreen( gs );
    }
    
}