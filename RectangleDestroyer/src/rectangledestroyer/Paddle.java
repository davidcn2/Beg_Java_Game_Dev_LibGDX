package rectangledestroyer;

import core.BaseActor;
import com.badlogic.gdx.math.Rectangle;

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

public class Paddle extends BaseActor { // Extends the BaseActor class.
    
    /*
    The main purpose of the Paddle class involves returning the bounding rectangle for the paddle Actor.
    
    Methods include:
    
    getRectangle:  Returns the bounding rectangle for the Paddle Actor.
    */
    
    public Paddle()
    {

        // The constructor of the class calls the constructor of the parent (BaseActor).

        // Call the constructor for the BaseActor (parent / super) class.
        super();

    }
    
    public Rectangle getRectangle()
    { 

        // The function returns the bounding rectange for the paddle Actor.

        // Return bounding rectangle for the Actor.
        return new Rectangle( getX(), getY(), getWidth(), getHeight() );
        
    }
    
}