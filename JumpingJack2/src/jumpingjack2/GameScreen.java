package jumpingjack2;

// Java imports...
import java.util.ArrayList;

// General LibGDX imports...
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

// LibGDX - Box2D imports...
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.ContactImpulse;

// LibGDX tilemap imports...
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

// Core and custom code imports...
import core.BaseGame;
import core.BaseScreen;
import core.Box2DActor;
import core.GameUtils;
import core.ParticleActor;
import routines.ArrayRoutines;

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
    about the game -- background, player, coin, ball, crate, dirt, ground / platform, TiledMap, and 
    related properties (map height and width, timers, speeds, ...).
    
    Methods include:
    
    addSolid:  Automates the process of creating solid Box2D objects -- ground, walls, and platforms.
      Uses a RectangleMapObject as its parameter, which easily supports TiledMap objects.
    create:  Initializes Box2D world.  Sets defaults.  Configures and adds the Actors to the stage.
      Sets up base actors for cloning.  Sets up TiledMap.  Configures the ContactListener event.
    render:  Called when the screen should render itself.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    worldContactListener:  Returns an implementation of the ContactListener interface for the World.
    */
    
    /*
    Important Objects:
    
    TiledMap:  Store the data from the tilemap file, which is loaded using a static method from the 
      TmxMapLoader class.
    OrthogonalTileMapRenderer:  Used to draw the contents of the various layers of the tilemap.
      The layers to be rendered are specified by an array of integers.
    OrthographicCamera:  Used to determine which region of a tilemap layer should be rendered, 
      analogous to the role of the Camera object that belongs to each Stage.
    */
    
    // Declare object variables...
    private Player player; // Player object holding related properties.
    private World world; // Highest level class in Box2D system.  World containing Box2DActor objects.
    private ArrayList<Box2DActor> removeList; // List of actors to remove.
    private ParticleActor baseSparkle; // ParticleActor that will act as the (base) sparkle effect for coin.
    TiledMap tiledMap; // Map, in Tiled format.
    OrthographicCamera tiledCamera; // Camera to use with Tiled map.
    TiledMapRenderer tiledMapRenderer; // Renderer to use with Tiled map.
    
    // Declare regular variables...
    private final int[] backgroundLayer = new int[1]; // Indices of the tilemap layers to be rendered before the main stage.  Contains background.
    private final int[] tileLayer = new int[1]; // Indices of the tilemap layers to be rendered after the main stage.  Contains tiles / objects on background.
    
    /*
    int[] backgroundLayer = {0};
    int[] tileLayer = {1};
    */

    // Game world dimensions.
    private final int mapWidth; // Total map width, in pixels.
    private final int mapHeight; // Total map height, in pixels.
    
    // g = Screen object for game window (background, ground, planes, stars, ...).
    public GameScreen(BaseGame g)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Sets game world dimensions.
        // 3.  Populates arrays.
        // 4.  Calls the create() function to perform remaining startup logic.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(g, 800, 600);
        
        // Set game world dimensions.
        this.mapWidth = 1280;
        this.mapHeight = 600;
        
        // Populate arrays.
        ArrayRoutines.addAll(backgroundLayer, 0);
        ArrayRoutines.addAll(tileLayer, 1);
        
        // Initialize Box2D world.  Set defaults.  Configure and add the Actors to the stage.
        // Set up base actors for cloning.  Configure the ContactListener event.
        create();
        
    }
    
    // rmo = Information to use to add solid Box2D object -- includes x, y, width, and height.
    public void addSolid(RectangleMapObject rmo)
    {
        
        // The function automates the process of creating solid Box2D objects -- ground, walls, and platforms.
        // The function uses a RectangleMapObject as its parameter, which easily supports TiledMap objects.
        
        Rectangle r; // Rectangle object with properties from RectangleMapObject passed.
        Box2DActor solid; // New solid object to create.
        
        r = rmo.getRectangle(); // Create Rectangle based on passed object.
        solid = new Box2DActor(); // Instantiate new Box2DActor object.
        solid.setPosition( r.x, r.y ); // Position Actor using passed values.
        solid.setSize( r.width, r.height ); // Set size of actor using passed values.
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
        3.  Start setting up the player object.
        4.  Set up the (base) coin object.
        5.  Set up the (base) sparkle object.
        6.  Sets up tile map, renderer, and camera.
        7.  Sets up objects across all layers in TiledMap.
        8.  Configure the ContactListener event.
        */
        
        // Regular variables.
        String name; // Current object name (in loop).
        
        // Declare object variables.
        Coin baseCoin; // Box2DActor / Coin object that will act as the base coin (cloned).
        Coin coin; // Used to clone coin actors.
        Texture coinTex; // Texture (image) to use for coin.ol
        Texture jumpTex; // Texture (image) to use for jumping player.
        MapObjects objects; // Collection of objects contained in all the layers.
        Rectangle r; // Rectangle object used when iterating through items in layers.
        RectangleMapObject rectangleObject; // Rectangle shaped map object used when iterating through items in layers.
        Texture standTex; // Texture (image) to use for standing player.
        Animation walkAnim; // Animation for walking player.
        
        // Initialize the World with standard gravity and sleep.
        world = new World(new Vector2(0, -9.8f), true);
        
        // Initialize empty array lists.
        removeList = new ArrayList<>(); // Initialize ArrayList to hold actors to remove later.
        // removeList = new ArrayList<Box2DActor>(); // ArrayList for removing objects later
        
        // TiledMap will provide the background image.
        
        // Set up the player object.
        
        // Create the player object.
        player = new Player();
        
        // Set the properties for the player object and add to the scene graph.
        
        // Create animation related to player, loading images.
        walkAnim = GameUtils.parseImageFiles(
          "assets/walk-", ".png", 3, 0.15f, Animation.PlayMode.LOOP_PINGPONG );
        player.storeAnimation( "walk", walkAnim ); // Store animation in actor (for walking).
        standTex = new Texture( Gdx.files.internal("assets/stand.png") ); // Load image to buffer.
        standTex.setFilter( TextureFilter.Linear, TextureFilter.Linear ); // Set filter type -- controlling how 
          // pixel colors are interpolated when image is rotated or stretched.
        player.storeAnimation( "stand", standTex ); // Store animation in actor (for standing).
        jumpTex = new Texture( Gdx.files.internal("assets/jump.png") ); // Load image to buffer.
        jumpTex.setFilter( TextureFilter.Linear, TextureFilter.Linear ); // Set filter type -- controlling how 
          // pixel colors are interpolated when image is rotated or stretched.
        player.storeAnimation( "jump", jumpTex ); // Store animation in actor (for jumping).
        player.setSize( 60, 90 ); // Set size of player.
        mainStage.addActor( player ); // Add (player) actor to scene graph.
        
        // Set other player properties later...
        
        // Set up the (base) coin object.
        baseCoin = new Coin(); // Create a new Coin / Box2DActor for the (base) coin object.
        coinTex = new Texture( Gdx.files.internal("assets/coin.png") ); // Load image to buffer.
        coinTex.setFilter( TextureFilter.Linear, TextureFilter.Linear ); // Set filter type -- controlling how 
          // pixel colors are interpolated when image is rotated or stretched.
        baseCoin.storeAnimation( "default", coinTex ); // Store animation in Actor.
        
        // Set up the (base) sparkle object.
        baseSparkle = new ParticleActor(); // Create a new ParticleActor for the (base) sparkle object.
        baseSparkle.load( "assets/sparkler.pfx", "assets/" ); // Load assets related to particle effect.
        
        // Set up tile map, renderer, and camera.
        tiledMap = new TmxMapLoader().load( "assets/platform-map.tmx" ); // Load the TiledMap.
        tiledMapRenderer = new OrthogonalTiledMapRenderer( tiledMap ); // Associate renderer with loaded TiledMap.
        tiledCamera = new OrthographicCamera(); // Instantiate Orthographic Camera.
        tiledCamera.setToOrtho( false, getViewWidth(), getViewHeight() ); // Set the camera to an orthographic projection.
        tiledCamera.update(); // Recalculate the projection and view matrix of the camera and the Frustum planes.
        
        // Iterate over the ObjectData layer of the tilemap to get data pertaining to the player 
        // and coin objects.
        
        // Store collection of standard objects across all layers in TiledMap.
        objects = tiledMap.getLayers().get("ObjectData").getObjects();
        
        // Loop through objects.
        for (MapObject object : objects)
            
            {
            // Store name of current object in loop.
            name = object.getName();
            
            // All object data assumed to be stored as rectangles.
            
            // Cast current object to a RectangleMapObject to allow for getting position.
            rectangleObject = (RectangleMapObject)object;
            
            // Get rectangle associated with current object.
            r = rectangleObject.getRectangle();
            
            // Based on object name...
            switch (name)
                {
                case "player": // Looking at player object...
                    player.setPosition( r.x, r.y ); // Set position of player actor.
                    break;
                case "coin": // Looking at one of the coin objects...
                    coin = baseCoin.clone(); // Copy the coin object.
                    coin.setPosition( r.x, r.y ); // Set position of current coin actor.
                    mainStage.addActor( coin ); // Add current coin actor to scene graph.
                    coin.initializePhysics( world ); // Initialize Body (based on BodyDef) and Fixture.  Adds to World.
                    break;
                default: // All other objects...
                    System.err.println( "Unknown tilemap object: " + name ); // Display error message.
                    break;
                }
            
            } // End ... Loop through objects in all layers.
        
        // Now that the position of the player position is known, the physics-related data can be initialized.
        player.setDynamic(); // Set the body as dynamic -- affected by forces and collisions and NOT stationary.
        player.setShapeRectangle(); // Set up the Box2D rectangle for the Actor.
        // set standard density, average friction, small restitution
        player.setPhysicsProperties( 1, 0.5f, 0.1f ); // Set crate to use standard density, average friction, 
          // and small restitution.
        player.setMaxSpeedX( 2 ); // Set maximum speed in x-direction for player.
        player.setFixedRotation(); // Set the body to not rotate.
        player.initializePhysics( world ); // Initialize Body (based on BodyDef) and Fixture.  Adds to World.
        
        // Next, iterate over the PhysicsData layer of the tilemap.
        // Using the preceding addSolid method, initialize the solid objects.
        
        // Store collection of physics objects across all layers in TiledMap.
        objects = tiledMap.getLayers().get("PhysicsData").getObjects();
        
        // Loop through objects.
        for (MapObject object : objects)
            
            {
            // If looking at an object of type, RectangleMapObject, then...
            if (object instanceof RectangleMapObject)
                // Object is of type, RectangleMapObject.
                // Create solid Box2D object.
                addSolid( (RectangleMapObject)object );
            
            // Otherwise, ...
            else
                // Object is of unknown type.
                System.err.println("Unknown PhysicsData object.");
            }
        
        // Implement ContactListener interface for the World.
        world.setContactListener(worldContactListener());
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void render(float dt)
    {
        
        /*
        The function overrides the render method of the BaseScreen function.
        The override occurs due to the the need to render the tilemap and handle different layers.
        
        The function occurs during the render phase and accomplishes the following:
        
        1.  Adjusts Actor positions and other properties in the UI stage.
        2.  If game not paused, adjusts Actor positions and other properties in the main stage and 
            processes player input.
        3.  Draws the graphics (using proper layer order).
            Scroll background more slowly to create parallax effect.
        */
        
        Camera mainCamera;
        
        // Call the Actor.act(float) method on each actor in the UI stage.
        // Typically called each frame.  The method also fires enter and exit events.
        // Updates the position of each Actor based on time.
        uiStage.act(dt);
        
        // Only pause gameplay events, not UI events.

        // If game not paused, then...
        if ( !isPaused() )
        
            {
            // Game active (not paused).

            // Call the Actor.act(float) method on each actor in the non-UI stage.
            // Typically called each frame.  The method also fires enter and exit events.
            // Updates the position of each Actor based on time.
            mainStage.act(dt);
            
            // Handle game logic -- allow processing based on player actions / input.
            update(dt);
            }
        
        // Draw graphics.
        
        // Overdraw the area with the given glClearColor.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        
        // Clear the area using the specified buffer.  Supports multiple buffers.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Draw the layers in the necessary order -- background (0), scenery / foreground (1), main stage, 
        // and ui stage.
        
        // Get Camera object associated with the main stage.
        mainCamera = mainStage.getCamera();
        
        // Center main camera horizontally on player .
        mainCamera.position.x = player.getX() + player.getOriginX();
        
        // Bound main camera to layout -- determining center of window.
        mainCamera.position.x = MathUtils.clamp(
        mainCamera.position.x, getViewWidth() / 2, mapWidth - getViewWidth() / 2);
        mainCamera.update();

        // Scroll background more slowly to create parallax effect.
        tiledCamera.position.x = mainCamera.position.x / 4 + mapWidth / 4;
        tiledCamera.position.y = mainCamera.position.y;
        
        // Update tilemap camera.
        tiledCamera.update();
        tiledMapRenderer.setView(tiledCamera); // Sets the projection matrix for rendering, 
          // as well as the bounds of the map which should be rendered.
        tiledMapRenderer.render(backgroundLayer); // Render the background layer.
        
        // Adjust tilemap camera to stay in sync with main.
        tiledCamera.position.x = mainCamera.position.x;
        tiledCamera.position.y = mainCamera.position.y;
        
        // Update tilemap camera.
        tiledCamera.update();
        
        tiledMapRenderer.setView(tiledCamera); // Sets the projection matrix for rendering, 
          // as well as the bounds of the map which should be rendered.
        tiledMapRenderer.render(tileLayer); // Render the tile layer.
        
        mainStage.draw(); // Draw the main stage.
        uiStage.draw(); // Draw the UI stage.
        
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
    
    private ContactListener worldContactListener()
    {
        
        /*
        The function returns an implementation of the ContactListener interface for the World.
        
        • beginContact:  Called when a pair of fixtures first come into contact with each other.
        • endContact:  Called when a pair of fixtures cease being in contact with each other.
        • preSolve:  Provides a chance to alter the contact before processing.
        • postSolve:  Provides the means to find out what impulses were caused by the collision
        response after its application.
        
        The types of contact events important to the JumpingJack game include:

        • When a Coin object and the "main" fixture of a Player object first make contact, the
        coin should be added to the removeList.  In addition, spawns a sparkle effect.  Handled
        in the beginContact method.
        
        • If any solid (that is, non-Coin) object and the "bottom" fixture of a Player first
        make contact, add 1 to the player's ground-counting variable, and set the player's
        animation to stand.  Handled in the beginContact method.
        
        • If any solid (that is, non-Coin) object and the "bottom" fixture of a Player leave
        contact, subtract 1 from the player's ground-counting variable.  Handled in the
        endContact method.
        */
        
        // Return the ContactListener...
        return new ContactListener()
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
                ParticleActor sparkle; // ParticleActor to use as sparkle object.
                
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
                        sparkle = baseSparkle.clone(); // Copy the sparkle object.
                        sparkle.setPosition( // Set the position of the sparkle object to center of coin.
                          c.getX() + c.getOriginX(), c.getY() + c.getOriginY() );
                        sparkle.start(); // Start the particle effect.
                        mainStage.addActor(sparkle); // Add actor to scene graph.
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
            
        };
                
    }
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.
        
        // 1.  Pauses the game when pressing the P key.
        // 2.  Resets the game when pressing the R key.
        // 3.  Causes the player to jump when pressing the space key -- if on the ground.
        
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
        
        // Return a value.
        return false;
    
    }
    
}
