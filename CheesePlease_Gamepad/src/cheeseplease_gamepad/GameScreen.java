package cheeseplease_gamepad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.controllers.*;
import core.BaseActor;
import core.BaseGame;
import core.BaseScreen;
import core.PhysicsActor;
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

public class GameScreen extends BaseScreen // Extends the BaseScreen class.
{

    /* Detailed class description:

    The class contains logic related to setting up and handling interaction related to the game-related stage.

    LibGDX methods for InputProcessor include:

    keyDown:  Called when user presses a key.  P causes the game to pause.

    LibGDX methods for ControllerListener include:

    buttonDown:  Called when user presses a button on the gamepad.  Pressing the X button pauses the game.

    Custom methods include:

    create:  The method occurs during the startup / create phase and contains initialization logic.
    update:  The method occurs during the update phase (render method) and contains code related to game logic.

    */

    // Constants...
    private final int mapWidth; // Game world height.
    private final int mapHeight; // Game world width.

    private boolean win; // Whether player has won the game.
    private float timeElapsed; // Total elapsed time.

    private PhysicsActor mousey; // PhysicsActor object that will act as the mouse.
    private BaseActor cheese; // BaseActor object that will act as the cheese.
    @SuppressWarnings("FieldCanBeLocal")
    private BaseActor floor; // BaseActor object that will act as the background tiles.
    private Image winImage; // Image object with text that displays when winning.
    private Label timeLabel; // LibGDX Label object that will display elapsed time text.
    // Note:  A Label is an extension of an Actor.

    // g = Screen object for game window (mouse, cheese, floor, win text, and elapsed time text).
    GameScreen(BaseGame g)
    {

        // The constructor of the class:

        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Sets game world dimensions.
        // 3.  Launches the startup / create phase (function).

        // Call the constructor for the BaseScreen (parent / super) class.
        // Sets window width and height to 640 by 480.
        super(g, 640, 480);

        // Set game world dimensions.
        this.mapWidth = 1000;
        this.mapHeight = 1000;

        // Launch the startup / create phase (function).
        create();

    }

    private void create()
    {

        /*
        The function occurs during the startup / create phase and accomplishes the following:

        1.  Loads (the image), configures, and adds the floor to the stage.
        2.  Loads (the image), configures, and adds the cheese to the stage.
        3.  Loads (the images), configures (including animation and collision detection), and adds the mouse to the stage.
        4.  Loads, configures, and adds the "You Win!" graphic to the stage.
        5.  Loads, configures, and adds the time elapsed label to the stage.
        6.  Builds uiTable with time elapsed label and "You Win!" graphic.
        *   No input listeners.
        */

        String filename; // Filename for current animation frame.  Examples:  mouse0.png, mouse1.png, ...
        String text; // Text to display related to elapsed time.

        // Animation<TextureRegion> anim; // Animation object associated with mouse.
        Animation anim; // Animation object associated with mouse.
        Array<TextureRegion> framesArray; // LibGDX array of textures used in animation for mouse.
        Texture mouseTex; // Texture used when loading image for animation for mouse at rest.
        Texture tex; // Texture used temporarily when loading images to animation frames for mouse.
        Texture winTex; // Texture used to load image intended for display when winning game.
        TextureRegion[] frames; // Stores array of images (in this case for mouse animation).

        // Set default values.
        win = false;
        timeElapsed = 0;
        text = "Time: --";

        // Create a new BaseActor for the floor (background tiles).
        floor = new BaseActor();

        // Set the properties for the floor and add to the scene graph.
        floor.setTexture( new Texture(Gdx.files.internal("assets/images/tiles-1000-1000.jpg")) ); // Load image to buffer.
        floor.setPosition( 0, 0 ); // Place image at (0, 0) within window.
        mainStage.addActor( floor ); // Add floor Actor to the scene graph.

        // Create a new BaseActor for the cheese.
        cheese = new BaseActor();

        // Set the properties for the cheese and add to the scene graph.
        cheese.setTexture( new Texture(Gdx.files.internal("assets/images/cheese.png")) ); // Load image to buffer.
        cheese.setPosition( 400, 300 ); // Place image at (400, 300) within window.
        cheese.setOrigin( cheese.getWidth()/2, cheese.getHeight()/2 ); // Set cheese origin coordinates to center of image, to handle rotations.
        cheese.setEllipseBoundary(); // Set collision polygon for cheese as ellipse.
        mainStage.addActor( cheese ); // Add cheese Actor to the scene graph.

        // Create new PhysicsActor for the mouse.
        mousey = new PhysicsActor();

        // Set the properties for the mouse.
        mousey.setPosition( 20, 20 ); // Place image at (20, 20) within window.
        mousey.setMaxSpeed(100); // Set maximum speed.
        mousey.setDeceleration(200); // Set deceleration.

        // Set up array of images (in this case for an animation) for mouse.  Similar to buffer in Direct-X.
        frames = new TextureRegion[4];

        // Loop through and add animation frames (TextureRegion objects) to array for mouse.
        for (int n = 0; n < 4; n++)
        {
            // Set filename for current animation frame.  Examples:  mouse0.png, mouse1.png, ...
            filename = "assets/images/mouse" + n + ".png";

            // Load image to buffer.
            tex = new Texture(Gdx.files.internal(filename));

            // Set file type -- controlling how pixel colors are interpolated when image is
            // rotated or stretched.
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            // Add texture for current frame for cheese to standard array.
            frames[n] = new TextureRegion( tex );
        }

        // Copy textures from standard Java to LibGDX array.
        framesArray = new Array<>(frames);

        // Create animation object for mouse.
        // 0.1f ... Frame interval.  The amount of time to display each image.
        // framesArray ... Array of frames to use in animation.
        // Animation.PlayMode.LOOP_PINGPONG ... Indicates how frames should be played.
        // LOOP = In the order given.
        // LOOP_REVERSED = In reverse order.
        // LOOP_PINGPONG = From first to last to first again.
        // LOOP_RANDOM = In random order.
        
        // anim = new Animation<>(0.1f, framesArray, Animation.PlayMode.LOOP_PINGPONG);
        anim = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP_PINGPONG);

