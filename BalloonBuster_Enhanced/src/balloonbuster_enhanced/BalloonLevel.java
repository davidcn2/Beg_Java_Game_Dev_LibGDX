package balloonbuster_enhanced;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import core.BaseActor;
import core.BaseScreen;

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

public class BalloonLevel extends BaseScreen { // Extends the BaseScreen class.

    // The class extends the basic functionality of a BaseScreen class and stores information
    // about the game -- background, balloons, and labels and related properties (map height and width,
    // spawn timer, number of popped and escaped balloons, and click count).
    
    // Methods include:
    
    // create:  Configures and adds the Actors to the stage (except the balloons):  background and labels.
    //          Loads the sounds.
    // keyDown:  The function gets called when the user presses a key.  Pauses the game when pressing the
    //           P key.
    // touchDown:  The function gets called when user clicks the mouse.  Increments number of mouse clicks.
    // update:  Occurs during the update phase (render method) and contains code related to game logic.
    
    // Constants...
    private final String initEscapedMsg; // Initial message to display related to escaped balloons.
    private final String initHitRatioMsg; // Initial message to display related to hit ratio.
    private final String initPoppedMsg; // Initial message to display related to popped balloons.
    private final int mapHeight; // Game world height.
    private final int mapWidth; // Game world width.
    private final float spawnInterval; // Number of seconds between spawning of each new balloon.

    private float spawnTimer; // Number of seconds since last spawning of a new balloon.
    private int popped; // Number of popped (clicked) balloons.
    private int escaped; // Number of balloons that escaped -- left the window.
    private int clickCount; // Number of mouse clicks.
    private BaseActor background; // Creates a BaseActor object that will act as the background.
    private Label poppedLabel; // LibGDX Label object that will display number of popped balloons.
    private Label escapedLabel; // LibGDX Label object that will display number of escaped balloons.
    private Label hitRatioLabel; // LibGDX Label object that will display radio of popped balloons to mouse clicks.
    private Sound poppedSound; // Sound to play when user pops a balloon.
    
    // g = Screen object for game window (background, balloons, and labels).
    public BalloonLevel(Game g) {

        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Sets game world dimensions.
        // 3.  Sets time between appearance of each new balloon.
        // 4.  Sets starting value for number of seconds since appearance of last balloon.
        // 5.  Sets starting number of poppped and escaped balloons and click count.
        // 6.  Sets initial text for labels.
        // 7.  Configures and adds the actors (except the balloons) to stage:  background and labels.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(g);

        // Set game world dimensions.
        this.mapWidth = 640;
        this.mapHeight = 480;

        // Set time between appearance of each new balloon.
        this.spawnInterval = 0.5f;

        // Set starting value for number of seconds since appearance of last balloon.
        this.spawnTimer = 0;

        // Set starting number of popped and escaped balloons and click count.
        this.popped = 0;
        this.escaped = 0;
        this.clickCount = 0;

        // Set initial text for labels.
        this.initPoppedMsg = "Popped: 0";
        this.initEscapedMsg = "Escape: 0";
        this.initHitRatioMsg = "Hit Ratio: ---";

        // Configure and add the actors (except the balloons) to the stage:  background and labels.
        create();

    }

