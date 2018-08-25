package cheeseplease_gamepad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.controllers.*;
import core.BaseGame;
import core.BaseScreen;
import core.XBoxGamepad;

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

public class MenuScreen extends BaseScreen { // Extends the BaseScreen class.

    /*


    The class extends the basic functionality of a BaseScreen class and sets up the main menu screen.

    LibGDX methods for ControllerListener include:

    buttonDown:  Called when user presses a button on the gamepad.  Pressing the start button launches the main game.

    Methods include:

    create:  Calls constructor for BaseScreen and configures and adds objects in the title screen.
    update:  Occurs during the update phase (render method) and contains code related to game logic.

    */

    MenuScreen(BaseGame g)
    {

        // The constructor of the class:

        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Calls the function for the create phase -- configures and adds objects in the title screen.

        // Call the constructor for the BaseScreen (parent / super) class.
        // Sets window width and height to 640 by 480.
        super(g, 640, 480);

        // Configure and add the objects in the title screen.
        create();

    }

    private void create()
    {

        /*
        The function occurs during the startup / create phase and accomplishes the following:

        1.  Loads, configures, and adds the tiles (background) graphic to the Skin.
        2.  Sets the background in the uiTable to the tiles graphic.
        3.  Loads, configures, and adds the "Cheese, Please!" title graphic to the Skin.
        4.  Loads, configures, and adds the "created with libGDX" title graphic to the Skin.
        5.  Creates, configures, and adds input listener for the start button.
        6.  Builds uiTable with "Cheese, Please!", start buttons, and "created with libGDX".
        */

        // Note:  Images support the Drawable interface, unlike Textures.  Tables require the Drawable interface.

        @SuppressWarnings("SpellCheckingInspection")
        Image libgdxImage; // Image object with "created with libGDX" text.
        Image titleImage; // // Image object with "Cheese, Please!" text.
        TextButton startButton; // TextButton object used to start the game.
        Texture bgTex; // Texture object with background image to display (1000px x 1000px tiles).
        @SuppressWarnings("SpellCheckingInspection")
        Texture libgdxTex; // Texture object with "created with libGDX" text.
        Texture titleTex; // Texture object with "Cheese, Please!" text.

        // Load tiles graphic (1000px x 1000 px tiles) used for background into Texture object.
        bgTex = new Texture(Gdx.files.internal("assets/images/tiles-1000-1000.jpg"), true);

        // To ensure that image scales smoothly, specify linear filtering.
        bgTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Add the Texture with the tiles (background) graphic to the Skin.
        game.skin.add( "bgTex", bgTex );

        // Set background image for Table to tiles graphic.
        uiTable.background( game.skin.getDrawable("bgTex") );

        // Load "Cheese, Please!" title graphic into Texture object.
        titleTex = new Texture(Gdx.files.internal("assets/images/cheese-please.png"), true);

        // To ensure that image scales smoothly, specify linear filtering.
        titleTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Create new image object for "Cheese, Please!" title with associated Texture.
        titleImage = new Image( titleTex );

        // Load "created with libGDX" title graphic into Texture object.
        libgdxTex = new Texture(Gdx.files.internal("assets/images/created-libgdx.png"), true);

        // To ensure that image scales smoothly, specify linear filtering.
        libgdxTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Create new image object for "created with libGDX" title with associated Texture.
        libgdxImage = new Image( libgdxTex );

        // Create textButton object used to start game with the word, Start, using the style, uiTextButtonStyle.
        startButton = new TextButton("Start", game.skin, "uiTextButtonStyle");

        /*
        Two action types -- polling and discrete.
        Polling:  Checks the state of the input hardware devices (such as the keyboard) during every iteration of the game loop.
                  Suited for continuous actions. > Constant movement.
        Discrete:  Automatically called when certain events occur (such as the initial press or release of a key, or click of a
                   mouse button).
                   Suited for irregularly occurring actions. > Jumping.
        */

        // Discrete action type used below --> only need to respond to a single click.

        // Add an InputListener to the start Button, which will start the game.
        // Example of an anonymous inner class.
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
                        // Occurs when the user performs a mouse down (click) event on the start button.

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
                        game.setScreen( new GameScreen(game) );
                    }

                }
        ); // End of code for adding InputListener to start Button.

        // Set up Table object with main menu layout.

        // Add cell encompassing both columns containing the "Cheese, Please!" image.
        uiTable.add(titleImage);

        // Add a row to the table.
        uiTable.row();

        // Add cell on the left containing the start button.
        uiTable.add(startButton);

        // Add a row to the table.
        uiTable.row();

        // Add cell, containing the "created with libGDX" image, aligned to the right,
        uiTable.add(libgdxImage).expandX().right();

    }

    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {
        // The function occurs during the update phase and contains code related to game logic.
    }

    // InputProcessor methods for handling discrete input (occurs irregularly).
    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {

        // The function gets called when the user presses a button on the controller.
        // Switches to the main game when user presses the start button.

        // If user presses down the start button, then...
        if (buttonCode == XBoxGamepad.BUTTON_START) {

            // User pressed down the start button.

            // Switch to the main game.
            game.setScreen(new GameScreen(game));

        }

        return false;
    }

}