        // Add Animation object to hash map for walking mouse.
        mousey.storeAnimation( "walk", anim );

        // Set filename for animation frame for mouse at rest.
        mouseTex = new Texture(Gdx.files.internal("assets/images/mouse0.png"));

        // To ensure that image scales smoothly, specify linear filtering.
        mouseTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Add Animation object to hash map for resting mouse.
        mousey.storeAnimation( "stop", mouseTex );

        // Set mouse origin coordinates to center of image, to handle rotations.
        mousey.setOrigin( mousey.getWidth()/2, mousey.getHeight()/2 );

        // Set mouse to rotate image to match velocity (speed).
        mousey.setAutoAngle(true);

        // Set collision polygon for turtle as ellipse.
        mousey.setEllipseBoundary();

        // Add mouse Actor to the scene graph.
        mainStage.addActor(mousey);

        ////////////////////
        // USER INTERFACE //
        ////////////////////

        // Load win message image to buffer.
        winTex = new Texture(Gdx.files.internal("assets/images/you-win.png"), true);

        // To ensure that image scales smoothly, specify linear filtering.
        winTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Create new Image object for win message with associated Texture.
        winImage = new Image( winTex );

        // Set win message Image to not visible.
        winImage.setVisible(false);

        // Add Label showing time elapsed.  Apply Skin property of uiLabelStyle.
        timeLabel = new Label( text, game.skin, "uiLabelStyle" );

