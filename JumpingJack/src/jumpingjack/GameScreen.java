package jumpingjack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import java.util.ArrayList;
import core.BaseActor;
import core.BaseGame;
import core.BaseScreen;
import core.Box2DActor;
import core.GameUtils;

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

public class GameScreen extends BaseScreen // Extends the BaseScreen class.
{

    /*
    The class extends the basic functionality of a BaseScreen class and stores information
    about the game -- background, player, coin, ball, crate, dirt, ground / platform, and 
    related properties (map height and width, timers, speeds, ...).
    
    Methods include:
    
    addSolid:  Automates the process of creating solid Box2D objects -- ground, walls, and platforms.
    create:  Initializes Box2D world.  Sets defaults.  Configures and adds the Actors to the stage.
      Sets up base actors for cloning.  Configures the ContactListener event.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    */
    
    // Declare object variables.
    private Player player; // Player object holding related properties.
    private ArrayList<Box2DActor> removeList; // List of actors to remove.
    private World world; // Highest level class in Box2D system.  World containing Box2DActor objects.
    
    // Declare regular variables.
    private int coins = 0;
    
    // Game world dimensions.
    private final int mapWidth; // Total map width, in pixels.
    private final int mapHeight; // Total map height, in pixels.
    
    // g = Screen object for game window (background, ground, planes, stars, ...).
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
        
