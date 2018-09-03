package core;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import routines.ArrayRoutines;

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

public class BaseActor3D 
{
    
    /* The class simulates the basic functionality of an Actor class in LibGDX, but in a 3D environment.

    The transformation data for a ModelInstance object is stored in its transform field as a Matrix4 object:
    a four-by-four grid of numbers.  From this object, a Vector3 can be extracted that contains the position 
    of the object.  Another Vector3 can be extracted that contains the scaling factor in each direction 
    (initialized to 1 in all directions, which results in no change in the default size).  The transformation 
    also stores the orientation of the model, which cannot be stored with a single number (in contrast to the 
    rotation value of an Actor), because an object in three-dimensional space can be rotated any amount around
    any combination of the x, y, and z axes.  For many technical reasons (such as computation, performance,
    and avoiding a phenomena known as gimbal lock2), an object called a Quaternion (corresponding to a
    mathematical object of the same name) is used to store orientation data.  For convenience, rather than 
    work with the Matrix4 directly, separate objects store the position, rotation, and scale data for
    each BaseActor3D object.  The objects will be combined into a Matrix4 and stored in the ModelInstance when 
    needed.
    
    Methods include:

    act:  Updates the transformation data of the model instance.
    addPosition:  Adds the passed vector or combination of coordinates (x, y, z) to the current position.
    calculateTransform:  Combines the position, rotation, and scale data into a Matrix4 object.
    clone:  Returns a BaseActor3D with the same properties as the current.
    copy:  Copies properties from the passed to the current BaseActor3D.
    draw:  Renders the model instance using the supplied ModelBatch and Environment.
    getBoundingPolygon:
    getPosition:  Returns the position of the 3D actor -- as a vector.
    getTurnAngle:  Returns the angle in radians of the rotation around the Y axis.
    moveForward:  Moves the 3D actor forward by the specified amount.
    moveRight:  Moves the 3D actor right by the specified amount.
    moveUp:  Moves the 3D actor up by the specified amount.
    overlaps:
    setColor:  Changes the color of all materials belonging to the model instance to the passed value.
    setEllipseBase:
    setModelInstance:  Sets the model instance to the passed object.
    setPosition:  Uses a vector parameter or combination of coordinates (x, y, z) to set the position of
      the 3D actor.
    setRectangleBase:
    setTurnAngle:  Sets the angle in radians of the rotation around the Y axis.
    turn:  Adjusts the angle in radians of the rotation around the Y axis by the passed value.
    */
    
    // Declare object variables.
    private Polygon boundingPolygon; // Bounding polygon for the BaseActor3D -- supports collision detection.
    private ModelInstance modelData; // Instance of the model object, in addition to a
      // transformation matrix with position, rotation, and scaling data.
    private final Vector3 position; // Position, stored as a vector (x, y, and z components).
    private final Quaternion rotation; // Rotation of the model instance.
    private final Vector3 scale; // Scale, stored as a vector (x, y, and z components).
    
    public BaseActor3D()
    {
        
        /*
        The constructor of the class initializes the model instance, position, rotatio, scale, and
        bounding polygon.  The model instance and bounding polygon are actually set to null.
        */
        
        modelData = null;
        position  = new Vector3( 0, 0, 0 );
        rotation  = new Quaternion();
        scale     = new Vector3( 1, 1, 1 );
        boundingPolygon = null;
        
    }

    // dt = Time in seconds since the last frame.  Also called delta.
    public void act(float dt)
    {
        // The function updates the transformation data of the model instance.
        modelData.transform.set( calculateTransform() );   
    }
    
    // v = Vector to add to current position.
    public void addPosition(Vector3 v)
    {
        // The function adds the passed vector to the current position.
        position.add( v );  
    }
    
    // x = X-coordinate to add to current position.
    // y = Y-coordinate to add to current position.
    // z = Z-coordinate to add to current position.
    public void addPosition(float x, float y, float z)
    {
        // The function add the passed combination of coordinate parameters (x, y, and z)
        // to the current position.
        addPosition( new Vector3(x, y, z) );  
    }
    
    public Matrix4 calculateTransform()
    {
        // The method combines the position, rotation, and scale data into a Matrix4 object.
        
        // Return a Matrix4 object based on the position, rotation, and scale of the model instance.
        return new Matrix4( position, rotation, scale );
    }
    
