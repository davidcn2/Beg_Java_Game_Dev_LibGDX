package PlaneDodger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.math.MathUtils;
import core.*;
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

Subclass: A Java subclass is a class which inherits a method or methods from a Java superclass.
A Java class may be either a subclass, a superclass, both, or neither!

Polymorphism:  Polymorphism is the ability of an object to take on many forms. The most common use 
of polymorphism in OOP occurs when a parent class reference is used to refer to a child class object.
Any Java object that can pass more than one IS-A test is considered to be polymorphic.
*/

public class GameScreen extends BaseScreen // Extends the BaseScreen class.
{
    
    // The class extends the basic functionality of a BaseScreen class and stores information
    // about the game -- background, ground, player, stars, sparkles, enemies, and explosions and 
    // related properties (map height and width, timers, speeds, ...).
    
    // Methods include:
    
    // create:  Sets defaults.  Configures and adds the Actors to the stage.  Sets up base actors for cloning.
    // update:  Occurs during the update phase (render method) and contains code related to game logic.
    
    // Declare regular variables.
    private float enemySpeed; // Speed of enemy.
    private float enemyTimer; // Amount of time (seconds) since addition of last enemy.
    private boolean gameOver; // Whether game over -- occurs when player intersects an enemy.
    private float starTimer; // Amount of time (seconds) since addition of last yellow star.
    
    // Declare objects.
    private PhysicsActor[] background; // PhysicsActor array that will act as the background objects.
    private PhysicsActor baseEnemy; // PhysicsActor object that will act as the base enemy (cloned).
    private AnimatedActor baseExplosion; // AnimatedActor object that will act as the base explosion when
      // player intersects an enemy.
    private AnimatedActor baseSparkle; // AnimatedActor object that will act as the base sparkle (over the yellow 
      // star).  Displayed when player intersects a yellow star.
    private PhysicsActor baseStar; // PhysicsActor object that will act as the base yellow star (cloned).
    private ArrayList<PhysicsActor> enemyList; // PhysicsActor array that will act as the enemies (red planes).
    private PhysicsActor[] ground; // PhysicsActor array that will act as the ground objects.
    private PhysicsActor player; // PhysicsActor object that will act as the player (green plane).
    private ArrayList<BaseActor> removeList; // List of actors to remove.
    private ArrayList<PhysicsActor> starList; // PhysicsActor array that will act as the yellow stars.
    
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
        
