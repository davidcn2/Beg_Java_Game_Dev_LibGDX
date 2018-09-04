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
    getBoundingPolygon:  Returns a bounding polygon with properties (position and rotation) based on
      those of the Actor.
    getPosition:  Returns the position of the 3D actor -- as a vector.
    getTurnAngle:  Returns the angle in radians of the rotation around the Y axis.
    moveForward:  Moves the 3D actor forward by the specified amount.
    moveRight:  Moves the 3D actor right by the specified amount.
    moveUp:  Moves the 3D actor up by the specified amount.
    overlaps:  Determines whether the bounding polygon for the passed Actor intersects (significantly)
      with that of the current.  Moves current Actor minimum amount to avoid intersection.
    setColor:  Changes the color of all materials belonging to the model instance to the passed value.
    setEllipseBase:  Sets the bounding polygon approximately to an elliptical (eight-sided) shape based 
      on properties of the BoundingBox (smallest box that contains the entire model).
    setModelInstance:  Sets the model instance to the passed object.
    setPosition:  Uses a vector parameter or combination of coordinates (x, y, z) to set the position of
      the 3D actor.
    setRectangleBase:  Sets the bounding polygon to a rectangular shape based on properties of the
      BoundingBox (smallest box that contains the entire model).
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
        
        // The function returns a bounding polygon with properties (position and rotation) based
        // on those of the Actor.
        
        // Set the position (x and z coordinates) of the bounding polygon to that of the Actor.
        boundingPolygon.setPosition( position.x, position.z );
        
        // Set rotation of the bounding polygon to the turn angle of the Actor.
        boundingPolygon.setRotation( getTurnAngle() );
        
        // Return the bounding polygon.
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
    
    // other = Other Actor to check for collision detection.
    // resolve = Whether to move the other Actor along the minimum translation vector to prevent overlap.
    public boolean overlaps(BaseActor3D other, boolean resolve)
    {
        
        // The function determines whether the bounding polygon for the passed Actor intersects 
        // (significantly) with that of the current.  Moves current Actor minimum amount to avoid 
        // intersection.  The function returns whether a significant overlap occurs.
        
        // Declare constants.
        final float significant = 0.5f; // Minimum significant penetration depth -- length of the minimum
        // translation vector (MTV), which is the smallest vector along which an intersecting shape can 
        // get moved to be separate from the other shape.
        
        // Declare regular variables.
        boolean polyOverlap; // Whether Actors overlap.
        
        // Declare object variables.
        MinimumTranslationVector mtv; // Minimum magnitude vector required to push the
        // polygon defined by verts1 out of the collision with the polygon defined by verts2.
        Polygon poly1; // Reference to bounding polygon for current Actor.
        Polygon poly2; // Reference to bounding polygon for passed Actor.
        
        // Store bounding polygons for current and passed Actors.
        poly1 = this.getBoundingPolygon();
        poly2 = other.getBoundingPolygon();

        // If polygons are NOT intersecting, then...
        if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
            // Polygons are NOT intersecting.
            // Exit function.
            return false;
        
        // Polygons are intersecting.
        
        /*
        Polygons are intersecting.
        Checks whether specified counter-clockwise wound convex polygons overlap.
        If polygons overlap, obtains a Minimum Translation Vector indicating the
        minimum magnitude vector required to push the polygon p1 out of collision
        with polygon p2.
        Minimum Translation Vector indicates the minimum magnitude vector required
        to push the polygon defined by verts1 out of the collision with the polygon
        defined by verts2.
        */
        
        // Instantiate a minimum transaction vector object.
        mtv = new MinimumTranslationVector();
        
        // Obtain a minimum translation vector indicating the minimum magnitude vector
        // required to push the current Actor (polygon) out of the collision with the
        // other.
        polyOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
        
        // If polygons intersect and resolve parameter flagged, then...
        if (polyOverlap && resolve)
        {
            // Polygons intersect and resolve parameter flagged.

            // Moves current Actor (adds X and Y to current position) based on minimum
            // translation vector --
            this.addPosition( mtv.normal.x * mtv.depth, 0,  mtv.normal.y * mtv.depth );
        }
        
        // Compare penetration depth to minimum significant value.
        // The penetration depth is the length of the minimum translation vector (MTV), which is the
        // smallest vector along which an intersecting shape can get moved to be separated from the
        // other shape.

        // If polygon intersection more than minimum significant value, then return polygons as
        // intersecting.  Otherwise, return polygons as not intersecting.
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
    
    /*
    Two methods exist for 2D collision detection -- setEllipseBase and setRectangleBase.
    */
    
    public void setEllipseBase()
    {
        
        /*
        The function sets the bounding polygon approximately to an elliptical (eight-sided) shape.  First, 
        determine the dimensions of the object in the x and z dimensions.  The dimensions are analogous 
        to the width and height in the two-dimensional case.  Determine the values by calculating the 
        BoundingBox associated to the model -- the smallest box that contains the entire model.  A
        bounding box stores the dimensions of the model using two Vector3 objects:  min and max.  Min
        and max store the values of the smallest and largest coordinates contained by the model, 
        respectively.  Next, use the values to create the array of vertices to pass to the polygon object.
        */
        
        // Declare constants.
        final float a = 0.75f; // Offset amount.
        
        // Declare regular variables.
        float[] vertices = new float[16]; // Array of vertices to use when constructing the bounding polygon.
        
        // Declare object variables.
        Vector3 max; // Vector based on the largest coordinates contained by the model.
        Vector3 min; // Vector based on the smallest coordinates contained by the model.
        BoundingBox modelBounds; // Smallest box that contains the entire model.
        
        // Calculate the BoundingBox associated to the model in order to determine the dimensions of
        // the object in the x and z dimensions.
        modelBounds = modelData.calculateBoundingBox( new BoundingBox() );
        
        // Store the vector based on the largest coordinates contained by the model.
        max = modelBounds.max;
        
        // Store the vector based on the smallest coordinates contained by the model.
        min = modelBounds.min;

        // Use the minimum and maximum vectors to create the array of vertices needed to build the
        // bounding polygon -- in the shape of an ellipse.
        ArrayRoutines.addAll( vertices, max.x, 0, a*max.x, a*max.z, 0,max.z, a*min.x, a*max.z,
          min.x,0, a*min.x,a*min.z, 0,min.z, a*max.x, a*min.z );
        
        // Create the bounding polygon using the array of vertices based on the smallest and largest
        // coordinates.
        boundingPolygon = new Polygon(vertices);
        
        // Set the origin point to which all of the local vertices of the polygon are relative.
        boundingPolygon.setOrigin( 0, 0 );
    
    }
    
    public void setRectangleBase()
    {
        
        /*
        The function sets the bounding polygon to a rectangular shape.  First, determine the dimensions 
        of the object in the x and z dimensions.  The dimensions are analogous to the width and height 
        in the two-dimensional case.  Determine the values by calculating the BoundingBox associated to 
        the model -- the smallest box that contains the entire model.  A bounding box stores the 
        dimensions of the model using two Vector3 objects:  min and max.  Min and max store the values 
        of the smallest and largest coordinates contained by the model, respectively.  Next, use the 
        values to create the array of vertices to pass to the polygon object.
        */
        
        // Declare regular variables.
        float[] vertices = new float[8]; // Array of vertices to use when constructing the bounding polygon.
        
        // Declare object variables.
        Vector3 max; // Vector based on the largest coordinates contained by the model.
        Vector3 min; // Vector based on the smallest coordinates contained by the model.
        BoundingBox modelBounds; // Smallest box that contains the entire model.
        
        // Calculate the BoundingBox associated to the model in order to determine the dimensions of
        // the object in the x and z dimensions.
        modelBounds = modelData.calculateBoundingBox( new BoundingBox() );
        
        // Store the vector based on the largest coordinates contained by the model.
        max = modelBounds.max;
        
        // Store the vector based on the smallest coordinates contained by the model.
        min = modelBounds.min;

        // Use the minimum and maximum vectors to create the array of vertices needed to build the
        // bounding polygon -- in the shape of a rectangle.
        ArrayRoutines.addAll( vertices, max.x, max.z, min.x, max.z, min.x, min.z, max.x, min.z );
        
        // Create the bounding polygon using the array of vertices based on the smallest and largest
        // coordinates.
        boundingPolygon = new Polygon(vertices);
        
        // Set the origin point to which all of the local vertices of the polygon are relative.
        boundingPolygon.setOrigin( 0, 0 );
        
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
