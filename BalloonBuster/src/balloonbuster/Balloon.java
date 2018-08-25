/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balloonbuster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import core.*;

/**
 *
 * @author dcohn
 */
public class Balloon extends BaseActor { // Extends the BaseActor class.
    
    // The class extends the basic functionality of a BaseActor class and contains properties and methods
    // used to generate a random balloon and update its position over time.
    
    // Methods include:
    
    // act:  Updates the position of the Balloon Actor and related properties, including time.
    // initBalloon:  Loads the balloon image and sets its horizontal position off-screen.
    
    public float amplitude; // Change amount.
    public float initialY; // Initial Y position of balloon, before factoring in random adjustments.
    public float oscillation; // Regular variation in magnitude or position around a central point.
    public float speed; // Balloon speed.
    public float time; // Time elapsed, in seconds.
    public int offsetX; // Initial X position of balloon.
    
    public Balloon()
    {
        
        // The constructor of the class:
        
        // 1.  Sets positional properties of balloon Actor to follow a sine wave-based path across the screen.
        
        // Calculates the position of each ballon parametrically:  the x and y coordinates are each
        // a function of another variable, time.  As time passes, the x coordinate of the balloon
        // steadily increases, while the y coordinate is calculated according to the formula:  
        // y = A x sin(B * x) + C.
        
        // 2.  Calls the function that loads the balloon image and sets its horizontal position.
        
        // Set defaults.
        time = 0;
        offsetX = -100;
        
        // Set random balloon positional properties.
        speed = MathUtils.random(0.5f, 2.0f) * 80; // x
        amplitude = MathUtils.random(0.5f, 2.0f) * 50; // a
        oscillation = MathUtils.random(0.5f, 2.0f) * 0.01f; // b
        initialY = MathUtils.random(0.5f, 2.0f) * 120; // c
        
        // Calls the function that loads the balloon image and sets its horizontal position.
        initBalloon();
        
    }
    
    public final void initBalloon()
    {
        
        // The function loads the balloon image and sets its horizontal position off-screen.
        
        // Load image (to memory) to use for balloon.
        setTexture( new Texture( Gdx.files.internal("assets/red-balloon.png")) );
        
        // Set horizontal (initial spawn) position of balloon off-screen (defaults to -100).
        setX(offsetX);
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void act(float dt)
    {
        
        // The function updates the position of the Balloon Actor and related properties, including time:
        
        // 1.  Performs a time based positional update.
        // 2.  Updates the elapsed time value.
        // 3.  Sets current position of balloon Actor.
        
        float xPos; // Horizontal position of balloon.
        float yPos; // Vertical position of balloon.
        
        // Call the act method of the Actor, which performs a time based positional update.
        super.act(dt);
        
        // Add seconds since last frame to elapsed time.
        time += dt;
        
        // Set current position of balloon Actor.
        xPos = speed * time + offsetX;
        yPos = amplitude * MathUtils.sin(oscillation * xPos) + initialY;
        setPosition( xPos, yPos );
        
    }      
    
}
