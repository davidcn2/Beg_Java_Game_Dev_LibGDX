package balloonbuster_enhanced;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import core.BaseActor;

public class Balloon extends BaseActor { // Extends the BaseActor class.
    
    // The class extends the basic functionality of a BaseActor class and contains properties and methods
    // used to generate a random balloon and update its position over time.
    
    // Methods include:
    
    // act:  Updates the position of the Balloon Actor and related properties, including time.
    //       If necessary, removes the balloon.
    // initBalloon:  Loads the balloon image, sets its horizontal position off-screen, and tints and
    // scales the graphic to a random color and size.
    // markForRemoval:  Gets called when removing a balloon and starts the countdown timer.
    
    public float amplitude; // Change amount.
    public final float fadeDuration; // Duration of fade out effect when popping balloon.
    public float initialY; // Initial Y position of balloon, before factoring in random adjustments.
    public float oscillation; // Regular variation in magnitude or position around a central point.
    public float speed; // Balloon speed.
    public float startingRemovalTime; // Starting time when balloon removal initiated.
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
        startingRemovalTime = 0;
        offsetX = -100;
        fadeDuration = 0.5f;
        
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
        
        // The function loads the balloon image, sets its horizontal position off-screen, and tints
        // and scales the graphic to a random color and size.
        
        // Load image (to memory) to use for balloon.
        setTexture( new Texture( Gdx.files.internal("assets/gray-balloon.png")) );
        
        // Set horizontal (initial spawn) position of balloon off-screen (defaults to -100).
        setX(offsetX);
        
        // Set tint of balloon image to random color.
        setRandomTintColor();
        
        // Set random scale (sizing) for balloon.
        setScale(MathUtils.random(0.75f, 1.25f));
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void act(float dt)
    {
        
        // The function updates the position of the Balloon Actor and related properties, including time.
        // If necessary, the function removes the balloon.
        
        // 1.  Performs a time based positional update.
        // 2.  Updates the elapsed time value.
        // 3.  Sets current position of balloon Actor.
        // 4.  If balloon removal in process -- remove balloon after specified duration (class constant).
        
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
        
        // If balloon removal in progress, then...
        if (startingRemovalTime > 0)
        
        {
            
            // Balloon removal in progress.
            
            // If difference between elapsed and marked time greater than one second, then...
            if ((time - startingRemovalTime) > fadeDuration)
            {
                // Difference between elapsed and marked time greater than one second.
                
                // Remove balloon.
                remove();
            }
                        
        }
        
    }
    
    public void markForRemoval()
    {
        // The function gets called when removing a balloon and starts the countdown timer.
        startingRemovalTime = time;
    }
    
}
