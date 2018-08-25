package cheeseplease_gamepad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import core.BaseActor;
import core.BaseGame;
import core.BaseScreen;
import core.PhysicsActor;

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
*/

@SuppressWarnings("unused")
public class CheeseLevel extends BaseScreen { // Extends the BaseScreen class.

    // The class extends the basic functionality of a BaseScreen class and stores information
    // about the real game -- cheese, mouse, background tiles, win and time elapsed text,
    // and related properties (map height and width, total elapsed time, and whether player won).

    // Methods include:

    // create:  Configures and adds the Actors to the stage:  floor, cheese, and mouse.  Loads "yOU WIn"
    //          graphic to buffer (Texture), creates related Image, and defaults to not visible.  Creates
    //          Label with time elapsed.  Builds cheese game table and defaults to visible.
    // keyDown:  The function gets called when the user presses a key.  Pauses the game when pressing the P key.
    // update:  Occurs during the update phase (render method) and contains code related to game logic.

    private final int mapHeight; // Game world height.
    private final int mapWidth; // Game world width.
    private float timeElapsed; // Total elapsed time.
    private boolean win; // Whether player has won the game.
    @SuppressWarnings("SpellCheckingInspection")
    private PhysicsActor mousey; // Creates an PhysicsActor object that will act as the mouse.
    private BaseActor cheese; // Creates a BaseActor object that will act as the cheese.
    @SuppressWarnings("FieldCanBeLocal")
    private BaseActor floor; // Creates a BaseActor object that will act as the background tiles.
    private BaseActor winText; // Creates a BaseActor object that will act as the text that displays
    // when winning.
    private Image winImage; // Creates an Image object that will act as the text that displays when winning.
    private Label timeLabel; // LibGDX Label object that will display elapsed time text.
    // Note:  A Label is an extension of an Actor.

    // g = Screen object for game window (mouse, cheese, floor, win text, and elapsed time text).
    public CheeseLevel(BaseGame g)
    {

        // The constructor of the class:

        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Initializes the constants.
        // 3.  Launches the startup / create phase (function).

        // Call the constructor for the BaseScreen (parent / super) class.
        // Creates a window with dimensions of 640px by 480px.
        super(g, 640, 480);

        // Configure and add the actors to stage:  floor, cheese, mouse, and win and time messages.
        create();

        // Set game world dimensions.
        this.mapHeight = 1000;
        this.mapWidth = 1000;

    }

