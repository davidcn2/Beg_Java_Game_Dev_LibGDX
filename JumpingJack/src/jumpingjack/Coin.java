package jumpingjack;

import com.badlogic.gdx.physics.box2d.World;
import core.Box2DActor;

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
*/

public class Coin extends Box2DActor // Extends the Box2DActor class.
{

    /*
    The class stores information about a Coin (an extension of the Box2DActor class).
    
    Methods include:
    
    clone:  Returns a Coin with the same properties as the current.
    initializePhysics:  Initializes Box2D properties for the Coin.
    */
    
    public Coin()
    {
        // Invoke constructor of parent class -- Box2DActor -- initializing Box2D related variables for Coin.
        super();
    }

    // world = Highest level class in Box2D system.  World containing Box2DActor.
    @Override
    public void initializePhysics(World world)
    {
        // The function initializes Box2D properties for the Coin.
        
        setStatic(); // Set the Coin as a static object (not intended to be affected by forces / collisions).
        setShapeCircle(); // Set the Coin as a circle.
        fixtureDef.isSensor = true; // Set the Coin as a Sensor (to collect contact details, but not 
        // generate a collision).
        
        // Initializes the Body of the Coin, based on the BodyDef, and the Fixture (automatically added to 
        // the Body).  The function also stores additional data related to the body and fixture.
        super.initializePhysics(world);
    }
    
    @SuppressWarnings({"MethodDoesntCallSuperMethod", "CloneDoesntCallSuperClone"})
    @Override
    public Coin clone()
    {
        
        // The function returns a Coin with the same properties as the current.
        // Only the information from the AnimatedActor class is duplicated, because copies of a given 
        // object will likely have different starting positions, which affects the initialization of the Body.
        
        Coin newbie; // Coin to which to copy properties.
        
        // Instantiate new Box2DActor object.
        newbie = new Coin();
        
        // Copy properties of current (class-level) to new Coin object.
        // The copy method only copies the AnimatedActor data.
        newbie.copy( this );
        
        // Return the new Coin object.
        return newbie;
        
    }
    
}
