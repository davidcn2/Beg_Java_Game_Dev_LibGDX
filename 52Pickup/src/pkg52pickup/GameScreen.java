package pkg52pickup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import core.BaseActor;
import core.BaseGame;
import core.BaseScreen;
import java.util.ArrayList;

/*
Interface (implements) vs Sub-Class (extends)...

The distinction is that implements means that you're using the elements of a Java Interface in your
class, and extends means that you are creating a subclass of the class you are extending. You can
only extend one class in your new class, but you can implement as many interfaces as you would like.

Interface:  A Java interface is a bit like a class, except a Java interface can only contain method
signatures and fields. An Java interface cannot contain an implementation of the methods, only the
signature (name, parameters and exceptions) of the method. You can use interfaces in Java as a way
to achieve polymorphism.

Abstract:  Abstract classes are similar to interfaces.  You cannot instantiate them, and they may
contain a mix of methods declared with or without an implementation. However, with abstract classes,
you can declare fields that are not static and final, and define public, protected, and private
concrete methods.

Subclass: A Java subclass is a class which inherits a method or methods from a Java superclass.
A Java class may be either a subclass, a superclass, both, or neither!

Polymorphism:  Polymorphism is the ability of an object to take on many forms. The most common use
of polymorphism in OOP occurs when a parent class reference is used to refer to a child class object.
Any Java object that can pass more than one IS-A test is considered to be polymorphic.

ArrayList supports dynamic arrays that can grow as needed.
*/

public class GameScreen extends BaseScreen { // Extends the BaseScreen class.
    
    /* 
    The class extends the basic functionality of a BaseScreen class and stores information
    about the game -- background, cards, piles, glow effect, and related properties (map height and width, 
    hint timer, ...).
    
    Methods include:
    
    // create:  Sets defaults.  Configures and adds the Actors to the stage.
    // keyDown:  The function gets called when the user presses a key.  Pauses the game when pressing the
    //           P key.  Resets the game when pressing the R key.
    // update:  Occurs during the update phase (render method) and contains code related to game logic.
    */
    
    private BaseActor background; // BaseActor that will act as the background object.
    private ArrayList<Card> cardList; // Array containing the fifty-two cards.
    private ArrayList<Pile> pileList; // Array containing the four piles to which the player will drag cards.
    private BaseActor glowEffect; // BaseActor that will act as the glow effect hint to the player.
    private float hintTimer;
    
    // Game world dimensions.
    final int mapWidth;
    final int mapHeight;
    
    public GameScreen(BaseGame g)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Sets game world dimensions.
        // 3.  Calls the create() function to perform remaining startup logic.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(g, 800, 600);
        
        // Set game world dimensions.
        this.mapWidth = 800;
        this.mapHeight = 600;
        