        // Configure and add the actors to the stage:  background, ground, planes, and stars.
        create();
        
    }

    public final void create()
    {
        
        // The function occurs during the startup / create phase and accomplishes the following:
        
        // 1.  Sets defaults and initializes empty array lists.
        // 2.  Sets up background and ground objects.
        // 3.  Sets up the player object.
        // 4.  Sets up the (base) star object.
        // 5.  Sets up the (base) sparkle object.
        // 6.  Sets up the (base) enemy object.
        // 7.  Sets up the (base) explosion object.
        
        // Declare objects.
        Animation anim; // Animation object for player.
        PhysicsActor bg0; // First of the background objects.
        PhysicsActor bg1; // Second of the background objects.
        Animation explosionAnim; // Animation object for explosion.
        PhysicsActor gr0; // First of the ground objects.
        PhysicsActor gr1; // Second of the ground objects.
        Animation redAnim; // Animation object for enemy.
        Animation sparkleAnim; // Animation for sparkle when player collects a yellow star.
        Texture starTex; // Texture (image) to use for yellow star.
        
        // Set defaults.
        gameOver = false; // Default game to state of active.
        starTimer = 0; // Default time since addition of last yellow star as 0, since none added yet.
        enemyTimer = 0; // Default time since addition of last enemy as 0, since none added yet.
        enemySpeed = -250; // Default horizontal acceleration (heading to the left) for enemy.
        
        // Initialize empty array lists.
        removeList = new ArrayList<>(); // Initialize array to hold actors to remove.
        starList = new ArrayList<>(); // Initialize array to hold yellow stars.
        enemyList = new ArrayList<>(); // Initialize array to hold enemies.
        
        // Alternative code:
        // removeList = new ArrayList<BaseActor>();
        // starList = new ArrayList<PhysicsActor>();
        // enemyList = new ArrayList<PhysicsActor>();
        
        // Set up the background objects -- includes the ground.
        
        // Create a new PhysicsActor array for the background objects.
        background = new PhysicsActor[2];
        
        // Create the first of the background objects.
        bg0 = new PhysicsActor();
        
        // Note the use of higher speed for the ground to implement parallax scrolling and give the illusion of
        // depth.
        
        // Set the properties for the first background object and add to the scene graph and array.
        bg0.storeAnimation( "default", new Texture(Gdx.files.internal("images/sky.png")) ); // Store actor animation.
        bg0.setPosition( 0, 0 ); // Set position of bottom left corner of actor.
        bg0.setVelocityXY( -50, 0 ); // Set the velocity vector of actor.
        mainStage.addActor( bg0 ); // Add actor to scene graph.
        background[0] = bg0; // Add actor to background object array.
        
        // Set the properties for the second background object and add to the scene graph and array.
        // Copy first background object and position after (to the right).
        bg1 = bg0.clone(); // Copy the first background object.
        bg1.setX( bg0.getWidth() ); // Set x-position of second actor to place to right of first.
        mainStage.addActor(bg1); // Add actor to scene graph.
        background[1] = bg1; // Add actor to background object array.
        
        // Create a new PhysicsActor array for the ground objects.
        ground = new PhysicsActor[2];
        
        // Create the first of the ground objects.
        gr0 = new PhysicsActor();
        
        // Set the properties for the first ground object and add to the scene graph and array.
        gr0.storeAnimation( "default", new Texture(Gdx.files.internal("images/ground.png")) ); // Store actor animation.
        gr0.setPosition( 0, 0 ); // Set position of bottom left corner of actor.
        gr0.setVelocityXY( -200, 0 ); // Set the velocity vector of actor.
        gr0.setRectangleBoundary(); // Sets the properties of the bounding polygon related to the texture 
          // region, using a rectangle.
        mainStage.addActor( gr0 ); // Add actor to scene graph.
        ground[0] = gr0; // Add actor to ground object array.
        
        // Set the properties for the second ground object and add to the scene graph and array.
        // Copy first ground object and position after (to the right).
        gr1 = gr0.clone(); // Copy the second ground object.
        gr1.setX( gr0.getWidth() ); // Set x-position of second actor to place to right of first.
        mainStage.addActor(gr1); // Add actor to scene graph.
        ground[1] = gr1; // Add actor to ground object array.
        
        // Set up the player object.
        
        // Create the player object.
        player = new PhysicsActor();
        
        // Set the properties for the player object and add to the scene graph.
        anim = GameUtils.parseImageFiles( "images", "planeGreen", "png", 3, 0.1f, 
          Animation.PlayMode.LOOP_PINGPONG ); // Create animation related to player, loading images.
        player.storeAnimation( "default", anim ); // Store animation in actor.
        player.setPosition( 200, 300 ); // Set position of bottom left corner of actor.
        player.setAccelerationXY(0, -600); // Set acceleration of actor to reflect gravity.
        player.setOriginCenter(); // Set origin of actor to the center of associated image -- for rotation.
        player.setEllipseBoundary(); // Sets the properties of the bounding polygon related to the texture 
          // region, using an ellipse.
        mainStage.addActor( player ); // Add actor to scene graph.
        
        // Set up the base star object.
        
        // Create the base yellow star object (allows for cloning / copying).
        baseStar = new PhysicsActor();
        
        // Set the properties for the base yellow star object.
        starTex = new Texture(Gdx.files.internal("images/star.png")); // Load image to buffer.
        starTex.setFilter(TextureFilter.Linear, TextureFilter.Linear); // Set filter type -- controlling how 
          // pixel colors are interpolated when image is rotated or stretched.
        baseStar.storeAnimation( "default", starTex ); // Store animation in actor.
        baseStar.setVelocityXY( -200, 0 ); // Set acceleration of actor to move to the left.
        baseStar.setOriginCenter(); // Set origin of actor to the center of associated image -- for rotation.
        baseStar.setEllipseBoundary(); // Sets the properties of the bounding polygon related to the texture 
          // region, using an ellipse.

        // Set the properties for the base sparkle object.
        
        // Create the base sparkle object (allows for cloning / copying).
        baseSparkle = new AnimatedActor();
        
        // Set the properties for the base sparkle object.
        sparkleAnim = GameUtils.parseSpriteSheet("images/sparkle.png", 8, 8, 0.01f, 
          PlayMode.NORMAL ); // Create animation related to sparkle, loading images.
        baseSparkle.storeAnimation( "default", sparkleAnim ); // Store animation in actor.
        baseSparkle.setWidth( 64 ); // Set width of actor, in pixels.
        baseSparkle.setHeight( 64 ); // Set height of actor, in pixels.
        baseSparkle.setOriginCenter(); // Set origin of actor to the center of associated image -- for rotation.
        baseSparkle.setFrameTiming( 64, 0.01f ); // Set number of frames in animation.
        
        // Set the properties for the base enemy object.
        
        // Create the base enemy object (allows for cloning / copying).
        baseEnemy = new PhysicsActor();
        
        // Set the properties for the base enemy object.
        redAnim = GameUtils.parseImageFiles( "images", "planeRed", "png", 3, 0.1f, 
          Animation.PlayMode.LOOP_PINGPONG ); // Create animation related to enemy, loading images.
        baseEnemy.storeAnimation( "default", redAnim ); // Store animation in actor.
        baseEnemy.setWidth( baseEnemy.getWidth() * 1.25f ); // Set width of actor (to 1.25x size of image), in pixels.
        baseEnemy.setHeight( baseEnemy.getHeight() * 1.25f ); // Set height of actor (to 1.25x size of image), in pixels.
        baseEnemy.setOriginCenter(); // Set origin of actor to the center of associated image -- for rotation.
        baseEnemy.setEllipseBoundary(); // Sets the properties of the bounding polygon related to the texture 
          // region, using an ellipse.
        
        // Set the properties for the base explosion object.
        
        // Create the explosion object.
        baseExplosion = new AnimatedActor();
        
        // Set the properties for the explosion object.
        explosionAnim = GameUtils.parseSpriteSheet( "images/explosion.png", 6, 6, 0.03f, 
          PlayMode.NORMAL ); // Create animation related to explosion, loading images.
        baseExplosion.storeAnimation( "default", explosionAnim ); // Store animation in actor.
        baseExplosion.setWidth( 96 ); // Set width of actor, in pixels.
        baseExplosion.setHeight( 96 ); // Set height of actor, in pixels.
        baseExplosion.setOriginCenter(); // Set origin of actor to the center of associated image -- for rotation.
        baseExplosion.setFrameTiming( 36, 0.03f ); // Set number of frames in animation.
        
    }

    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt) 
    {   
        
        /*
        The function occurs during the update phase (render method) and accomplishes the following:
        
        1.  Adjusts position of background and ground objects as they move past left edge of screen.
        2.  If at or past star timer target, adds new yellow star.
        3.  If at or past enemy timer target, adds new enemy.
        4.  Performs collision detection -- stars, enemies, and ground.
        5.  Clears actors in removal list.
        */
        
        // Declare regular variables.
        int i; // Counter used to loop through background and ground objects.
        
        // Declare objects.
        PhysicsActor bg; // Used to loop through background objects.  Current object in loop.
        PhysicsActor enemy; // New enemy object to create.
        AnimatedActor explosion; // New explosion object to create when player hits an enemy plane.
        PhysicsActor gr; // Used to loop through ground objects.  Current object in loop.
        AnimatedActor sparkle; // New sparkle object to create when player hits a star.
        PhysicsActor star; // New star object to create.
        
        // Set up background objects to provide infinite scrolling effect.
        // The left edge of image 2 is adjacent to the right edge of image 1, and they both move to the 
        // left at the same rate.  When the right edge of image 1 moves completely past the left edge of 
        // the screen, image 1 will be repositioned to the opposite side.  The left edge of image 1 will 
        // become adjacent to the right edge of image 2.  The process continues indefinitely.
        
        // Manage background objects.
        
        // Loop through background and ground objects.
        for (i = 0; i < 2; i++)
        {
            // Get reference to current background object in loop.
            bg = background[i];
            
            // If current background object past left edge of screen, then...
            if ( bg.getX() + bg.getWidth() < 0 )
                // Current background object past left edge of screen.
                // Set position of current object to right of the other.
                bg.setX( bg.getX() + 2 * bg.getWidth() );
            
            // Get reference to current ground object in loop.
            gr = ground[i];
            
            // If current ground object past left edge of screen, then...
            if ( gr.getX() + gr.getWidth() < 0 )
                // Current ground object past left edge of screen.
                // Set position of current object to right of the other.
                gr.setX( gr.getX() + 2 * gr.getWidth() );
        }
        
        // If game no longer active, then...
        if ( gameOver )
            // Game no longer active.
            // Exit function.
            return;
          
        // for continuous velocity adjustment,
        //   uncomment the code below.
        // if (Gdx.input.isKeyPressed(Keys.SPACE))
        //    player.addVelocityXY(0, 25);
            
        // Add new stars at regular interval.
        starTimer += dt; // Add seconds passed to star timer.
        
        // If one or more seconds passed since addition of last yellow star, then...
        if (starTimer > 1)
            
        {
            
            // One or more seconds passed since addition of last yellow star.
            
            starTimer = 0; // Reset star timer.
            star = baseStar.clone(); // Create copy of base star object.
            star.setPosition( 900, MathUtils.random(100, 500) ); // Randomly position star.
            starList.add( star ); // Add star to active list.
            star.setParentList( starList ); // Set reference of (star) actor to ArrayList.
            mainStage.addActor( star ); // Add (star) actor to scene graph.
        }
        
        // Add enemies at regular interval.
        enemyTimer += dt; // Add seconds passed to enemy timer.
        
        // If three or more seconds passed since addition of last enemy, then...
        if (enemyTimer > 3)
        {
            
            // Three or more seconds passed since addition of last enemy.
            
            enemyTimer = 0; // Reset enemy timer.
            
            // If enemy speed (horizontally) greater than -800, then...
            if (enemySpeed > -800)
                // Enemy speed (horizontally) greater than -800.
                // Reduce by 15.
                enemySpeed -= 15;
            
            enemy = baseEnemy.clone(); // Create copy of base enemy object.
            enemy.setPosition( 900, MathUtils.random(100, 500) ); // Randomly position enemy.
            enemy.setVelocityXY(enemySpeed, 0); // Set acceleration of actor to move to the left.
            enemy.setRotation( 10 ); // Set rotation of enemy.
            enemy.addAction( Actions.forever(Actions.sequence( Actions.rotateBy(-20, 1), 
              Actions.rotateBy(20, 1) ) )); // Add action to enemy to constantly rotate.
            
            enemyList.add( enemy );// Add enemy to active list.
            enemy.setParentList( enemyList ); // Set reference of (enemy) actor to ArrayList.
            mainStage.addActor( enemy ); // Add (enemy) actor to scene graph.
            
        }
        
        // Perform collision detection.
        
        // If player at top edge of map, then...
        if ( player.getY() > mapHeight - player.getHeight() )
        {
            
            // Player at top edge of screen.
            
            // Set velocity of player to 0.
            player.setVelocityXY( 0, 0 );
            
            // Reposition player so that top meets edge of map.
            player.setY( mapHeight - player.getHeight() );
            
        }
        
        // Loop through ground objects.
        for (i = 0; i < 2; i++)
        {
            
            // Get reference to current ground object in loop.
            gr = ground[i];
            
            // If player overlapping ground (more than minimum significant value), then...
            // The true automatically repositions the player to avoid overlap.
            if ( player.overlaps(gr, true) )
            {
                // Player overlapping ground (more than minimum significant value).
                
                // Set velocity of player to 0.
                player.setVelocityXY(0,0);
            }
        }
        
        // Clear objects from removal list.
        removeList.clear();
        
        // Loop through active yellow stars.
        for (PhysicsActor star2 : starList )
        {
            
            // If yellow star past left edge of screen, then...
            if ( star2.getX() + star2.getWidth() < 0 )
                // Yellow star past left edge of screen.
                // Add star to removal list.
                removeList.add(star2);
                
            // If player overlaps yellow star (more than MSV), then...
            // The false DOES NOT automatically reposition the player to avoid overlap.
            if ( player.overlaps(star2, false) )
            {
                
                // Player overlaps yellow star (more than MSV).
                removeList.add(star2); // Add star to removal list.
                sparkle = baseSparkle.clone(); // Create copy of base sparkle object.
                sparkle.moveToOrigin(star2); // Center sparkle within player object.
                
                // Add action to remove sparkle from screen in 0.64 seconds.
                //sparkle.addAction( Actions.sequence( Actions.delay(0.64f), Actions.removeActor() ) );
                sparkle.removeAfterSinglePass( sparkle );
                
                mainStage.addActor(sparkle); // Add (sparkle) actor to scene graph.
                
            } // End ... If player overlaps star.
            
        } // End ... Loop through stars.
        
        // Loop through active enemies.
        for (PhysicsActor enemy2 : enemyList )
        {
            
            // If enemy past left edge of screen, then...
            if ( enemy2.getX() + enemy2.getWidth() < 0)
                // Enemy past left edge of screen.
                // Add enemy to removal list.
                removeList.add(enemy2);
                
            // If player overlaps enemy (more than MSV), then...
            // The false DOES NOT automatically reposition the player to avoid overlap.
            if ( player.overlaps(enemy2, false) )
            {
                
                // Player overlaps enemy (more than MSV).
                
                explosion = baseExplosion.clone(); // Create copy of base explosion object.
                explosion.moveToOrigin(player); // Center explosion within player object.
                
                // Add action to remove explosion from screen in 1.08 seconds.
                //explosion.addAction( Actions.sequence( Actions.delay(1.08f), Actions.removeActor() ) );
                explosion.removeAfterSinglePass( explosion );
                
                mainStage.addActor(explosion); // Add (explosion) actor to scene graph. 
                removeList.add(player); // Add player to removal list.                
                gameOver = true; // Flag game as over.
                
            } // End ... If player overlaps enemy.
            
        } // End ... Loop through active enemies.
        
        // Destroy actors in removal list -- clear them from the stage.
        
        // Loop through actors in removal list.
        for (BaseActor ba : removeList)
        {
            // Remove actor from the stage.
            ba.destroy();
        }
        
    }

    // Handle discrete key events.
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.
        
        // 1.  Pauses the game when pressing the P key.
        // 2.  Resets the game when pressing the R key.
        // 3.  Moves the player up when pressing the space key.
        
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