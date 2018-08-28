package core;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

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

public class Box2DActor extends AnimatedActor // Extends the AnimatedActor class.
{
    
    /*
    The main purpose of the Box2DActor class involves using Box2D to simulate a rigid body in 2D.
    
    Methods include:
    
    act:  Commits four actions...
      1.  Performs a time based positional update.
      2.  Updates the elapsed time value -- if animation NOT paused.
      3.  Caps speeds, if maximum values set.
      4.  Updates image properties -- position and rotation -- based on physics data.
    applyForce:  Applies to body a force (for continuous action) to center of mass, waking from sleep 
      if necessary.
    applyImpulse:  Applies to body an impulse at a point (for discrete action), waking from sleep if
      necessary.
    clone:  Returns a Box2DActor with the same properties as the current.
    getBody:  Returns the Body related to the Actor, to ease future removal.
    getSpeed:  Returns the speed of the body.
    getVelocity:  Returns the velocity of the body.
    initializePhysics:  Initializes the Body, based on the BodyDef, and the Fixture (automatically added to 
      the Body).  The function also stores additional data related to the body and fixture.
    setDynamic:  Sets the body as dynamic -- for bodies affected by forces and collisions / which move.
    setFixedRotation:  Sets the body to not rotate.
    setMaxSpeed:  Sets the maximum speed overall for bodies.
    setMaxSpeedX:  Sets the maximum speed in the x-direction for bodies.
    setMaxSpeedY:  Sets the maximum speed in the y-direction for bodies.
    setPhysicsProperties:  Sets properties related to fixture, including density, friction, and restitution.
    setShapeCircle:  Sets the shape of fixture to that of a circle, including properties such as position 
      and radius.
    setShapeRectangle:  Sets shape of fixture to that of a rectangle, including properties such as 
      position, width, and height.
    setSpeed:  Sets the speed of a body.
    setStatic:  Sets the body as static -- for bodies not intended to be affected by forces or collisions 
      and which do not move.
    setVelocity:  Sets the velocity of a body.
    
    ...
    
    The World class, the highest level in the Box2D system, manages all the physics entities, 
    performing the calculations for the simulation, and reporting all the collision events.
    
    Each physics entity managed by the World object is a Body.  The overall properties of each Body
    are set using a BodyDef object.  The individual parts of the BodyDef are represented using 
    Fixture objects.
    
    The BodyDef can be used to store the following:
    
    • The initial position and angle of the body.
    
    • The initial linear velocity (which represents a change in position) and angular velocity 
    (which represents the rate of rotation).
    
    • Damping values (which, if set, will gradually decrease the linear and angular velocity
    over time)
    
    • The type of object: 
    Static --> For bodies not intended to be affected by forces or collisions and which do not move.
    Examples:  ground, walls.
    Dynamic --> For bodies affected by forces and collisions / which move.
    
    • A Boolean value that indicates whether the body can rotate (which defaults to true,
    but is typically set to false for player objects and other types of objects that should
    not tip over or spin).
    
    A Fixture, which represents a physical part of the associated Body, is initialized using a FixtureDef
    object that stores the following information:
    
    • The physical shape of the object, which can be a circle (via the CircleShape class), a
    polygon (via the PolygonShape class), or a rectangle (which is implemented via the
    setAsBox method of the PolygonShape class).
    
    • The density of the object, which is used to calculate the mass of the object (equal to
    the product of the area, calculated from the shape, and the density). Typically, the
    greater the density, the greater the mass, and the less of an effect forces will have
    when applied to this object. Generally, a density value of 1.0 should be used as a
    baseline, and thought of as having the same density as water. Heavier objects will
    have a greater density; lighter objects will have a lesser density.
    
    • The friction of the object, which is used to calculate an opposing force when two
    objects slide across each other. A value of 0 represents a perfectly slippery surface
    with no friction; the velocity of the two objects sliding across each other will not be
    affected at all. A value of 1 represents high friction; the speed of the two objects will
    be greatly decreased while they are in contact.
    
    • The restitution of the object, which is used to measure the "bounciness" of an object
    in response to a collision. A value of 0 indicates that there will be no bounce at all
    after a collision, while a value of 1 indicates that the object will bounce all the way
    back up to the original height from which the object initially fell.
    */
    
    // Declare objects.
    protected Body body; // Physics entity within world.  Properties set using BodyDef object.
    protected BodyDef bodyDef;
      // Used to initialize body.  Reusable.
      // Body definition data:  position, angle, linearVelocity, angularVelocity, type (static, dynamic),
      // fixedRotation (can this object rotate?).
    protected FixtureDef fixtureDef; 
      // Fixture definition - used to initialize fixture.
      // Fixture data: shape, density, friction, restitution (0 to 1).
      // *** Weight is calculated via density * area.
      // Fixture - attached to body.
    
    // Declare regular variables.
    protected Float maxSpeed; // Maximum overall speed.
    protected Float maxSpeedX; // Maximum speed allowed in the x direction.
    protected Float maxSpeedY; // Maximum speed allowed in the y direction.
    
