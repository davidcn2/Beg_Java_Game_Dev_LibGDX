/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cheeselauncher;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author dcohn
 */
public class AnimatedActor extends BaseActor { // Extends the BaseActor class.
    
    // An Animation stores a list of TextureRegions representing an animated sequence, 
    // e.g. for running or jumping.  Each region of an Animation is called a key frame, 
    // multiple key frames make up the animation. 
    
    public float elapsedTime; // Total elapsed time the animation has been playing.
    public Animation anim; // Animation object.
    
    public AnimatedActor()
    {
        
        // The constructor of the class calls the constructor of the parent (BaseActor)
        // and initializes the elapsed time.
        
        super(); // Call the constructor for the BaseActor (parent / super) class.
        elapsedTime = 0; // Initialized elapsed time to 0.
    
    }
    
    public void setAnimation(Animation a)
    {
        
        // The function creates a texture with the first animation frame and updates
        // the public Animation object (anim) to the one passed.
        
        // Create a texture with the first animation frame.
        Texture t = a.getKeyFrame(0).getTexture();
        setTexture( t );
        
        // Set animation properties.
        anim = a;
    
    }
    
    // dt = Time in seconds since the last frame.  Also called delta.
    @Override
    public void act(float dt)
    {
        
        // The function:
        // 1.  Performs a time based positional update.
        // 2.  Updates the elapsed time value.
        // 3.  Sets rotation of the Actor texture to match the directiom of the movement.
        
        // Call the act method of the Actor, which performs a time based positional update.
        super.act( dt );
        
        // Add seconds since last frame to elapsed time.
        elapsedTime += dt;
        
        // If speed in either direction greater than zero pixels per second, then...
        if (velocityX > 0 || velocityY > 0)
            
            // Speed in x or y direction greater than zero pixels per second.
            
            // Set rotation of the Actor texture to match the directiom of the movement.
            // Calculates the value based on velocity, an arctangent function, and
            // converting from radians to degrees.
            setRotation( MathUtils.atan2( velocityY, velocityX ) * MathUtils.radiansToDegrees );
    
    }
    
    // A Batch is used to draw 2D rectangles that reference a texture (region). 
    // The class will batch the drawing commands and optimize them for processing by the GPU. 
    
    // batch = Sprite used to draw image, dependent on and referencing a texture (buffer).
    // parentAlpha = Alpha / transparency to use when drawing image.
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
    
        // The function updates and draws the image using an animation key frame based on the elapsed time.
        
        // Use the getKeyFrame method of the Animation class to retrieve the correct image based
        // on the current elapsed time.
        // Set the texture region and coordinates to the the specified texture (the animation frame).
        region.setRegion( anim.getKeyFrame(elapsedTime) );
        
        // Set the tinting color of and draw the Actor.
        super.draw(batch, parentAlpha);
        
    }
    
}
