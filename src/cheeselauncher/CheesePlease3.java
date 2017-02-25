/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cheeselauncher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 *
 * @author dcohn
 */
public class CheesePlease3 extends Game { // Extends the Game class from LibGDX.
    
    // The class extends the basic functionality of a Game object in LibGDX.
    
    // Methods include:
    // create:  Creates the 2D scene graph and adds the Actors:  floor, cheese, mouse, and win message.
    // render:  Resets the speed of the mouse (to zero), handles key presses, adjusts Actor positions, 
    // checks for a win and adjusts accordingly, and draws the graphics.
    
    public Stage mainStage; // Stores a 2D scene graph containing the hierarchies of actors.
    // Stage handles the viewport and distributes input events.
    private BaseActor mousey; // Creates a BaseActor object that will act as the mouse.
    private BaseActor cheese; // Creates a BaseActor object that will act as the cheese.
    private BaseActor floor; // Creates a BaseActor object that will act as the background tiles.
    private BaseActor winText; // Creates a BaseActor object that will act as the text that displays
    // when winning.
    
    @Override
    public void create()
    {
        
        // The function creates the 2D scene graph and adds the Actors:  floor, cheese, mouse, and
        // win message.
        
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
        
        // Create a new BaseActor for the mouse.
        mousey = new BaseActor();
        
        // Set the properties for the mouse and add to the scene graph.
        mousey.setTexture( new Texture(Gdx.files.internal("images/mouse.png")) ); // Load image to buffer.
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
        if ( cheeseRectangle.contains(mouseyRectangle) )
            // Bounding rectangle for cheese contains mouse.
            // Set visibility of win message to true.
            winText.setVisible( true );
        
        // Draw graphics.
        
        // Overdraws the area with the given glClearColor.
        Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
        
        // Clear the area using the specified buffer.  Supports multiple buffers.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Draw the stage -- containing the Actors.
        mainStage.draw();
        
    }
    
}
