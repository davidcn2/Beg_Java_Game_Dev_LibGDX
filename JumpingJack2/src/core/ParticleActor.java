package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

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

public class ParticleActor extends Actor { // Extends the Actor class.
    
    /*
    Detailed class description:

    The class extends the basic functionality of an Actor class in LibGDX to support the addition of particle
    effect functionality.  The particle effect object represents a set of particles controllers.  The object
    can be updated, rendered, or transformed -- meaning that the changes will be applied on all the particle
    controllers.
    
    Methods include:

    load:  Loads all the assets required by the controllers inside the effect.
    start:  Starts the particle effect.
    stop:  Stops the particle effect -- after finishing its current iteration.
    isRunning:  Returns whether the particle effect is still running.
    setPosition:  Sets the position of the particle effect.
    act:  Performs time based positional updates of the actor and particle effect.  Removes the particle effect upon completion when NOT continues.
    draw:  Draws the particle effect.
    clone:  Returns a ParticleActor with the same properties as the current.

    The class extends the basic functionality of a Game class.
    */
    
    private ParticleEffect pe; // ParticleEffect object.
    
    public ParticleActor()
    {
            
        // The constructor of the class:
        
        // 1.  Calls the constructor for the Actor (parent / super) class.
        // 2.  Creates a new ParticleEffect object.
            
        // Call the constructor for the Actor (parent / super) class.
        super();
        
        // Create new ParticleEffect object.
        pe = new ParticleEffect();
        
    }
        
    // pfxFile = Path to pfx file, containing particle effect attributes.
    // imageDirectory = Path to image to use with particle effect.
    public void load(String pfxFile, String imageDirectory)
    {
        
        // The function loads all the assets required by the controllers inside the effect.
        
        // Load pfx and image files used with particle effect.
        System.out.println("Before");
        pe.load(Gdx.files.internal(pfxFile), Gdx.files.internal(imageDirectory));
        System.out.println("After");
        
    }

    public void start()
    {
        
        // The function starts the particle effect.
        
        // Start the particle effect.
        pe.start();
        
    }

    // pauses continuous emitters
    public void stop()
    {
        
        // The function stops the particle effect -- after finishing its current iteration.
        
        // Allow the particle effect to finish.
        pe.allowCompletion();
        
    }

    public boolean isRunning()
    {
    
        // The function returns whether the particle effect is still running.
        
        // Return whether particle effect running.
        return !pe.isComplete();
    
    }

    // px = Horizontal (x) location to place particle effect.
    // py = Vertical (y) location to place particle effect.
    @Override
    public void setPosition(float px, float py)
    {
        
        // The function sets the position of the particle effect.
        
        // Loop through emitters.
        for ( ParticleEmitter e : pe.getEmitters() )
            
            // Set position of emitter to passed values.
            e.setPosition(px, py);
        
    }

    // dt = Time in seconds since the last frame.  Also called delta.
    @Override
    public void act(float dt)
    {
        
        // The function performs time based positional updates of the actor and particle effect.
        // Removes the particle effect upon completion when NOT continues.

        // Calls the act method of the Actor (parent / super) class.
        // Updates the actor based on time.
        super.act( dt );
        
        // Perform a time based positional update on the particle effect. 
        pe.update( dt );

        // If particle effect complete and emitters NOT continuous, then...
        if ( pe.isComplete() && !pe.getEmitters().first().isContinuous() )
            {
            // Particle effect complete and emitters NOT continuous.
                
            // Dispose of the texture for each sprite for each ParticleEmitter.
            pe.dispose();
            
            // Remove the actor from its parent.
            this.remove();
            }
        
    }

    // batch = Sprite used to draw image, dependent on and referencing a texture (buffer).
    // parentAlpha = Alpha / transparency to use when drawing image.
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        // The function draws the particle effect.
        
        // Draw the particle effect.
        pe.draw(batch);
        
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod", "CloneDoesntCallSuperClone"})
    @Override
    public ParticleActor clone()
    {
        
        // The function returns a ParticleActor with the same properties as the current.
        
        ParticleActor newbie; // ParticleActor to which to copy properties.
        
        // Instantiate new ParticleActor object.
        newbie = new ParticleActor();
        
        // Copy properties of current (class-level) to new ParticleActor object.
        newbie.pe = new ParticleEffect(this.pe);
        
        // Return the new ParticleActor object.
        return newbie;
        
    }
    
}