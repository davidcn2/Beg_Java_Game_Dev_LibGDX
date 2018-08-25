package rectangledestroyer;

import core.BaseActor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

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

public class Brick extends BaseActor { // Extends the BaseActor class.
    
    /*
    The main purpose of the Paddle class involves handling logic related to Brick Actors.
    
    Methods include:
    
    clone:  Returns a Brick Actor with the same properties as the current.
    destroy:  Removes the Brick Actor from the screen and array, parentList.
    getRectangle:  Returns the bounding rectangle for the Brick Actor.
    */
    
    public Brick()
    {

        // The constructor of the class calls the constructor of the parent (BaseActor).

        // Call the constructor for the BaseActor (parent / super) class.
        super();

    }
    
    @SuppressWarnings({"MethodDoesntCallSuperMethod", "CloneDoesntCallSuperClone"})
    @Override
    public Brick clone()
    {
        
        // The function returns a Brick Actor with the same properties as the current.
        
        Brick newbie; // Brick Actor to which to copy properties.
        
        // Instantiate new Brick object.
        newbie = new Brick();
        
        // Copy properties of current (class-level) to new Brick object.
        newbie.copy( this );
        
        // Return the new Brick object.
        return newbie;
        
    }
    
    @Override
    public void destroy()
    {
    
        // The function removes the Brick Actor from the screen and array, parentList.
        
        // Set up action to remove the Brick Actor from the screen after 0.5 secnods, using a fade out effect.
        addAction( Actions.sequence( Actions.fadeOut(0.5f), Actions.removeActor() ) );
        
        // If BaseActor array exists, then...
        if (parentList != null)
        parentList.remove(this); // BaseActor array exists.  Remove Brick BaseActor from array.
        
    }
    
    public Rectangle getRectangle()
    { 

        // The function returns the bounding rectange for the brick Actor.

        // Return bounding rectangle for the Actor.
        return new Rectangle( getX(), getY(), getWidth(), getHeight() );
        
    }
    
}