    public final void create() {

        // The function occurs during the startup / create phase and accomplishes the following:
        
        // 1.  Configures and adds the Actors (except the balloons) to the stage:  background and labels.
        // 2.  Loads the sounds.
        
        BitmapFont font; // BitmapFont object storing font used when display text.
        LabelStyle style; // Style (color to tint) to associate with font used in Labels.

        // Create a new BaseActor for the background.
        background = new BaseActor();

        // Set the properties for the background and add to the scene graph.
        background.setTexture(new Texture(Gdx.files.internal("assets/sky.jpg")));
        background.setPosition(0, 0);
        mainStage.addActor(background);

        // Initialize BitmapFont (image based font) object.
        // Defaults to the size 15 Arial font file included in the LibGDX libraries.
        font = new BitmapFont();

        // Set style (tinting color for Font).
        style = new LabelStyle(font, Color.NAVY);

        // Label Note:  Best practices include avoiding scaling -- use a high-resolution image, instead.
        // Set up Label object that will display number of popped balloons.
        poppedLabel = new Label(initPoppedMsg, style); // Add text and style to Label.
        poppedLabel.setFontScale(2); // Make font appear larger by using setFontScale method.
        poppedLabel.setPosition(20, 440); // Set coordinates of the Label.
        uiStage.addActor(poppedLabel); // Add popped balloons Label to the scene graph.

        // Set up Label object that will display number of escaped balloons.
        escapedLabel = new Label(initEscapedMsg, style); // Add text and style to Label.
        escapedLabel.setFontScale(2); // Make font appear larger by using setFontScale method.
        escapedLabel.setPosition(220, 440); // Set coordinates of the Label.
        uiStage.addActor(escapedLabel); // Add escaped balloons Label to the scene graph.

        // Set up Label object that will display balloon hit ratio.
        hitRatioLabel = new Label(initHitRatioMsg, style); // Add text and style to Label.
        hitRatioLabel.setFontScale(2); // Make font appear larger by using setFontScale method.
        hitRatioLabel.setPosition(420, 440); // Set coordinates of the Label.
        uiStage.addActor(hitRatioLabel); // Add escaped balloons Label to the scene graph.
        
        // Load sound to play when user pops a balloon.
        poppedSound = Gdx.audio.newSound(Gdx.files.internal("assets/Pop16.wav"));

    }

    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt) 
    {

        /*
        The function occurs during the update phase (render method) and accomplishes the following:
        
        1.  Updates spawn timer.
        2.  If necessary, display new balloon based on spawn timer and interval.
        3.  Remove off-screen balloons.
        4.  Recalculate and store percent of popped balloons to overall mouse clicks.
        5.  Update labels -- popped and escaped balloons and hit ratio.
        */
        
        int percent; // Ratio of popped balloons to mouse clicks.
        Balloon b; // Reference to a Balloon object.

        // Add number of seconds since last update to timer with seconds since last balloon appearance. 
        spawnTimer += dt;

        // Check time for next balloon spawn.
        
        // If number of seconds since last balloon appearance greater than constant interval, then...
        if (spawnTimer > spawnInterval) 
        {
            
            // Number of seconds since last balloon appearance greater than constant interval.
            
            // Decrease spawn timer by constant interval.
            spawnTimer -= spawnInterval;

            // Create a new Balloon Actor.
            b = new Balloon();
            
            // Add touchDown InputListener to Balloon Actor.
            b.addListener(
                new InputListener() 
                        
                    {
                    
                        // ev = Type of input event.
                        // x = The x coordinate of the mouse click -- origin is in the upper left corner.
                        // y = The y coordinate of the mouse click -- origin is in the upper left corner.
                        // button = The button clicked.
                        @Override
                        public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button)
                        {
                        // Occurs when the user clicks a balloon.

                        Action fadeOutBalloon; // Action object used to fade out balloon image.
                            
                        // Increment number of popped balloons.
                        popped++;

                        // Add effect to balloon to rotate and fade out.
                        fadeOutBalloon = Actions.parallel(
                          Actions.alpha(1),  // Set transparency value.
                          Actions.rotateBy(360, 1), // Set rotation amount and duration.
                          Actions.scaleTo(0, 0, b.fadeDuration), // Set x and y amount and duration -- for scaling.
                          Actions.fadeOut(b.fadeDuration)); // Set duration of fade out.
                        b.addAction( fadeOutBalloon );
                        
                        // Play popped sound.
                        poppedSound.play();
                        
                        // Mark balloon for removal from stage.
                        b.markForRemoval();

                        // Return a value.
                        return true;
                        }
                        
                    }
                    
            );

            // Add new balloon Actor to the scene graph.
            mainStage.addActor(b);

        }

        // Remove balloons that are off-screen.
        
        // Loop through each actor in the stage.
        // Works since the background always is within the window.
        for (Actor a : mainStage.getActors())
            
            {
                // If the horizontal or vertical position of the actor is outside the window, then...
                if (a.getX() > mapWidth || a.getY() > mapHeight) 
                    { 
                    // The horizontal or vertical position of the actor is outside the window.
                        
                    // Increment the number of escaped balloons.
                    escaped++;
                    
                    // Remove Balloon Actor from stage.
                    a.remove();
                    }
            }

        // Update user interface -- labels related to popped and escaped balloons.
        poppedLabel.setText("Popped: " + popped);
        escapedLabel.setText("Escaped: " + escaped);

        // If number of clicks greater than zero, then...
        if (clickCount > 0) 
            {
            // Number of clicks greater than zero.
                
            // Recalculate and store percent of popped balloons to overall mouse clicks.
            percent = (int) (100.0 * popped / clickCount);
            
            // Update hit ratio label.
            hitRatioLabel.setText("Hit Ratio: " + percent + "%");
            }
        
    }

    // screenX = The x coordinate of the mouse click -- origin is in the upper left corner.
    // screenY = The Y coordinate of the mouse click -- origin is in the upper left corner.
    // pointer = The pointer for the event.
    // button = The button clicked.
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) 
    {
        
        // The function occurs when the user clicks the window.  Increments the number of mouse clicks.
        
        // Increment the number of mouse clicks.
        clickCount++;
        
        // Return a value.
        return false;
        
    }
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.
        
        // Pauses the game when pressing the P key.
        // Exits the game when pressing the Escape key.
        
        // If the user pressed the P key, then...
        if (keycode == Input.Keys.P)
        {
            // The user pressed the P key.
            
            // Pause the game.
            togglePaused();
        }
        
        // If the user pressed the Escape key, then...
        if (keycode == Input.Keys.ESCAPE)
        {
            // The user pressed the Escape key.
            
            // Exit the game.
            Gdx.app.exit();
        }
        
        // Return a value.
        return false;
        
    }

}
