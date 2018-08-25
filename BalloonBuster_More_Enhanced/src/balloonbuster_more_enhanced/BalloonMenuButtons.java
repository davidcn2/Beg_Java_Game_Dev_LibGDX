package balloonbuster_more_enhanced;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import core.BaseGame;
import core.BaseScreenTable;

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

public class BalloonMenuButtons extends BaseScreenTable { // Extends the BaseScreenTable class.

    // The class extends the basic functionality of a BaseScreenTable class and stores information
    // about the main menu (text and background image).

    // Methods include:

    // create:  Configures and adds the main menu Actors to the stage via Tables:  background, title text, and start button.
    // keyDown:  The function gets called when the user presses a key.  Sets the screen to the main game
    // when pressing the S key.
    // update:  Occurs during the update phase (render method) and contains code related to game logic.

    // g = Screen object for main menu.
    BalloonMenuButtons(BaseGame g)
    {

        // The constructor of the class:

        // 1.  Calls the constructor for the BaseScreenTable (parent / super) class.
        // 2.  Calls the function for the create phase -- configures and adds actors to stage.

        // Call the constructor for the BaseScreenTable (parent / super) class.
        // Sets window width and height to 640 by 480.
        super(g, 640, 480, true);

        // Configure and add the actors to stage for the main menu:  background, title text, and instructions.
        create();
    }

    private void create()
    {

        /*
        The function occurs during the startup / create phase and accomplishes the following:

        1.  Loads, configures, and adds the tiles (background) graphic to the Skin.
        2.  Sets the background in the uiTable to the tiles graphic.
        3.  Configures the title label using the Skin.
        4.  Configures the start button using the Skin.
        5.  Creates, configures, and adds input listener for the start button.
        6.  Builds Table objects with title label and start button.
        7.  Creates Stack with Table objects -- uiTable_Center and uiTable_Bottom.
        8.  Adds Stack to root table, uiTable.
        */

        Stack stkTables; // Stack containing tables occurring in same location.
        Texture bgTex; // Texture object with background image to display (1000px x 1000px tiles).
        //String textInstructions; // Text to display on main menu screen -- instructions.
        TextButton startButton; // TextButton object used to start the game.
        String textTitle; // Title text.

        // Set defaults.
        //textInstructions = " Press S to start ";
        textTitle = " Balloon Buster ";

        // Load tiles graphic (640px x 480px tiles) used for background into Texture object.
        bgTex = new Texture(Gdx.files.internal("assets/images/tiles-menu.jpg"), true);

        // To ensure that image scales smoothly, specify linear filtering.
        bgTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Add the Texture with the tiles (background) graphic to the Skin.
        game.skin.add( "bgTex", bgTex );

        // Turn on all debug lines (table, cell, and widget) in Table.
        //uiTable.setDebug(true);

        // Set background image for Table to tiles graphic.
        uiTable.background( game.skin.getDrawable("bgTex") );

        // Note:  Best practices include avoiding scaling -- use a high-resolution image, instead.

        // Add Label showing instructions.  Apply Skin property of uiLabelStyle.
        //instructionsLabel = new Label( textInstructions, game.skin, "uiLabelStyle" );

        // Add Label showing main menu title text.  Apply Skin property of uiLabelStyle.
        Label titleLabel = new Label(textTitle, game.skin, "uiLabelStyle");

        // Set up Table objects with main menu layout.

        // Add cell containing title, set 50 pixels above the bottom and 20 pixels from the left.
        uiTable_Bottom.add(titleLabel).bottom().expandY().padBottom(50f).padLeft(20f).left().expandX();
        //uiTable_Bottom.setDebug(true);

        // Add cell containing the instructions, centered within the window.
        //uiTable_Center.add(instructionsLabel).center();

        // Create TextButton object used to start game with the word, Start, using the style, uiTextButtonStyle.
        // Note:  Labels and TextButtons are extensions of Actor objects.
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
                        game.setScreen( new BalloonLevel(game) );
                    }

                }
        ); // End of code for adding InputListener to start Button.

        // Add cell in the center containing the start button.
        uiTable_Center.add(startButton);


        // Create LibGDX Stack object.
        stkTables = new Stack();

        // The method will set the size of the Stack to that of the root table (uiTable) -- effectively the stage.
        stkTables.setFillParent(true);

        //stkTables.setDebug(true);

        // Add the Stack to the root table, aligning to the bottom left corner (in LibGDX terms -- 0, 0).
        uiTable.add(stkTables).expandY().bottom().expandX().left();

        // Add the tables to the Stack.
        stkTables.addActor(uiTable_Center);
        stkTables.addActor(uiTable_Bottom);

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
            game.setScreen( new BalloonLevel(game) );
        }

        // Return a value.
        return false;
    }

}