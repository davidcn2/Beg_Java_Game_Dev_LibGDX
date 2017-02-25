/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cheeselauncher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author dcohn
 */
/**
 *
 * @author dcohn
 */
public class CheesePlease4 extends Game { // Extends the Game class from LibGDX.
    
    // The class extends the basic functionality of a Game object in LibGDX.
    
    // Methods include:
    // create:  Creates the 2D scene graph and adds the Actors:  floor, cheese, mouse, and win message.
    // render:  Resets the speed of the mouse (to zero), handles key presses, adjusts Actor positions, 
    // checks for a win and adjusts accordingly, and draws the graphics.
    
    public Stage mainStage; // Stores a 2D scene graph containing the hierarchies of actors.
    // Stage handles the viewport and distributes input events.
    private AnimatedActor mousey; // Creates an AnimatedActor object that will act as the mouse.
    private BaseActor cheese; // Creates a BaseActor object that will act as the cheese.
    private BaseActor floor; // Creates a BaseActor object that will act as the background tiles.
    private BaseActor winText; // Creates a BaseActor object that will act as the text that displays
    // when winning.
    private boolean win; // Whether player has won the game.
    
    @Override
    public void create()
    {
        
        // The function creates the 2D scene graph and adds the Actors:  floor, cheese, mouse, and
        // win message.
        
        // Set default values.
        win = false;
        
        // Create a new 2D scene graph.
        mainStage = new Stage();
        
        // Create a new BaseActor for the floor (background tiles).
        floor = new BaseActor();
        
        // Set the properties for the floor and add to the scene graph.
        floor.setTexture( new Texture(Gdx.files.internal("images/tiles.jpg")) ); // Load image to buffer.
        floor.setPosition( 0, 0 ); // Place image at (0, 0) within window.
        mainStage.addActor( floor ); // Add floor Actor to the scene graph.
        
        // Create a new BaseActor for the cheese.
        cheese = new BaseActor();
        
        // Set the properties for the cheese and add to the scene graph.
        cheese.setTexture( new Texture(Gdx.files.internal("images/cheese.png")) ); // Load image to buffer.
        cheese.setPosition( 400, 300 ); // Place image at (400, 300) within window.
        mainStage.addActor( cheese ); // Add cheese Actor to the scene graph.
        
        // Stores array of images (in this case for an animation).  Similar to buffer in Direct-X.
        TextureRegion[] frames = new TextureRegion[4]; 
        
        // Loop through and add animation frames (TextureRegion objects) to array.
        for (int n = 0; n < 4; n++)
        {
            // Set filename for current animation frame.  Examples:  mouse0.png, mouse1.png, ...
            String filename = "images/mouse" + n + ".png";
            
            // Loan image to buffer.
            Texture tex = new Texture(Gdx.files.internal(filename));
            
            // Set file type -- controlling how pixel colors are interpolated when image is
            // rotated or stretched.
            tex.setFilter(Texture.TextureFilter.Linear, TextureFilter.Linear);
            
            // Store texture in standard Java array.
            frames[n] = new TextureRegion( tex );
        }
        
        // Copy textures from standard Java to LibGDX array.
        Array<TextureRegion> framesArray = new Array<>(frames);
        
        // Create animation object for mouse.
        // 0.1f ... Frame interval.  The amount of time to display each image.
        // framesArray ... Array of frames to use in animation.
        // Animation.PlayMode.LOOP_PINGPONG ... Indicates how frames should be played.
        // LOOP = In the order given.
        // LOOP_REVERSED = In reverse order.
        // LOOP_PINGPONG = From first to last to first again.
        // LOOP_RANDOM = In random order.
        Animation anim = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP_PINGPONG);
        
        // Create a new AnimatedActor for the mouse.
        mousey = new AnimatedActor();
        
        // Set the properties for the mouse and add to the scene graph.
        // mousey.setTexture( new Texture(Gdx.files.internal("images/mouse.png")) ); // Load image to buffer.
        mousey.setAnimation( anim );
        
        // Set mouse origin coordinates to center of image, to handle roations.
        mousey.setOrigin( mousey.getWidth() / 2, mousey.getHeight() / 2 );
        
        mousey.setPosition( 20, 20 ); // Place image at (20, 20) within window.
        mainStage.addActor( mousey ); // Add mouse Actor to the scene graph.
        
