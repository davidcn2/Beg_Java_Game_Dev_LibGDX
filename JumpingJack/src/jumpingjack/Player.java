package jumpingjack;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.math.Vector2;
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

public class Player extends Box2DActor // Extends the Box2DActor class.
{
    
    /*
    The class stores information about the Player (an extension of the Box2DActor class).
    
    Platformer-style characters have two basic types of movement:
    
    • Moving to the left and right.
    • Jumping.
    
    While moving is relatively straightforward to implement using forces, jumping is surprisingly 
    complicated.  Difficulty arises since the player can jump only when standing on top of a solid
    object.  The Player class includes a method named isOnGround that indicates when the player is 
    standing on top of a solid object.  Implementation involves: 
    
    • Adding a fixture to the player body, set as a sensor and positioned beneath the main fixture. 
    • Contact events will be used to keep track of how many solid objects the sensor is overlapping,
    stored in a variable named groundCount. 
    • When groundCount is greater than 0, the bottom of the player is touching a solid object, and 
    isOnGround will return true.
    
    Methods include:
    
    adjustGroundCount:  Adjusts number of contacts between Player and ground objects by passed parameter.
    initializePhysics:  initializes Box2D properties for the Player and attaches a sensor fixture to
      detect ground contact.
    */
    
    public int groundCount; // Number of solid objects detected beneath player.
    
    public Player()
    {
        
        // The function invokes the constructor of the parent class and sets default values of class-level 
        // variables.
        
        // Invoke constructor of parent class -- Box2DActor -- initializing Box2D related variables for Coin.
        super();
        
        // Set defaults.
        groundCount = 0;
        
    }
    
    // i = Value by which to adjust number of contacts between Player and ground objects.
    public void adjustGroundCount(int i)
    {
        // Adjust number of contacts between Player and ground objects by passed parameter.
        groundCount += i;
    }
    
    // world = Highest level class in Box2D system.  World containing Box2DActor.
    @Override
    public void initializePhysics(World world)
    {
        
        /*
        The function initializes Box2D properties for the Player and attaches a sensor fixture to
        detect ground contact.
        
        Initialization includes:
        
        1.  Initialize Box2D properties -- BodyDef, Body, and Fixture -- related to Player.
        2.  Create and attach sensor fixture to detect whether touching a ground object.
        */
        
        // Declare objects.
        Fixture bottomFixture; // Fixture object associated with bottom of Player body.
        FixtureDef bottomSensor; // FixtureDef objects associated with bottom of Player body.
        PolygonShape sensorShape; // Rectangle acting as sensor box at bottom of Player.
        
        // Declare regular variables.
        float h; // Height of player and sensor box.
        float w; // Width of player -- slightly larger than that of sensor box.
        float x; // X-coordinate of sensor box.
        float y; // Y-coordinate of sensor box.
        
        // Initialize Box2D properties -- BodyDef, Body, and Fixture -- related to Player.
        super.initializePhysics(world);
        
        // Create additional player-specific fixture to detect contact with ground objects.
        bottomSensor = new FixtureDef();
        
        // Specify as a sensor -- will collect contact details, but not generate collisions.
        bottomSensor.isSensor = true;
        
        // Set sensor as a PolygonShape, allowing for a box / rectangle.
        sensorShape = new PolygonShape();
        
        // Center coordinates of sensor box - offset from body center.
        x = 0;
        y = -20;
        
        // Set dimensions of sensor box.
        w = getWidth() - 8;
        h = getHeight();
        
        // Set sensor as rectangle using body center offset distance, basing off 
        // center (divided by 2), and scaling for physics (divided by 100).
        sensorShape.setAsBox( w / 200, h / 200, new Vector2(x / 200, y / 200), 0 );
        
        // Set shape of (sensor) fixture to rectangle with properties specified in function.
        bottomSensor.shape = sensorShape;
        
        // Create and attach the new fixture to the Body.
        bottomFixture = body.createFixture(bottomSensor);
        
        // Insert name of Fixture, allowing for easy identification.
        bottomFixture.setUserData("bottom");
        
    }
    
    public boolean isOnGround()
    {
        return (groundCount > 0);
    }
    
}
