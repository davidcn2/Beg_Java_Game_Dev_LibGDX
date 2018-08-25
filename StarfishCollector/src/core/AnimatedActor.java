package core;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import java.util.HashMap;

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

ArrayList supports dynamic arrays that can grow as needed.
*/

public class AnimatedActor extends BaseActor { // Extends the BaseActor class.
    
    /*
    The main purpose of the AnimatedActor class involves managing a set of animations,
    and selecting the correct image from the active animation.  Active refers here
    to the animation currently being rendered.
    
    An Animation stores a list of TextureRegions representing an animated sequence, 
    e.g. for running or jumping.  Each region of an Animation is called a key frame, 
    multiple key frames make up the animation. 
    
    A HashMap data structure will get used to store animations associated with actors.
    String objects will get used as keys, and Animation objects will be the associated
    values.  For example, in a top-view adventure game, the main character might have
    four animations named north, south, east, and west.
    */

    // Methods include:
    
    // act:  Performs a time based positional update and updates the elapsed time value.
    // draw:  Updates and draws the image for the active animation using a key frame based on the
    //        elapsed time.
    // getAnimationName:  Returns the key for the active Animation object in the hash map.
    // setActiveAnimation:  Sets the active Animation (key and object) using the passed key.
    // storeAnimation:  Adds an Animation object to the hash map using the specified key.
    
    private float elapsedTime; // Total elapsed time the animation has been playing.
    private Animation activeAnim; // Current (active) Animation object.
    private String activeName; // Name / key value for current (active) Animation object.
    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<String,Animation> animationStorage; // HashMap data structure storing Animation
      // objects and associated keys.
    
    public AnimatedActor()
    {
        
        // The constructor of the class calls the constructor of the parent (BaseActor)
        // and initializes the elapsed time, current (active) Animation object and related key, 
        // and the Animation object hash map.
        
        super(); // Call the constructor for the BaseActor (parent / super) class.
        elapsedTime = 0; // Initialized elapsed time to 0.
        activeAnim = null; // Initialize current (active) Animation object.
        activeName = null; // Initialize key representing the current (active) Animation to no selection.
        // animationStorage = new HashMap<String,Animation>(); // Create hash map to contain Animation objects.
        animationStorage = new HashMap<>(); // Create hash map to contain Animation objects.
        
        
    }
    
    // name = Key value to associate with Animation object.
    // anim = Animation object to add to hash map.
    public void storeAnimation(String name, Animation anim)
    {
        
        // The method stores the passed Animation object in the hash map.
        // The method uses the passed key when adding the object to the hash map.
        // If no active animation exists, the method sets the passed one as active.
        
        // Add the passed Animation object to the hash map using the provided key.
        animationStorage.put(name, anim);
        
        // If no Animation object key set as current, then...
        if (activeName == null)
            
            // No animation object key set as current.
            // Set the passed Animation object as current.
            setActiveAnimation(name);
    
    }
    
    // name = Key value to associate with Animation object.
    // tex = Texture (stores a single image) to use as starting point to create Animation object.
    public void storeAnimation(String name, Texture tex)
    {
        
        // The method builds an Animation object using the passed Texture and adds the results to
        // the hash map.
        // The method uses the passed key when adding the object to the hash map.
        // If no active animation exists, the method sets the passed one as active. 
        
        Animation anim; // Animation object created using passed Texture.
        TextureRegion reg; // Stores image (similar to a buffer from Direct-X).  Includes more 
          // functionality than a Texture.  Supports storage of multiple images or animation frames.
          // The TextureRegion here will get built using the passed Texture object.
        
        // Create a TextureRegion object using the passed Texture.
        reg = new TextureRegion(tex);
        
        // Create an array of TextureRegion objects out of the TextureRegion, to support conversion to an
        // Animation.
        TextureRegion[] frames = { reg };
        
        // Create an Animation out of the array of TextureRegion objects, using one second as the delay
        // between frames.
        anim = new Animation(1.0f, frames);
        
        // Stored Animation object in hash map using passed key value.
        storeAnimation(name, anim);
        
        // If no Animation object key set as current, then...
        if (activeName == null)
            
            // No animation object key set as current.
            // Set the passed Animation object as current.
            setActiveAnimation(name);
    
    }
    
    // name = Key for the Animation object to set as active in the hash map.
    public void setActiveAnimation(String name)
    {
    
        // The method sets the active Animation (key and object) using the passed key.
        // The method also resets the elapsed time and updates the width and height of
        // the related Actor to that of the Animation.
        
        Texture tex; // Texture containing first animation frame of active Animation -- after setting
                     // using key.
        
        // If hash map contains passed key, then...
        if ( animationStorage.containsKey(name) )
            
            {
            // Hash map contains passed key.
                    
            // Set the active Animation key using the passed value.
            activeName = name;

            // Set the active Animation Object using the passed key.
            activeAnim = animationStorage.get(name);

            // Reset elapsed time related to animation.
            elapsedTime = 0;

            // Create a Texture with the first animation frame.
            tex = activeAnim.getKeyFrame(0).getTexture();

            // Set width and height of related Actor to that of Texture.
            setWidth( tex.getWidth() );
            setHeight( tex.getHeight() );
            }
        
        else
            
            {
            // Hash map does NOT contain passed key.
                
            // Display that no animation exists in the hash map with the passed name.
            System.out.println("No animation: " + name);
            }
        
    }
    
    public String getAnimationName()
    {
    // The function returns the key for the active Animation object in the hash map.
    return activeName;
    }

    // dt = Time in seconds since the last frame.  Also called delta.
    @Override
    public void act(float dt)
    {
        
        // The function:
        
        // 1.  Performs a time based positional update.
        // 2.  Updates the elapsed time value.
        
        // Call the act method of the Actor, which performs a time based positional update.
        super.act( dt );
    
        // Add seconds since last frame to elapsed time.
        elapsedTime += dt;
        
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        
        // The function updates and draws the image for the active Animation using a key frame based on
        // the elapsed time.
        
        // Using the current (active) Animation object in the hash map...
        // Use the getKeyFrame method of the Animation class to retrieve the correct image based
        // on the current elapsed time.
        // Set the texture region and coordinates to the the specified texture (the animation frame).
        region.setRegion( activeAnim.getKeyFrame(elapsedTime) );
        
        // Set the tinting color of and draw the Actor.
        super.draw(batch, parentAlpha);
        
    }
    
}