package rectangledestroyer;

import core.PhysicsActor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Intersector;
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

public class Powerup extends PhysicsActor { // Extends the PhysicsActor class.
    
    /*
    The class handles functionality related to the Powerup Actors.
    
    When a brick is destroyed, it may occasionally spawn a random item that falls toward the bottom of the
    screen.  If the player collects the item (by touching it with the paddle), some feature of the game will 
    be changed, such as the size of the paddle.  We will refer to these items as power-ups, even though 
    their effect may increase the difficulty of the game.
    
    Methods include:
    
    clone:  Returns a Powerup Actor with the same properties as the current.
    getRectangle:  Returns the bounding rectangle for the Powerup Actor.
    overlaps:  Returns whether bounding rectangle for powerup and paddle intersect.
    randomize:  Randomly selects one of the stored animations to use as the powerup.
    */
    
    public Powerup()
    {
        
        // The constructor of the class calls the constructor of the parent (PhysicsActor).

        // Call the constructor for the PhysicsActor (parent / super) class.
        super();
        
    }
    
    @SuppressWarnings({"MethodDoesntCallSuperMethod", "CloneDoesntCallSuperClone"})
    @Override
    public Powerup clone()
    {
        
        // The function returns a Brick Actor with the same properties as the current.
        
        Powerup newbie; // Brick Actor to which to copy properties.
        
        // Instantiate new Powerup object.
        newbie = new Powerup();
        
        // Copy properties of current (class-level) to new Brick object.
        newbie.copy( this );
        
        // Return the new Brick object.
        return newbie;
        
    }
    
    public Rectangle getRectangle()
    { 

        // The function returns the bounding rectange for the powerup Actor.

        // Return bounding rectangle for the Actor.
        return new Rectangle( getX(), getY(), getWidth(), getHeight() );
        
    }
    
    // other = Reference to paddle used in checking for intersection with powerup.
    public boolean overlaps(Paddle other)
    {
        // Returns whether bounding rectangle for powerup and paddle intersect.
        return Intersector.overlaps( this.getRectangle(), other.getRectangle() );
    }
    
    public void randomize()
    {
        
        // The function randomly selects one of the stored animations to use as the powerup.
        
        ArrayList<String> names; // Array with keys of animations.
        int n; // Random number between 0 and number of animations (-1).
        
        // Store array list with animation keys (from hash map).
        // names = new ArrayList<String>( animationStorage.keySet() );
        names = new ArrayList<>( animationStorage.keySet() );
        
        // Generate random number between 0 and the number of animations (-1).
        n = MathUtils.random( names.size() - 1 );
        
        // Set the active Animation (key and object) using the passed key.
        setActiveAnimation( names.get(n) );
        
    }
    
}