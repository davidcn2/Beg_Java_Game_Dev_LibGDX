package treasurequest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import core.*;
import java.util.ArrayList;
import routines.*;

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
    about the game -- background, player, key, coins, door, and wall and related properties
    (map height and width, timers, speeds, ...).
    
    Methods include:
    
    create:  Sets defaults / lists.  Configures and adds the Actors to the stage.  Sets up TiledMap.
    render:  Called when the screen should render itself.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
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
    
    // Declare object variables.
    private BaseActor baseCoin; // BaseActor object that will act as the base coin (cloned).
    private ArrayList<BaseActor> coinList; // BaseActor array that will act as the coins.
    private BaseActor door; // BaseActor object that will act as the door.
    private BaseActor key; // BaseActor object that will act as the key.
    private PhysicsActor player; // PhysicsActor object that will act as the player.
    private ArrayList<BaseActor> removeList; // List of actors to remove.
    private OrthographicCamera tiledCamera; // Camera to use with Tiled map.
    private TiledMap tiledMap; // Map, in Tiled format.
    private OrthogonalTiledMapRenderer tiledMapRenderer; // Renderer to use with Tiled map.
    private ArrayList<BaseActor> wallList; // BaseActor array that will act as the walls.
    
    // Declare regular variables.
    private final int[] backgroundLayers = new int[2]; // Indices of the tilemap layers to be rendered before the main stage.
    private final int[] foregroundLayers = new int[1]; // Indices of the tilemap layers to be rendered after the main stage.
    private boolean hasKey; // Whether player owns key.
    private final int tileCountHeight; // Number of tiles in map -- vertically.
    private final int tileCountWidth; // Number of tiles in map -- horizontally.
    private final int tileSize; // Size of tiles in map, in pixels.
    
    // Game world dimensions.
    final int mapWidth; // Total map width, in pixels.
    final int mapHeight; // Total map height, in pixels.
    
    // g = Screen object for game window (background, player, key, coins, door, wall, ...).
    public GameScreen(BaseGame g)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Specifies tile counts and size.
        // 3.  Sets game world dimensions.
        // 4.  Populates arrays.
        // 5.  Calls the create() function to perform remaining startup logic.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(g, 800, 600);
        
        // Specify tile counts and size.
        tileCountHeight = 20;
        tileCountWidth = 20;
        tileSize = 64;
        
        // Set game world dimensions.
        this.mapWidth = tileSize * tileCountWidth;
        this.mapHeight = tileSize * tileCountHeight;
        
        // Populate arrays.
        ArrayRoutines.addAll(backgroundLayers, 0, 1);
        ArrayRoutines.addAll(foregroundLayers, 2);
        
        // Configure and add the actors to the stage:  background, player, key, coins, door, and wall.
        create();
        
    }
    
    public final void create()
    {
        
        /* 
        The function occurs during the startup / create phase and accomplishes the following:
        
        1.  Sets defaults and initializes empty array lists.
        2.  Sets up the player object.
        3.  Sets up the key object.
        4.  Sets up the door object.
        5.  Sets up tile map, renderer, and camera.
        6.  Sets up objects across all layers in TiledMap -- player, coins, door, key.
        7.  Sets up wall layer in TiledMap.
        */
        
        // Constants.
        final float t = 0.15f;
        
        // Regular variables.
        String name; // Current object name (in loop).
        
        // Object variables.
        BaseActor coin; // Used to clone coin actors.
        MapObjects objects; // Collection of objects contained in all the layers.
        Texture playerTex; // Texture (image) to use for player.
        Rectangle r; // Rectangle object used when iterating through items in layers.
        RectangleMapObject rectangleObject; // Rectangle shaped map object used when iterating through items in layers.
        BaseActor solid; // Properties of a wall segment.
        
        // Initialize empty array lists.
        coinList = new ArrayList<>(); // Initialize array to hold coins.
        wallList = new ArrayList<>(); // Initialize array to hold walls.
        removeList = new ArrayList<>(); // Initialize array to hold actors to remove.
        
        // Alternative code:
        // coinList = new ArrayList<BaseActor>();
        // wallList = new ArrayList<BaseActor>();
        // removeList = new ArrayList<BaseActor>();
        
        // Initialize the player, key, door, and a base coin instance -- other than the positions.
        // Also, initialize the lists.
        // The positions get set later, after retrieving the related information from the tilemap.
        
        // Create the player object.
        player = new PhysicsActor();
        
        // Set the properties for the player object.
        playerTex = new Texture( Gdx.files.internal("assets/general-single.png") ); // Load image to buffer.
        // player.storeAnimation("default", playerTex); // Store animation in actor.
        
        // Store animations for actor.
        player.storeAnimation( "down", GameUtils.parseSpriteSheet( "assets/general-48.png", 3, 4,
          new int[] {0, 1, 2}, t, PlayMode.LOOP_PINGPONG ) );
        player.storeAnimation( "left", GameUtils.parseSpriteSheet( "assets/general-48.png", 3, 4,
          new int[] {3, 4, 5}, t, PlayMode.LOOP_PINGPONG ) );
        player.storeAnimation( "right", GameUtils.parseSpriteSheet( "assets/general-48.png", 3, 4,
          new int[] {6, 7, 8}, t, PlayMode.LOOP_PINGPONG ));
        player.storeAnimation( "up", GameUtils.parseSpriteSheet( "assets/general-48.png", 3, 4,
          new int[] {9, 10, 11}, t, PlayMode.LOOP_PINGPONG ));
        
        player.setSize( 48, 48 ); // Set width and height of image.
        player.setEllipseBoundary(); // Set properties of ellipse bounding polygon related to the texture.
        mainStage.addActor( player ); // Add (player) actor to scene graph.
        
        // Create the key object.
        key = new BaseActor();
        
        // Set the properties for the key object.
        key.setTexture( new Texture(Gdx.files.internal("assets/key.png")) ); // Load image to buffer.
        key.setSize( 36, 24 ); // Set width and height of actor.
        key.setEllipseBoundary(); // Set properties of ellipse bounding polygon related to the texture.
        mainStage.addActor( key ); // Add (player) actor to scene graph.
        
        // Create the door object.
        door = new BaseActor();
        
        // Set the properties for the door object.
        door.setTexture( new Texture(Gdx.files.internal("assets/door.png")) ); // Load image to buffer.
        door.setRectangleBoundary(); // Set properties of rectangle bounding polygon related to the texture.
        mainStage.addActor( door ); // Add (door) actor to scene graph.
        
        // Set up the base coin object.
        
        // Create the base coin object (allows for cloning / copying).
        baseCoin = new BaseActor();
        
        baseCoin.setTexture( new Texture(Gdx.files.internal("assets/coin.png")) ); // Load image to buffer.
        baseCoin.setEllipseBoundary(); // Set properties of ellipse bounding polygon related to the texture.
        
        // Set up tile map, renderer, and camera.
        tiledMap = new TmxMapLoader().load( "assets/game-map.tmx" ); // Load the TiledMap.
        tiledMapRenderer = new OrthogonalTiledMapRenderer( tiledMap ); // Associate renderer with loaded TiledMap.
        tiledCamera = new OrthographicCamera(); // Instantiate Orthographic Camera.
        tiledCamera.setToOrtho( false, getViewWidth(), getViewHeight() ); // Set the camera to an orthographic projection.
        tiledCamera.update(); // Recalculate the projection and view matrix of the camera and the Frustum planes.
        
        // Set up objects across all layers in TiledMap.
        
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
                    coinList.add( coin ); // Add coin actor to ArrayList.
                    break;
                case "door": // Looking at door object...
                    door.setPosition( r.x, r.y ); // Set position of door actor.
                    break;
                case "key": // Looking at key object...
                    key.setPosition( r.x, r.y ); // Set position of key actor.
                    break;
                default: // All other objects...
                    System.err.println( "Unknown tilemap object: " + name ); // Display error message.
                    break;
                }
            
            } // End ... Loop through objects in all layers.
        
        // Set up wall layer in TiledMap.
        // Gather the geometric data that represents solid walls.
        
        // Store collection of wall objects across all layers in TiledMap.
        objects = tiledMap.getLayers().get("PhysicsData").getObjects();
        
        // Loop through objects.
        for (MapObject object : objects)
        
            {
            // Since evaluating walls, not necessary to know name of current object in loop.
                
            // Cast current object to a RectangleMapObject to allow for getting position.
            rectangleObject = (RectangleMapObject)object;
            
            // Get rectangle associated with current object.
            r = rectangleObject.getRectangle();
            
            // Store properties of a wall segment.
            solid = new BaseActor(); // Create a BaseActor for the current wall segment.
            solid.setPosition( r.x, r.y ); // Set position of current wall segment.
            solid.setSize( r.width, r.height ); // Set size of current wall segment.
            solid.setRectangleBoundary(); // Set properties of rectangle bounding polygon related to the wall.
            wallList.add(solid); // Add current wall segment to ArrayList.
            }
        
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
        */
        
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
        
        // Draw the layers in the necessary order -- background (0), scenery (1), main stage, 
        // foreground / overlay, and ui stage.
        
        // Render the background layers.
        tiledMapRenderer.render(backgroundLayers);
        
        // Draw the main stage.
        mainStage.draw();
        
        // Render the foreground layers.
        tiledMapRenderer.render(foregroundLayers);
        
        // Draw the UI stage.
        uiStage.draw();
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {
        
        /*
        The function occurs during the update phase (render method) and accomplishes the following:
        
        1.  Sets velocity vector of player based on current key press(es).
        2.  Handles wall intersections.
        3.  Handles door intersections.
        4.  Handles coin intersections.
        5.  Clears actors in removal list.
        6.  Adjusts the Camera objects used to render graphics to center on player and stay within view bounds.
        */
        
        // Declare regular variables.
        float playerSpeed; // Speed (velocity) of player.
        
        // Declare object variables.
        Camera mainCamera; // Camera corresponding to the main stage.
        
        // Set speed (velocity) of player (assuming a key press).  Determine direction in next steps.
        playerSpeed = 100;
        
        // Reset player velocity to nothing / zero.
        player.setVelocityXY( 0, 0 );
        
        // Perform continuous polling of key presses to determine player velocity vector.
        
        // If user pressed left arrow, then...
        if (Gdx.input.isKeyPressed( Keys.LEFT ))
        {
            // User pressed left arrow.
            
            // Set player to move left.
            player.setVelocityXY( -playerSpeed, 0 );
            
            // Switch to animation of player moving left.
            player.setActiveAnimation( "left" );
        }
        
        
        // If user pressed right arrow, then...
        if (Gdx.input.isKeyPressed( Keys.RIGHT ))
        {
            // User pressed right arrow.
            
            // Set player to move right.
            player.setVelocityXY( playerSpeed, 0 );
            
            // Switch to animation of player moving right.
            player.setActiveAnimation( "right" );
        }
        
        // If user pressed up arrow, then...
        if (Gdx.input.isKeyPressed( Keys.UP ))
        {
            // User pressed the up arrow.
            
            // Set player to move up.
            player.setVelocityXY( 0, playerSpeed );
            
            // Switch to animation of player moving up.
            player.setActiveAnimation( "up" );
        }
        
        // If user pressed down arrow, then...
        if (Gdx.input.isKeyPressed( Keys.DOWN ))
        {
            // User pressed the down arrow.
            
            // Set player to move down.
            player.setVelocityXY(0,-playerSpeed);
            
            // Switch to animation of player moving down.
            player.setActiveAnimation( "down" );
        }
        
        // If player speed less than one, then...
        if ( player.getSpeed() < 1 )
        {
            // Player speed less than one.
            
            // Pause animation and set to first frame.
            player.pauseAnimation();
            player.setAnimationFrame(1);
        }
        
        // Otherwise -- player at speed one or higher...
        else
            // Player at speed one or higher.
            
            // Start animation.
            player.startAnimation();
        
        /*
        for (BaseActor wall : wallList)
        {
        // If player intersects wall, adjust minimum distance to avoid intersection.
        player.overlaps(wall, true);
        }
        */
        
        // Loop through all walls in list.
        wallList.forEach((wall) -> 
            {
            // If player intersects wall, adjust minimum distance to avoid intersection.
            player.overlaps(wall, true);
            });
        
        // If player overlaps key and still exists in stage, then...
        if ( key.getStage() != null && player.overlaps(key, false) )
            {
            // Player overlaps key and still exists in stage.
            hasKey = true; // Flag player as owning key.
            removeList.add(key); // Add key to list of actors to remove from stage.
            }
        
        // If player overlaps door and still exists in stage, then...
        if ( door.getStage() != null && player.overlaps(door, true) )
            {
            // Player overlaps door and still exists in stage.
                
            // If player has key, then...
            if ( hasKey )
                // Player has key.
                // Add door to list of actors to remove from stage.
                removeList.add( door );
            }
        
        /*
        for (BaseActor coin : coinList)
        {
        // If player overlaps coin, then...
        if ( player.overlaps( coin, false) )
        // Player overlaps coin.
        // Add coin to list of actors to remove from stage.
        removeList.add(coin);
        }
         */
        // Loop through coins.
        // For each coin player overlaps, ...
        coinList.stream().filter((coin) -> ( player.overlaps( coin, false) )).forEachOrdered((coin) -> 
            {
            // Player overlaps coin.
            // Add coin to list of actors to remove from stage.
            removeList.add(coin);
            });
        
        /*
        for (BaseActor ba : removeList)
        {
        ba.destroy();
        }
         */
        // Loop through actors in removal list.
        removeList.forEach((ba) -> 
            {
            // Remove actor from the stage.
            ba.destroy();
            });
        
        /*
        Adjust the Camera objects used to render the graphics.
        Due to the existence of a game world larger than the window size, need to adjust the
        position of the camera to stay centered on the player.
        Also, need to ensure that the field of view of the camera stays bounded within the game world.
        Handle both cameras -- one corresponding to the main stage and the other to the tilemap.
        */
        
        // Adjust camera related to the main stage.
        mainCamera = mainStage.getCamera(); // Get reference to camera corresponding to the main stage.
        
        // Center camera on the player.
        mainCamera.position.x = player.getX() + player.getOriginX();
        mainCamera.position.y = player.getY() + player.getOriginY();
        
        // Bound camera to layout.
        mainCamera.position.x = MathUtils.clamp( mainCamera.position.x, getViewWidth() / 2, 
          mapWidth - getViewWidth() / 2 );
        mainCamera.position.y = MathUtils.clamp( mainCamera.position.y, getViewHeight() / 2, 
          mapHeight - getViewHeight() / 2 );
        
        // Update camera related to the main stage.
        mainCamera.update();
        
        // Adjust tilemap camera to stay in sync with main.
        tiledCamera.position.x = mainCamera.position.x;
        tiledCamera.position.y = mainCamera.position.y;
        
        // Update tilemap camera.
        tiledCamera.update();
        
        tiledMapRenderer.setView( tiledCamera ); // Sets the projection matrix for rendering, 
          // as well as the bounds of the map which should be rendered.
        
    }
    
    // Handle discrete key events.
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.
        
        // 1.  Pauses the game when pressing the P key.
        // 2.  Resets the game when pressing the R key.
        
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
            
        // If the user pressed the space key, then...
        if (keycode == Keys.SPACE)
        {
            // The user pressed the space key.
            
            // Move the player up.
            player.setVelocityXY( 0, 300 );
        }
            
        // Return a value.
        return false;
        
    }
        
}