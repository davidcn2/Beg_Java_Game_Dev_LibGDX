package starfishcollector_polished;

import core.PhysicsActor;
import core.BaseActor;
import core.BaseScreen;
import core.BaseGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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

Subclass: A Java subclass is a class which inherits a method or methods from a Java superclass.
A Java class may be either a subclass, a superclass, both, or neither!

Polymorphism:  Polymorphism is the ability of an object to take on many forms. The most common use 
of polymorphism in OOP occurs when a parent class reference is used to refer to a child class object.
Any Java object that can pass more than one IS-A test is considered to be polymorphic.

ArrayList supports dynamic arrays that can grow as needed.
*/

public class TurtleLevel extends BaseScreen // Extends the BaseScreen class.
{
    
    // The class extends the basic functionality of a BaseScreen class and stores information
    // about the game -- interface (text and buttons), background (ocean / overlay), rocks, starfish, turtle,
    // and related properties.
    
    // Methods include:
    
    // create:  Configures and adds the Actors to the stage:  ocean, overlay, rocks, starfish, and turtle.
    //          Loads sounds.  Creates Label with number of starfish left to collect.  Creates pause Button.
    //          Builds Table object with user-interface layout:  starfish left label and pause button.
    //          Creates and configures Stack.  Creates and configures audio Slider.
    // dispose: Occurs during the cleanup phase and clears objects from memory.
    // update:  Occurs during the update phase (render method) and contains code related to game logic.
    
    // Constants...
    private final int mapWidth; // Game world height.
    private final int mapHeight; // Game world width.
    
    private BaseActor ocean; // BaseActor object that will act as the background.
    private ArrayList<BaseActor> rockList; // Dynamic array for rock Actors.
    private ArrayList<BaseActor> starfishList; // Dynamic array for starfish Actors.
    private Label starfishLeftLabel; // Text label displaying number of starfish left to collect.
    private Music instrumental; // Instrumental music -- loops constantly.
    private Music oceanSurf; // Ocean surf music -- loops constantly
    private PhysicsActor turtle; // PhysicsActor object that will act as the turtle.
    private Sound waterDrop; // Water drop sound to play when capturing a starfish.
    private Table pauseOverlay; // Table containing pause menu elements -- paused and volume labels, 
    // resume and quit buttons, and volume slider.
    
    private float audioVolume; // Volume to use with Music objects.
    
    // g = Screen object for game window (background -- ocean, rocks, starfish, and turtle).
    public TurtleLevel(BaseGame g)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Sets game world dimensions.
        // 3.  Launches the startup / create phase (function).
        
        // Call the constructor for the BaseScreen (parent / super) class.
        // Sets window width and height to 800 by 600.
        super(g, 800, 600);
        
        // Set game world dimensions.
        this.mapWidth = 1000; // 800
        this.mapHeight = 1000; // 600
        
