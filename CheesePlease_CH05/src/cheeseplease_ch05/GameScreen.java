package cheeseplease_ch05;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import core.BaseActor;
import core.BaseGame;
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

public class GameScreen extends BaseScreen
{
        
    // Constants...
    private final int mapWidth; // Game world height.
    private final int mapHeight; // Game world width.

    private boolean win; // Whether player has won the game.
    private float timeElapsed; // Total elapsed time.

    private PhysicsActor mousey; // PhysicsActor object that will act as the mouse.
    private BaseActor cheese; // BaseActor object that will act as the cheese.
    private BaseActor floor; // BaseActor object that will act as the background tiles.
    private Image winImage; // Image object with text that displays when winning.
    private Label timeLabel; // LibGDX Label object that will display elapsed time text.
    // Note:  A Label is an extension of an Actor.

    // g = Screen object for game window (mouse, cheese, floor, win text, and elapsed time text).
    public GameScreen(BaseGame g)
    {

        // The constructor of the class:

        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Sets game world dimensions.
        // 3.  Launches the startup / create phase (function).

        // Call the constructor for the BaseScreen (parent / super) class.
        // Sets window width and height to 640 by 480.
        super(g, 640, 480);

        // Set game world dimensions.
        this.mapWidth = 1000;
        this.mapHeight = 1000;

        // Launch the startup / create phase (function).
        create();

    }

