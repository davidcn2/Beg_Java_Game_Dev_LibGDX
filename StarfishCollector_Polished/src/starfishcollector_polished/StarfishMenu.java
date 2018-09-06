/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starfishcollector_polished;

import core.BaseActor;
import core.BaseScreen;
import core.BaseGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

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
*/

public class StarfishMenu extends BaseScreen { // Extends the BaseScreen class.
    
    // The class extends the basic functionality of a BaseScreen class and stores information
    // about the main menu (text and background image).
    
    // Methods include:
    
    // create:  Configures and adds the Actors to the stage:  background, Starfish Collector (title text),
    //          and instructions.
    // keyDown:  The function gets called when the user presses a key.  Sets the screen to the main game
    // when pressing the S key.
    // update:  Occurs during the update phase (render method) and contains code related to game logic.
    
    // g = Screen object for main menu.
    public StarfishMenu(BaseGame g)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Calls the function for the create phase -- configures and adds actors to stage.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(g, 640, 480);
        
        // Configure and add the actors to stage:  background, Starfish Collector (title text), and instructions.
        create();
    }
    
    public final void create()
    {
        
        /*
        The function occurs during the startup / create phase and accomplishes the following:
        
        1.  Configures and adds the Actors:  background, Starfish Collector (title text), and instructions.
        */
        
        String text; // Text to display on main menu screen.
        BaseActor background; // Creates a BaseActor object that will act as the background in the main menu.
        BaseActor titleText; // Creates a BaseActor object to display the Starfish Collector image.
        BitmapFont font; // BitmapFont object storing font used when display main menu text.
        Label instructions; // LibGDX Label object that will display main menu text.
        LabelStyle style; // Style (color to tint) to associate with font used in Label.
        
        // Set defaults.
        text = " Press S to start, M for main menu ";
        
        // Create a new 2D scene graph to hold the main menu actors (background and text).
        // uiStage = new Stage();
        
        // Create a new BaseActor for the background in the main menu.
        background = new BaseActor();
        
        // Set the properties for the background and add to the scene graph.
        background.setTexture( new Texture(Gdx.files.internal("assets/images/water-640-480.jpg")) ); // Load image to buffer.
        uiStage.addActor( background ); // Add background image Actor to the scene graph.
        
        // Create a new BaseActor for the Starfish Collector image.
        titleText = new BaseActor();
        
        // Set the properties for the Starfish Collector image to add to the scene graph.
        titleText.setTexture( new Texture(Gdx.files.internal("assets/images/starfish-collector.png")) ); // Load image to buffer.
        titleText.setPosition( 20, 100 ); // Place image at (20, 100) within window.
        uiStage.addActor( titleText ); // Add Starfish Collector Actor to the scene graph.
        
        // Configure main menu instructions message (Label / BitmapFont).
        
        // Initialize BitmapFont (image based font) object.
        // Defaults to the size 15 Arial font file included in the LibGDX libraries.
        font = new BitmapFont();
        
        // Set style (tinting color for Font).
        style = new LabelStyle( font, Color.TEAL );
        
        // Set up Label object that will display main menu instructions.
        // Note:  Best practices include avoiding scaling -- use a high-resolution image, instead.
        instructions = new Label( text, style ); // Add text and style to Label.
        instructions.setFontScale(2); // Make font appear larger by using setFontScale method.
        instructions.setPosition(100, 50); // Set coordinates of the Label.
        
        // Set up color pause effect for main menu instructions text.
        instructions.addAction(
          Actions.forever(
            Actions.sequence(
              Actions.color( new Color(1, 1, 0, 1), 0.5f ),
              Actions.delay( 0.5f ),
              Actions.color( new Color(0.5f, 0.5f, 0, 1), 0.5f )
            )));
        
        uiStage.addActor( instructions ); // Add main menu instructions Label to the scene graph.
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {
        // The function occurs during the update phase and contains code related to game logic.
    }
    
    // InputProcessor methods for handling discrete input.
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.
        // Sets the screen to the main game when pressing the S key.
        
        // If the user pressed the S key, then...
        if (keycode == Keys.S)
        {
            // The user pressed the S key.
            
            // Switch to the main game.
            game.setScreen( new TurtleLevel(game) );
        }
        
        // Return a value.
        return false;
    }
    
}