        // Launch the startup / create phase (function).
        create();
        
    }
    
    public final void create()
    {
        
        // The function occurs during the startup / create phase and accomplishes the following:
        
        // 1.  Configures and adds the Actors to the stage:  ocean, overlay, rocks, starfish, and turtle.
        // 2.  Loads sounds.
        // 3.  Creates Label with number of starfish left to collect.
        // 4.  Creates pause Button.
        // 5.  Builds Table object with user-interface layout:  starfish left label and pause button.
        // 6.  Creates and configures Stack.
        // 7.  Creates and configures pause and volume Labels and resume and quit Buttons.
        // 8.  Creates and configures audio Slider.
        // 9.  Builds pause overlay table and defaults to not visible.
        
        float w; // Width of resume Button.
        String fileName; // Filename for current animation frame.  Examples:  turtle-1.png, turtle-2.png, ...
        
        Action[] spinStarfish; // Action object associated with starfish -- used to rotate.
        Animation anim; // Animation object associated with turtle.
        Array<TextureRegion> framesArray; // LibGDX array of textures used in animation for turtle.
        BaseActor overlay; // Copy of ocean with translucent color used to create underwater effect.
        BaseActor rock; // BaseActor object for rock.  Acts as template for rocks in array.
        BaseActor r; // BaseActor object for each rock in loop.
        BaseActor starfish; // BaseActor object for starfish.  Acts as template for starfish in array.
        BaseActor s; // BaseActor object for each starfish in loop.
        Button pauseButton; // Button object used for pausing the game.
        
        // A ButtonStyle object can store one or more images, one of which will be displayed, depending on
        // the current state of the button. An image stored in the up field serves as the default image.
        ButtonStyle pauseStyle; // Stores style properties associated with pause button (images, ...).
        
        Drawable pauseBackground; // Background to use in pause Table.
        Label pauseLabel; // Label with the text, Paused, displayed in pause Table.
        Label volumeLabel; // Label with the text, Volume, displayed in pause Table.
        Slider audioSlider; // Slider used to control volume.
        Stack stacker; // Used to stack tables in the correct order (ui and pause in this case).
        TextButton quitButton; // TextButton allowing for quitting the game.
        TextButton resumeButton; // TextButton allowing for resuming the game after pausing.
        Texture frame1; // Texture used when loading image for animation for turtle at rest.
        Texture pauseTexture; // Texture used when loading image for pause button.
        Texture tex; // Texture used temporarily when loading images to animation frames for swimming turtle.
        TextureRegion[] frames; // Stores array of images (in this case for turtle animation).
        
        // Create new BaseActors for the background (ocean) and its overlay.
        ocean = new BaseActor();
        overlay = new BaseActor();
        
        // Name background (ocean) and overlay.
        ocean.setActorName("Ocean");
        overlay.setActorName("Overlay");
        
        // Set the properties for the background and add to the scene graph.
        ocean.setTexture( new Texture(Gdx.files.internal("assets/images/water.jpg")) );
        ocean.setPosition( 0, 0 );
        mainStage.addActor( ocean );
        
        // Copy properties of ocean to overlay.
        // overlay = ocean.clone();
        overlay.copy(ocean);
        
        // Set position of overlay fifty pixels to the left and top of ocean.
        overlay.setPosition(-50, -50);
        
        // Use translucent overlay using black with 0.25 alpha.
        overlay.setTintColor(1, 1, 1, 0.25f);
        
        // Add overlay to the scene graph.
        uiStage.addActor(overlay);
        
         // Move overlay to back of layer to allow for pushing of pause Button.
         // Note:  Ordering must occur AFTER adding object to stage.
        overlay.toBack();
        
        // Create new BaseActor for a rock.
        rock = new BaseActor();
        
        // Name rock.
        rock.setActorName("Template Rock");
        
        // Set image related to rock.
        rock.setTexture( new Texture(Gdx.files.internal("assets/images/rock.png")) );
        
        // Set collision polygon for rock as ellipse.
        rock.setEllipseBoundary();
        
        // Initialize dynamic array for rock Actors.
        // rockList = new ArrayList<BaseActor>();
        rockList = new ArrayList<>();
        
        // Set coordinates for rocks.
        int[] rockCoords = {200,0, 200,100, 250,200, 360,200, 470,200};
        
        // Loop through and create rocks -- copying properties from initial object.
        for (int i = 0; i < 5; i++)
            
            {
            // Create new BaseActor for a rock.
            r = new BaseActor();
            
            // Copy properties of initial (template) to current rock.
            // rock.copyToPassed(r);
            // r = rock.clone();
            r.copy(rock);
            
            // Name rock.
            r.setActorName("Rock");
            
            // Set the position (x and y coordinates of the bottom left corner) of the rock Actor
            // coordinates in next two elements in array.
            r.setPosition( rockCoords[i * 2], rockCoords[(i * 2) + 1] );
            
            // Add rock Actor to the scene graph.
            mainStage.addActor( r );
            
            // Add rock Actor to dynamic array.
            rockList.add( r );
            }
        
        // Create new BaseActor for starfish.
        starfish = new BaseActor();
        
        // Name starfish.
        starfish.setActorName("Template Starfish");
        
        // Set image related to starfish.
        starfish.setTexture( new Texture(Gdx.files.internal("assets/images/starfish.png")) );
        
        // Set collision polygon for starfish as ellipse.
        starfish.setEllipseBoundary();
        
        // Set up array of Actions.
        spinStarfish = new Action[3];
        
        // Create Actions to rotate starfish image (1 to 3 degrees per second).
        spinStarfish[0] = Actions.parallel(
          Actions.alpha(1),  // Set transparency value.
          Actions.forever(
            Actions.rotateBy(3, 1) // Set rotation amount and duration.
            ));
        spinStarfish[1] = Actions.parallel(
          Actions.alpha(1),  // Set transparency value.
          Actions.forever(
            Actions.rotateBy(5, 1) // Set rotation amount and duration.
            ));
        spinStarfish[2] = Actions.parallel(
          Actions.alpha(1),  // Set transparency value.
          Actions.forever(
            Actions.rotateBy(7, 1) // Set rotation amount and duration.
            ));
        
        // Initialize dynamic array for rock Actors.
        // starfishList = new ArrayList<BaseActor>();
        starfishList = new ArrayList<>();
        
        // Set coordinates for starfish.
        int[] starfishCoords = {400,100, 100,400, 650,400};
        
        // Loop through and create starfish -- copying properties from initial object.
        for (int i = 0; i < 3; i++)
            
            {
            // Create new BaseActor for a starfish.
            s = new BaseActor();
            
            // Copy properties of initial (template) to current starfish.
            // starfish.copyToPassed(s);
            // s = starfish.clone();
            s.copy(starfish);
            
            // Name starfish.
            s.setActorName("Starfish");
            
            // Set the position (x and y coordinates of the bottom left corner) of the starfish Actor
            // coordinates in next two elements in array.
            s.setPosition( starfishCoords[i * 2], starfishCoords[(i * 2) + 1] );
            
            // Add Action to starfish.
            s.addAction( spinStarfish[i] );
            
            // Add starfish Actor to the scene graph.
            mainStage.addActor( s );
            
            // Add starfish Actor to dynamic array.
            starfishList.add( s );
            }
        
        // Create new PhysicsActor for the turtle.
        turtle = new PhysicsActor();
        
        // Name turtle.
        turtle.setActorName("Turtle");
        
        // Set up array of images (in this case for an animation) for turtle.  Similar to buffer in Direct-X.
        frames = new TextureRegion[6];
        
        // Loop through and add animation frames (TextureRegion objects) to array for turtle.
        for (int n = 1; n <= 6; n++)
            
            {
            // Set filename for current animation frame.  Examples:  turtle-1.png, turtle-2.png, ...
            fileName = "assets/images/turtle-" + n + ".png";
            
            // Load image to buffer.
            tex = new Texture(Gdx.files.internal(fileName));
            
            /*
            Set file type -- controlling how pixel colors are interpolated when image is
            rotated or stretched.

            > Nearest:  To represent each pixel on the screen, the method uses the pixel of the texture (texel) 
            that best matches to the pixel of the screen.  This is the default filter. As this filter only uses one 
            texel for every pixel on the screen, the method applies the filter very quickly.  The result is an image 
            with “hard” borders.

            > Linear:  To represent each pixel on the screen, the method uses bilinear interpolation, taking 
            the four closest texels that best match with the screen pixel.  The result is smooth scaling.
            But, processing costs will also be bigger than GL_NEAREST.
            */
            tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            
            // Add texture for current frame for turtle to standard array.
            frames[n - 1] = new TextureRegion( tex );
            }
        
        // Copy textures from standard Java to LibGDX array.
        // framesArray = new Array<TextureRegion>(frames);
        framesArray = new Array<>(frames);
        
        // Create animation object for turtle.
        // 0.1f ... Frame interval.  The amount of time to display each image.
        // framesArray ... Array of frames to use in animation.
        // Animation.PlayMode.LOOP ... Indicates how frames should be played.
        // LOOP = In the order given.
        // LOOP_REVERSED = In reverse order.
        // LOOP_PINGPONG = From first to last to first again.
        // LOOP_RANDOM = In random order.
        anim = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP);
        
        // Add Animation object to hash map for swimming turtle.
        turtle.storeAnimation( "swim", anim );
        
        // Set filename for animation frame for turtle at rest.
        frame1 = new Texture(Gdx.files.internal("assets/images/turtle-1.png"));
        
        // Add Animation object to hash map for resting turtle.
        turtle.storeAnimation( "rest", frame1 );
        
        // Set turtle origin coordinates to center of image, to handle roations.
        turtle.setOrigin( turtle.getWidth() / 2, turtle.getHeight() / 2 );
        
        turtle.setPosition( 20, 20 ); // Place image at (20, 20) within window.
        turtle.setRotation( 90 ); // Rotate turtle 90 degrees to the left.
        turtle.setEllipseBoundary(); // Set collision polygon for turtle as ellipse.
        turtle.setMaxSpeed(100); // Set maximum speed.
        turtle.setDeceleration(200); // Set deceleration.
        turtle.setTintColorToDefault(); // Set default tint color.
        
        // Add turtle Actor to the scene graph.
        mainStage.addActor(turtle);
        
        // Load audio objects.
        waterDrop = Gdx.audio.newSound(Gdx.files.internal("assets/audio/Water_Drop.ogg"));
        instrumental = Gdx.audio.newMusic(Gdx.files.internal("assets/audio/Master_of_the_Feast.ogg"));
        oceanSurf = Gdx.audio.newMusic(Gdx.files.internal("assets/audio/Ocean_Waves.ogg"));
        
        audioVolume = 0.80f; // Set volume to use with Music objects.
        
        instrumental.setLooping(true); // Set instrumental music to loop.
        instrumental.setVolume(audioVolume); // Set volume for instrumental music.
        instrumental.play(); // Play instrumental music.
        
        oceanSurf.setLooping(true); // Set ocean surf music to loop.
        oceanSurf.setVolume(audioVolume); // Set volume for ocean surf music.
        oceanSurf.play(); // Play ocean surf music.
        
        // Add Label showing number of starfish left to collect.  Apply Skin property of uiLabelStyle.
        starfishLeftLabel = new Label("Starfish Left: --", game.skin, "uiLabelStyle");
        
        // Load image for pause button (to buffer).
        pauseTexture = new Texture(Gdx.files.internal("assets/images/pause.png"));
        
        // Store texture (image buffer) for pause button in Skin class for later reuse.
        game.skin.add("pauseImage", pauseTexture );
        
        // Create ButtonStyle object for the pause button.
        pauseStyle = new ButtonStyle();
        
        // Set default image for the pause button (used for the up position).
        pauseStyle.up = game.skin.getDrawable("pauseImage");
        
        // Create pause Button object using style, pauseStyle.
        pauseButton = new Button( pauseStyle );
        
        // Add an InputListener to the pause Button, which will activate the togglePause method -- defined
        // by the BaseScreen class.
        pauseButton.addListener(
            new InputListener()
                    
            {
                // event = Type of input event.
                // x = The x coordinate of the mouse click -- origin is in the upper left corner.
                // y = The Y coordinate of the mouse click -- origin is in the upper left corner.
                // button = The button clicked.
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
                    {
                    // Occurs when the user clicks the pause button.
                    
                    // Reverse the pause state of the game.
                    togglePaused();
                    
                    // Updates visiblity of pause Table.
                    pauseOverlay.setVisible( isPaused() );
                    
                    // Return a value.
                    return true;
                    }
                
            }
        ); // End of code for adding InputListener to pause Button.
        
        // Set up Table object with user-interface layout.
        uiTable.pad(10); // Set padding, in pixels, for Table (on all sides).
        uiTable.add(starfishLeftLabel); // Add first cell in row, containing Label with number of starfish left.
        uiTable.add().expandX(); // Add second cell in row, extended all the way to the right to force the next
        // one to align with the rightmost edge.
        uiTable.add(pauseButton); // Add a cell containing the pause Button to the Table.
        uiTable.row(); // Add a row to the table.
        uiTable.add().colspan(3).expandY(); // Add a cell that encompasses all three columns and extends
        // the remaining vertical length of the screen.
        
        // Start setting up pause menu table.
        pauseOverlay = new Table(); // Create new Table object to store pause menu elements.
        pauseOverlay.setFillParent(true); // Size the root table to the stage.
        
        // Create and configure Stack.
        stacker = new Stack(); // Create Stack object to render uiTable followed by pauseOverlay.
        stacker.setFillParent(true); // Size the root table to the stage.
        uiStage.addActor(stacker); // Add Stack to uiStage screen.
        stacker.add(uiTable); // Add uiTable Table to Stack.
        stacker.add(pauseOverlay); // Add pauseOverlay Table to Stack.
        
        // Add a white texture to the skin and use the newDrawable method to create a tinted version
        // of the texture using a translucent black color, based on the image, white4px.png.
        game.skin.add("white", new Texture( Gdx.files.internal("assets/images/white4px.png")) );
        pauseBackground = game.skin.newDrawable("white", new Color(0,0,0,0.8f) );
        
        // Create Label for use in pause Table with the text, Paused, with the uiLabelStyle style from the Skin.
        pauseLabel = new Label("Paused", game.skin, "uiLabelStyle");

        // Create TextButton for use in pause Table to resume game, with the uiTextButtonStyle style from the Skin.
        resumeButton = new TextButton("Resume", game.skin, "uiTextButtonStyle");
        
        // Store width of resume Button.
        w = resumeButton.getWidth();
        
        // Add an InputListener to the resume Button, which will resume the game.
        resumeButton.addListener(
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
                // Occurs when the user performs a mousedown (click) event on the resume button.

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
                togglePaused(); // Reverses the pause state of the game (true to false, false to true).
                
                // Updates visibility of pause Table.
                pauseOverlay.setVisible( isPaused() );
                }
                
            }
        ); // End of code for adding InputListener to resume Button.

        // Create TextButton for use in pause Table to quit game, with the uiTextButtonStyle style from the Skin.
        quitButton = new TextButton("Quit", game.skin, "uiTextButtonStyle");
        
        // Add an InputListener to the quit Button, which will quit the game.
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
                // Occurs when the user performs a mousedown (click) event on the resume button.

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

        // Create Label for use in pause Table with the text, Volume, with the uiLabelStyle style from the Skin.
        volumeLabel = new Label("Volume", game.skin, "uiLabelStyle");
        
        // Create Slider object to control volume.
        // Minimum = 0
        // Maximum = 1
        // Step Size = 0.005
        // Vertical = false -- use horizontal orientation
        // Apply uiSliderStyle style from Skin.
        audioSlider = new Slider(0, 1, 0.005f, false, game.skin, "uiSliderStyle" );
        
        // Set slider to default value for audio volume.
        audioSlider.setValue( audioVolume );
        
        // Add an InputListener to the audio Slider, which will adjust the volume.
        audioSlider.addListener(
            new ChangeListener() // Fired when something in the Slider changes.
        
            {
                // event = Type of input event.
                // actor = Reference to actor involved in the change.
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor)
                {
                // Occurs when the user adjusts the Slider.
                audioVolume = audioSlider.getValue(); // Get value for audio volume after Slider adjustment.
                instrumental.setVolume(audioVolume); // Set audio volume for instrumental to updated value.
                oceanSurf.setVolume(audioVolume); // Set audio volume for ocean surf to updated value.
                }
                
            }
        ); // End of code for adding ChangeListener to Slider.
        
        // Set up pause overlay table.
        pauseOverlay.setBackground(pauseBackground); // Set background (tinted image).
        pauseOverlay.add(pauseLabel).pad(20); // Add a cell with the pause Label with 20 pixels of padding.
        pauseOverlay.row(); // Add a new row.
        pauseOverlay.add(resumeButton); // Add a cell with the resume Button.
        pauseOverlay.row(); // Add a new row.
        pauseOverlay.add(quitButton).width(w); // Add a cell with the quit button using the same width as resume.
        pauseOverlay.row(); // Add a new row.
        pauseOverlay.add(volumeLabel).padTop(100); // Add a cell with the volume Label with 100 pixels of padding.
        pauseOverlay.row(); // Add a new row.
        pauseOverlay.add(audioSlider).width(400); // Add a cell with the audio Slider set to 400 pixels in width.
        
        // Default the pause overlay table as not visible.
        pauseOverlay.setVisible(false);
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {
        
        /*
        The function occurs during the update phase (render method) and contains code related to game logic:
        
        1.  Sets acceleration of turtle in X and Y directions to 0.
	2.  Process user input (keys) related to movement.
	3.  Set correct animation for turtle.
	4.  Restrict turtle to window boundaries.
	5.  Adjust position of turtle to minimum distance to avoid collisions.
	6.  Remove any overlapping starfish.
        7.  Centers camera on player (mouse).
        */
        
        Camera cam; // Camera object that determines which part of stage to render.
                
        ArrayList<BaseActor> removeList; // List of starfish Actor objects to remove.
        
        // Set acceleration of turtle in X and Y directions to 0.
        turtle.setAccelerationXY(0,0);
        
        // If user clicked the left key, rotate to the left. 
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            turtle.rotateBy(90 * dt);
        
        // If user clicked the right key, rotate to the right. 
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            turtle.rotateBy(-90 * dt);
        
        // If user clicked the up key, increase the Y acceleration of the turtle by 100.
        if (Gdx.input.isKeyPressed(Keys.UP))
            turtle.accelerateForward(100);
        
        // Set correct animation.
        
        // If turtle moving at greater than or equal to one pixel per second and animation type of rest
        // active, then...
        if ( turtle.getSpeed() >= 1 && turtle.getAnimationName().equals("rest") )
            
            // Turtle moving at greater than or equal to one pixel per second and animation type of rest
            // active.
            
            // Change animation type for turtle to swim.
            turtle.setActiveAnimation("swim");
        
        // If turtle moving at less than one pixel per second and animation type of swim active, then...
        if ( turtle.getSpeed() < 1 && turtle.getAnimationName().equals("swim") )
            
            // Turtle moving at less than one pixel per second and animation type of swim active.
            
            // Change animation type for turtle to rest.
            turtle.setActiveAnimation("rest");
        
        // Restrict turtle to window boundaries.

        // X Boundaries = 0 to window - turtle width.
        turtle.setX( MathUtils.clamp( turtle.getX(), 0, mapWidth - turtle.getWidth() ));
        
        // Y Boundaries = 0 to window - mouse height.
        turtle.setY( MathUtils.clamp( turtle.getY(), 0, mapHeight - turtle.getHeight() ));
        
        /*
        // Loop through rocks.
        for (BaseActor r : rockList)
            {
            // Check whether turtle intersects current turtle in loop.
            // If so, move turtle minimum distance to not collide with rock.
            turtle.overlaps(r, true);
            }
        */
        
        // Loop through rocks.
        // Check whether turtle intersects each rock in loop.
        // If so, move turtle minimum distance to not collide with rock.
        // Also, play impact sound.
        //rockList.forEach((r) -> turtle.overlaps(r, true));

        // Loop through rocks...
        rockList.forEach(
            r -> {
                
            // If turtle overlaps rock, move to minimum distance to not collide.
            if ( turtle.overlaps(r, true) )
                
                {
                // Play starfish collection sound.
                //impactSound.play();
                }
                
            });
        
        // Initialize dynamic array for starfish Actors to remove.
        // ArrayList<BaseActor> removeList = new ArrayList<BaseActor>();
        removeList = new ArrayList<>();
        
        /*
        // Loop through starfish.
        // Check whether turtle intersects each starfish in loop.
        // If so, mark starfish for removal.
        for (BaseActor s : starfishList)
            {
            if ( turtle.overlaps(s, false) )
                removeList.add(s);
            }
        */
        
        // Loop through starfish.
        // Check whether turtle intersects each starfish in loop.
        // If so, mark starfish for removal.
        starfishList.forEach(
            s -> {
            if ( turtle.overlaps(s, false) )
                
                {
                // Remove starfish.
                removeList.add(s);
                }
                
            });
        
        /*
        // Loop through each starfish in removal list.
        // Remove each starfish in the removal list.
        for (BaseActor b : removeList)
            {
            b.remove(); // Remove the starfish from the stage.
            starfishList.remove(b); // Remove the starfish from list used for the update.
            }
        */
        
        // Loop through each starfish in removal list.
        // Remove each starfish in the removal list.
        removeList.forEach(
            b -> {
            b.remove(); // Remove the starfish from the stage.
            starfishList.remove(b); // Remove the starfish from list used for the update.
            waterDrop.play(audioVolume); // Play water drop sound when removing starfish.
            });
        
        // Get Camera object associated with the main stage.
        cam = mainStage.getCamera();

        // Center camera on player (turtle).
        cam.position.set( turtle.getX() + turtle.getOriginX(),
            turtle.getY() + turtle.getOriginY(), 0 );
        
        // Bound camera to layout -- determining center of window.
        cam.position.x = MathUtils.clamp(cam.position.x, getViewWidth() / 2, mapWidth - getViewWidth() / 2);
        cam.position.y = MathUtils.clamp(cam.position.y, getViewHeight() / 2, mapHeight - getViewHeight() / 2);

        // Update Label with current number of starfish left to collect.
        starfishLeftLabel.setText( "Starfish Left: " + starfishList.size() );
        
    }
    
    @Override
    public void dispose()
    {
        
        /*
        The function occurs during the cleanup phase and clears objects from memory.
        */
        
        // Clear objects from memory.
        waterDrop.dispose();
        instrumental.dispose();
        oceanSurf.dispose();
        
    }
    
}