    private void create()
    {

        // The function occurs during the startup / create phase and accomplishes the following:

        // 1.  Configures and adds the Actors to the stage:  floor, cheese and mouse.
        // 2.  Loads "yOU WIn" graphic to buffer (Texture), creates related Image, and defaults to not visible.
        // 3.  Creates Label with time elapsed.
        // 4.  Builds cheese game table and defaults to visible.

        String filename; // Filename for current animation frame.  Examples:  mouse0.png, mouse1.png, ...
        String text; // Text to display related to elapsed time.
        // Animation<TextureRegion> anim; // Animation object associated with mouse.
        Animation anim; // Animation object associated with mouse.
        Array<TextureRegion> framesArray; // LibGDX array of textures used in animation for mouse.
        //BitmapFont font; // BitmapFont object storing font used when display elapsed time.
        //LabelStyle style; // Style (color to tint) to associate with font used in Label.
        Texture mouseTex; // Texture used temporarily when loading animation frame for resting mouse.
        Texture tex; // Texture used temporarily when loading images to animation frames for walking mouse.
        Texture winTex; // Texture used temporarily when loading image displayed when winning.
        TextureRegion[] frames; // Stores array of images (in this case for mouse animation).

        // Set default values.
        win = false;
        timeElapsed = 0;
        text = "Time: --";

        // Create a new 2D scene graph to hold the non-UI actors.
        //mainStage = new Stage();

        // Create a new 2D scene graph to hold the UI actors.
        //uiStage = new Stage();

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
        cheese.setOrigin( cheese.getWidth() / 2, cheese.getHeight() / 2 ); // Set cheese origin coordinates to center of image, to handle rotations.
        cheese.setEllipseBoundary(); // Set collision polygon for cheese as ellipse.
        mainStage.addActor( cheese ); // Add cheese Actor to the scene graph.

        // Create a new PhysicsActor for the mouse.
        mousey = new PhysicsActor();

        // Set the properties for the mouse and add to the scene graph.
        // mousey.setTexture( new Texture(Gdx.files.internal("assets/images/mouse.png")) ); // Load image to buffer.
        //mousey.setAnimation( anim );

        mousey.setPosition( 20, 20 ); // Place image at (20, 20) within window.
        mousey.setMaxSpeed(100); // Set maximum speed of mouse.
        mousey.setDeceleration(200); // Set deceleration of mouse.

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
            tex.setFilter(Texture.TextureFilter.Linear, TextureFilter.Linear);

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

        /*
        Set filter type -- controlling how pixel colors are interpolated when image is
        rotated or stretched.

        > Nearest:  To represent each pixel on the screen, the method uses the pixel of the texture (texel)
        that best matches to the pixel of the screen.  This is the default filter. As this filter only uses one
        texel for every pixel on the screen, the method applies the filter very quickly.  The result is an image
        with “hard” borders.

        > Linear:  To represent each pixel on the screen, the method uses bilinear interpolation, taking
        the four closest texels that best match with the screen pixel.  The result is smooth scaling.
        But, processing costs will also be bigger than GL_NEAREST.
        */
        mouseTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Add Animation object to hash map for resting mouse.
        mousey.storeAnimation( "stop", mouseTex );

        // Set mouse origin coordinates to center of image, to handle rotations.
        mousey.setOrigin( mousey.getWidth() / 2, mousey.getHeight() / 2 );

        // Orient mouse in direction of motion.
        mousey.setAutoAngle(true);

        // Set collision polygon for mouse as ellipse.
        mousey.setEllipseBoundary();

        // Add mouse Actor to the scene graph.
        mainStage.addActor( mousey );

        ////////////////////
        // USER INTERFACE //
        ////////////////////

        // Load "yOU WIn" image to buffer.
        winTex = new Texture(Gdx.files.internal("assets/images/you-win.png"), true);

        // Set pixel color interpolation (used while rotating and stretching) to bilinear, for smoothest scaling.
        winTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Next step:  Create Image.  An Image object allows for conversion to a Drawable, which Table objects
        // require in cells, for displaying graphics.  Each Texture object desired for inclusion in a table,
        // therefore, requires using an Image.  All user-interface objects store images using the Drawable
        // interface in order to support resizing.  The Texture class does not implement the Drawable interface.
        // Conveniently, though, the Image constructor accepts a Texture as input and supports conversion to a
        // Drawable object automatically.

        // Create Image by converting the Texture with the text to display when winning.
        winImage = new Image( winTex );

        // Set the "yOU WIn" Image to not visible.
        winImage.setVisible(false);

        // Add Label showing time elapsed.  Apply Skin property of uiLabelStyle.
        timeLabel = new Label( text, game.skin, "uiLabelStyle" );

        // Set up Table object with user-interface layout.
        uiTable.pad(10); // Set padding, in pixels, for Table (on all sides).
        uiTable.add().expandX(); // Add first cell in row, extended all the way to the right to force the next
        // one to align with the rightmost edge.
        uiTable.add(timeLabel); // Add a cell containing the time elapsed Label to the Table.
        uiTable.row(); // Add a row to the table.
        uiTable.add(winImage).colspan(2).padTop(50); // Add a cell containing the "yOU WIn" Image that
        // encompasses both columns and includes fifty pixels of padding on all sides.
        uiTable.row(); // Add a row to the table.
        uiTable.add().colspan(2).expandY();// Add a cell that encompasses both columns and extends the
        // remaining vertical length of the screen.

        /*
        // Create a new BaseActor for the win message.
        winText = new BaseActor();

        // Set the properties for the win message and add to the scene graph.
        winText.setTexture( new Texture(Gdx.files.internal("assets/images/you-win.png")) ); // Load image to buffer.
        winText.setPosition( 170, 60 ); // Place image at (170, 60) within window.
        winText.setVisible( false ); // Hide the win message when the application starts.
        uiStage.addActor( winText ); // Add win message Actor to the scene graph.

        // Configure elapsed time message (Label / BitmapFont).

        // Initialize BitmapFont (image based font) object.
        // Defaults to the size 15 Arial font file included in the LibGDX libraries.
        font = new BitmapFont();

        // Set style (tinting color for Font).
        style = new LabelStyle( font, Color.NAVY );

        // Set up Label object that will display elapsed time.
        // Note:  Best practices include avoiding scaling -- use a high-resolution image, instead.
        timeLabel = new Label( text, style ); // Add text and style to Label.
        timeLabel.setFontScale(2); // Make font appear larger by using setFontScale method.
        timeLabel.setPosition(500, 440); // Set coordinates of the Label.
        uiStage.addActor( timeLabel ); // Add time elapsed Label to the scene graph.
        */

    }

    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {

        /*
        The function occurs during the update phase (render method) and accomplishes the following:

        1.  Resets the acceleration of the mouse (to zero).
        2.  Handles key presses -- adjusts x and y acceleration.
	    3.  Sets correct animation (walk vs stop).
        4.  Restricts mouse to window boundaries.
        5.  Checks for a win and adjusts accordingly.
        6.  Updates elapsed time and related message.
        7.  Centers camera on player (mouse).
        */

        Action fadeInColorCycleForever; // Action object used to fade in You Win image.
        Action spinShrinkFadeOut; // Action object used to rotate, shrink, and fade out cheese image.
        Camera cam; // Camera object that determines which part of stage to render.
        //Rectangle cheeseRectangle; // Stores bounding rectangle (x, y, width, height) of cheese.
        //Rectangle mouseyRectangle; // Stores bounding rectangle (x, y, width, height) of mouse.

        // Set acceleration of mouse in X and Y directions to 0.
        mousey.setAccelerationXY(0,0);

        /*
        Two action types -- polling and discrete.
        Polling:  Checks the state of the input hardware devices (such as the keyboard) during every iteration of the game loop.
                  Suited for continuous actions. > Constant movement.
        Discrete:  Automatically called when certain events occur (such as the initial press or release of a key, or click of a
                   mouse button).
                   Suited for irregularly occurring actions. > Jumping.
        */

        // Polling action type used below --> movement.

        // If user clicked the left key, decrease the X acceleration of the mouse by 100.
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            mousey.addAccelerationXY(-100,0);

        // If user clicked the right key, increase the X acceleration of the mouse by 100.
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            mousey.addAccelerationXY(100,0);

        // If user clicked the up key, increase the Y acceleration of the mouse by 100.
        if (Gdx.input.isKeyPressed(Keys.UP))
            mousey.addAccelerationXY(0,100);

        // If user clicked the down key, decrease the Y acceleration of the mouse by 100.
        if (Gdx.input.isKeyPressed(Keys.DOWN))
            mousey.addAccelerationXY(0,-100);

        // Set correct animation.

        // If mouse moving and currently showing "stop" animation, then...
        if ( mousey.getSpeed() > 1 && mousey.getAnimationName().equals("stop") )
            // Mouse moving and currently showing "stop" animation.
            // Display "walk" animation.
            mousey.setActiveAnimation("walk");

        // If mouse sitting still and currently showing "walk" animation, then...
        if ( mousey.getSpeed() < 1 && mousey.getAnimationName().equals("walk") )
            // Mouse sitting still and currently showing "walk" animation.
            // Display "stop" animation.
            mousey.setActiveAnimation("stop");

        // Bound mouse to the rectangle defined by mapWidth and mapHeight.
        // aka Restrict mouse to window boundaries.

        // X Boundaries = 0 to window - mouse width.
        mousey.setX( MathUtils.clamp( mousey.getX(), 0,  mapWidth - mousey.getWidth() ));

        // Y Boundaries = 0 to window - mouse height.
        mousey.setY( MathUtils.clamp( mousey.getY(), 0,  mapHeight - mousey.getHeight() ));

        // If player did not win yet and mouse intersects cheese, then...
        if ( !win && cheese.overlaps( mousey, true ) )

        {
            // Player did not win yet and mouse intersects cheese.

            // Set win flag to true.
            win = true;

            // Create action to rotate cheese image (360 degrees per second), shrink (changing both
            // scale factors to 0 over the course of a second), and fade out (over the course of
            // one second.
            spinShrinkFadeOut = Actions.parallel(
                    Actions.alpha(1),  // Set transparency value.
                    Actions.rotateBy(360, 1), // Set rotation amount and duration.
                    Actions.scaleTo(0, 0, 2), // Set x and y amount and duration -- for scaling.
                    Actions.fadeOut(1)); // Set duration of fade out.

            // Add action to cheese Actor.
            cheese.addAction( spinShrinkFadeOut );

            // Ensure that cheese Actor rotates around its center (rather than a corner).
            // Set origin point of Actor, which serves as center of rotation.
            //cheese.setOrigin( cheese.getWidth() / 2, cheese.getHeight() / 2 );

            // Display You Win graphic through a fade in effect occurring over a period of two seconds.
            // Use an infinite loop containing a two-step sequence:
            // 1.  Shift the color tint to red. > Occupies one second.
            // 2.  Shift the color tint to blue. > Occupies one second.
            fadeInColorCycleForever = Actions.sequence(
                    Actions.alpha(0), // Set transparency value.
                    Actions.show(), // Set visible to true.
                    Actions.fadeIn(2), // Set duration of fade in.
                    Actions.forever(
                            Actions.sequence(
                                    // Color shade to approach, duration.
                                    Actions.color( new Color(1, 0, 0, 1), 1 ),
                                    Actions.color( new Color(0, 0, 1, 1), 1 )
                            )
                    )
            );

            // Add action to win text Actor.
            winImage.addAction( fadeInColorCycleForever );
        }

        // If player did not win game yet, then...
        if ( !win )
        {
            // Player did not win game yet.

            // Update elapsed time variable.
            timeElapsed += dt;

            // Update text for Label displaying elapsed time.
            timeLabel.setText( "Time: " + (int)timeElapsed );
        }

        // Adjust Camera.

        // Get Camera object associated with the main stage.
        cam = mainStage.getCamera();

        // Center Camera on player (mouse).
        cam.position.set( mousey.getX() + mousey.getOriginX(),
                mousey.getY() + mousey.getOriginY(), 0 );

        // Bound Camera to layout -- determining center of window.
        cam.position.x = MathUtils.clamp(cam.position.x, getViewWidth() / 2, mapWidth - getViewWidth() / 2);
        cam.position.y = MathUtils.clamp(cam.position.y, getViewHeight() / 2, mapHeight - getViewHeight() / 2);

        // Update Camera.
        cam.update();

    }

    /*
    Two action types -- polling and discrete.
    Polling:  Checks the state of the input hardware devices (such as the keyboard) during every iteration of the game loop.
              Suited for continuous actions. > Constant movement.
    Discrete:  Automatically called when certain events occur (such as the initial press or release of a key, or click of a
               mouse button).
               Suited for irregularly occurring actions. > Jumping.
    */

    // InputProcessor methods for handling discrete input (occurs irregularly).

    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {

        // The function gets called when the user presses a key.
        // Pauses the game when pressing the P key.

        // If the user pressed the P key, then...
        if (keycode == Keys.P)
        {
            // The user pressed the P key.

            // Pause the game.
            togglePaused();
        }

        // Return a value.
        return false;

    }

}
