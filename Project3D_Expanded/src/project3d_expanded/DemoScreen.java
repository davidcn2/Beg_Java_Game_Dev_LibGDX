package project3d_expanded;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import core.BaseActor3D;
import core.BaseGame;
import core.BaseScreen;
import core.ModelUtils;
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

public class DemoScreen extends BaseScreen // Extends the BaseScreen class.
{
    
    /*
    The class extends the basic functionality of a BaseScreen class and stores information
    about the game -- screen, crates, and player -- and related properties.
    
    Methods include:
    
    create:  Configures and adds the Actors to the stage:  screen, crates, and player.  Sets position 
      and direction of the camera.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    */
    
    BaseActor3D player; // BaseActor3D object that will act as the player.
        
    // g = Screen object for game window (screen, crates, player, ...).
    public DemoScreen(BaseGame g)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Calls the create() function to perform remaining startup logic.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(g, 800, 600);
        
        // Configure and add the actors to the stage:  screen, crates, and player.
        create();
        
    }

    public final void create()
    {
        
        // The function occurs during the startup / create phase and accomplishes the following:
        
        // 1.  Configures and adds the Actors to the stage:  screen, crates, and player.
        // 2.  Sets position and direction of the camera.
        
        // Declare object variables.
        BaseActor3D markerO; // BaseActor3D object for purple crate.
        BaseActor3D markerX; // BaseActor3D object for red crate.
        BaseActor3D markerY; // BaseActor3D object for green crate.
        BaseActor3D markerZ; // BaseActor3D object for blue crate.
        ModelInstance modCrateO; // Instance of the purple crate object, in addition to a
          // transformation matrix with position, rotation, and scaling data.
        BaseActor3D screen; // BaseActor3D object for the screen
        ModelInstance screenInstance; // Instance of the screen object, in addition to a
          // transformation matrix with position, rotation, and scaling data.
        Texture screenTex; // Texture to use for the screen model.
        Texture texCrate; // Texture to use for the crate models.
        ModelInstance testModel; // Instance of the player object, in addition to a
          // transformation matrix with position, rotation, and scaling data.
        Texture[] texSides = new Texture[6]; // Texture array to use with the player model.
        
        // 1.  Configure and add the screen.
        System.out.println("1.  Creating screen.");
        
        // Create new BaseActor3D for the screen.
        screen = new BaseActor3D();
        
        // Load image to buffer.
        screenTex = new Texture(Gdx.files.internal("assets/starfish-collector.png"), true);
        
        // Set filter type -- controlling how pixel colors are interpolated when image is
        // rotated or stretched.
        screenTex.setFilter( TextureFilter.Linear, TextureFilter.Linear );
        
        // Create a model instance in the form of a 16 x 2 x 0.1 cube / box with no color.
        screenInstance = ModelUtils.createBox(16, 12, 0.1f, screenTex, null);
        
        // Set model instance for the screen.
        screen.setModelInstance(screenInstance);
        
        // Add screen 3D Actor to the scene graph.
        mainStage3D.addActor(screen);

        // 2.  Configure and add the crates.
        System.out.println("2.  Creating crates.");
        
        // Load image to buffer.
        texCrate = new Texture(Gdx.files.internal("assets/crate.jpg"), true);

        // Create new BaseActor3D for the first crate.
        markerO = new BaseActor3D();
        
        // Create a model instance in the form of a 1x1x1 purple cube / box.
        modCrateO = ModelUtils.createBox( 1, 1, 1, texCrate, Color.PURPLE );
        
        // Set model instance for the purple crate.
        markerO.setModelInstance(modCrateO);
        
        // Set position of the purple crate.
        markerO.setPosition(0,0,0);
        
        // Add purple crate 3D Actor to the scene graph.
        mainStage3D.addActor(markerO);
        
        // Start with a copy of the purple crate.
        markerX = markerO.clone();
        
        // Set the color of the (first) copy to red.
        markerX.setColor(Color.RED);
        
        // Set the position of the red crate.
        markerX.setPosition(5, 0, 0);
        
        // Add red crate 3D Actor to the scene graph.
        mainStage3D.addActor(markerX);
        
        // Make another copy of the purple crate.
        markerY = markerO.clone();
        
        // Set the color of the (second) copy to green.
        markerY.setColor(Color.GREEN);
        
        // Set the position of the green crate.
        markerY.setPosition(0, 5, 0);
        
        // Add green crate 3D Actor to the scene graph.
        mainStage3D.addActor(markerY);
        
        // Make another copy of the purple crate.
        markerZ = markerO.clone();
        
        // Set the color of the (third) copy to blue.
        markerZ.setColor(Color.BLUE);
        
        // Set the position of the blue crate.
        markerZ.setPosition(0, 0, 5);
        
        // Add blue crate to the scene graph.
        mainStage3D.addActor(markerZ);
       
        // 3.  Configure and add the player.
        System.out.println("3.  Creating player.");
        
        // Create new BaseActor3D for the player.
        player = new BaseActor3D();
        
        // Build the array with the textures for each side of the player cube.
        
        /*
        texSides = { 
                new Texture(Gdx.files.internal("assets/xneg.png")),
                new Texture(Gdx.files.internal("assets/xpos.png")),
                new Texture(Gdx.files.internal("assets/yneg.png")),
                new Texture(Gdx.files.internal("assets/ypos.png")),
                new Texture(Gdx.files.internal("assets/zneg.png")),
                new Texture(Gdx.files.internal("assets/zpos.png"))  };
        */
        ArrayRoutines.addAll(texSides, new Texture(Gdx.files.internal("assets/xneg.png")), 
          new Texture(Gdx.files.internal("assets/xpos.png")), 
          new Texture(Gdx.files.internal("assets/yneg.png")),
          new Texture(Gdx.files.internal("assets/ypos.png")),
          new Texture(Gdx.files.internal("assets/zneg.png")),
          new Texture(Gdx.files.internal("assets/zpos.png")));

        // Create a model instance in the form of a 0.5 x 0.5 x 0.5 cube / box using the texture array.
        testModel = ModelUtils.createCubeTexture6(texSides);
        
        // Set model instance for the player.
        player.setModelInstance(testModel);
        
        // Set the position of the player.
        player.setPosition(0, 1, 8);
        
        // Add player to the scene graph.
        mainStage3D.addActor(player);
        
        //  4.  Set camera position and direction.
        System.out.println("4.  Setting camera position and direction.");
        
        mainStage3D.setCameraPosition(3, 4, 10);
        mainStage3D.setCameraDirection(0, 0, 0);
        
    }

    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {   
        
        /*
        The function occurs during the update phase and contains code related to game logic for moving the
        player and camera:
        
        (With Shift key press)              (Without Shift key pressed)
        1.  W = Move player forward.        1.  W = Move camera forward.
        2.  S = Move player backward.       2.  S = Move camera backward.
        3.  A = Move player left.           3.  A = Move camera left.
        4.  D = Move player right.          4.  D = Move camera right.
        5.  Q = Turn player left.           5.  Q = Turn camera left.
        6.  E = Turn player right.          6.  E = Turn camera right.
        7.  R = Move player up.             7.  R = Move camera up.
        8.  F = Move player down.           8.  F = Move camera down.
                                            9.  T = Tilt camera upward.
                                            10. G = Tilt camera downward.
        */
        
        // Declare constants.
        final float speed = 3.0f; // Movement speed.
        final float rotateSpeed = 45.0f; // Rotation speed.
        
        // If NOT pressing either (or both) Shift key(s), then...
        if ( !(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) )
        {
            
            // NOT Pressing either (or both) Shift key(s).
            
            // If pressing the W key, then...
            if ( Gdx.input.isKeyPressed(Keys.W) )
                // Pressing the W key.  Move the player forward.
                player.moveForward( speed * dt );
            
            // If pressing the S key, then...
            if ( Gdx.input.isKeyPressed(Keys.S) )
                // Pressing the S key.  Move the player backward.
                player.moveForward( -speed * dt );
            
            // If pressing the A key, then...
            if ( Gdx.input.isKeyPressed(Keys.A) )
                // Pressing the A key.  Move the player left.
                player.moveRight( -speed * dt );
            
            // If pressing the D key, then...
            if ( Gdx.input.isKeyPressed(Keys.D) )
                // Pressing the D key.  Move the player right.
                player.moveRight( speed * dt );
            
            // If pressing the Q key, then...
            if ( Gdx.input.isKeyPressed(Keys.Q) )
                // Pressing the Q key.  Turn the player left.
                player.turn( -rotateSpeed * dt );
            
            // If pressing the E key, then...
            if ( Gdx.input.isKeyPressed(Keys.E) )
                // Pressing the E key.  Turn the player right.
                player.turn( rotateSpeed * dt );
            
            // If pressing the R key, then...
            if ( Gdx.input.isKeyPressed(Keys.R) )
                // Pressing the R key.  Move the player up.
                player.moveUp( speed * dt );
            
            // If pressing the F key, then...
            if ( Gdx.input.isKeyPressed(Keys.F) )
                // Pressing the F key.  Move the player down.
                player.moveUp( -speed * dt );
            
        } // End ... If NOT pressing either (or both) Shift key(s)

        // If pressing either (or both) Shift key(s), then...
        if ( Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT) )
        {
            
            // Pressing either (or both) Shift key(s).
            
            // If pressing the W key, then...
            if (Gdx.input.isKeyPressed(Keys.W))
                // Pressing the W key.  Move the camera forward.
                mainStage3D.moveCameraForward( speed * dt );
            
            // If pressing the S key, then...
            if (Gdx.input.isKeyPressed(Keys.S))
                // Pressing the S key.  Move the camera backward.
                mainStage3D.moveCameraForward( -speed * dt );
            
            // If pressing the A key, then...
            if (Gdx.input.isKeyPressed(Keys.A))
                // Pressing the A key.  Move the camera left.
                mainStage3D.moveCameraRight( speed * dt );
            
            // If pressing the D key, then...
            if (Gdx.input.isKeyPressed(Keys.D))
                // Pressing the D key.  Move the camera right.
                mainStage3D.moveCameraRight( -speed * dt );

            // If pressing the R key, then...
            if (Gdx.input.isKeyPressed(Keys.R))
                // Pressing the R key.  Move the camera up.
                mainStage3D.moveCameraUp( speed * dt );
            
            // If pressing the F key, then...
            if (Gdx.input.isKeyPressed(Keys.F)) 
                // Pressing the F key.  Move the player down.
                mainStage3D.moveCameraUp( -speed * dt );

            // If pressing the Q key, then...
            if (Gdx.input.isKeyPressed(Keys.Q))
                // Pressing the Q key.  Turn the player left.
                mainStage3D.turnCamera(-rotateSpeed * dt);
            
            // If pressing the E key, then...
            if (Gdx.input.isKeyPressed(Keys.E))
                // Pressing the E key.  Turn the player right.
                mainStage3D.turnCamera(rotateSpeed * dt);
            
            // If pressing the T key, then...
            if (Gdx.input.isKeyPressed(Keys.T))
                // Pressing the T key.  Tilt camera upward.
                mainStage3D.tiltCamera(-rotateSpeed * dt);
            
            // If pressing the G key, then...
            if (Gdx.input.isKeyPressed(Keys.G))
                // Pressing the G key.  Tilt camera downward.
                mainStage3D.tiltCamera(rotateSpeed * dt);
            
        } // End ... If pressing either (or both) Shift key(s)
        
    }
    
    // Handle discrete key events.
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.
        
        // 1.  Resets the game when pressing the Z key.
        
        // If the user pressed the Z key, then...
        if (keycode == Keys.Z)
            // The user pressed the Z key.
            
            // Reset the game.
            game.setScreen( new DemoScreen(game) );
        
        // Return a value.
        return false;
        
    }
    
}