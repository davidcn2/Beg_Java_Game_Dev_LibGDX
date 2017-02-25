/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cheeselauncher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author dcohn
 */
/**
 *
 * @author dcohn
 */
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

public class CheeseLevel implements Screen { // Implements Screen interface from LibGDX.
    
    // The class extends the basic functionality of a Game object in LibGDX.
    
    // Methods include:
    // create:  Creates the 2D scene graph and adds the Actors:  floor, cheese, mouse, and win message.
    // render:  Resets the speed of the mouse (to zero), handles key presses, adjusts Actor positions, 
    // checks for a win and adjusts accordingly, and draws the graphics.
    
    private final int mapHeight; // Game world height.
    private final int mapWidth; // Game world width.
    private final int viewHeight; // Window height.
    private final int viewWidth; // Window width.
    private float timeElapsed; // Total elapsed time.
    private boolean win; // Whether player has won the game.
    private AnimatedActor mousey; // Creates an AnimatedActor object that will act as the mouse.
    private BaseActor cheese; // Creates a BaseActor object that will act as the cheese.
    private BaseActor floor; // Creates a BaseActor object that will act as the background tiles.
    private BaseActor winText; // Creates a BaseActor object that will act as the text that displays
    // when winning.
    private final Game game; // Screen object used for window with mouse and cheese.
    // Game objects allow an application to easily have multiple screens.
    private Label timeLabel; // LibGDX Label object that will display elapsed time text.
    // Note:  A Label is an extension of an Actor.
    private Stage mainStage; // Stores a 2D scene graph containing the hierarchies of actors.
    // Stage handles the viewport and distributes input events.  Contains the non-UI actors.
    private Stage uiStage; // Stores a 2D scene graph containing the UI actors.
    
    // g = Screen object for game window (mouse and cheese).
    public CheeseLevel(Game g) 
    {
        
        // The constructor of the class:
        
        // 1.  Initializes the constants.
        // 2.  Sets the Screen object to the one passed.
        // 3.  Calls a function to create the 2D scene graphs and adds the Actors.
        
        // Set game world dimensions.
        this.mapHeight = 800;
        this.mapWidth = 800;
        
        // Set window dimensions.
        this.viewWidth = 640;
        this.viewHeight = 480;
        
        // Store Screen object for game (mouse and cheese).
        this.game = g;
        
        // Create the 2D scene graphs and add the actors.
        create();
        
    }

    private void create()
    {
        
        // The function occurs during the render phase and accomplishes the following:
        
        // 1.  Creates the 2D scene graphs.
        // 2.  Configures and adds the Actors:  floor, cheese, mouse, and win and time messages.
        
        String filename; // Filename for current animation frame.  Examples:  mouse0.png, mouse1.png, ...
        String text; // Text to display related to elapsed time.
        Animation anim; // Animation object associated with mouse.
        Array<TextureRegion> framesArray; // LibGDX array of textures used in animation.
        BitmapFont font; // BitmapFont object storing font used when display elapsed time.
        LabelStyle style; // Style (color to tint) to associate with font used in Label.
        Texture tex; // Texture used temporarily when load images to animation frames.
        TextureRegion[] frames; // Stores array of images (in this case for an animation).
        
        // Set default values.
        win = false;
        timeElapsed = 0;
        text = "Time: 0";
        
        // Create a new 2D scene graph to hold the non-UI actors.
        mainStage = new Stage();
        
        // Create a new 2D scene graph to hold the UI actors.
        uiStage = new Stage();
        
        // Create a new BaseActor for the floor (background tiles).
        floor = new BaseActor();
        
        // Set the properties for the floor and add to the scene graph.
        floor.setTexture( new Texture(Gdx.files.internal("images/tiles-800-800.jpg")) ); // Load image to buffer.
        floor.setPosition( 0, 0 ); // Place image at (0, 0) within window.
        mainStage.addActor( floor ); // Add floor Actor to the scene graph.
        
        // Create a new BaseActor for the cheese.
        cheese = new BaseActor();
        
        // Set the properties for the cheese and add to the scene graph.
        cheese.setTexture( new Texture(Gdx.files.internal("images/cheese.png")) ); // Load image to buffer.
        cheese.setPosition( 400, 300 ); // Place image at (400, 300) within window.
        cheese.setOrigin( cheese.getWidth() / 2, cheese.getHeight() / 2 ); // Set cheese origin coordinates to center of image, to handle roations.
        mainStage.addActor( cheese ); // Add cheese Actor to the scene graph.
        
        // Set up array of images (in this case for an animation).  Similar to buffer in Direct-X.
        frames = new TextureRegion[4]; 
        
        // Loop through and add animation frames (TextureRegion objects) to array.
        for (int n = 0; n < 4; n++)
        {
            // Set filename for current animation frame.  Examples:  mouse0.png, mouse1.png, ...
            filename = "images/mouse" + n + ".png";
            
            // Load image to buffer.
            tex = new Texture(Gdx.files.internal(filename));
            
            // Set file type -- controlling how pixel colors are interpolated when image is
            // rotated or stretched.
            tex.setFilter(Texture.TextureFilter.Linear, TextureFilter.Linear);
            
            // Store texture in standard Java array.
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
        anim = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP_PINGPONG);
        
        // Create a new AnimatedActor for the mouse.
        mousey = new AnimatedActor();
        
        // Set the properties for the mouse and add to the scene graph.
        // mousey.setTexture( new Texture(Gdx.files.internal("images/mouse.png")) ); // Load image to buffer.
        mousey.setAnimation( anim );
        
        // Set mouse origin coordinates to center of image, to handle roations.
        mousey.setOrigin( mousey.getWidth() / 2, mousey.getHeight() / 2 );
        
        mousey.setPosition( 20, 20 ); // Place image at (20, 20) within window.
        mainStage.addActor( mousey ); // Add mouse Actor to the scene graph.
        
        // Create a new BaseActor for the win message.
        winText = new BaseActor();
        
        // Set the properties for the win message and add to the scene graph.
        winText.setTexture( new Texture(Gdx.files.internal("images/you-win.png")) ); // Load image to buffer.
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
        
    }
    
