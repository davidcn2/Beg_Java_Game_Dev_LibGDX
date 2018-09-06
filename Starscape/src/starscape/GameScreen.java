package starscape;

import core.BaseGame;
import core.BaseScreen;
import core.ParticleActor;
import core.PhysicsActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.MathUtils;
import core.BaseActor;

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
    about the game -- paddle, ball, base objects, bricks, power-ups, and removal list and 
    related properties (map height and width, timers, speeds, ...).
    
    Methods include:
    
    create:  Sets defaults.  Configures and adds the Actors to the stage.
    keyDown:  The function gets called when the user presses a key.  Pauses the game when pressing the
              P key.  Resets the game when pressing the R key.  Generates an explosion when pressing the
              SPACE key.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    */
    
    // Game world dimensions.
    private final int mapWidth; // = 800;
    private final int mapHeight; // = 600;
    
    private PhysicsActor spaceship; // PhysicsActor that will act as the spaceship.
    private ParticleActor thruster; // ParticleActor that will act as the truster.
    private ParticleActor baseExplosion; // ParticleActor that will act as the (base) explosion.
    
    // g = Screen object for game window (mouse, cheese, floor, win text, and elapsed time text).
    public GameScreen(BaseGame g)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Sets game world dimensions.
        // 3.  Calls create function to configure and adds actors to the stage.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(g, 800, 600);
        
        // Set game world dimensions.
        this.mapWidth = 800;
        this.mapHeight = 600;
        
        // Configure and add the actors to the stage:  background, spaceship, thurster, and explosion.
        create();
        
    }
    
    public final void create()
    {
        
        /*
        The function occurs during the startup / create phase and accomplishes the following:
        
        1.  Sets up background object.
        2.  Sets up spaceship object.
        3.  Sets up thruster object.
        4.  Sets up explosion object.
        */
        
        BaseActor background; // BaseActor that will act as background.
        BaseActor thrusterAdjuster; // BaseActor to associate with particle effect (ParticleActor).
        Texture shipTex; // Texture used for the spaceship.
        
        System.out.println("Initializing background...");
        
        // 1.  Create new BaseActor for the background.
        background = new BaseActor();
        
        // Set the properties for the background and add to the scene graph.
        background.setTexture( new Texture(Gdx.files.internal("assets/space.png")) ); // Load image to buffer.
        background.setPosition(0, 0); // Place image at (0, 0) -- relative to bottom left corner -- within window (stage).
        mainStage.addActor(background); // Add background Actor to the scene graph.
        
        System.out.println("Initializing spaceship...");
        
        // 2.  Create new BaseActor for the spaceship.
        spaceship = new PhysicsActor();
        
        shipTex = new Texture(Gdx.files.internal("assets/spaceship.png")); // Load image to buffer.
        shipTex.setFilter(TextureFilter.Linear, TextureFilter.Linear); // Set filter type -- controlling how pixel colors are interpolated when image is rotated or stretched.
        spaceship.storeAnimation( "default", shipTex ); // Add Animation object to hash map for spaceship.
        spaceship.setPosition(400, 300); // Place image at (400, 300) -- relative to bottom left corner -- within window (stage).
        spaceship.setOriginCenter(); // Set spaceship origin coordinates to center of image, to handle rotations.
        spaceship.setMaxSpeed(200); // Set maximum speed of spaceship.
        spaceship.setDeceleration(20); // Set deceleration of spaceship.
        mainStage.addActor(spaceship); // Add spaceship Actor to the scene graph.
        
        // Create thruster objects.
        
        System.out.println("Initializing thruster...");
        
        // Create new ParticleActor for the thruster.
        thruster = new ParticleActor();
        thruster.load("assets/thruster.pfx", "assets/"); // Load pfx (attributes) file for the thruster particle effect.
        
        System.out.println("Initializing thruster adjuster...");
        
        // Create new BaseActor to which to associate particle effect (Particle Actor).
        // The draw methods of particle effects do not directly support aligning and resizing.
        // The addition of the BaseActor adds support for the aligning and resizing.
        thrusterAdjuster = new BaseActor();
        
        // A single transparent pixel will get used with the BaseActor.
        thrusterAdjuster.setTexture( new Texture(Gdx.files.internal("assets/blank.png")) );
        thrusterAdjuster.addActor(thruster); // Add ParticleActor to BaseActor.
        thrusterAdjuster.setPosition( 0, 32 ); // Set position of BaseActor -- relative to bottom left corner -- within window (stage).
        thrusterAdjuster.setRotation( 90 ); // Set rotation of BaseActor.
        thrusterAdjuster.setScale( 0.25f ); // Set scale of BaseActor.
        thruster.start(); // Start the particle effect.
        spaceship.addActor(thrusterAdjuster); // Add spaceship Actor to the scene graph.
        
        System.out.println("Initializing (base) explosion...");
        
        // 4.  Create new ParticleActor for the explosion.
        baseExplosion = new ParticleActor();
        baseExplosion.load("assets/explosion.pfx", "assets/"); // Load pfx (attributes) file for the explosion particle effect.
    
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {
        
        /*
        The function occurs during the update phase and contains code related to game logic.
        The following logic occurs:

        1.  Processes movement related actions (arrow key presses and joystick movement).
        */

        /*
        Two action types -- polling and discrete.
        Polling:  Checks the state of the input hardware devices (such as the keyboard) during every iteration of the game loop.
                  Suited for continuous actions. > Constant movement.
        Discrete:  Automatically called when certain events occur (such as the initial press or release of a key, or click of a
                   mouse button).
                   Suited for irregularly occurring actions. > Jumping.
        */
        
        // Process keyboard input.

        // Clear acceleration of spaceship -- set both directions to 0.
        spaceship.setAccelerationXY(0,0);
        
        // If the user pressed the left arrow key, then...
        if ( Gdx.input.isKeyPressed(Keys.LEFT) )
            
            // User pressed the left arrow key.

            // Rotate spaceship to the left based on the time passed since the last frame.
            spaceship.rotateBy(180 * dt);
        
        // If the user pressed the right arrow key, then...
        if ( Gdx.input.isKeyPressed(Keys.RIGHT) )
            
            // User pressed the right arrow key.

            // Rotate spaceship to the right based on the time passed since the last frame.
            spaceship.rotateBy(-180 * dt);
        
        // If the user pressed the up arrow key, then...
        if ( Gdx.input.isKeyPressed(Keys.UP) )
            
            {
            // User pressing the up arrow key -- activate thrusters.

            // Add to the acceleration of the spaceship, based on the existing rotation and increasing the speed by 100.
            spaceship.addAccelerationAS(spaceship.getRotation(), 100);
            
            // Display the rocket fire when the user presses the up key.
            thruster.start();
            }
        
        else
            
            {
            // User NOT pressing the up arrow key.

            // Hide the rocket fire when the user not pressing the up key.
            thruster.stop();
            }
        
    }
        
    // InputProcessor methods for handling discrete input (occurs irregularly).
        
    // keycode = Code corresponding to to key pressed by user -- one of the constants in Input.Keys.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.

        // Processes the following types of key input:
        // 1.  Pauses the game when pressing the P key.
        // 2.  Resets the game when pressing the R key.
        // 3.  Generates an explosion when pressing the SPACE key.
        // 4.  Exits the game when pressing the Escape key.

        ParticleActor explosion; // ParticleActor to use for current explosion.

        // If the user pressed the P key, then...
        if (keycode == Keys.P)
            
            // The user pressed the P key.

            // Pause the game.
            togglePaused();

        // If the user pressed the R key, then...
        if (keycode == Keys.R)    
            
            // The user pressed the R key.

            // Restart the game.
            game.setScreen( new GameScreen(game) );

        // If the user pressed the SPACE key, then...
        if (keycode == Keys.SPACE)
        {
            
            // The user pressed the SPACE key.
            
            // Make a copy of the base explosion ParticleActor object.
            explosion = baseExplosion.clone();
            
            // Randomly place the explosion.
            explosion.setPosition( MathUtils.random(800), MathUtils.random(600) );
            
            // Start the particle effect.
            explosion.start();
            
            // Add explosion Actor to the scene graph.
            mainStage.addActor(explosion);
            
        }
        
        // If the user pressed the Escape key, then...
        if (keycode == Keys.ESCAPE)
            // The user pressed the Escape key.
            
            {
            // Exit the game.
            Gdx.app.exit();
            }

        // Return a value.
        return false;
        
    }
    
}