package piratecruiser;

// Java imports...
import java.util.ArrayList;

// General LibGDX imports...
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;

// Core and custom code imports...
import core.BaseActor3D;
import core.BaseGame;
import core.BaseScreen;
import core.ModelUtils;

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
    about the game -- player / ship, rocks, floor / water, and sky..
    
    Methods include:
    
    create:  Configures and adds the Actors to the stage:  player / ship, rocks, floor / water, and sky.
      Sets position of the camera.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    */
    
    // Declare object variables.
    BaseActor3D player; // BaseActor3D object that will act as the player.
    ArrayList<BaseActor3D> rockList; // Dynamic array for rock Actors.

    // g = Screen object for game window (player / ship, rocks, floor / water, and sky, ...).
    public GameScreen(BaseGame g)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Calls the create() function to perform remaining startup logic.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(g, 800, 600);
        
        // Configure and add the actors to the stage:  player / ship, rocks, floor / water, and sky.
        create();
        
    }

    public final void create()
    {
    
        // The function occurs during the startup / create phase and accomplishes the following:
        
        // 1.  Configures and adds the Actors to the stage:  player / ship, rocks, floor / water, and sky.
        // 2.  Sets position of the camera.
        
        // Declare object variables.
        BaseActor3D baseRock; // BaseActor3D object for rock.  Acts as template for rocks in array.
        BaseActor3D floor; // BaseActor3D object for the floor (water).
        ModelInstance floorInstance; // Instance of the floor (water) model, in addition to a
          // transformation matrix with position, rotation, and scaling data.
        Texture floorTex; // Texture used for floor (water).
        ModelLoader loader; // Instance of class used to load models.
        ModelInstance rockInstance; // Instance of the rock model, in addition to a
          // transformation matrix with position, rotation, and scaling data.
        BaseActor3D rock1; // BaseActor3D object for the first rock.
        BaseActor3D rock2; // BaseActor3D object for the second rock.
        BaseActor3D rock3; // BaseActor3D object for the third rock.
        Model rockModel; // Model used as a template for each rock.
        ModelInstance shipInstance; // Instance of the ship model, in addition to a
          // transformation matrix with position, rotation, and scaling data.
        Model shipModel; // Model used as a template for the ship.
        BaseActor3D skydome; // BaseActor3D object for the sky dome.
        ModelInstance skyInstance; // Instance of the sky dome model, in addition to a
          // transformation matrix with position, rotation, and scaling data.
        Texture skyTex; // Texture used for sky dome.
        
        // 1.  Initialize the model loader.
        loader = new ObjLoader();
        
        // 2.  Configure and add the floor (water).
        
        // Create new BaseActor3D for the floor (water).
        floor = new BaseActor3D();
        
        // Load image to buffer.
        floorTex = new Texture( Gdx.files.internal("assets/water.jpg"), true );
        
        // Set filter type -- controlling how pixel colors are interpolated when image is
        // rotated or stretched.
        floorTex.setFilter( TextureFilter.Linear, TextureFilter.Linear );
        
        // Create a model instance in the form of a 500 x 0.1 x 500 cube / box with gray color and
        // the water texture.
        floorInstance = ModelUtils.createBox( 500, 0.1f, 500, floorTex, Color.GRAY );
        
        // Set model instance for the floor (water).
        floor.setModelInstance(floorInstance);
        
        // Add floor (water) 3D Actor to the scene graph.
        mainStage3D.addActor(floor);

        // 3.  Configure and add the player (associated with the ship).
        
        // Create new BaseActor3D for the floor (water).
        player = new BaseActor3D();
        
        // Set position of the player.
        player.setPosition( 0, 0, 0 );

        // Load the model for the ship.
        shipModel = loader.loadModel( Gdx.files.internal("assets/ship.obj") );
        
        /*
        Rotate / turn the model by 180 degrees, so that its forward direction aligns with the negative
        z axis.  Transform the meshes of the ship model by applying a rotation matrix.
        */
        
        // Loop through the meshes.
        for (Mesh m : shipModel.meshes)
            // Set rotation for the current mesh in the loop.
            m.transform( new Matrix4().setToRotation(0, 1, 0, 180) );
        
        // Set model instance for the ship.
        shipInstance = new ModelInstance(shipModel);
        
        // Set model instance for the player using the ship template.
        player.setModelInstance(shipInstance);
        
        // Set bounding polygon for player to that of an ellipse based on smallest box that contains 
        // the entire model.
        player.setEllipseBase();
        
        // Add player 3D Actor to the scene graph.
        mainStage3D.addActor(player);

        // 4.  Configure and add the sky dome.
        
        // Create new BaseActor3D for the sky dome.
        skydome = new BaseActor3D();
        
        // Load image to buffer.
        skyTex = new Texture( Gdx.files.internal("assets/sky-sphere.png"), true );
        
        // Create a model instance in the form of a 500 pixel radius inverted sphere with white color and
        // the sky dome texture.
        skyInstance = ModelUtils.createSphereInv( 500, skyTex, Color.WHITE );
        
        // Set model instance for the sky dome.
        skydome.setModelInstance(skyInstance);
        
        // Set position of the sky dome.
        skydome.setPosition( 0, 0, 0 );
        
        // Add sky dome 3D Actor to the scene graph.
        mainStage3D.addActor(skydome);

        // 5.  Configure and add the rocks.
        
        // Initialize dynamic array for rock Actors.
        // rockList = new ArrayList<BaseActor3D>();
        rockList = new ArrayList<>();
        
        // Load the model for the base rock.
        rockModel = loader.loadModel( Gdx.files.internal("assets/rock.obj") );
        
        // Set model instance for the base rock.
        rockInstance = new ModelInstance(rockModel);
        
        // Create new BaseActor3D for the base rock.
        baseRock = new BaseActor3D();
        
        // Set model instance for the base rock.
        baseRock.setModelInstance(rockInstance);
        
        // Set bounding polygon for the base rock to that of an ellipse.
        baseRock.setEllipseBase();

        // Copy properties of initial (template) to first rock.
        rock1 = baseRock.clone();
        
        // Set position of first rock.
        rock1.setPosition( 2, 0, 2);
        
        // Add first 3D rock Actor to the scene graph.
        mainStage3D.addActor(rock1);
        
        // Add first rock 3D rock Actor to dynamic array.
        rockList.add(rock1);

        // Copy properties of initial (template) to second rock.
        rock2 = baseRock.clone();
        
        // Set position of second rock.
        rock2.setPosition( -4, 0, 4 );
        
        // Add second 3D rock Actor to the scene graph.
        mainStage3D.addActor(rock2);
        
        // Add second rock 3D rock Actor to dynamic array.
        rockList.add(rock2);

        // Copy properties of initial (template) to third rock.
        rock3 = baseRock.clone();
        
        // Set position of third rock.
        rock3.setPosition( 6, 0, 6 );
        
        // Add third 3D rock Actor to the scene graph.
        mainStage3D.addActor(rock3);
        
        // Add third rock 3D rock Actor to dynamic array.
        rockList.add(rock3);

        // 6.  Set camera position.
        mainStage3D.setCameraPosition( 2, 3, 15 );
        
    }

    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {
        
        /*
        The function occurs during the update phase and contains code related to game logic for moving the
        player / ship and camera:
        
        The three tasks accomplished in the update phase include:
        
        1.  Check for collisions between the player and the rocks.
        2.  Process continuous user input (allow the ship to move forward and backward and turn 
            left and right).
        3.  Set the camera direction to always face the player.
        
        (Without Shift key press)           
        1.  Up = Move player forward.
        2.  Down = Move player backward.
        3.  Left = Turn player left.
        4.  Right = Turn player right.
        
        (General key press)
        1.  Escape = Exit application.
        */
        
        // Declare constants.
        final float speed = 3.0f; // Movement speed.
        final float rotateSpeed = 45.0f; // Rotation speed.

        // 1.  Check for collisions between the player and the rocks.
        
        // Loop through rocks.
        // Check whether player / ship intersects each rock in loop.
        // If so, move player / ship minimum distance to not collide with rock.
        /*
        for ( BaseActor3D rock : rockList )
        player.overlaps(rock, true);
         */
        rockList.forEach((rock) -> {
            player.overlaps( rock, true );
        });

        // 2.  Process continuous user input (allow the ship to move forward and and backward and 
        // turn left and right).
            
        // If user pressed the up arrow key, then...
        if ( Gdx.input.isKeyPressed(Keys.UP) )
            // User pressed the up arrow key.
            // Move the player forward.
            player.moveForward( speed * dt );

        // If user pressed the down arrow key, then...
        if ( Gdx.input.isKeyPressed(Keys.DOWN) )
            // User pressed the down arrow key.
            // Move the player forward.
            player.moveForward( -speed * dt );

        // If user pressed the left arrow key, then...
        if ( Gdx.input.isKeyPressed(Keys.LEFT) )
            // User pressed the left arrow key.
            // Turn the player to the left.
            player.turn( -rotateSpeed * dt );  

        // If user pressed the right arrow key, then...
        if ( Gdx.input.isKeyPressed(Keys.RIGHT) )
            // User pressed the right arrow key.
            // Turn the player to the right.
            player.turn( rotateSpeed * dt );

        // If the user pressed the Escape key, then...
        if ( Gdx.input.isKeyPressed(Keys.ESCAPE) )
            // The user pressed the Escape key.
            // Exit the game.
            Gdx.app.exit();
        
        // 3.  Set the camera direction to face the player.
        mainStage3D.setCameraDirection( player.getPosition() );
        
    }
    
}
