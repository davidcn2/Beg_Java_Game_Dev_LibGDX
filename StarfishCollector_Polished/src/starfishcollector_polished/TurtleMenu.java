/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starfishcollector_polished;

import core.BaseScreen;
import core.BaseGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/*
Interface (implements) vs Sub-Class (extends)...

The distinction is that implements means that you're using the elements of a Java Interface in your 
class, and extends means that you are creating a subclass of the class you are extending. You can 
only extend one class in your new class, but you can implement as many interfaces as you would like.

Interface:  A Java interface is a bit like a class, except a Java interface can only contain method
signatures and fields. An Java interface cannot contain an implementation of the methods, only the 
signature (name, parameters and exceptions) of the method. You can use interfaces in Java as a way
to achieve polymorphism.

Subclass: A Java subclass is a class which inherits a method or methods from a Java superclass.
A Java class may be either a subclass, a superclass, both, or neither!

Polymorphism:  Polymorphism is the ability of an object to take on many forms. The most common use 
of polymorphism in OOP occurs when a parent class reference is used to refer to a child class object.
Any Java object that can pass more than one IS-A test is considered to be polymorphic.

ArrayList supports dynamic arrays that can grow as needed.
*/

public class TurtleMenu extends BaseScreen { // Extends the BaseScreen class.
    
    // The class extends the basic functionality of a BaseScreen class and sets up the main menu screen.
    
    // Methods include:
    
    // create:  Calls constructor for BaseScreen and configures and adds objects in the title screen.
    // update:  Occurs during the update phase (render method) and contains code related to game logic.
    
    public TurtleMenu(BaseGame g)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Calls the function for the create phase -- configures and adds objects in the title screen.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        // Sets window width and height to 800 by 600.
        super(g, 800, 600);
        
        // Configure and add the objects in the title screen.
        create();
        
    }
    
    public final void create()
    {
    
        /*
        The function occurs during the startup / create phase and accomplishes the following:
        
        1.  Loads, configures, and adds the water graphic to the Skin.
        2.  Sets the background in the uiTable to the water graphic.
        3.  Loads, configures, and adds the "StaRfISH COllectOR" title graphic to the Skin.
        4.  Loads, configures, and adds the "created with libGDX" title graphic to the Skin.
        5.  Creates, configures, and adds input listener for start and quit buttons.
        6.  Builds uiTable with "StaRfISH COllectOR", start and quit buttons, and "created with libGDX".
        */
        
        // Note:  Images support the Drawable interface, unlike Textures.  Tables require the Drawable interface.
        
        float w; // Width of start button (image).
        
        Image libgdxImage; // Image object with "created with libGDX" text.
        Image titleImage; // Image object with "StaRfISH COllectOR" text.
        TextButton quitButton; // TextButton object used to quit the game.
        TextButton startButton; // TextButton object used to start the game.
        Texture libgdxTex; // Texture object with "created with libGDX" text.
        Texture titleTex; // Texture object with "StaRfISH COllectOR" text.
        Texture waterTex; // Texture object with water graphic used for ocean.
        
        // Load water graphic used for ocean into Texture object.
        waterTex = new Texture(Gdx.files.internal("assets/images/water.jpg"));
        
        // To ensure that image scales smoothly, specify linear filtering.
        waterTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        // Add the Texture with the water graphic to the Skin.
        game.skin.add( "waterTex", waterTex );
        
        // Set background image for Table to water graphic.
        uiTable.background( game.skin.getDrawable("waterTex") );
        
        // Load "StaRfISH COllectOR" title graphic into Texture object.
        titleTex = new Texture(Gdx.files.internal("assets/images/starfish-collector.png"));
        
        // To ensure that image scales smoothly, specify linear filtering.
        titleTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        // Create new image object for "StaRfISH COllectOR" title with associated Texture.
        titleImage = new Image( titleTex );
        
        // Load "created with libGDX" title graphic into Texture object.
        libgdxTex = new Texture(Gdx.files.internal("assets/images/created-libgdx.png"));
        
        // To ensure that image scales smoothly, specify linear filtering.
        libgdxTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        // Create new image object for "created with libGDX" title with associated Texture.
        libgdxImage = new Image( libgdxTex );
        
        // Create textButton object used to start game with the word, Start, using the style, uiTextButtonStyle.
        startButton = new TextButton("Start", game.skin, "uiTextButtonStyle");
        
        // Add an InputListener to the start Button, which will start the game.
        startButton.addListener(
            new InputListener()
                    
            {
                // event = Type of input event.
                // x = The x coordinate of the mouse click -- origin is in the upper left corner.
                // y = The Y coordinate of the mouse click -- origin is in the upper left corner.
                // pointer = Pointer for the event.
                // button = The button clicked.
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
                {
                // Occurs when the user performs a mousedown (click) event on the start button.
                    
                // Return a value.
                return true;
                }
        
                // event = Type of input event.
                // x = The x coordinate of the mouse click -- origin is in the upper left corner.
                // y = The Y coordinate of the mouse click -- origin is in the upper left corner.
                // pointer = Pointer for the event.
                // button = The button clicked.
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                {
                   
                // Switch to the main game.
                game.setScreen( new TurtleLevel(game) );
                }
                
            }
        ); // End of code for adding InputListener to start Button.
        
        // Create textButton object used to quit game with the word, Quit, using the style, uiTextButtonStyle.
        quitButton = new TextButton("Quit", game.skin, "uiTextButtonStyle");
        
        // Add an InputListener to the start Button, which will start the game.
        quitButton.addListener(
            new InputListener()
                    
            {
                // event = Type of input event.
                // x = The x coordinate of the mouse click -- origin is in the upper left corner.
                // y = The Y coordinate of the mouse click -- origin is in the upper left corner.
                // pointer = Pointer for the event.
                // button = The button clicked.
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
                {
                // Occurs when the user performs a mousedown (click) event on the quit button.
                    
                // Return a value.
                return true; 
                }
                
                // event = Type of input event.
                // x = The x coordinate of the mouse click -- origin is in the upper left corner.
                // y = The Y coordinate of the mouse click -- origin is in the upper left corner.
                // pointer = Pointer for the event.
                // button = The button clicked.
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                {
                // Occurs when the user releases the mouse when clicking on the quit button.
                    
                // Exit the application.
                Gdx.app.exit();
                }
                
            }
        ); // End of code for adding InputListener to quit Button.
        
        // Store width of start button (image).
        w = startButton.getWidth();
        
        // Set up Table object with main menu layout.
        
        // Add cell encompassing both columns containing the "StaRfISH COllectOR" image.
        uiTable.add(titleImage).colspan(2);
        
        // Add a row to the table.
        uiTable.row();
        
        // Add cell on the left containing the start button.
        uiTable.add(startButton);
        
        // Add cell on the right containing the quit button with the same width as the start.
        uiTable.add(quitButton).width(w);
        
        // Add a row to the table.
        uiTable.row();
        
        // Add cell encompassing both columns containing the "created with libGDX" image, aligned to the right.
        uiTable.add(libgdxImage).colspan(2).right().padTop(50);
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {
        // The function occurs during the update phase and contains code related to game logic.
    }
    
}