        // Initialize Box2D world.  Set defaults.  Configure and add the Actors to the stage.
        // Set up base actors for cloning.  Configure the ContactListener event.
        create();
        
    }
    
    // t = Reference to Texture to use in animation / as image.
    // x = X-coordinate of position at which to place Actor.
    // y = Y-coordinate of position at which to place Actor.
    // w = Width to which to set Actor.
    // h = Height to which to set Actor.
    public void addSolid (Texture t, float x, float y, float w, float h)
    {
        
        // The function automates the process of creating solid Box2D objects -- ground, walls, and platforms.
        
        Box2DActor solid; // New solid object to create.
        
        solid = new Box2DActor(); // Instantiate new Box2DActor object.
        t.setFilter( TextureFilter.Linear, TextureFilter.Linear ); // Set filter type -- controlling how 
          // pixel colors are interpolated when image is rotated or stretched.
        solid.storeAnimation( "default", t ); // Store animation in Actor.
        solid.setPosition( x, y ); // Position Actor using passed values.
        solid.setSize( w, h ); // Set size of actor using passed values.
        mainStage.addActor( solid ); // Add actor to scene graph.
        solid.setStatic(); // Set the body as static -- not affected by forces or collisions and stationary.
        solid.setShapeRectangle(); // Set up the Box2D rectangle for the Actor.
        solid.initializePhysics(world); // Initialize Body (based on BodyDef) and Fixture.  Adds to World.
        
    }
    
    public final void create()
    {
        
        /*
        The function occurs during the startup / create phase and accomplishes the following:
        
        1.  Initializes the World with standard gravity and sleep.
        2.  Sets defaults and initializes empty array lists.
        3.  Set up the background object.
        4.  Configure solid objects -- ground and dirt.
        5.  Set up the crate object.
        6.  Set up the ball object.
        7.  Set up the (base) coin object.
        8.  Set up the three coin objects.
        9.  Set up the player object.
        10.  Configure the ContactListener event.
        */
        
        Coin baseCoin; // Box2DActor / Coin object that will act as the base coin (cloned).
        BaseActor bg; // Actor to use as background object.
        Box2DActor ball; // Box2DActor to use as ball object.
        Coin coin1; // Box2DActor / Coin object that will act as the first coin.
        Coin coin2; // Box2DActor / Coin object that will act as the second coin.
        Coin coin3; // Box2DActor / Coin object that will act as the third coin.
        Box2DActor crate; // Box2DActor to use as crate object.
        Texture ballTex; // Texture (image) to use for ball.
        Texture coinTex; // Texture (image) to use for coin.
        Texture crateTex; // Texture (image) to use for crate.
        Texture dirtTex; // Texture (image) to use for dirt.
        Texture groundTex; // Texture (image) to use for ground.
        Texture jumpTex; // Texture (image) to use for jumping player.
        Texture standTex; // Texture (image) to use for standing player.
        Texture t; // Texture (image) to use for background.
        Animation walkAnim; // Animation for walking player.
        
        // Initialize the World with standard gravity and sleep.
        world = new World(new Vector2(0, -9.8f), true);
        
        // Initialize empty array lists.
        removeList = new ArrayList<>(); // Initialize ArrayList to hold actors to remove later.
        // removeList = new ArrayList<Box2DActor>(); // ArrayList for removing objects later
        
        // Set up the background object.
        bg = new BaseActor(); // Create a new BaseActor for the background object.
        t = new Texture( Gdx.files.internal("assets/sky.png") ); // Load image to buffer. 
        bg.setTexture( t ); // Sets texture properties, including applying to region.
        mainStage.addActor( bg ); // Add (background) actor to scene graph.
        
        // Configure solid objects...
        
        // Set up the ground objects.
        groundTex = new Texture( Gdx.files.internal("assets/ground.png") ); // Load image to buffer. 
        
        // Create solid Box2D objects related to ground.
        addSolid( groundTex, 0, 0, 800, 32 );
        addSolid( groundTex, 150, 250, 100, 32 );
        addSolid( groundTex, 282, 250, 100, 32 );
        
        // Set up the dirt objects.
        dirtTex = new Texture( Gdx.files.internal("assets/dirt.png") ); // Load image to buffer.
        
        // Create solid Box2D objects related to dirt.
        addSolid( dirtTex, 0,0, 32,600 );
        addSolid( dirtTex, 768,0, 32,600 );
        // ---------------------------------
        
        // Set up the crate object.
        crate = new Box2DActor(); // Create a new Box2DActor for the crate object.
        crateTex = new Texture( Gdx.files.internal("assets/crate.png") ); // Load image to buffer.
        crateTex.setFilter( TextureFilter.Linear, TextureFilter.Linear ); // Set filter type -- 
          // controlling how pixel colors are interpolated when image is rotated or stretched.
        crate.storeAnimation( "default", crateTex ); // Store animation in Actor.
        crate.setPosition( 500, 100 ); // Set position of crate object.
        mainStage.addActor( crate ); // Add (crate) actor to scene graph.
        crate.setDynamic(); // Set the body as dynamic -- affected by forces and collisions and NOT stationary.
        crate.setShapeRectangle(); // Set up the Box2D rectangle for the Actor.
        crate.setPhysicsProperties( 1, 0.5f, 0.1f ); // Set crate to use standard density, average friction, and
          // small restitution.
        crate.initializePhysics( world ); // Initialize Body (based on BodyDef) and Fixture.  Adds to World.
        
        // Set up the ball object.
        ball = new Box2DActor(); // Create a new Box2DActor for the ball object.
        ballTex = new Texture( Gdx.files.internal("assets/ball.png") ); // Load image to buffer.
        ballTex.setFilter( TextureFilter.Linear, TextureFilter.Linear ); // Set filter type -- 
          // controlling how pixel colors are interpolated when image is rotated or stretched.
        ball.storeAnimation( "default", ballTex ); // Store animation in Actor.
        ball.setPosition( 300, 320 ); // Set position of ball object.
        mainStage.addActor( ball ); // Add (ball) actor to scene graph.
        ball.setDynamic(); // Set the body as dynamic -- affected by forces and collisions and NOT stationary.
        ball.setShapeCircle(); // Set up the Box2D circle for the Actor.
        ball.setPhysicsProperties( 1, 0.1f, 0.5f ); // Set crate to use standard density, small friction, and
          // average restitution.
        ball.initializePhysics( world ); // Initialize Body (based on BodyDef) and Fixture.  Adds to World.
        
        // Set up the (base) coin object.
        baseCoin = new Coin(); // Create a new Coin / Box2DActor for the (base) coin object.
        coinTex = new Texture( Gdx.files.internal("assets/coin.png") ); // Load image to buffer.
        coinTex.setFilter(TextureFilter.Linear, TextureFilter.Linear); // Set filter type -- 
          // controlling how pixel colors are interpolated when image is rotated or stretched.
        baseCoin.storeAnimation( "default", coinTex ); // Store animation in Actor.
        
        // Set up first coin on stage.
        coin1 = baseCoin.clone(); // Copy the first coin object.
        coin1.setPosition( 500, 250 ); // Set position of first coin object.
        mainStage.addActor( coin1 ); // Add (first coin) actor to scene graph.
        coin1.initializePhysics( world ); // Initialize Body (based on BodyDef) and Fixture.  Adds to World.
        
        // Set up second coin on stage.
        coin2 = baseCoin.clone(); // Copy the second coin object.
        coin2.setPosition( 550, 250 ); // Set position of second coin object.
        mainStage.addActor( coin2 ); // Add (second coin) actor to scene graph.
        coin2.initializePhysics( world ); // Initialize Body (based on BodyDef) and Fixture.  Adds to World.
        
        // Set up third coin on stage.
        coin3 = baseCoin.clone(); // Copy the third coin object.
        coin3.setPosition( 600, 250 ); // Set position of third coin object.
        mainStage.addActor( coin3 ); // Add (third coin) actor to scene graph.
        coin3.initializePhysics(world); // Initialize Body (based on BodyDef) and Fixture.  Adds to World.
        
        // Set up the player object.
        
        // Create the player object.
        player = new Player();
        
        // Set the properties for the player object and add to the scene graph.
        
        // Create animation related to player, loading images.
        walkAnim = GameUtils.parseImageFiles(
          "assets/walk-", ".png", 3, 0.15f, Animation.PlayMode.LOOP_PINGPONG );
        player.storeAnimation( "walk", walkAnim ); // Store animation in actor (for walking).
        standTex = new Texture( Gdx.files.internal("assets/stand.png") ); // Load image to buffer.
        standTex.setFilter(TextureFilter.Linear, TextureFilter.Linear); // Set filter type -- controlling how 
          // pixel colors are interpolated when image is rotated or stretched.
        player.storeAnimation( "stand", standTex ); // Store animation in actor (for standing).
        jumpTex = new Texture( Gdx.files.internal("assets/jump.png") ); // Load image to buffer.
        jumpTex.setFilter(TextureFilter.Linear, TextureFilter.Linear); // Set filter type -- controlling how 
          // pixel colors are interpolated when image is rotated or stretched.
        player.storeAnimation( "jump", jumpTex ); // Store animation in actor (for jumping).
        player.setPosition( 164, 300 ); // Set position of player.
        player.setSize( 60, 90 ); // Set size of player.
        mainStage.addActor(player); // Add (player) actor to scene graph.
        
        // Set Box2D properties for player.
        player.setDynamic(); // Set the body as dynamic -- affected by forces and collisions and NOT stationary.
        player.setShapeRectangle(); // Set up the Box2D rectangle for the Actor.
        // set standard density, average friction, small restitution
        player.setPhysicsProperties( 1, 0.5f, 0.1f ); // Set crate to use standard density, average friction, 
          // and small restitution.
        player.setFixedRotation(); // Set the body to not rotate.
        player.setMaxSpeedX( 2 ); // Set maximum speed in x-direction for player.
        player.initializePhysics(world); // Initialize Body (based on BodyDef) and Fixture.  Adds to World.
        
        /*
        Implement the ContactListener interface.
        
        • beginContact:  Called when a pair of fixtures first come into contact with each other.
        • endContact:  Called when a pair of fixtures cease being in contact with each other.
        • preSolve:  Provides a chance to alter the contact before processing.
        • postSolve:  Provides the means to find out what impulses were caused by the collision
        response after its application.
        
        The types of contact events important to the JumpingJack game include:

        • When a Coin object and the "main" fixture of a Player object first make contact, the
        coin should be added to the removeList.  Handled in the beginContact method.
        
        • If any solid (that is, non-Coin) object and the "bottom" fixture of a Player first
        make contact, add 1 to the player's ground-counting variable, and set the player's
        animation to stand.  Handled in the beginContact method.
        
        • If any solid (that is, non-Coin) object and the "bottom" fixture of a Player leave
        contact, subtract 1 from the player's ground-counting variable.  Handled in the
        endContact method.
        */
        
        world.setContactListener(
            new ContactListener()
            {
                
            // contact = Contact object containing information about the collision.  Relates to two objects.
            @Override
            public void beginContact(Contact contact)
            {
                
                // Called when a pair of fixtures first come into contact with each other.
                
                Coin c; // Reference to coin object involved in contact.
                Object objC; // User data associated with coin object.
                Object objP; // User data associated with player object.
                Player p; // Reference to player object involved in contact.
                
                // If contact involved coin, store user data associated with coin.  Otherwise, store null.
                objC = GameUtils.getContactObject(contact, Coin.class);
                
                // If coin object NOT set to null, then...
                if (objC != null)
                    {
                    // Coin object NOT set to null.
                    // Indicates contact between coin and ?.
                        
                    // System.out.println("? in contact with coin.");
                        
                    // If contact involved player, store user data associated with player.
                    // Otherwise, store null.
                    objP = GameUtils.getContactObject(contact, Player.class, "main");
                    
                    // If player object NOT set to null, then...
                    if (objP != null)
                        {
                        // Player object NOT set to null.
                        // Indicates contact between coin and player.
                            
                        // System.out.println("Player in contact with coin.");
                            
                        c = (Coin)objC; // Get reference to coin involved in contact.
                        removeList.add( c ); // Add coin to removal list.
                        }
                    
                    // Exit function.
                    return; // Avoid possible jumps.
                    }

                // If contact involved bottom of player, store user data associated with Player.
                // Otherwise, store null.
                objP = GameUtils.getContactObject(contact, Player.class, "bottom");
                
                // If player object NOT set to null, then...
                if ( objP != null )
                    {
                    // Player object NOT set to null.
                    // Indicates contact between player and ground.
                    p = (Player)objP; // Get reference to player.
                    p.adjustGroundCount( 1 ); // Add one to ground contact.
                    p.setActiveAnimation( "stand" ); // Set "stand" as active animation for player.
                    }
                
            }
            
            // contact = Contact object containing information about the collision.  Relates to two objects.
            @Override
            public void endContact(Contact contact)
            {
                
                // Called when a pair of fixtures cease being in contact with each other.
                
                Object objC; // User data associated with coin object.
                Object objP; // User data associated with player object.
                Player p; // Reference to player object involved in contact.
                
                // If contact involved coin, store user data associated with coin.  Otherwise, store null.
                objC = GameUtils.getContactObject(contact, Coin.class);
                
                // If coin object NOT set to null, then...
                if (objC != null)
                    // Coin object NOT set to null.
                    // Indicates coin leaving contact with ?.
                    // Exit function.
                    return;
                
                // If contact involved player, store user data associated with player.  Otherwise, store null.
                objP = GameUtils.getContactObject(contact, Player.class, "bottom");
                
                // If player object NOT set to null, then...
                if ( objP != null )
                    {
                    // Player object NOT set to null.
                    // Indicates player leaving contact with ground.
                    p = (Player)objP; // Get reference to player.
                    p.adjustGroundCount( -1 ); // Subtract one from ground contact.
                    }
                
            }
            
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) { }
            
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) { }
            });
        
    }
    
    @Override
    public void update(float dt)
    {
        
        /*
        The function occurs during the update phase (render method) and accomplishes the following:
        
        1.  Clears actors in removal list.
        2.  Activate physics simulation (Box2D) using the step method of the World object.
        3.  Remove cleared objects from the stage.
        4.  Process continuous input -- left and right arrows for movement.
        5.  Adjust animation of player to stand or walk, depending on conditions.
        */
        
        // Clear objects from removal list.
        removeList.clear();
        
        // Activate the physics simulation (Box2D) using the step method of the World object.
        // Values set below assume the game is running at 60 frames per second.
        world.step( 1/60f, 6, 2 );
        
        /*
        for (Box2DActor ba : removeList)
        {
        ba.destroy();
        world.destroyBody( ba.getBody() );
        }
        */
        
        // Destroy actors in removal list -- clear them from the stage.
        
        // Loop through actors in removal list.
        removeList.stream().map((ba) -> {
            
            // Remove actor from the stage.
            ba.destroy();
            
            // Return reference to actor.
            return ba;
            
        }).forEachOrdered((ba) -> {
            // Remove body from Box2D world.
            world.destroyBody( ba.getBody() );
        });

        // Process continuous input.
        
        // If the user pressed the left button, then...
        if( Gdx.input.isKeyPressed(Keys.LEFT) )
        {
            // User pressed the left button.
            player.setScale( -1, 1 ); // Change orientation of graphic (flip horizontally).
            player.applyForce( new Vector2(-3.0f, 0) ); // Apply force to move player left.
        }

        if( Gdx.input.isKeyPressed(Keys.RIGHT) )
        {
            // User pressed the right button.
            player.setScale( 1, 1 ); // Use regular orientation of graphic.
            player.applyForce( new Vector2(3.0f, 0) ); // Apply force to move player right.
        }

        // If player speed greater than 0.1 and standing, then...
        if ( player.getSpeed() > 0.1 && player.getAnimationName().equals("stand") )
            // Player speed greater than 0.1 and standing.
            // Set active animation to walk.
            player.setActiveAnimation("walk");
        
        // If player speed less than 0.1 and walking, then...
        if ( player.getSpeed() < 0.1 && player.getAnimationName().equals("walk") )
            // Player speed less than 0.1 and walking.
            // Set active animation to stand.
            player.setActiveAnimation("stand");
        
    }
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.
        
        // 1.  Pauses the game when pressing the P key.
        // 2.  Resets the game when pressing the R key.
        // 3.  Causes the player to jump when pressing the space key -- if on the ground.
        // 4.  Exits the game when pressing the Escape key.
        
        Vector2 jumpVec; // Vector associated with jumping movement.
        
        // InputProcessor methods for handling discrete input.
        
        // If the user pressed the P key, then...
        if (keycode == Keys.P)
        {
            // The user pressed the P key.
            
            // Pause the game.
            togglePaused();
        }

        // If the user pressed the R key, then...
        if (keycode == Keys.R)
        {
            // The user pressed the R key.
            
            // Reset the game.
            game.setScreen( new GameScreen(game) );
        }

        // If the user pressed the space key and player is on the ground, then...
        if (keycode == Keys.SPACE && player.isOnGround() )
        {
            // User pressed the space key and player is on the ground.
            jumpVec = new Vector2( 0, 3 ); // Establish vector associated with jumping movement.
            player.applyImpulse( jumpVec ); // Applies impulse to player to cause a jump.
            player.setActiveAnimation("jump"); // Set active animation of player to jump.
        }
        
        // If the user pressed the Escape key, then...
        if (keycode == Keys.ESCAPE)
        {
            // The user pressed the Escape key.
            
            // Exit the game.
            Gdx.app.exit();
        }
        
        // Return a value.
        return false;
    
    }
    
}