        // Create a new BaseActor for the win message.
        winText = new BaseActor();
        
        // Set the properties for the win message and add to the scene graph.
        winText.setTexture( new Texture(Gdx.files.internal("images/you-win.png")) ); // Load image to buffer.
        winText.setPosition( 170, 60 ); // Place image at (170, 60) within window.
        winText.setVisible( false ); // Hide the win message when the application starts.
        mainStage.addActor( winText ); // Add win message Actor to the scene graph.
        
    }
    
    @Override
    public void render()
    {
        
        // The function resets the speed of the mouse (to zero), handles key presses,
        // adjusts Actor positions, checks for a win and adjusts accordingly, and
        // draws the graphics.
        
        // Set speed of mouse in X and Y directions to 0.
        mousey.velocityX = 0;
        mousey.velocityY = 0;
        
        // If user clicked the left key, decrease the X speed of mouse by 100. 
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            mousey.velocityX -= 100;
        
        // If user clicked the right key, increase the X speed of mouse by 100.
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            mousey.velocityX += 100;
        
        // If user clicked the up key, increase the Y speed of mouse by 100.
        if (Gdx.input.isKeyPressed(Keys.UP))
            mousey.velocityY += 100;
        
        // If user clicked the down key, decrease the Y speed of mouse by 100.
        if (Gdx.input.isKeyPressed(Keys.DOWN))
            mousey.velocityY -= 100;
        
        // Store the time span between the current and the last frame in seconds.
        float dt = Gdx.graphics.getDeltaTime();
        
        // Calls the Actor.act(float) method on each actor in the stage.
        // Typically called each frame.  The method also fires enter and exit events.
        // Updates the position of each Actor based on time.
        mainStage.act(dt);
        
        // Check win condition:  mousey must overlap cheese.
        
        // Store bounding rectangle (x, y, width, height) of cheese.
        Rectangle cheeseRectangle = cheese.getBoundingRectangle();
        
        // Store bounding rectangle (x, y, width, height) of mouse.
        Rectangle mouseyRectangle = mousey.getBoundingRectangle();
        
        // If bounding rectangle for cheese contains mouse, then...
        if ( !win && cheeseRectangle.contains(mouseyRectangle) )
            
        {
            // Bounding rectangle for cheese contains mouse.
            
            // Set win flag to true.
            win = true;
            
            // Create action to rotate cheese image (360 degrees per second), shrink (changing both
            // scale factors to 0 over the course of a second), and fade out (over the course of
            // one second.
            Action spinShrinkFadeOut = Actions.parallel(
              Actions.alpha(1),  // Set transparency value.
              Actions.rotateBy(360, 1), // Set rotation amount and duration.
              Actions.scaleTo(0, 0, 1), // Set x and y amount and duration -- for scaling.
              Actions.fadeOut(1)); // Set duration of fade out.
            
            // Add action to cheese Actor.
            cheese.addAction( spinShrinkFadeOut );
            
            // Ensure that cheese Actor rotates around its center (rather than a corner).
            // Set origin point of Actor, which serves as center of rotation.
            cheese.setOrigin( cheese.getWidth() / 2, cheese.getHeight() / 2 );
            
            // Display You Win graphic through a fade in effect occurring over a period of two seconds.
            // Use an infinite loop containing a two-step sequence:
            // 1.  Shift the color tint to red. > Occupies one second.
            // 2.  Shift the color tint to blue. > Occupies one second.
            Action fadeInColorCycleForever = Actions.sequence(
              Actions.alpha(0), // Set transparency value.
              Actions.show(), // Set visible to true.
              Actions.fadeIn(2), // Set duration of fade in.
              Actions.forever(
                Actions.sequence(
                  // Color shade to approach, duration.
                  Actions.color( new Color(1, 0, 0, 1), 1 ),
                  Actions.color( new Color(0, 0, 1, 1), 1 )
                )
              )
            );
            
            // Add action to win text Actor.
            winText.addAction( fadeInColorCycleForever );
        }
            
        // Draw graphics.
        
        // Overdraws the area with the given glClearColor.
        Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
        
        // Clear the area using the specified buffer.  Supports multiple buffers.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Draw the stage -- containing the Actors.
        mainStage.draw();
        
    }
    
}