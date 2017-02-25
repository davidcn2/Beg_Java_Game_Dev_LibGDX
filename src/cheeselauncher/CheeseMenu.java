/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cheeselauncher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

/**
 *
 * @author dcohn
 */
public class CheeseMenu implements Screen {
    
    private Stage uiStage; // Stores a 2D scene graph containing the background and text actors.
    private final Game game; // Screen object used for main menu.
    // Allows an application to easily have multiple screens.
    
    // g = Screen object for main menu.
    public CheeseMenu(Game g)
    {
        
        // The constructor of the class:
        
        // 1.  Sets the Screen object to the one passed.
        // 2.  Calls a function to create the 2D scene graphs and adds the Actors.
        
        // Store Screen object for game (mouse and cheese).
        this.game = g;
        
        // Create the 2D scene graphs and add the actors.
        create();
        
    }
    
    private void create()
    {
        
        /*
        The function occurs during the create phase and accomplishes the following:
        
        // 1.  Creates the 2D scene graph.
        // 2.  Configures and adds the Actors:  background, Cheese Please (title text), and instructions.
        */
        
        String text; // Text to display on main menu screen.
        BaseActor background; // Creates a BaseActor object that will act as the background in the main menu.
        BaseActor titleText; // Creates a BaseActor object to display the Cheese Please image.
        BitmapFont font; // BitmapFont object storing font used when display main menu text.
        Label instructions; // LibGDX Label object that will display main menu text.
        LabelStyle style; // Style (color to tint) to associate with font used in Label.
        
        // Set defaults.
        text = " Press S to start, M for main menu ";
        
        // Create a new 2D scene graph to hold the main menu actors (background and text).
        uiStage = new Stage();
        
        // Create a new BaseActor for the background in the main menu.
        background = new BaseActor();
        
        // Set the properties for the background and add to the scene graph.
        background.setTexture( new Texture(Gdx.files.internal("images/tiles-menu.jpg")) ); // Load image to buffer.
        uiStage.addActor( background ); // Add background image Actor to the scene graph.
        
        // Create a new BaseActor for the Cheese Please image.
        titleText = new BaseActor();
        
        // Set the properties for the Cheese Please image to add to the scene graph.
        titleText.setTexture( new Texture(Gdx.files.internal("images/cheese-please.png")) ); // Load image to buffer.
        titleText.setPosition( 20, 100 ); // Place image at (20, 100) within window.
        uiStage.addActor( titleText ); // Add Cheese Please Actor to the scene graph.
        
        // Configure main menu instructions message (Label / BitmapFont).
        
        // Initialize BitmapFont (image based font) object.
        // Defaults to the size 15 Arial font file included in the LibGDX libraries.
        font = new BitmapFont();
        
        // Set style (tinting color for Font).
        style = new LabelStyle( font, Color.YELLOW );
        
        // Set up Label object that will display main menu instructions.
        // Note:  Best practices include avoiding scaling -- use a high-resolution image, instead.
        instructions = new Label( text, style ); // Add text and style to Label.
        instructions.setFontScale(2); // Make font appear larger by using setFontScale method.
        instructions.setPosition(100, 50); // Set coordinates of the Label.
        
        // Set up color pause effect for main menu instructions text.
        instructions.addAction(
          Actions.forever(
            Actions.sequence(
              Actions.color( new Color(1, 1, 0, 1), 0.5f ),
              Actions.delay( 0.5f ),
              Actions.color( new Color(0.5f, 0.5f, 0, 1), 0.5f )
            )));
        
        uiStage.addActor( instructions ); // Add main menu instructions Label to the scene graph.
        
    }
    
    @Override
    public void render(float dt)
    {
        
        /*
        The function occurs during the render phase and accomplishes the following:
        
        1.  Handles key presses -- launches the game (with the mouse and cheese).
        2.  Adjusts Actor positions and other properties.
        3.  Draws the graphics.
        */

        // If user clicked the S key, display the game screen (containing the mouse and cheese). 
        if (Gdx.input.isKeyPressed(Keys.S))
            game.setScreen( new CheeseLevel(game) );
        
        // Call the Actor.act(float) method on each actor in the stages.
        // Typically called each frame.  The method also fires enter and exit events.
        // Updates the position of each Actor based on time.
        uiStage.act(dt);
        
        // Draw graphics.
        
        // Overdraw the area with the given glClearColor.
        Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
        
        // Clear the area using the specified buffer.  Supports multiple buffers.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Draw the stage -- containing the Actors.
        uiStage.draw();
        
    }
    
    @Override
    public void resize(int width, int height)
    {
        
    }
    
    @Override
    public void pause()
    {
        
    }
    
    @Override
    public void resume()
    {
        
    }
    
    @Override
    public void dispose()
    {
        
    }
    
    @Override
    public void show()
    {
        
    }
    
    @Override
    public void hide() 
    {
        
    }
    
}