    @SuppressWarnings({"MethodDoesntCallSuperMethod", "CloneDoesntCallSuperClone", "CloneDeclaresCloneNotSupported"})
    @Override
    public BaseActor3D clone()
    {
        
        // The function returns a BaseActor3D with the same properties as the current.
        
        BaseActor3D newbie; // BaseActor3D to which to copy properties.
        
        // Instantiate new BaseActor3D object.
        newbie = new BaseActor3D();
        
        // Copy properties of current (class-level) to new BaseActor3D object.
        newbie.copy(this);
        
        // Return the new BaseActor3D object.
        return newbie;
        
    }
    
    // orig = BaseActor3D from which to copy properties.
    public void copy(BaseActor3D orig)
    {
        
        // The function copies properties from the passed to the current BaseActor3D.
        // Properties include:  model instance, position, rotation, scale, and
        // bounding polygon.
        
        // Copy model instance from passed to current BaseActor3D.
        this.modelData = new ModelInstance(orig.modelData);
        
        // Copy position, rotation, and scale from passed to current BaseActor3D.
        this.position.set( orig.position );
        this.rotation.set( orig.rotation );
        this.scale.set( orig.scale );
        
        // If bounding polygon exists in passed BaseActor3D, then...
        if (orig.boundingPolygon != null)
            // Bounding polygon exists in passed BaseActor3D.
            // Create bounding polygon in current with vertices in bounding polygon of passed BaseActor3D.
            this.boundingPolygon = new Polygon( orig.boundingPolygon.getVertices() );
        
    }
    
    // batch = Used to render three-dimensional objects.
    // env = Contains the uniform values specific for a location.
    public void draw(ModelBatch batch, Environment env)
    {
        // The function renders the model instance using the supplied ModelBatch and Environment.
        batch.render( modelData, env );    
    }
    
    public Polygon getBoundingPolygon()
    {          
        boundingPolygon.setPosition( position.x, position.z );
        boundingPolygon.setRotation( getTurnAngle() );
        return boundingPolygon;
    }
    
    public Vector3 getPosition()
    {
        // The function returns the position of the 3D actor -- as a vector.
        return position;
    }
    
    public float getTurnAngle()
    {   
        // The function returns the angle in radians of the rotation around the Y axis.
        return rotation.getAngleAround( 0, -1, 0 );
    }
    
    /*
    Movement Information:
    
    When a BaseActor3D is first initialized, it will be assumed that the forward direction is
    represented by the vector (0, 0, –1).  The vector (0, 0, -1) can be assumed since the
    initial position of the camera will have a positive z coordinate, and the actor will be 
    facing away from the camera.  Similarly, the initial upward direction is the vector 
    (0, 1, 0), and the rightward direction is the vector (1, 0, 0).  After the actor has been 
    rotated, the relative forward, upward, and rightward directions can be determined by 
    transforming these original vectors by the actor's current rotation.  Then, to move a given 
    distance in one of these relative directions, scale the corresponding vector by the desired 
    distance, and add the result to the current position.
    */
    
    // dist = Distance by which to move forward.
    public void moveForward(float dist)
    {
        // The function moves the 3D actor forward by the specified amount.
        addPosition( rotation.transform( new Vector3(0, 0, -1) ).scl( dist ) );  
    }
    
    // dist = Distance by which to move right.
    public void moveRight(float dist)
    {
        // The function moves the 3D actor right by the specified amount.
        addPosition( rotation.transform( new Vector3(1, 0, 0) ).scl( dist ) );  
    }
    
    // dist = Distance by which to move up.
    public void moveUp(float dist)
    {
        // The function moves the 3D actor up by the specified amount.
        addPosition( rotation.transform( new Vector3(0, 1, 0) ).scl( dist ) );  
    }
    
