package rectangledestroyer;

import core.BaseGame;
import core.BaseScreen;
import core.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
    about the game -- paddle, ball, base objects, bricks, power-ups, and removal list and 
    related properties (map height and width, timers, speeds, ...).
    
    Methods include:
    
    // create:  Sets defaults.  Configures and adds the Actors to the stage.  Sets up base actors for cloning.
    // keyDown:  The function gets called when the user presses a key.  Pauses the game when pressing the
    //           P key.  Resets the game when pressing the R key.
    // update:  Occurs during the update phase (render method) and contains code related to game logic.
    */
    
    private Paddle paddle; // BaseActor that will act as the paddle.
    private Ball ball; // PhysicsActor that will act as the ball.
    private Brick baseBrick; // BaseActor that will act as the base brick (cloned).
    private ArrayList<Brick> brickList; // PhysicsActor array that will act as the bricks.
    private Powerup basePowerup; // PhysicsActor that will act as the base powerup (cloned).
    private ArrayList<Powerup> powerupList; // PhysicsActor array that will act as the powerups.
    private ArrayList<BaseActor> removeList; // List of actors to remove.
    
    // Game world dimensions.
    private final int mapWidth; // = 800;
    private final int mapHeight; // = 600;
    
    // g = Screen object for game window (background, paddle, ball, bricks, powerups, ...).
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
        
        // Configure and add the actors to the stage:  background, paddle, ball, bricks, powerups, ...
        create();
        
    }

    public final void create()
    {
        
        /*
        The function occurs during the startup / create phase and accomplishes the following:
        
        1.  Sets defaults and initializes empty array lists.
        2.  Sets up paddle object.
        3.  Sets up base brick object.
        4.  Sets up ball object.
        5.  Sets up base power-up object.
        6.  Sets up bricks.
        */
        
        Brick brick; // Current Brick in loop.
        Color[] colorArray; // Array of colors to use for bricks.
        Texture ballTex; // Texture (image) to use for ball.
        Texture brickTex; // Texture (image) to use for base brick.
        Texture paddleTex; // Texture (image) to use for paddle.
        
        // Create the paddle object.
        paddle = new Paddle();
        
        // Set the properties for the paddle object and add to the scene graph.
        paddleTex = new Texture(Gdx.files.internal("assets/paddle.png")); // Load image to buffer.
        paddleTex.setFilter( TextureFilter.Linear, TextureFilter.Linear ); // Set filter type -- controlling how 
          // pixel colors are interpolated when image is rotated or stretched.
        paddle.setTexture( paddleTex ); // Assign texture and set properties.
        mainStage.addActor(paddle); // Add actor to scene graph.
        
        // Set the properties for the base brick object and clone later.
        // The clones will get added to the scene graph.
        
        // Create the base brick object.
        baseBrick = new Brick();
        
        // Set the properties for the base brick object and clone later.
        brickTex = new Texture(Gdx.files.internal("assets/brick-gray.png")); // Load image to buffer.
        baseBrick.setTexture( brickTex ); // Assign texture and set properties.
        baseBrick.setOriginCenter(); // Set origin of actor to the center of associated image -- for rotation.
        baseBrick.copyUsingTintColor = false;
        
        // baseBrick.setColor(Color.BLUE);
        
        // Instantiate array to hold bricks.
        // brickList = new ArrayList<Brick>();
        brickList = new ArrayList<>();
        
        // Create the ball object.
        ball = new Ball();
        
        // Set the properties for the paddle object and add to the scene graph.
        ballTex = new Texture(Gdx.files.internal("assets/ball.png")); // Load image to buffer.
        ball.storeAnimation( "default", ballTex ); // Store (default / normal mode ) actor animation.
        ball.setPosition( 400, 200 ); // Set position of bottom left corner of actor.
        ball.setVelocityAS( 30, 300 ); // Set velocity vector of actor using passed angle and speed.
        ball.setAccelerationXY( 0, -10 ); // Set acceleration vector of actor using passed x and y values.
        mainStage.addActor( ball ); // Add actor to scene graph.
        
        // Set the properties for the base powerup object and clone later.
        // The clones will get added to the scene graph.
        
        // Create the base powerup object.
        basePowerup = new Powerup();
        
        // Set the properties for the base powerup object and clone later.
        basePowerup.setVelocityXY(0, -100); // Set velocity vector of actor using passed x and y values.
        basePowerup.storeAnimation("paddle-expand", // Store paddle expander actor animation.
          new Texture(Gdx.files.internal("assets/paddle-expand.png")) );
        basePowerup.storeAnimation("paddle-shrink", // Store paddle shrinker actor animation.
          new Texture(Gdx.files.internal("assets/paddle-shrink.png")) );
        basePowerup.setOriginCenter(); // Set origin of actor to the center of associated image -- for rotation.
        
        // Instantiate array to hold powerups.
        // powerupList = new ArrayList<Powerup>();
        powerupList = new ArrayList<>();
        
        // Instantiate array to hold actors to remove.
        // removeList = new ArrayList<BaseActor>();
        removeList = new ArrayList<>();
        
        // Store array of colors to use for bricks.
        colorArray = new Color[] { Color.RED, Color.ORANGE, Color.YELLOW,
        Color.GREEN, Color.BLUE, Color.PURPLE };
        
        // Next, create the bricks and add them to the scene graph and array.
        
        // Loop vertically through bricks to create.
        for (int j = 0; j < 6; j++)
        
            {
            
            // Loop horizontally through bricks to create.
            for (int i = 0; i < 10; i++)
                {
                // Copy a brick.
                brick = baseBrick.clone();
                
                // Position the brick.
                brick.setPosition( 8 + 80 * i, 500 - (24 + 16) * j );
                
                // Set the color of the brick.
                brick.setColor( colorArray[j] );
                
                // Add brick to array.
                brickList.add( brick );
                
                // Set reference of (brick) actor to ArrayList.
                brick.setParentList( brickList );
                
                // Add brick (actor) to scene graph.
                mainStage.addActor( brick );
                }
            
            }
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {
        
        /*
        The function occurs during the update phase (render method) and accomplishes the following:
        
        1.  Adjusts horizontal position of center of paddle to x coordinate of mouse.
        2.  Adjust paddle to remain inside left and right edges of screen.
        3.  Bounce ball off screen edges.
        4.  Bounce ball off paddle.
        5.  Check whether ball hit any of the bricks and handle as necessary.
        6.  Check whether any of the power-ups have collided with the paddle and handle as necessary.
        7.  Clears actors in removal list.
        */
        
        Powerup pow; // Power-up to spawn.
        String powName; // Name of power-up overlapping paddle.
        
        // First, the horizontal position of the paddle must be continuously adjusted to center on the 
        // x coordinate of the mouse.  Also, the paddle object should be bound to the screen.
        
        // Set horizontal position of paddle (center) to current x coordinate of the mouse.
        paddle.setPosition( Gdx.input.getX() - paddle.getWidth() / 2, 32 );
        
        // If paddle outside left edge of screen, then...
        if ( paddle.getX() < 0 )
            // Paddle outside left edge of screen.
            // Align to left edge of screen.
            paddle.setX(0);
        
        // If paddle outside right edge of screen, then...
        if ( paddle.getX() + paddle.getWidth() > mapWidth )
            // Paddle outside right edge of screen.
            // Align to right edge of screen.
            paddle.setX(mapWidth - paddle.getWidth());
        
        // Next, as necessary, bounce the ball off the screen edges.
        
        // If ball outside left edge of screen, then...
        if (ball.getX() < 0)
            
            {
            // Ball outside left edge of screen.
            
            // Align ball to left edge of screen.
            ball.setX(0);
            
            // Multiply horizontal component of velocity by -1 to reverse x direction of ball.
            ball.multVelocityX(-1);
            }
        
        // If ball outside right edge of screen, then...
        if (ball.getX() + ball.getWidth() > mapWidth)
            
            {
            // Ball outside right edge of screen.
                
            // Align ball to right edge of screen.
            ball.setX( mapWidth - ball.getWidth() );
            
            // Multiply horizontal component of velocity by -1 to reverse x direction of ball.
            ball.multVelocityX(-1);
            }
        
        // If ball outside bottom edge of screen, then...
        if (ball.getY() < 0)
            
            {
            // Ball outside bottom edge of screen.
                
            // Align ball to bottom edge of screen.
            ball.setY(0);
            
            // Multiply vertical component of velocity by -1 to reverse y direction of ball.
            ball.multVelocityY(-1);
            }
        
        // If ball outside top edge of screen, then...
        if (ball.getY() + ball.getHeight() > mapHeight)
            
            {
            // Ball outside top edge of screen.
                
            // Align ball to top edge of screen.
            ball.setY( mapHeight - ball.getHeight() );
            
            // Multiply vertical component of velocity by -1 to reverse y direction of ball.
            ball.multVelocityY(-1);
            }
        
        // Bounce ball off paddle.
        ball.overlaps(paddle, true);
        
        // Check whether ball hit any of the bricks and handle as necessary.
        
        // Any brick hitting the ball gets added to the removeList array, which later calls the destroy
        // method -- activating its fade-out effect.
        // When the ball hits a brick, a twenty percent chance exists for a random power-up to appear.
        // If the power-up spawns, an action will use an animated scaling effect.  The effect will
        // cause the power-up to appear to grow from a single pixel to its full size over the course of
        // half a second.
        
        // Clear contents of Actor removal list.
        removeList.clear();
        
        // Loop through bricks.
        for (Brick br : brickList)
            
            {
            // If ball overlaps brick, then...
            if ( ball.overlaps(br, true) ) 
                {
                // Ball overlaps brick.
                
                // Calling the overlaps function causes the ball to bounce off the affected brick.
                
                // Add the brick to the removal list.
                removeList.add(br);
                
                // If random number within range (20% chance of power-up), then...
                if (Math.random() < 0.20)
                    {  
                    // Random number within range (20% chance of power-up).
                    
                    // Clone a power-up actor.
                    pow = basePowerup.clone();
                    
                    // Randomly select one of the stored animations to use as the power-up.
                    pow.randomize();
                    
                    // Center power-up within center of brick.
                    pow.moveToOrigin(br);
                    
                    // Set initial scale to 0, 0 (effectively, a size of 0).
                    pow.setScale(0,0);
                    
                    // Add action to scale to full size within 0.50 seconds.
                    pow.addAction( Actions.scaleTo(1,1, 0.5f) );
                    
                    // Add power-up actor to array.
                    powerupList.add(pow);
                    
                    // Set reference of (power-up) actor to ArrayList.
                    pow.setParentList(powerupList);
                    
                    // Add power-up actor to scene graph.
                    mainStage.addActor(pow);
                    }
                
                }
            
            }
        
        // Check whether any of the power-ups have collided with the paddle and handle as necessary.
        // If a power-up collided with the paddle, determine the name of the animation and carry out 
        // the associated effect.  The only powerup effects involve changing the size of the paddle.
        // Reasonable constraints will be set on the maximum and minimum size the paddle can attain. 
        // Animate the change in size using an Action.
        
        // Loop through power-ups in array.
        for (Powerup pow2 : powerupList)
            
            {
            
            // If power-up overlaps paddle, then...
            if ( pow2.overlaps(paddle) )
                
                {
                // Power-up overlaps paddle.
                
                // Store name of power-up overlapping paddle.
                powName = pow2.getAnimationName();
                
                // If power-up is "paddle-expand" and paddle less than maximum, then...
                if ( powName.equals("paddle-expand") && paddle.getWidth() < 256)
                    
                    {
                    // Power-up is "paddle-expand" and paddle less than maximum.
                        
                    // Add action to paddle to increase width by 32 pixels over the course of 0.50 seconds.
                    paddle.addAction( Actions.sizeBy(32, 0, 0.5f) );
                    }
                
                // Otherwise, if power-up is "paddle-shrink" and paddle greater than minimum, then...
                else if ( powName.equals("paddle-shrink") && paddle.getWidth() > 64)
                    
                    {
                    // Power-up is "paddle-shrink" and paddle greater than minimum.
                        
                    // Add action to paddle to decrease width by 32 pixels over the course of 0.50 seconds.
                    paddle.addAction( Actions.sizeBy(-32, 0, 0.5f) );
                    }
                
                // Add power-up to removal list.
                removeList.add(pow2);
                }
            
            }
        
        // Destroy actors in removal list -- clear them from the stage.
        
        // Loop through actors in removal list.
        for (BaseActor ba : removeList)
        {
            // Remove actor from the stage.
            ba.destroy();
        }
        
    }

    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.
        // Pauses the game when pressing the P key.
        // Resets the game when pressing the R key.
        // Exits the game when pressing the Escape key.
        
        // Depending on key pressed...
        switch (keycode) {
            
            case Input.Keys.P:
                
                // The user pressed the P key.
                
                // Pause the game.
                togglePaused();
                break;
                
            case Input.Keys.R:
                
                // The user pressed the R key.
                
                // Reset the game.
                game.setScreen( new GameScreen(game) );
                break;
                
            case Input.Keys.ESCAPE:
                
                // The user pressed the Escape key.
                
                // Exit the game.
                Gdx.app.exit();
                break;
                
            default:
                break;
                
        } // End - Switch ... Depending on key pressed...
        
        // Return a value.
        return false;
        
    }
    
}