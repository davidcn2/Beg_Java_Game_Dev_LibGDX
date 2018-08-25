/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BaseActor extends Actor { // Extends the Actor class from LibGDX.
    
    // The class extends the basic functionality of an Actor class in LibGDX.
    
    // Methods include:
    
    // act:  Overrides the parent / super class act method.  Calls the act method of the
    //       Actor (parent / super) class.  Updates Actor position based on time.
    // draw:  Sets the tinting color of and draws the Actor.
    // getBoundingRectangle:  Sets the properties of the bounding rectangle related to the
    //                        texture region.  Returns results as a Rectangle.
    // 
    // setTexture:  Sets the properties of a texture.
    // getTintColor:  Gets the tint color of the Actor.
    // setTintColor:  Sets the tint color of the Actor.
    // setRandomTintColor:  Sets the tint color of the Actor to a random color.
    
    public TextureRegion region; // Stores image (similar to a buffer from Direct-X).  Includes more
    // functionality than a Texture.  Supports storage of multiple images or animation frames.
    // Stores coordinates (u, v), that determine which rectangular subarea of the Texture to use.
    public Rectangle boundary; // Encapsulates a 2D rectangle defined by its corner point in the
    // bottom left and its extents in x (width) and y (height).  The object will contain the X and Y
    // coordinates and height and width of the texture region.
    public float velocityX; // Speed in pixels per second in the x direcion.
    public float velocityY; // Speed in pixels per second in the y direction.
    private Color tintColor; // Color to tint the Actor.
    private final ColorWorks colorEngine; // Contains color related functionality.
    
    public BaseActor()
    {
        
        // The constructor of the class calls the constructor of the parent (Actor),
        // creates texture region and 2D rectangle objects, sets the x and y
        // velocities to 0 pixels per second, and initializes the color engine.
        
        /*
        Java notes:
        
        1.  A constructor does not have a return type.
        2.  The name of the constructor must be the same as the name of the class.
        3.  Unlike methods, constructors are not considered members of a class.
        4.  A constructor is called automatically when a new instance of an object is created.
        */
        
        super(); // Call the constructor for the Actor (parent / super) class.
        region = new TextureRegion(); // Create a TextureRegion object.
        boundary = new Rectangle(); // Create a 2D rectangle to reflect position and borders of image.
        velocityX = 0; // Set speed in x direction to 0 pixels per second.
        velocityY = 0; // Set speed in y direction to 0 pixels per second.
        
        // Initialize color engine object.
        colorEngine = new ColorWorks();
    
    }
    
    // t = Texture (stores a single image).
    public void setTexture(Texture t)
    {
        
        // The function sets the properties of a texture.
        
        int h; // Height of the passed texture.
        int w; // Width of the passed texture.
        
        // Set texture properties.
        w = t.getWidth(); // Get the width of the passed texture.
        h = t.getHeight(); // Get the height of the passed texture.
        setWidth( w ); // Set the width of the texture region to that of the passed texture.
        setHeight( h ); // Set the height of the texture region to that of the passed texture.
        region.setRegion( t ); // Set the texture region and coordinates to the size of the specified texture.
    
        // Set default color the Actor will be tinted when drawn.
        tintColor = getColor();
        
    }
    
    public Rectangle getBoundingRectangle()
    {
        
        // The function sets the properties of the bounding rectangle related to the texture region.
        // The function returns the bounding rectangle.
        
        // Set the properties of the bounding rectangle related to the texture region.
        // Use the properties of the texture region.
        boundary.set( getX(), getY(), getWidth(), getHeight() );
        
        // Return the bounding rectangle.
        return boundary;
        
    }
    
    // dt = Time in seconds since the last frame.  Also called delta.
    @Override
    public void act(float dt)
    {
        
        // The function calls the act method of the Actor, which performs
        // a time based positional update.  Next, the function moves the
        // actor based on time elapsed and its velocity in each director.
        
        // Calls the act method of the Actor (parent / super) class.
        // Updates the actor based on time.
        super.act ( dt );
        
        // Calculate the distance the Actor has traveled since the last update.
        // Move the Actor based on elapsed time and the distance traveled.
        // Formula:  distance = rate x time.
        moveBy( velocityX * dt, velocityY * dt);
        
    }
    
    // A Batch is used to draw 2D rectangles that reference a texture (region). 
    // The class will batch the drawing commands and optimize them for processing by the GPU. 
    
    // batch = Sprite used to draw image, dependent on and referencing a texture (buffer).
    // parentAlpha = Alpha / transparency to use when drawing image.
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        
        // The function sets the tinting color of and draws the Actor.
        
        // Color c; // Stores the color the Actor will be tinted when drawn.
        
        // Store the color the Actor will be tinted when drawn.
        // The returned instance can be modified to change the color.
        // c = getColor();
        
        // Set the color used to tint images when they are added to the Batch.
        // Default:  Color.WHITE.
        // Set the Color values (red, green, blue, and alpha / transparency) of the
        // Batch object to equal those of the Color stored in the Actor class.
        //batch.setColor(c.r, c.g, c.b, c.a);
        batch.setColor(tintColor.r, tintColor.g, tintColor.b, tintColor.a);
        
        // If the Actor is visible, then...
        if ( isVisible() )
            
            // Actor is visible.
            // Draw the texture, taking into account its position, 
            // origin (center of rotation), width and height, scaling factors, and
            // rotation angle.
            batch.draw( region, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() );
        
    }

    public Color getTintColor() {
        // The function returns the Color used to tint the Actor.
        return tintColor;
    }

    public void setTintColor(Color tintColor) {
        // The function sets the Color used to tint the Actor.
        this.tintColor = tintColor;
    }
    
    public void setRandomTintColor() {
        
        // The function sets a random tint Color for the Actor.
        this.tintColor = colorEngine.getRandomColor();
    
    }
    
}
