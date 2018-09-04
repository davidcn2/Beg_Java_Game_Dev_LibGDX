package project3d_expanded;

import core.BaseGame;

public class DemoScreenGame extends BaseGame
{
    
    // The class extends the basic functionality of a BaseGame class and creates a new GameScreen object
    // with the expanded 3D game.  The expanded 3D game (Demo Screen) launches when the application starts.
    
    // Methods include:
    
    // create:  Creates a new GameScreen object used to set to the main screen when the
    //          application starts.
    
    @Override
    public void create() 
    {
        
        // The function creates a new DemoScreen object used to go to the main screen
        // when the application starts.
        
        // Create new DemoScreen, which will act as the level object.
        DemoScreen gs = new DemoScreen(this);
        
        // Set the screen to the one just created.
        setScreen( gs );
        
    }
    
}