    // other =
    // resolve =
    public boolean overlaps(BaseActor3D other, boolean resolve)
    {
        
        // Declare constants.
        final float significant = 0.5f;
        
        // Declare regular variables.
        boolean polyOverlap;
        
        // Declare object variables.
        MinimumTranslationVector mtv;
        Polygon poly1;
        Polygon poly2;
        
        poly1 = this.getBoundingPolygon();
        poly2 = other.getBoundingPolygon();

        if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
            return false;

        mtv = new MinimumTranslationVector();
        polyOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
        if (polyOverlap && resolve)
        {
            this.addPosition( mtv.normal.x * mtv.depth, 0,  mtv.normal.y * mtv.depth );
        }
        
        return (polyOverlap && (mtv.depth > significant));
    }
    
    // c = Color to which to change the material of the model instance.
    public void setColor(Color c)
    {
        // The function changes the color of all materials belonging to the model instance to the 
        // passed value.
        
        // Loop through materials belonging to model instance.
        // Set color of each material.
        for (Material m : modelData.materials)
            m.set( ColorAttribute.createDiffuse(c) ); 
    }
    
    // m = Model instance to copy to class-level variable.
    public void setModelInstance(ModelInstance m)
    {
        // The function sets the model instance to the passed object.
        modelData = m;
    }

    // v = Vector to use in setting position of the 3D actor.
    public void setPosition(Vector3 v)
    {  
        // The function uses a vector parameter to set the position of the 3D actor.
        position.set(v);  
    }
    
    // x = X-coordinate to use in setting position of 3D actor.
    // y = Y-coordinate to use in setting position of 3D actor.
    // z = Z-coordinate to use in setting position of 3D actor.
    public void setPosition(float x, float y, float z)
    {
        // The function uses a combination of x, y, and z coordinates sent as three parameters to set
        // the position of the 3D actor.
        position.set( x, y, z );  
    }
    
    public void setEllipseBase()
    {
        
        // Declare constants.
        final float a = 0.75f; // offset amount.
        
        // Declare regular variables.
        float[] vertices = new float[16];
        
        // Declare object variables.
        Vector3 max;
        Vector3 min;
        BoundingBox modelBounds;
        
        modelBounds = modelData.calculateBoundingBox( new BoundingBox() );
        max = modelBounds.max;
        min = modelBounds.min;

        /*
        vertices = 
            {max.x,0, a*max.x,a*max.z, 0,max.z, a*min.x,a*max.z,
             min.x,0, a*min.x,a*min.z, 0,min.z, a*max.x,a*min.z };
        */
        ArrayRoutines.addAll( vertices, max.x, 0, a*max.x, a*max.z, 0,max.z, a*min.x, a*max.z,
          min.x,0, a*min.x,a*min.z, 0,min.z, a*max.x, a*min.z );
        boundingPolygon = new Polygon(vertices);
        boundingPolygon.setOrigin(0,0);
    
    }
    
    // 2D collision detection
    public void setRectangleBase()
    {
        
        // Declare regular variables.
        float[] vertices = new float[8];
        
        // Declare object variables.
        BoundingBox modelBounds;
        Vector3 max;
        Vector3 min;
        
        modelBounds = modelData.calculateBoundingBox( new BoundingBox() );
        max = modelBounds.max;
        min = modelBounds.min;

        /*
        float[] vertices = 
            {max.x, max.z, min.x, max.z, min.x, min.z, max.x, min.z};
        */
        ArrayRoutines.addAll( vertices, max.x, max.z, min.x, max.z, min.x, min.z, max.x, min.z );
        
        boundingPolygon = new Polygon(vertices);
        boundingPolygon.setOrigin(0,0);
    }
    
    /*
    The amount of rotation around the upward-pointing axis is also called the yaw angle.  Similarly, 
    the rotation around the sideways-pointing axis (the motion from tilting your head up and down) is
    called the pitch angle, and the rotation around the forward-pointing axis (the motion from tilting 
    your head to the left and to the right) is called the roll angle.
    */
    
    // degrees = Angle in radians of the rotation around the Y axis.
    public void setTurnAngle(float degrees)
    {
        // The function sets the angle in radians of the rotation around the Y axis.
        rotation.set( new Quaternion(Vector3.Y, degrees) );  
    }
    
    // degrees = Number of radians to rotate.
    public void turn(float degrees)
    {
        // The function adjusts the angle in radians of the rotation around the Y axis by the passed value.
        rotation.mul( new Quaternion(Vector3.Y, -degrees) );  
    }
    
}
