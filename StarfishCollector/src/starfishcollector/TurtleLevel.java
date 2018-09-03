package starfishcollector;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import core.BaseActor;
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
    // about the game -- background (ocean / overlay), rocks, starfish, turtle and related properties
    // (map height and width).
    
    // Methods include:
    
    // create:  Configures and adds the Actors to the stage:  ocean, overlay, rocks, starfish, and turtle.
    // update:  Occurs during the update phase (render method) and contains code related to game logic.
    
    // Constants...
    private final int mapWidth; // Game world height.
    private final int mapHeight; // Game world width.
    
    private BaseActor ocean; // BaseActor object that will act as the background.
    private ArrayList<BaseActor> rockList; // Dynamic array for rock Actors.
    private ArrayList<BaseActor> starfishList; // Dynamic array for starfish Actors.
    private PhysicsActor turtle; // PhysicsActor object that will act as the turtle.
    
    /*
    private BaseActor ocean;
    private ArrayList<BaseActor> rockList;
    private ArrayList<BaseActor> starfishList;
    private PhysicsActor turtle;
    private int mapWidth = 800;
    private int mapHeight = 600;
    */

    // g = Screen object for game window (background -- ocean, rocks, starfish, and turtle).
    public TurtleLevel(Game g)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Sets game world dimensions.
        // 3.  Configures and adds the actors to the stage:  ocean, overlay, rocks, starfish, and turtle.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(g);
        
        // Set game world dimensions.
        this.mapWidth = 800;
        this.mapHeight = 600;
        
        // Configure and add the actors to the stage:  ocean, overlay, rocks, starfish, and turtle.
        create();
        
    }
    
    public final void create()
    {
        
        // The function occurs during the startup / create phase and accomplishes the following:
        
        // 1.  Configures and adds the Actors to the stage:  ocean, overlay, rocks, starfish, and turtle.
        
        String fileName; // Filename for current animation frame.  Examples:  turtle-1.png, turtle-2.png, ...
        Animation anim; // Animation object associated with turtle.
        Array<TextureRegion> framesArray; // LibGDX array of textures used in animation for turtle.
        BaseActor overlay; // Copy of ocean with translucent color used to create underwater effect.
        BaseActor rock; // BaseActor object for rock.  Acts as template for rocks in array.
        BaseActor r; // BaseActor object for each rock in loop.
        BaseActor starfish; // BaseActor object for starfish.  Acts as template for starfish in array.
        BaseActor s; // BaseActor object for each starfish in loop.
        Texture frame1; // Texture used when loading image for animation for turtle at rest.
        Texture tex; // Texture used temporarily when loading images to animation frames for swimming turtle.
        TextureRegion[] frames; // Stores array of images (in this case for turtle animation).
        
        // Create new BaseActors for the background (ocean) and its overlay.
        ocean = new BaseActor();
        overlay = new BaseActor();
        
        // Name background (ocean) and overlay.
        ocean.setActorName("Ocean");
        overlay.setActorName("Overlay");
        
        // Set the properties for the background and add to the scene graph.
        ocean.setTexture( new Texture(Gdx.files.internal("assets/water.jpg")) );
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
        
        // Create new BaseActor for a rock.
        rock = new BaseActor();
        
        // Name rock.
        rock.setActorName("Template Rock");
        
        // Set image related to rock.
        rock.setTexture( new Texture(Gdx.files.internal("assets/rock.png")) );
        
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
        starfish.setTexture( new Texture(Gdx.files.internal("assets/starfish.png")) );
        
        // Set collision polygon for starfish as ellipse.
        starfish.setEllipseBoundary();
        
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
            fileName = "assets/turtle-" + n + ".png";
            
            // Load image to buffer.
            tex = new Texture(Gdx.files.internal(fileName));
            
            // Set filter type -- controlling how pixel colors are interpolated when image is
            // rotated or stretched.
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
        frame1 = new Texture(Gdx.files.internal("assets/turtle-1.png"));
        
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
        rockList.forEach((r) -> turtle.overlaps(r, true));

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
                removeList.add(s);
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
            });
        
        /*
        // Get Camera object associated with the main stage.
        cam = mainStage.getCamera();

        // Center camera on player (turtle).
        cam.position.set( turtle.getX() + turtle.getOriginX(),
            turtle.getY() + turtle.getOriginY(), 0 );
        
        // Bound camera to layout -- determining center of window.
        cam.position.x = MathUtils.clamp(cam.position.x, viewWidth / 2, mapWidth - viewWidth / 2);
        cam.position.y = MathUtils.clamp(cam.position.y, viewHeight / 2, mapHeight - viewHeight / 2);
        */

    }
    
    /*
    public void update(float dt) 
    {   
        // process input
        turtle.setAccelerationXY(0,0);

        if (Gdx.input.isKeyPressed(Keys.LEFT)) 
            turtle.rotateBy(90 * dt);
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            turtle.rotateBy(-90 * dt);
        if (Gdx.input.isKeyPressed(Keys.UP)) 
            turtle.accelerateForward(100);

        // set correct animation
        if ( turtle.getSpeed() > 1 && turtle.getAnimationName().equals("rest") )
            turtle.setActiveAnimation("swim");
        if ( turtle.getSpeed() < 1 && turtle.getAnimationName().equals("swim") )
            turtle.setActiveAnimation("rest");

        // bound turtle to the screen
        turtle.setX( MathUtils.clamp( turtle.getX(), 0,  mapWidth - turtle.getWidth() ));
        turtle.setY( MathUtils.clamp( turtle.getY(), 0,  mapHeight - turtle.getHeight() ));

        for (BaseActor r : rockList)
        {
            turtle.overlaps(r, true);
        }

        ArrayList<BaseActor> removeList = new ArrayList<BaseActor>();
        for (BaseActor s : starfishList)
        {
            if ( turtle.overlaps(s, false) )
                removeList.add(s);
        }

        for (BaseActor b : removeList)
        {
            b.remove();             // remove from stage
            starfishList.remove(b); // remove from list used by update
        }
    }
    */
    
}