        // Configure and add the actors to the stage:  background, ...
        create();
        
    }
    
    public final void create()
    {
        
        /*
        The function occurs during the startup / create phase and accomplishes the following:
        
        // 1.  Sets defaults and initializes empty array lists.
        // 2.  Sets up background object.
        // 3.  Sets up pile objects and populates array.
        // 4.  Sets up card objects and populates array.
        // 5.  Sets up glow effect object.
        */
        
        String fileName; // Name of image to assign to current card in loop.
        String[] rankNames; // Array of card rank names.
        String[] suitNames; // Array of card suit names.
        //Card card; // Reference to each card.
        Pile pile; // Reference to each card pile.
        Texture glowTex; // Texture (image) to use for the glow effect.
        Texture pileTex; // Texture (image) to use for the tiles.
        
        // Create the background object.
        background = new BaseActor();
        
        // Set the properties for the background object and add to the scene graph.
        background.setTexture( new Texture(Gdx.files.internal("assets/felt.jpg")) ); // Assign and set properties of texture.
        mainStage.addActor(background); // Add Actor to scene graph.
    
        /*
        Initialize the pile objects.
        
        Indicate pile locations by showing backs of playing cards.
        Set pile size to slightly larger than that of cards to make for clear identification,
        even with cards on top.
        Equally space piles along top of screen.
        Set up rectangular boundary for purposes of collision detection later.
        */
        
        // Create array of piles.
        // pileList = new ArrayList<Pile>();
        pileList = new ArrayList<>();
        
        // Load image used for each pile to buffer.
        pileTex = new Texture(Gdx.files.internal("assets/cardBack.png"));
        
        // Loop through piles.
        for (int n = 0; n < 4; n++)
            {
            pile = new Pile(); // Create pile.
            pile.setTexture( pileTex ); // Assign and set properties of texture.
            pile.setWidth(120); // Set width of Actor.
            pile.setHeight(140); // Set height of Actor.
            pile.setOriginCenter(); // Set origin of actor to the center of associated image -- for rotation.
            pile.setPosition(70 + 180 * n, 400); // Set position of bottom left corner of actor.
            pile.setRectangleBoundary(); // Set bounding rectangle based on texture dimensions.
            pileList.add( pile ); // Add pile to array.
            mainStage.addActor( pile ); // Add Actor to scene graph.
            }
        
        /*
        Initialize the card objects.
        
        Arrays will be used to contain the names of the various ranks and suits, for use in initializing 
        the Card data as well as constructing the file name of the associated image.  The most subtle 
        part of the code involves setting the z-index of each card, which controls the order in which they
        are rendered, and can be done only after an Actor is added to a Stage.  Actors with lower z-index 
        values render before actors with higher values, and thus appear “beneath” them on the screen.
        
        The reason to set the z-index of Cards to the particular value 5 is so they will render after the 
        background object and the four piles, which have z-indices 0 through 4.  The background and piles
        have the lower values because they were the first five objects added to this stage.
        */
        
        // Store the card rank and suit names.
        rankNames = new String[] {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        suitNames = new String[] {"Clubs", "Hearts", "Spades", "Diamonds"};
        
        // Instantiate the card array.
        // cardList = new ArrayList<Card>();
        cardList = new ArrayList<>();
        
        // Store the card properties, assign images, and add to the scene graph.
        // Loop through card ranks.
        // for (int r = 0; r < rankNames.length; r++)
        for (String rankName : rankNames) 
            {
            // Loop through card suits.
            // for (int s = 0; s < suitNames.length; s++)
            for (String suitName : suitNames) {
                
                // Create card with current rank and suit.
                // Card card = new Card( rankNames[r], suitNames[s] );
                Card card = new Card(rankName, suitName);
                
                // Store name of file to assign to current card in loop.
                // String fileName = "assets/card" + suitNames[s] + rankNames[r] + ".png";
                fileName = "assets/card" + suitName + rankName + ".png";
                
                 // Load image to buffer, assign texture to Actor, and set properties.
                card.setTexture( new Texture(Gdx.files.internal(fileName)) );
                
                card.setWidth(80); // Set width of card.
                card.setHeight(100); // Set height of card.
                card.setOriginCenter(); // Set origin of actor to the center of associated image -- for rotation.
                card.setRectangleBoundary(); // Set bounding rectangle based on texture dimensions.
                cardList.add(card); // Add card to array.
                
                // Add touchDown, touchDragged InputListener, and touchUp InputListener methods to card Actor.
                card.addListener(
                    new InputListener() 
                            
                        {
                        
                            // event = Type of input event.
                            // x = The x coordinate of the mouse click -- origin is in the upper left corner.
                            // y = The y coordinate of the mouse click -- origin is in the upper left corner.
                            // button = The button clicked.
                            @Override
                            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
                            {
                                
                                /*
                                The event occurs when a finger went down on the screen or a mouse button was pressed. 
                                When the player first touches a Card (handled by the touchDown method), if the card
                                is not draggable, then exit the method and do not process any other input actions
                                for this card.  Otherwise, move the card to the top of the rendering order and store
                                the related movement data: the position on the Card that was touched, as well as the
                                original location of the Card on the Stage.
                                */
                                
                                // If card NOT draggable, then...
                                if (!card.dragable)
                                    // Card is NOT draggable.
                                    // Exit function.
                                    return false;

                                card.setZIndex(1000); // Render currently dragged card on top.
                                card.offsetX = x; // Store x-coordinate of the point where the player first touched the card.
                                card.offsetY = y; // Store y-coordinate of the point where the player first touched the card.
                                card.originalX = event.getStageX(); // Store original (x) location of the card on the Stage.
                                card.originalY = event.getStageY(); // Store original (y) location of the card on the Stage.
                                return true; // Return true to indicate event was handled and Multiplexer does not need to send to next InputProcessor.
                                
                            }

                            // event = Type of input event.
                            // x = The x coordinate of the mouse click -- origin is in the upper left corner.
                            // y = The y coordinate of the mouse click -- origin is in the upper left corner.
                            // pointer = Index of the pointer used in the drag operation.
                            @Override
                            public void touchDragged(InputEvent event, float x, float y, int pointer) 
                            {
                                
                                /*
                                The event occurs when a finger is being dragged over the screen or the mouse is dragged
                                while a button is pressed.
                                When the player drags a Card (handled by the touchDragged method), move the card
                                to a new position.  However, instead of moving the lower-left corner of the card
                                to the touch position, use the position on the card that was initially touched 
                                (stored in offsetX and offsetY).  Therefore, take the offsetX and offsetY values
                                into account when using the moveBy method of the card.
                                */
                                
                                // If card NOT draggable, then...
                                if (!card.dragable)
                                    // Card is NOT draggable.
                                    // Exit function.
                                    return;

                                // Move the card by adding / subtracting from current position.
                                card.moveBy(x - card.offsetX, y - card.offsetY);
                                
                            }

                            // event = Type of input event.
                            // x = The x coordinate of the mouse click -- origin is in the upper left corner.
                            // y = The y coordinate of the mouse click -- origin is in the upper left corner.
                            // button = The button clicked.
                            @Override
                            public void touchUp(InputEvent event, float x, float y, int pointer, int button) 
                            {
                                
                                /*
                                The event occurs when a finger was lifted from the screen or a mouse button 
                                was released.
                                When the player releases a Card (handled by the touchUp method), a variety of
                                actions could take place.  First, check whether the card overlaps any of the Pile
                                objects.  If the card is overlapping a pile and is the next card in sequence
                                (same suit, next greater rank index), then add an Action that slides the card
                                to the center of the pile, update the pile data, and lock the card in place by setting
                                dragable to false.  If the card overlaps one or more piles but is not the next card
                                in sequence for any of them, then add an Action that slides the card back to
                                its original position (to prevent the card from obstructing any part of the piles). 
                                If not overlapping any Pile objects when released, simply leave the card in the new 
                                position, adjusting only if part of the card is off-screen.
                                */

                                boolean overPile; // Whether card overlaps current pile in list.
                                float targetX; // Location to which to move card (x-coordinate).
                                float targetY; // Location to which to move card (y-coordinate).
                                
                                // Set defaults.
                                overPile = false;
                                
                                // Loop through piles in list.
                                for (Pile pile : pileList)
                                {
                                    // If card overlaps current pile, then...
                                    if ( card.overlaps(pile, false) )
                                    {
                                        // Set card as overlapping current pile.
                                        overPile = true;
                                        
                                        // If card is the next in the sequence (same suit, next greater rank
                                        // index), then...
                                        if ( card.getRankIndex() == pile.getRankIndex() + 1 
                                          && card.getSuit().equals( pile.getSuit() ) )
                                            {
                                            // Card is the next in the sequence (same suit, next greater rank index).
                                            
                                            // Set location to which to move card -- center of pile.
                                            targetX = pile.getX() + pile.getOriginX() - card.getOriginX();
                                            targetY = pile.getY() + pile.getOriginY() - card.getOriginY();
                                            
                                            // Flag card as no longer dragable.
                                            card.dragable = false;
                                            
                                            // Create action to move card (to center of pile).
                                            card.addAction( Actions.moveTo( targetX, targetY, 0.5f ) );
                                            
                                            // Add card to pile.
                                            pile.addCard(card);
                                            
                                            // Exit loop and function.
                                            return;
                                            }
                                    }
                                }

                                // If overlapping a pile, but not next in the sequence, then...
                                if (overPile) // overlapping but not right one, move off the pile
                                    // Overlapping a pile, but not next in the sequence.
                                    // Move card away from the pile.
                                    // Add action that moves card to original position before most recent movement.
                                    card.addAction( Actions.moveTo( 
                                            card.originalX - card.offsetX, card.originalY - card.offsetY, 0.5f ) );

                                // If left portion of card outside of screen, then...
                                if ( card.getX() < 0 )
                                    // Left portion of card outside of screen.
                                    // Adjust card position so that left edge aligns with screen.
                                    card.setX(0);
                                
                                // If right portion of card outside of screen, then...
                                if ( card.getX() + card.getWidth() > mapWidth )
                                    // Right portion of card outside of screen.
                                    // Adjust card position so that right edge aligns with screen.
                                    card.setX(mapWidth - card.getWidth());
                                
                                // If bottom portion of card outside of screen, then...
                                if ( card.getY() < 0 )
                                    // Bottom portion of card outside of screen.
                                    // Adjust card position so that bottom edge aligns with screen.
                                    card.setY(0);
                                
                                // If top portion of card outside of screen, then...
                                if ( card.getY() + card.getHeight() > mapHeight )
                                    // Top portion of card outside of screen.
                                    // Adjust card position so that top edge aligns with screen.
                                    card.setY(mapHeight - card.getHeight());
                            } // End of touchUp event.
                        } // End of InputListener.
                ); // End of AddListener for Card.
                
                mainStage.addActor(card); // Add Actor to scene graph.
                card.setZIndex(5); // Set z-index to ensure that cards to render after background and piles.
                // Cards created later should render earlier (on bottom).
                
            } // End ... Loop through card suits.
        } // End ... Loop through card ranks.
        
        /*
        Position cards.
        
        When the game starts, the Aces should be positioned on top of the four piles.  Scatter all other cards
        about the screen.  Iterate over cardList, and when the card has rank A, locate amd move the card to the 
        the first empty pile.  If the card has any other rank, randomize its position on the lower half of the
        screen.
        */
        
        // Loop through all cards.
        for (Card card : cardList)
            {
            // If current card an Ace, then... 
            if ( card.getRank().equals("A") )
                {
                // Current card in loop an Ace.
                    
                // Loop through piles.
                for (Pile currPile : pileList)
                    {
                    // If current pile in loop empty, then...
                    if ( currPile.isEmpty() )
                        {
                        // Current pile in loop empty.
                            
                        // Center card within pile.
                        card.moveToOrigin( currPile );
                        
                        // Add card to current pile.
                        currPile.addCard( card );
                        
                        // Flag card as not dragable.
                        card.dragable = false;
                        
                        // Exit loop.
                        break;
                        } // End ... If current pile in loop empty.
                    } // End ... Loop through piles.
                } // End ... If card an Ace.
            
            // Otherwise -- current card not an Ace, ...
            else
                {
                // Current card NOT an Ace.
                    
                // Set position of card to random position in bottom half of screen.
                card.setPosition( MathUtils.random(720), MathUtils.random(200) );
                }
            
            } // End ... Loop through cards.
        
        // Create the glow effect Actor.
        glowEffect = new BaseActor();
        
        // Set the properties for the glow effect object and add to the scene graph.
        glowTex = new Texture(Gdx.files.internal("assets/glowBlue.png")); // Load image to buffer.
        glowEffect.setTexture( glowTex ); // Assign texture and set properties.
        glowEffect.setWidth( cardList.get(0).getWidth() * 1.5f ); // Set width of glow effect Actor. to 1.5x that of card.
        glowEffect.setHeight( cardList.get(0).getHeight() * 1.5f ); // Set height of glow effect Actor to 1.5x that of card.
        glowEffect.setOriginCenter(); // Set origin of actor to the center of associated image -- for rotation.
        glowEffect.addAction( // Add action to glow effect Actor to fade in and out.
        Actions.forever( Actions.sequence( Actions.fadeOut(0.5f), Actions.fadeIn(0.5f) ) ) );
        glowEffect.setVisible( false ); // Set glow effect Actor to start out as invisible.
        mainStage.addActor( glowEffect ); // Add actor to scene graph.
        hintTimer = 0; // Default timer related to showing glow effect to 0.
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {
        
        /*
        The function occurs during the update phase (render method) and accomplishes the following:
        
        1.  Adds the amount of time passed since the last update to the hint timer.
        2.  If a touch event occurred, resets the hint timer.
        3.  If three seconds passed since last hint / touch event (and glow effect not already visible), show
            glow effect around next card in list.
        */
        
        // Add the amount of time passed since the last update to the hint timer.
        hintTimer += dt;
        
        // If a touch event occurred, reset the hint timer.
        
        // If a touch event occurred, then...
        if ( Gdx.input.isTouched() )
            {
            // Touch event occurred.
                
            // Reset the hint timer.
            hintTimer = 0;
            
            // Hide the glow effect.
            glowEffect.setVisible(false);
            }
        
        // Determine whether to activate hint mechanic.
        // Show the hint around the next card in the list (ordered by rank / suit).
        
        // If three seconds passed since last hint / touch event (and glow effect not already visible), then...
        if ( hintTimer > 3 && !glowEffect.isVisible() )
            {
            // Three seconds passed since last hint / touch event (and glow effect not already visible).
                
            // Loop through cards.
            for (Card hintCard : cardList)
                {
                // If current card in loop dragable, then...
                if ( hintCard.dragable )
                    {
                    // Current card in loop dragable.
                    glowEffect.setVisible(true); // Show glow effect.
                    glowEffect.moveToOrigin( hintCard ); // Center card within glow effect.
                    glowEffect.toFront(); // Adjust z-index of glow effect to front of other objects.
                    hintCard.toFront(); // Adjust z-index of card to front of other objects.
                    break; // Exit loop.
                    }
                } // End ... Loop through cards.
            } // End ... If three seconds passed since last hint / touch event (and glow effect not already visible).
        
    }
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.
        // Pauses the game when pressing the P key.
        
        // If the user pressed the P key, then...
        if (keycode == Input.Keys.P)
        {
            // The user pressed the P key.
            
            // Pause the game.
            togglePaused();
        }
        
        else if (keycode == Input.Keys.R)
        {
            // The user pressed the R key.
            
            // Reset the game.
            game.setScreen( new GameScreen(game) );
        }
            
        // Return a value.
        return false;
        
    }
    
}
