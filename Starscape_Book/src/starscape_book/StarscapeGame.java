package starscape_book;

public class StarscapeGame extends BaseGame
{
    public void create() 
    {  
        // initialize resources common to multiple screens
        
        // initialize and set start screen        
        GameScreen ms = new GameScreen(this);
        setScreen( ms );
    }
}