    public Box2DActor()
    {
        
        // The constructor initializes the class-level variables. 
        
        // Initialize class-level variables.
        
        // 1.  Body and related objects used to define its properties.
        body = null;
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        
        // 2.  Caps on overall speed.
        maxSpeed = null;
        maxSpeedX = null;
        maxSpeedY = null;
        
    }
    
    public void setDynamic()
    {
        // The function sets the body as dynamic -- for bodies affected by forces and collisions / which move.
        bodyDef.type = BodyType.DynamicBody;
    }
    
    public void setFixedRotation()
    {
        // The function sets the body to not rotate.
        bodyDef.fixedRotation = true;
    }
    
    public void setStatic()
    {
        // The function sets the body as static -- for bodies not intended to be affected by forces or 
        // collisions and which do not move.
        bodyDef.type = BodyType.StaticBody;
    }
    
    /*
    The next methods relate to the fixtures of the body: methods that set the shape to a circle or a
    rectangle, and a method to set the density, friction, and restitution all at once.  Note that the 
    pixel dimensions must be scaled to those of the physics.  Also, note that the body positions are set
    to the center of the shape.  For example, the dimensions of a rectangle are specified using 
    distances from the center -- half the total width and length, similar to how the radius of a circle 
    indicates distance from the center to the boundary.
    */
    
    public void setShapeRectangle()
    {
        // The function sets the shape of the fixture to that of a rectangle, including properties such 
        // as position, width, and height.
        
        PolygonShape rect; // Shape object in the form of a rectangle.
        
        // Set origin to center.
        setOriginCenter();
        
        // Center position of rectangle.
        bodyDef.position.set( (getX() + getOriginX()) / 100, (getY() + getOriginY()) / 100 );
        
        // Initialize shape.
        rect = new PolygonShape();
        
        // Set as rectangle using distance from center (width divided by 2) and scaling for physics 
        // (divided by 100).
        rect.setAsBox( getWidth() / 200, getHeight() / 200 );
        
        // Set shape of fixture to rectangle with properties specified in function.
        fixtureDef.shape = rect;
    }
    
    public void setShapeCircle()
    {
        // The function sets the shape of the fixture to that of a circle, including properties such as 
        // position and radius.
        
        CircleShape circ; // Shape object in the form of a circle.
        
        // Set origin to center.
        setOriginCenter();
        
        // Center position of circle.
        bodyDef.position.set( (getX() + getOriginX()) / 100, (getY() + getOriginY())/100 );
        
        // Initialize as a circle.
        circ = new CircleShape();
        
        // Set as circle using distance from center (width divided by 2) and scaling for physics 
        // (divided by 100).
        circ.setRadius( getWidth() / 200 );
        
        // Set shape of fixture to rectangle with properties specified in function.
        fixtureDef.shape = circ;
    }
    
    // density:  Mass per square metre.  Recommend a value of 1.0 as a baseline (similar to water).
    // friction:  Used to calculate an opposing force when two objects slide across each other.
    //   A value of 0 represents a perfectly slippery surface with no friction; the velocity of the 
    //   two objects sliding across each other will not be affected at all.  A value of 1 represents 
    //   high friction; the speed of the two objects will be greatly decreased while they are in contact.
    // restitution:  Used to measure the "bounciness" of an object in response to a collision.  A value
    //   of 0 indicates that there will be no bounce at all after a collision.  A value of 1 indicates 
    //   that the object will bounce all the way back up to the original height from which the object 
    //   initially fell.
    public void setPhysicsProperties(float density, float friction, float restitution)
    {
        // The function sets properties related to the fixture, including density, friction, and restitution.
        
        // Set properties of fixture.
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
    }
    
    // --------------------------------------------------------------------------------------------------
    
    // dt = Time in seconds since the last frame.  Also called delta.
    @Override
    public void act(float dt)
    {
        
        // The function:

        // 1.  Performs a time based positional update.
        // 2.  Updates the elapsed time value -- if animation NOT paused.
        // 3.  Caps speeds, if maximum values set.
        // 4.  Updates image properties -- position and rotation -- based on physics data.
        
        float a; // Current angle of body, in radians.
        float s; // Current speed of body.
        Vector2 center; // World position of center of mass.
        Vector2 v; // Current velocity of body, as a vector.
        
        // Call the act method of the AnimatedActor, which performs a time based positional update.
        super.act(dt);
        
        // Cap speeds, if maximum values set.
        
        // If maximum value set for speed in x-direction, then...
        if (maxSpeedX != null)
            {
            // Maximum value set for speed in x-direction.
            v = getVelocity(); // Get current velocity of body, as a vector.
            v.x = MathUtils.clamp(v.x, -maxSpeedX, maxSpeedX); // Get adjusted speed of body, limiting to max.
            setVelocity(v); // Set body to adjusted speed.
            }
        
        // If maximum value set for speed in y-direction, then...
        if (maxSpeedY != null)
            {
            // Maximum value set for speed in y-direction.
            v = getVelocity(); // Get current velocity of body, as a vector.
            v.y = MathUtils.clamp(v.y, -maxSpeedY, maxSpeedY); // Get adjusted speed of body, limiting to max.
            setVelocity(v); // Set body to adjusted speed.
            }
        
        // If maximum value set for overall speed, then...
        if (maxSpeed != null)
            {
            // Maximum value set for overall speed.
            s = getSpeed(); // Get current speed of body.
            
            // If speed of body greater than maximum allowed, then...
            if (s > maxSpeed)
                // Set speed of body to maximum allowed.
                setSpeed(maxSpeed);
            }
        
        // Update image data -- position and rotation -- based on physics data.
        
        // Get world position of center of mass.
        center = body.getWorldCenter();
        
        // Set position of Actor, scaling from Physics to pixel units.
        setPosition( 100 * center.x - getOriginX(), 100 * center.y - getOriginY() );
        
        // Get current angle of body, in radians.
        a = body.getAngle();
        
        // Set rotation of Actor, converting from radians to degrees.
        setRotation( a * MathUtils.radiansToDegrees );
        
    }
    
