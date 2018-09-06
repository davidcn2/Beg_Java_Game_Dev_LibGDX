package spacerocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.assets.AssetManager;
import core.AnimatedActor;
import core.BaseActor;
import core.BaseGame;
import core.BaseScreen;
import core.PhysicsActor;
import core.GameUtils;
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

@SuppressWarnings("unused")
public class GameScreen extends BaseScreen // Extends the BaseScreen class.
{

    /*
    Detailed class description:

    The class contains logic related to setting up and handling interaction related to the game-related stage.

    LibGDX methods for InputProcessor include:

    keyDown:  Called when user presses a key.  P causes the game to pause.  R causes the game to reset.  SPACE results in a laser.

    Custom methods include:

    create:  The method occurs during the startup / create phase and contains initialization logic.
    update:  The method occurs during the update phase (render method) and contains code related to game logic.
    wraparound:  Allows for implementing a world without boundaries -- adjusts position of actor to opposite side of screen when passing edge.
    */

    // Constants...
    private final int mapWidth; // Game world height.
    private final int mapHeight; // Game world width.

    // Specify constants to use for names of files associated with audio.
    private final String laserFireFile = "assets/audio/LaserFire.wav";
    private final String shipThrusterFile = "assets/audio/starfish_collect.wav";
    private final String shipExplosionFile = "assets/audio/SpaceshipExplosion.wav";
    private final String rockExplosionFile = "assets/audio/RockExplosion.wav";
    @SuppressWarnings("FieldCanBeLocal")
    private final String arcadeFunkFile = "assets/audio/ArcadeFunk.mp3";

    private boolean win; // Whether player has won the game.
    private float audioVolume; // Volume to use with Music objects.
    private float timeElapsed; // Total elapsed time.
    private int score; // Player score.
    private int shipCount; // Number of ships remaining.

    @SuppressWarnings("SpellCheckingInspection")
    private BaseActor rocketfire; // BaseActor object that will act as the rocket fire.
    private PhysicsActor spaceship; // BaseActor object that will act as the spaceship.

    // Create "base" objects to clone later.
    private AnimatedActor baseExplosion; // AnimatedActor that will act as the (base) explosion.
    private PhysicsActor baseLaser; // PhysicsActor that will act as the (base) laser beam.
    private PhysicsActor baseRocketfire; // PhysicsActor that will act as the (base) rocket fire.
    private PhysicsActor baseShip; // PhysicsActor that will act as the (base) spaceship.

    private ArrayList<PhysicsActor> laserList; // Array of laser beam PhysicsActor objects.
    private ArrayList<PhysicsActor> rockList; // Array of rock PhysicsActor objects.
    private ArrayList<BaseActor> removeList; // Array of BaseActor objects to remove from screen.

    @SuppressWarnings("FieldCanBeLocal")
    private Music arcadeFunk; // Background music -- loops constantly.
    @SuppressWarnings("FieldCanBeLocal")
    private Sound laserFire; // Laser fire sound to play when shooting.
    @SuppressWarnings("FieldCanBeLocal")
    private Sound rockExplosion; // Sound associated with rock explosion.
    @SuppressWarnings("FieldCanBeLocal")
    private Sound shipExplosion; // Sound associated with spaceship explosion.
    @SuppressWarnings("FieldCanBeLocal")
    private Sound shipThruster; // Sound associated with spaceship thruster.

    // UI Elements...
    private Label scoreLabel; // LibGDX Label object that will display score text.
    private Label shipLabel; // LibGDX Label object that will display ships remaining text.
    private Label timeLabel; // LibGDX Label object that will display elapsed time text.
    // Note:  A Label is an extension of an Actor.