    public final void create()
    {
        
        String filename; // Filename for current animation frame.  Examples:  mouse0.png, mouse1.png, ...
        String text; // Text to display related to elapsed time.
        
        Animation anim; // Animation object associated with mouse.
        Array<TextureRegion> framesArray; // LibGDX array of textures used in animation for mouse.
        Texture mouseTex; // Texture used when loading image for animation for mouse at rest.
        Texture tex; // Texture used temporarily when loading images to animation frames for mouse.
        Texture winTex; // Texture used to load image intended for display when winning game.
        TextureRegion[] frames; // Stores array of images (in this case for mouse animation).
        
        // Set default values.
        win = false;
        timeElapsed = 0;
        text = "Time: --";

        // Create a new BaseActor for the floor (background tiles).
        floor = new BaseActor();
        
        // Set the properties for the floor and add to the scene graph.
        floor.setTexture( new Texture(Gdx.files.internal("assets/images/tiles-1000-1000.jpg")) ); // Load image to buffer.
        floor.setPosition( 0, 0 ); // Place image at (0, 0) within window.
        mainStage.addActor( floor ); // Add floor Actor to the scene graph.

        // Create a new BaseActor for the cheese.
        cheese = new BaseActor();
        
        // Set the properties for the cheese and add to the scene graph.
        cheese.setTexture( new Texture(Gdx.files.internal("assets/images/cheese.png")) ); // Load image to buffer.
        cheese.setPosition( 400, 300 ); // Place image at (400, 300) within window.
        cheese.setOrigin( cheese.getWidth()/2, cheese.getHeight()/2 ); // Set cheese origin coordinates to center of image, to handle roations.
        cheese.setEllipseBoundary(); // Set collision polygon for cheese as ellipse.
        mainStage.addActor( cheese ); // Add cheese Actor to the scene graph.

        // Create new PhysicsActor for the mouse.
        mousey = new PhysicsActor();

        // Set the properties for the mouse.
        mousey.setPosition( 20, 20 ); // Place image at (20, 20) within window.
        mousey.setMaxSpeed(100); // Set maximum speed.
        mousey.setDeceleration(200); // Set deceleration.

        // Set up array of images (in this case for an animation) for mouse.  Similar to buffer in Direct-X.
        frames = new TextureRegion[4];
        
        // Loop through and add animation frames (TextureRegion objects) to array for mouse.
        for (int n = 0; n < 4; n++)
        {
            // Set filename for current animation frame.  Examples:  mouse0.png, mouse1.png, ...
            filename = "assets/images/mouse" + n + ".png";
            
            // Load image to buffer.
            tex = new Texture(Gdx.files.internal(filename));
            
            // Set file type -- controlling how pixel colors are interpolated when image is
            // rotated or stretched.
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            
            // Add texture for current frame for cheese to standard array.
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

        // Add Animation object to hash map for walking mouse.
        mousey.storeAnimation( "walk", anim );

        // Set filename for animation frame for mouse at rest.
        mouseTex = new Texture(Gdx.files.internal("assets/images/mouse0.png"));
        
        // To ensure that image scales smoothly, specify linear filtering.
        mouseTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        // Add Animation object to hash map for resting mouse.
        mousey.storeAnimation( "stop", mouseTex );

        // Set mouse origin coordinates to center of image, to handle roations.
        mousey.setOrigin( mousey.getWidth()/2, mousey.getHeight()/2 );
        
        // Set mouse to rotate image to match velocity (speed).
        mousey.setAutoAngle(true);
        
        // Set collision polygon for turtle as ellipse.
        mousey.setEllipseBoundary();
        
        // Add mouse Actor to the scene graph.
        mainStage.addActor(mousey);

        ////////////////////
        // USER INTERFACE //
        ////////////////////

        // Load win message image to buffer.
        winTex = new Texture(Gdx.files.internal("assets/images/you-win.png"), true);
        
        // To ensure that image scales smoothly, specify linear filtering.
        winTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        // Create new Image object for win message with associated Texture.
        winImage = new Image( winTex );
        
        // Set win message Image to not visible.
        winImage.setVisible(false);

        // Add Label showing time elapsed.  Apply Skin property of uiLabelStyle.
        timeLabel = new Label( text, game.skin, "uiLabelStyle" );

        // Set up Table object with user-interface layout.
        uiTable.pad(10); // Set padding, in pixels, for Table (on all sides).
        uiTable.add().expandX(); // Add first cell in row, extended all the way to the right to force the next
        // one to align with the rightmost edge.
        uiTable.add(timeLabel); // Add second cell in row, containing time elapsed Label.
        uiTable.row(); // Add a row to the Table.
        uiTable.add(winImage).colspan(2).padTop(50); // Add a cell that encompasses both columns, with 50 pixels
        // of padding at the top.
        uiTable.row(); // Add a row to the Table.
        uiTable.add().colspan(2).expandY(); // Add a cell that encompasses both columns, extending the remaining
        // vertical length of the screen.

    }

    @Override
    public void update(float dt) 
    {   
        mousey.setAccelerationXY(0,0);
        if (Gdx.input.isKeyPressed(Keys.LEFT)) 
            mousey.addAccelerationXY(-100,0);
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            mousey.addAccelerationXY(100,0);
        if (Gdx.input.isKeyPressed(Keys.UP)) 
            mousey.addAccelerationXY(0,100);
        if (Gdx.input.isKeyPressed(Keys.DOWN)) 
            mousey.addAccelerationXY(0,-100);

        // set correct animation
        if ( mousey.getSpeed() > 1 && mousey.getAnimationName().equals("stop") )
            mousey.setActiveAnimation("walk");
        if ( mousey.getSpeed() < 1 && mousey.getAnimationName().equals("walk") )
            mousey.setActiveAnimation("stop");

        // bound mousey to the rectangle defined by mapWidth, mapHeight
        mousey.setX( MathUtils.clamp( mousey.getX(), 0,  mapWidth - mousey.getWidth() ));
        mousey.setY( MathUtils.clamp( mousey.getY(), 0,  mapHeight - mousey.getHeight() ));

        // check win condition: mousey must be overlapping cheese
        if ( !win && cheese.overlaps( mousey, true ) )
        {
            win = true;

            Action spinShrinkFadeOut = Actions.parallel(
                    Actions.alpha(1),         // set transparency value
                    Actions.rotateBy(360, 1), // rotation amount, duration
                    Actions.scaleTo(0,0, 2),  // x amount, y amount, duration
                    Actions.fadeOut(1)        // duration of fade in
                );
            cheese.addAction( spinShrinkFadeOut );

            Action fadeInColorCycleForever = Actions.sequence( 
                    Actions.alpha(0),   // set transparency value
                    Actions.show(),     // set visible to true
                    Actions.fadeIn(2),  // duration of fade out
                    Actions.forever(    
                        Actions.sequence(
                            // color shade to approach, duration
                            Actions.color( new Color(1,0,0,1), 1 ),
                            Actions.color( new Color(0,0,1,1), 1 )
                        )
                    )
                );
            winImage.addAction( fadeInColorCycleForever );
        }

        if (!win)
        {
            timeElapsed += dt;
            timeLabel.setText( "Time: " + (int)timeElapsed );
        }

        // camera adjustment
        Camera cam = mainStage.getCamera();

        // center camera on player
        cam.position.set( mousey.getX() + mousey.getOriginX(), mousey.getY() + mousey.getOriginY(), 0 );

        // bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x, getViewWidth()/2,  mapWidth - getViewWidth()/2);
        cam.position.y = MathUtils.clamp(cam.position.y, getViewHeight()/2, mapHeight - getViewHeight()/2);
        cam.update();
    }

    // InputProcessor methods for handling discrete input
    @Override
    public boolean keyDown(int keycode)
    {
        if (keycode == Keys.P)    
            togglePaused(); 

        return false;
    }
    
}