    @Override
    public void resize(int width, int height)
    {
        
    }
    
    @Override
    public void pause()
    {
        
    }
    
    @Override
    public void resume()
    {
        
    }
    
    @Override
    public void dispose()
    {
        
    }
    
    @Override
    public void show()
    {
        
    }
    
    @Override
    public void hide()
    {
        
    }

    // delta = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void render(float delta) 
    {
    
    /*
    The function occurs during the render phase and accomplishes the following:

    1.  Resets the speed of the mouse (to zero).
    2.  Handles key presses -- adjusts x and y speed.
    3.  Adjusts Actor positions and other properties.
    4.  Restricts mouse to window boundaries.
    5.  Checks for a win and adjusts accordingly.
    6.  Updates elapsed time and related message.
    7.  Centers camera on player (mouse).
    8.  Draws the graphics.
    */

    //float dt; // Stores the time span between the current and the last frame in seconds.
    Action fadeInColorCycleForever; // Action object used to fade in You Win image.
    Action spinShrinkFadeOut; // Action object used to rotate, shrink, and fade out cheese image.
    Camera cam; // Camera object that determines which part of stage to render.
    Rectangle cheeseRectangle; // Stores bounding rectangle (x, y, width, height) of cheese.
    Rectangle mouseyRectangle; // Stores bounding rectangle (x, y, width, height) of mouse.

    // Set speed of mouse in X and Y directions to 0.
    mousey.velocityX = 0;
    mousey.velocityY = 0;

    // If user clicked the left key, decrease the X speed of mouse by 100. 
    if (Gdx.input.isKeyPressed(Keys.LEFT))
        mousey.velocityX -= 100;

    // If user clicked the right key, increase the X speed of mouse by 100.
    if (Gdx.input.isKeyPressed(Keys.RIGHT))
        mousey.velocityX += 100;

    // If user clicked the up key, increase the Y speed of mouse by 100.
    if (Gdx.input.isKeyPressed(Keys.UP))
        mousey.velocityY += 100;

    // If user clicked the down key, decrease the Y speed of mouse by 100.
    if (Gdx.input.isKeyPressed(Keys.DOWN))
        mousey.velocityY -= 100;

    // If user clicked the M key, display main menu.
    if (Gdx.input.isKeyPressed(Keys.M))
        game.setScreen( new CheeseMenu(game) );
    
    // Store the time span between the current and the last frame in seconds.
    //dt = Gdx.graphics.getDeltaTime();
    // Replaced with automatically populated parameter.

    // Call the Actor.act(float) method on each actor in the stages.
    // Typically called each frame.  The method also fires enter and exit events.
    // Updates the position of each Actor based on time.
    mainStage.act(delta);
    uiStage.act(delta);

    // Restrict mouse to window boundaries.

    // X Boundaries = 0 to window - mouse width.
    mousey.setX( MathUtils.clamp( mousey.getX(), 0, mapWidth - mousey.getWidth() ));

    // Y Boundaries = 0 to window - mouse height.
    mousey.setY( MathUtils.clamp( mousey.getY(), 0, mapHeight - mousey.getHeight() ));

    // Check win condition:  mousey must overlap cheese.

    // Store bounding rectangle (x, y, width, height) of cheese.
    cheeseRectangle = cheese.getBoundingRectangle();

    // Store bounding rectangle (x, y, width, height) of mouse.
    mouseyRectangle = mousey.getBoundingRectangle();

    // If player did not win yet and bounding rectangle for cheese contains mouse, then...
    if ( !win && cheeseRectangle.contains(mouseyRectangle) )

    {
        // Bounding rectangle for cheese contains mouse.

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
        cheese.setOrigin( cheese.getWidth() / 2, cheese.getHeight() / 2 );

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
        winText.addAction( fadeInColorCycleForever );
    }

    // If player did not win game yet, then...
    if ( !win )
    {
        // Player did not win game yet.

        // Update elapsed time variable.
        timeElapsed += delta;

        // Update text for Label displaying elapsed time.
        timeLabel.setText( "Time: " + (int)timeElapsed );
    }
    
    // Get Camera object associated with the main stage.
    cam = mainStage.getCamera();

    // Center camera on player (mouse).
    cam.position.set( mousey.getX() + mousey.getOriginX(),
        mousey.getY() + mousey.getOriginY(), 0 );

    // Bound camera to layout -- determining center of window.
    cam.position.x = MathUtils.clamp(cam.position.x, viewWidth / 2, mapWidth - viewWidth / 2);
    cam.position.y = MathUtils.clamp(cam.position.y, viewHeight / 2, mapHeight - viewHeight / 2);

    // Draw graphics.

    // Overdraw the area with the given glClearColor.
    Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);

    // Clear the area using the specified buffer.  Supports multiple buffers.
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // Draw the stages -- containing the Actors.
    mainStage.draw();
    uiStage.draw();
        
    }
    
}