    // g = Screen object for game window (mouse, cheese, floor, win text, and elapsed time text).
    GameScreen(BaseGame g)
    {

        // The constructor of the class:

        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Sets game world dimensions.
        // 3.  Launches the startup / create phase (function).

        // Call the constructor for the BaseScreen (parent / super) class.
        // Sets main window width and height to 800 x 600.
        // Sets ui window width and height to 800 x 600.
        super(g, 800, 600);

        // Set game world dimensions.
        this.mapWidth = 800;
        this.mapHeight = 600;

        // Launch the startup / create phase (function).
        create();

    }

    public final void create()
    {

        /*
        The function occurs during the startup / create phase and accomplishes the following:

        1.  Loads (the image), configures, and adds the background to the stage.
        2.  Loads (the image), configures, and adds the spaceship to the stage.
        3.  Loads (the image), configures, and adds the rocket floor to the spaceship.
        4.  Loads (the image) and configures the base laser.
        5.  Loads (the image), configures, and adds the rocks to the stage.
        6.  Loads (the image) and configures the base explosion.
        7.  Initializes the BaseActor removal list.
        8.  Loads and configures the audio objects.
        9.  Builds uiTable with score, ship count, time elapsed label, and "You Win!" graphic.
        */

        final int numRocks; // Number of rocks.

        float speedUp; // Velocity factor (0 to 1) of current rock.
        int randomCorner; // Random corner in which to place rock.  1 = Upper Left, 2 = Upper Right, 3 = Lower Left, 4 = Lower Right.
        String fileName; // Filename for current rock image.  Examples:  rock0.png, rock1.png, ...
        // String text; // Text to display related to elapsed time.
        String text; // Text to display related to elapsed time, score, or ships remaining.

        Animation explosionAnim; // Animation object associated with explosion.
        BaseActor background; // BaseActor that will act as background.
        PhysicsActor rock; // PhysicsActor that will act as a rock.
        Texture fireTex; // Texture used for the rocket fire.
        Texture laserTex; // Texture used for the lasers.
        Texture rockTex; // Texture used for a rock.
        Texture shipTex; // Texture used for the spaceship.
        // Texture winTex; // Texture used to load image intended for display when winning game.

        // Set default values.
        // win = false;
        score = 0;
        shipCount = 3;
        timeElapsed = 0;
        text = "Time: --";
        numRocks = 6;

        // Initialize asset manager.
        manager = new AssetManager();

        // 1.  Create new BaseActor for the background.
        background = new BaseActor();

        // Set the properties for the background and add to the scene graph.
        background.setTexture( new Texture(Gdx.files.internal("assets/images/space.png")) ); // Load image to buffer.
        background.setPosition( 0, 0 ); // Place image at (0, 0) -- relative to bottom left corner -- within window (stage).
        mainStage.addActor(background); // Add background Actor to the scene graph.

        // 2.  Configure spaceship and rocket fire.
        spaceship = new PhysicsActor();

        // Set the properties for the spaceship and add to the scene graph.

        // Load image to buffer.
        shipTex = new Texture(Gdx.files.internal("assets/images/spaceship.png"));

        // Set filter type -- controlling how pixel colors are interpolated when image is rotated or stretched.
        shipTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Add Animation object to hash map for spaceship.
        spaceship.storeAnimation( "default", shipTex );

        // Place image at (400, 300) -- relative to bottom left corner -- within window (stage).
        spaceship.setPosition( 400,300 );

        // Set spaceship origin coordinates to center of image, to handle rotations.
        spaceship.setOriginCenter();

        // Set maximum speed of spaceship.
        spaceship.setMaxSpeed(200);

        // Set deceleration of spaceship.
        spaceship.setDeceleration(20);

        // Set collision polygon for spaceship as ellipse.
        spaceship.setEllipseBoundary();

        // Add spaceship Actor to the scene graph.
        mainStage.addActor(spaceship);

        // 3.  Create new BaseActor for the rocket fire (copy for others).
        rocketfire = new BaseActor();

        // Place image at (-28, 24) -- relative to bottom left corner of spaceship.
        rocketfire.setPosition(-28,24);

        // Load image to buffer.
        fireTex = new Texture(Gdx.files.internal("assets/images/fire.png"));

        // Set filter type -- controlling how pixel colors are interpolated when image is rotated or stretched.
        fireTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Load image to buffer.
        rocketfire.setTexture( fireTex );

        // Add rocket fire Actor to the group associated with the spaceship.
        spaceship.addActor(rocketfire);

        // 4.  Configure lasers...

        // Create new PhysicsActor for the laser (copy for others).
        baseLaser = new PhysicsActor();

        // Load image to buffer.
        laserTex = new Texture(Gdx.files.internal("assets/images/laser.png"));

        // Set filter type -- controlling how pixel colors are interpolated when image is rotated or stretched.
        laserTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Add Animation object to hash map for laser.
        baseLaser.storeAnimation( "default", laserTex );

        // Set maximum speed of laser.
        baseLaser.setMaxSpeed(400);

        // Set deceleration speed of laser.
        baseLaser.setDeceleration(0);

        // Set collision polygon for laser as ellipse.
        baseLaser.setEllipseBoundary();

        // Set laser origin coordinates to center of image, to handle rotations.
        baseLaser.setOriginCenter();

        // Set laser to rotate image to match velocity (speed).
        baseLaser.setAutoAngle(true);

        // Create array of laser PhysicsActor objects.
        //laserList = new ArrayList<PhysicsActor>();
        laserList = new ArrayList<>();

        // 5.  Configure rocks...

        // Create array of rock PhysicsActor objects.
        //rockList = new ArrayList<PhysicsActor>();
        rockList = new ArrayList<>();

        // Loop through rocks.
        for (int n = 0; n < numRocks; n++)
        {

            // Create new PhysicsActor for a rock.
            rock = new PhysicsActor();

            // Set filename for current rock (variation).  Examples:  rock0.png, rock1.png, ...
            fileName = "assets/images/rock" + (n%4) + ".png";

            // Load image to buffer.
            rockTex = new Texture(Gdx.files.internal(fileName));

            // Set filter type -- controlling how pixel colors are interpolated when image is rotated or stretched.
            rockTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

            // Add Animation object to hash map for rock.
            rock.storeAnimation( "default", rockTex );

            // Pick a corner...
            randomCorner = MathUtils.random(1, 4);

            // Each corner includes a 150 x 150 area.
            // Place rock relative to bottom left corner -- within window (stage).
            // 0, 0 = bottom left.

            // If upper left corner, then...
            if (randomCorner == 1)
            {
                // Upper left corner.
                rock.setPosition(150 * MathUtils.random(), 150 * MathUtils.random() + 450);
            }

            else if (randomCorner == 2)
            {
                // Upper right corner.
                rock.setPosition(800 - (150 * MathUtils.random()), 150 * MathUtils.random() + 450);
            }

            else if (randomCorner == 3)
            {
                // Lower left corner.
                rock.setPosition(150 * MathUtils.random(), 150 * MathUtils.random());
            }

            else
            {
                // Lower right corner.
                rock.setPosition(800 - (150 * MathUtils.random()), 150 * MathUtils.random());
            }

            // Randomly set position of rock in screen.
            //rock.setPosition(800 * MathUtils.random(), 600 * MathUtils.random() );

            // Set rock origin coordinates to center of image, to handle rotations.
            rock.setOriginCenter();

            // Set collision polygon for rock as ellipse.
            rock.setEllipseBoundary();

            // Set rock to rotate image to match velocity (speed).
            rock.setAutoAngle(false);

            // Store velocity factor -- random number from zero to one -- for current rock.
            speedUp = MathUtils.random(0.0f, 1.0f);

            // Random velocity and rotation...
            // Set the velocity vector for the current rock using the angle and speed.
            rock.setVelocityAS( 360 * MathUtils.random(), 75 + 50*speedUp );

            // Set up action to (forever) rotate the rock.
            rock.addAction( Actions.forever( Actions.rotateBy(360, 2 - speedUp) ) );

            // Add current rock Actor to the scene graph.
            mainStage.addActor(rock);

            // Add current rock to lists / arrays.
            rockList.add(rock);
            rock.setParentList(rockList);

        }

        // 6.  Configure explosions...

        // Create new PhysicsActor for an explosion (copy for others).
        baseExplosion = new AnimatedActor();

        // Divides original explosion image into rectangular sections and returns the results in a two-dimensional array of
        // TextureRegion objects
        explosionAnim = GameUtils.parseSpriteSheet(
                "assets/images/explosion.png", 6, 6, 0.03f, PlayMode.NORMAL);

        // Add Animation object to hash map for explosion.
        baseExplosion.storeAnimation( "default", explosionAnim );

        // Set width and height of explosion Actor.
        baseExplosion.setWidth(96);
        baseExplosion.setHeight(96);

        // Set explosion origin coordinates to center of image, to handle rotations.
        baseExplosion.setOriginCenter();

        // 7.  Initialize BaseActor removal list.
        //removeList = new ArrayList<BaseActor>();
        removeList = new ArrayList<>();

        // 8.  Load and configure the audio objects.

        // Load audio objects.
        manager.load(arcadeFunkFile, Music.class);
        manager.load(laserFireFile, Sound.class);
        manager.load(shipThrusterFile, Sound.class);
        manager.load(shipExplosionFile, Sound.class);
        manager.load(rockExplosionFile, Sound.class);
        manager.finishLoading();

        // Set starting volume to use with Music objects.
        audioVolume = 0.80f;

        arcadeFunk = manager.get(arcadeFunkFile); // Get instrumental music from AssetManager.
        arcadeFunk.setLooping(true); // Set instrumental music to loop.
        arcadeFunk.setVolume(audioVolume); // Set volume for instrumental music.
        arcadeFunk.play(); // Play instrumental music.

        // 9.  Set up user interface.

        // Add Label showing time elapsed.  Apply Skin property of uiLabelStyle.
        timeLabel = new Label( text, game.skin, "uiLabelStyle_Small" );

        // Update text to reflect score.
        text = "Score: " + score;

        // Add Label showing score.  Apply Skin property of uiLabelStyle.
        scoreLabel = new Label( text, game.skin, "uiLabelStyle_Small" );

        // Update text to reflect ships remaining.
        text = "Ships: " + shipCount;

        // Add Label showing ships remaining.  Apply Skin property of uiLabelStyle.
        shipLabel = new Label( text, game.skin, "uiLabelStyle_Small" );

        // Set up Table object with user-interface layout.
        uiTable.pad(10); // Set padding, in pixels, for Table (on all sides).
        uiTable.add(scoreLabel).left();
        uiTable.add(shipLabel).expandX();
        uiTable.add(timeLabel).right(); // Add second cell in row, containing time elapsed Label.
        //uiTable.row(); // Add a row to the Table.
        //uiTable.add(winImage).colspan(2).padTop(50); // Add a cell that encompasses both columns, with 50 pixels
        // of padding at the top.
        uiTable.row(); // Add a row to the Table.
        uiTable.add().colspan(3).expandY(); // Add a cell that encompasses both columns, extending the remaining
        // vertical length of the screen.

    }

    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {

        /*
        The function occurs during the update phase and contains code related to game logic.
        The following logic occurs:

        1.  Processes movement related actions (arrow key presses and joystick movement).
        2.  Wrap objects within screen.
        3.  Check lasers for overlapping rocks and remove colliding objects.
        TBD.  Check win conditions.  If conditions met, implements win actions.
        5.  If win NOT occurring, increment elapsed time and update related label.
        */

        /*
        Two action types -- polling and discrete.
        Polling:  Checks the state of the input hardware devices (such as the keyboard) during every iteration of the game loop.
                  Suited for continuous actions. > Constant movement.
        Discrete:  Automatically called when certain events occur (such as the initial press or release of a key, or click of a
                   mouse button).
                   Suited for irregularly occurring actions. > Jumping.
        */

        int overlapRockCounter; // Counter for laser beams overlapping rocks.

        AnimatedActor explosion; // AnimatedActor that will act as the explosion.
        AnimatedActor explosion2; // AnimatedActor that will act as the second explosion.

        // Set defaults.
        overlapRockCounter = 0;

        manager.finishLoading();

        // 1.  Process input.

        // Clear acceleration of spaceship -- set both directions to 0.
        spaceship.setAccelerationXY(0,0);

        // If the user pressed the left arrow key, then...
        if (Gdx.input.isKeyPressed(Keys.LEFT))

            // User pressed the left arrow key.

            // Rotate spaceship to the left based on the time passed since the last frame.
            spaceship.rotateBy(180 * dt);

        // If the user pressed the right arrow key, then...
        if (Gdx.input.isKeyPressed(Keys.RIGHT))

            // User pressed the right arrow key.

            // Rotate spaceship to the right based on the time passed since the last frame.
            spaceship.rotateBy(-180 * dt);

        // If the user pressed the up arrow key, then...
        if (Gdx.input.isKeyPressed(Keys.UP))

        {

            // User pressing the up arrow key -- activate thrusters.

            // Add to the acceleration of the spaceship, based on the existing rotation and increasing the speed by 100.
            spaceship.addAccelerationAS(spaceship.getRotation(), 100);

            // Display the rocket fire when the user presses the up key.
            rocketfire.setVisible(true);

            // Play ship thruster sound.
            shipThruster = manager.get(shipThrusterFile);
            shipThruster.play(audioVolume);

        }

        else

        {

            // User NOT pressing the up arrow key.

            // Hide the rocket fire when the user not pressing the up key.
            rocketfire.setVisible(false);

        }

        // 2.  Wrap objects within screen.
        wraparound( spaceship );

        // Loop through visible rocks.
        for ( PhysicsActor rock : rockList )
        {
            // Wrap rock within screen.
            wraparound( rock );
        }

        // Clear BaseActor removal list.
        removeList.clear();

        // 3. Check lasers for overlapping rocks and remove colliding objects.

        // Loop through lasers.
        for ( PhysicsActor laser : laserList )
        {

            // wraparound( laser );

            // If laser NOT visible, then...
            if ( !laser.isVisible() )

                // Laser NOT visible.  Add to removal list.
                removeList.add( laser );

            // Loop through rocks.
            for ( PhysicsActor rock : rockList )
            {

                // If laser overlaps rock, then...
                if ( laser.overlaps(rock, false) )
                {

                    // Laser overlaps rock.

                    // Increment overlap counter.
                    overlapRockCounter++;

                    // Add laser and rock to removal list.
                    removeList.add( laser );
                    removeList.add( rock );

                    // Create a copy of the explosion AnimatedActor.
                    explosion = baseExplosion.clone();

                    // Center explosion within rock.
                    explosion.moveToOrigin(rock);

                    // Add explosion AnimatedActor to the scene graph.
                    mainStage.addActor(explosion);

                    // Add an Action to the explosion to remove the AnimatedActor after 1.08 seconds.
                    explosion.addAction( Actions.sequence(Actions.delay(1.08f), Actions.removeActor()) );

                    // Add to score.
                    score += 100;

                    // Update score label.
                    scoreLabel.setText( "Score: " + score );

                } // End ... If laser overlaps rock.

            } // Loop through rocks.

        } // Loop through lasers.

        // If first overlap in current update, then...
        if (overlapRockCounter == 1)

        {
            // First overlap in current update.
            // Play rock explosion sound.
            rockExplosion = manager.get(rockExplosionFile);
            rockExplosion.play(audioVolume);
        }

        // Loop through remaining rocks.
        for ( PhysicsActor rock : rockList )
        {

            // If rock overlaps ship, then...
            if ( rock.overlaps(spaceship, false) )
            {

                // Rock overlaps ship.

                // Play ship explosion sound.
                shipExplosion = manager.get(shipExplosionFile);
                shipExplosion.play(audioVolume);

                // Add laser and rock to removal list.
                removeList.add( spaceship );
                removeList.add( rock );

                // Create copies of the explosion AnimatedActor.
                explosion = baseExplosion.clone();
                explosion2 = baseExplosion.clone();

                // Center explosion within spaceship.
                explosion.moveToOrigin(spaceship);

                // Center second explosion within rock.
                explosion2.moveToOrigin(rock);

                // Add explosion AnimatedActor objects to the scene graph.
                mainStage.addActor(explosion);
                mainStage.addActor(explosion2);

                // Add an Action to the explosion to remove the AnimatedActor objects after 1.08 seconds.
                explosion.addAction( Actions.sequence(Actions.delay(1.08f), Actions.removeActor()) );
                explosion2.addAction( Actions.sequence(Actions.delay(1.08f), Actions.removeActor()) );

                // Reduce ship count.
                shipCount -= 1;

                // Update ships remaining label.
                shipLabel.setText( "Ships: " + shipCount );

                // Exit loop.
                break;

            } // End ... If rock overlaps ship.

        }

        // Loop through BaseActor objects in removal list.
        for (BaseActor ba : removeList)
        {
            // Remove the BaseActor from its Stage and parent list.
            ba.destroy();
        }

        // If win conditions not satisfied, then...
        if (!win)
        {
            // Win conditions not satisfied.

            // Increment timer by elapsed time.
            timeElapsed += dt;

            // Update elapsed time label.
            timeLabel.setText( "Time: " + (int)timeElapsed );
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
        // 3.  Generates a laser when pressing the SPACE key.
        // 4.  Exits the game when pressing the Escape key.

        PhysicsActor laser; // PhysicsActor that will act as the current laser.

        manager.finishLoading();

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

            // Play laser firing sound.
            laserFire = manager.get(laserFireFile);
            laserFire.play(audioVolume);

            // Make a copy of the base laser PhysicsActor object.
            laser = baseLaser.clone();

            // Center laser within spaceship.
            laser.moveToOrigin( spaceship );

            // Set velocity of laser based on spaceship rotation and using a speed of 400.
            laser.setVelocityAS( spaceship.getRotation(), 400 );

            // Add laser to both lists.
            laserList.add(laser);
            laser.setParentList(laserList);

            // Add laser PhysicsActor to the scene graph.
            mainStage.addActor(laser);

            // Add Action to laser to fade out over the course of 0.5 seconds after 0.5 elapse.
            laser.addAction(
                    Actions.sequence(Actions.delay(2), Actions.fadeOut(0.5f), Actions.visible(false)) );

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

    // ba = BaseActor to check whether past edges of the screen (Stage).
    private void wraparound(BaseActor ba)
    {

        /*
        The function allows for implementing a world with no “boundaries”: an object traveling past the right edge of
        the screen reappears on the left (and vice versa), and similarly for the bottom and top edges. This behavior is
        called wraparound.
        */

        // If base actor past left edge of screen, then...
        if ( ba.getX() + ba.getWidth() < 0 )

            // Base actor past left edge of screen.
            // Move necessary portion of actor to right edge of screen.
            ba.setX( mapWidth );

        // If base actor past right edge of screen, then...
        if ( ba.getX() > mapWidth )

            // Base actor past right edge of screen.
            // Move necessary portion of actor to left edge of screen.
            ba.setX( -ba.getWidth() );

        // If base actor past top edge of screen, then...
        if ( ba.getY() + ba.getHeight() < 0 )

            // Base actor past top edge of screen.
            // Move necessary portion of actor to bottom edge of screen.
            ba.setY( mapHeight );

        // If base actor past bottom edge of screen, then...
        if ( ba.getY() > mapHeight )

            // Base actor past bottom edge of screen.
            // Move necessary portion of actor to top of screen.
            ba.setY( -ba.getHeight() );

    }

}