    /*
    The movement of the body can be affected by applying either a force (for a continuous action) or 
    an impulse (for a discrete action).  In either case, the force should be applied to the center 
    of the body to avoid spinning the object.
    
    Objects exist in two states, either awake or asleep.
    Sleep allows for improved performance and ignoring unnecessary objects.
    */
    
    // force = The world force vector, usually in Newtons (N).
    public void applyForce(Vector2 force)
    {
        // Apply to body a force (for continuous action) to center of mass, waking from sleep if necessary.
        body.applyForceToCenter(force, true);
    }

    // impulse = The world impulse vector, usually in Newtons (N).
    public void applyImpulse(Vector2 impulse)
    {
        // Apply to body an impulse at a point (for discrete action), waking from sleep if necessary.
        // Apply an impulse at a point, immediately modifying the velocity.  The impulse
        // also modifies the angular velocity if the point of application is not at the 
        // center of mass.
        body.applyLinearImpulse(impulse, body.getPosition(), true);
    }
    
    @SuppressWarnings({"MethodDoesntCallSuperMethod", "CloneDoesntCallSuperClone"})
    @Override
    public Box2DActor clone()
    {
        
        // The function returns a Box2DActor with the same properties as the current.
        // Only the information from the AnimatedActor class is duplicated, because copies of a given 
        // object will likely have different starting positions, which affects the initialization of the Body.
        
        Box2DActor newbie; // Box2DActor to which to copy properties.
        
        // Instantiate new Box2DActor object.
        newbie = new Box2DActor();
        
        // Copy properties of current (class-level) to new Box2DActor object.
        // The copy method only copies the AnimatedActor data.
        newbie.copy( this );
        
        // Return the new Box2DActor object.
        return newbie;
        
    }
    
    public Body getBody()
    {
        // The function returns the Body related to the Actor, to ease future removal.
        return body;
    }
    
    public float getSpeed()
    {
        // The function returns the speed of the body.
        return getVelocity().len();
    }
    
    public Vector2 getVelocity()
    {
        // The function returns the velocity of the body.
        return body.getLinearVelocity();
    }
    
    // w = Highest level class in Box2D system.  World containing Box2DActor.
    public void initializePhysics(World w)
    {
        
        /*
        The function initializes the Body, based on the BodyDef, and the Fixture (automatically added to 
        the Body).  The function also stores additional data related to the body and fixture.
        Initializing the body results in automatic addition to the World.
        */
        
        Fixture f; // Fixture object automatically added to body.
        
        // Initialize the Body based on the BodyDef object.
        body = w.createBody(bodyDef);
        
        // Initialize the Fixture (automatically added to the Body).
        f = body.createFixture(fixtureDef);
        
        // Insert additional data in Fixture.
        
        // Insert name of Fixture, allowing for easy identification later when multiple fixtures exist.
        f.setUserData("main");
        
        // Insert additional data in Body.
        
        // Add reference to the current Box2DActor to ease future collision-detection.
        body.setUserData(this);
        
    }
    
    // f = Maximum speed to allow overall for bodies.
    public void setMaxSpeed(float f)
    {
        // The function sets the maximum overall speed of bodies.
        maxSpeed = f;
    }
    
    // f = Maximum speed to allow in x direction for bodies.
    public void setMaxSpeedX(float f)
    {
        // The function sets the maximum speed in the x direction of bodies.
        maxSpeedX = f;
    }
    
    // f = Maximum speed to allow in y direction for bodies.
    public void setMaxSpeedY(float f)
    {
        // The function sets the maximum speed in the y direction of bodies.
        maxSpeedY = f;
    }
    
    // s = Speed at which to set body.
    public void setSpeed(float s)
    {
        // The function sets the speed of a body.
        setVelocity( getVelocity().setLength(s) );
    }
    
    // vx = X-component of vector to use when setting velocity of a body.
    // vy = Y-component of vector to use when setting velocity of a body.
    public void setVelocity(float vx, float vy)
    {
        // The function sets the velocity of a body using x and y components of vectors (separately passed).
        body.setLinearVelocity( vx, vy );
    }

    // v = Vector to use when setting velocity of a body.
    public void setVelocity(Vector2 v)
    {
        // The function sets the velocity of a body using the passed vector.
        body.setLinearVelocity( v );
    }
    
}