        // Set up Table object with user-interface layout.
        uiTable.pad(10); // Set padding, in pixels, for Table (on all sides).
        uiTable.add().expandX(); // Add first cell in row, extended all the way to the right to force the next
        // one to align with the rightmost edge.
        uiTable.add(timeLabel); // Add second cell in row, containing time elapsed Label.
        uiTable.row(); // Add a row to the Table.
        uiTable.add(winImage).colspan(2).padTop(50); // Add a cell that encompasses both columns, with 50 pixels
        // of padding at the top.
        uiTable.row(); // Add a row to the Table.
        uiTable.add().colspan(2).expandY(); // Add a cell that encompasses both columns, extending the remaining
        // vertical length of the screen.

    }

    @Override
    public void update(float dt)
    {

        /*
        The function occurs during the update phase and contains code related to game logic.
        The following logic occurs:

        1.  Resets the acceleration of the mouse -- to nothing.
        2.  Processes movement related actions (arrow key presses and joystick movement).
        3.  Sets animation type to stop or walk, depending on speed.
        4.  Bounds mouse to the map.
        5.  Check win conditions (mouse overlapping cheese).  If overlap occurring, implements win actions.
        6.  If overlap NOT occurring, increment elapsed time and update related label.
        7.  Center camera on player.
        8.  Bound camera to layout.
        */

        /*
        Two action types -- polling and discrete.
        Polling:  Checks the state of the input hardware devices (such as the keyboard) during every iteration of the game loop.
                  Suited for continuous actions. > Constant movement.
        Discrete:  Automatically called when certain events occur (such as the initial press or release of a key, or click of a
                   mouse button).
                   Suited for irregularly occurring actions. > Jumping.
        */

        // Note that -1 vs 1 depend on the Controller type.  The example (XBoxGamepad) uses -1 for Left and Up).
        final float deadZone = 0.15f; // Threshold in joystick movement to consider actual activity.
        float xAxis; // State of the joystick on the x axis, ranging from -1 to 1.  -1 = Left, 1 = Right.
        float yAxis; // State of the joystick on the y axis, ranging from -1 to 1.  -1 = Up, 1 = Down.
        Camera cam; // Camera associated with the main Stage.
        Controller gamepad; // Reference to first active gamepad.

        // If one or more controller exists, then...
        if (Controllers.getControllers().size > 0)
        {

            // One or more controller exists.

            // Retrieve the instance of the (first) active Controller object.
            gamepad = Controllers.getControllers().get(0);

            /*
            To poll the state of the joystick, use getAxis(code), where code is an integer
            corresponding to either the left or right joystick, and either the x or y direction.
            The value returned is a float in the range from –1 to 1, with the meaning dependent on the Controller.
            */

            // Determine the state of the joystick (left vs right, up vs down).
            xAxis =  gamepad.getAxis(XBoxGamepad.AXIS_LEFT_X);
            yAxis = -gamepad.getAxis(XBoxGamepad.AXIS_LEFT_Y);

            /*
            Provided that the joystick has moved passed a certain threshold (called the deadzone, used to compensate
            for controller sensitivity, typically set to a value between 10 and 20 percent), set the acceleration.
            */

            // If x-axis of joystick less than deadzone, set to 0 to prevent horizontal movement.
            if (Math.abs(xAxis) < deadZone)
                xAxis = 0;

            // If y-axis of joystick less than deadzone, set to 0 to prevent vertical movement.
            if (Math.abs(yAxis) < deadZone)
                yAxis = 0;

            // Adjust acceleration based on joystick position (left vs right, top vs bottom).
            mousey.setAccelerationXY( xAxis * 100, yAxis * 100 );

        }

        // Otherwise -- if no controller exists, then...
        else

        {

            // No controller exists.  Use keyboard fallback controls.

            // Resets the acceleration for the mouse (to nothing).
            mousey.setAccelerationXY(0,0);

            // Polling action type used below --> movement.
            if (Gdx.input.isKeyPressed(Keys.LEFT))
                mousey.addAccelerationXY(-100,0);
            if (Gdx.input.isKeyPressed(Keys.RIGHT))
                mousey.addAccelerationXY(100,0);
            if (Gdx.input.isKeyPressed(Keys.UP))
                mousey.addAccelerationXY(0,100);
            if (Gdx.input.isKeyPressed(Keys.DOWN))
                mousey.addAccelerationXY(0,-100);

        }

        // set correct animation
        if ( mousey.getSpeed() > 1 && mousey.getAnimationName().equals("stop") )
            mousey.setActiveAnimation("walk");
        if ( mousey.getSpeed() < 1 && mousey.getAnimationName().equals("walk") )
            mousey.setActiveAnimation("stop");

        // bound mousey to the rectangle defined by mapWidth, mapHeight
        mousey.setX( MathUtils.clamp( mousey.getX(), 0,  mapWidth - mousey.getWidth() ));
        mousey.setY( MathUtils.clamp( mousey.getY(), 0,  mapHeight - mousey.getHeight() ));

        // check win condition: mousey must be overlapping cheese
        if ( !win && cheese.overlaps( mousey, true ) )
        {
            // Mousey overlapping cheese.  Implement win actions.

            // Flag win as occurring.
            win = true;

            // Set up action to spin, shrink, and fade out cheese.
            Action spinShrinkFadeOut = Actions.parallel(
                    Actions.alpha(1),         // set transparency value
                    Actions.rotateBy(360, 1), // rotation amount, duration
                    Actions.scaleTo(0,0, 2),  // x amount, y amount, duration
                    Actions.fadeOut(1)        // duration of fade in
            );
            cheese.addAction( spinShrinkFadeOut );

            // Set up action to fade in and cycle colors to the "You Win!" image.
            Action fadeInColorCycleForever = Actions.sequence(
                    Actions.alpha(0),   // set transparency value
                    Actions.show(),     // set visible to true
                    Actions.fadeIn(2),  // duration of fade out
                    Actions.forever(
                            Actions.sequence(
                                    // color shade to approach, duration
                                    Actions.color( new Color(1,0,0,1), 1 ),
                                    Actions.color( new Color(0,0,1,1), 1 )
                            )
                    )
            );
            winImage.addAction( fadeInColorCycleForever );
        }

        // If win conditions not satisfied, then...
        if (!win)
        {
            // Win conditions not satisfied.

            // Increment timer by elapsed time.
            timeElapsed += dt;

            // Update elapsed time label.
            timeLabel.setText( "Time: " + (int)timeElapsed );
        }

        // camera adjustment
        cam = mainStage.getCamera();

        // center camera on player
        cam.position.set( mousey.getX() + mousey.getOriginX(), mousey.getY() + mousey.getOriginY(), 0 );

        // bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x, getViewWidth()/2,  mapWidth - getViewWidth()/2);
        cam.position.y = MathUtils.clamp(cam.position.y, getViewHeight()/2, mapHeight - getViewHeight()/2);
        cam.update();
    }

    // InputProcessor methods for handling discrete input (occurs irregularly).
    @Override
    public boolean keyDown(int keycode)
    {

        // The function gets called when the user presses a key.
        // Pauses the game when pressing the P key.

        // If the user pressed the P key, then...
        if (keycode == Keys.P) {
            // The user pressed the P key.

            // Pause the game.
            togglePaused();
        }

        // Return a value.
        return false;

    }

    // InputProcessor methods for handling discrete input (occurs irregularly).
    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {

        // The function gets called when the user presses a button on the controller.
        // Pauses the game when pressing the X button on the gamepad.

        // If user presses down the X button, then...
        if (buttonCode == XBoxGamepad.BUTTON_X) {

            // User pressed down the X button.

            // Pause the game.
            togglePaused();

        }

        // Return a value.
        return false;

    }

}