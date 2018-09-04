package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
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

public class Stage3D 
{
    
    /* The class simulates the basic functionality of a Stage class in LibGDX, but in a 3D environment.

    The LibGDX Stage object handles rendering tasks (using its internal Camera and Batch objects),
    and manages a list of Actor objects.  In addition, act and draw methods exist, which call the
    related methods of all attached actors.
    
    Methods include:

    act:  Updates the camera and the transformation data (model instances) of the stage.
    addActor:  Adds the passed BaseActor3D to the ArrayList.
    draw:  Renders each object in the ArrayList.
    moveCamera:  Moves the camera using the specified parameter(s) -- either a vector  or its x, y, 
      and z components.
    moveCameraForward:  Moves the camera forward the passed distance.
    moveCameraRight:  Moves the camera to the right the passed distance.
    moveCameraUp:  Moves the camera up the passed distance.
    removeActor:  Removes the passed BaseActor3D from the ArrayList.
    setCameraDirection:  Sets the camera direction using the specified parameter(s).
    setCameraPosition:  Sets the camera position using the specified parameter(s).
    tiltCamera:  Tilts the camera up or down by the passed angle, to look higher or lower.
    turnCamera:  Rotates around the y-axis by the passed angle.
    */
    
    // Declare object variables.
    private final Environment environment; // Contains the uniform values specific for a location.
    private final PerspectiveCamera camera; // A Camera with perspective projection.
    private final ModelBatch modelBatch; // Used to render three-dimensional objects.
    private final ArrayList<BaseActor3D> actorList; // Set of BaseActor3D objects.
    
    public Stage3D()
    {
        
        /* 
        The constructor of the class accomplishes the following:
        
        1.  Initializes and configures the Environment.
        2.  Initializes and configures the PerspectiveCamera.
        3.  Initializes the ModelBatch object, to allow for rendering.
        4.  Initializes the ArrayList of BaseActor3D objects.
        */
        
        DirectionalLight dLight; // Instance of a DirectionalLight to add to the Environment.
        Color lightColor; // Color to assign to DirectionalLight.
        Vector3 lightVector; // Direction (vector) to assign to DirectionalLight.
        
        // Initialize the Environment.
        environment = new Environment();
        
        // Add a parameter (a subclass of the Attribute class) that defines the color of the ambient light 
        // in the scene.  In general, shades of gray are used for lights (rather than colors such as 
        // yellow or blue) so that the scene will not be tinted with unexpected colors.
        environment.set( new ColorAttribute(ColorAttribute.AmbientLight, 0.7f, 0.7f, 0.7f, 1f) );

        // Create an instance of a DirectionalLight.
        dLight = new DirectionalLight();
        
        // Use a brighter shade of gray for the DirectionalLight.
        lightColor = new Color(0.9f, 0.9f, 0.9f, 1);
        
        // Specify the direction for the DirectionalLight using a Vector3 object, pointing mostly
        // to the left and downward.
        lightVector = new Vector3(-1.0f, -0.75f, -0.25f);
        
        // Configure the DirectionalLight using the objects above.
        dLight.set( lightColor, lightVector );
        
        // Add the DirectionalLight to the Environment.
        environment.add( dLight ) ;

        /*
        A PerspectiveCamera is then initialized, with a field of view of 67 degrees, and with 
        near and far visibility set to 0.1 and 1000, respectively.  The near and far values
        have been chosen to guarantee that the view area contains the object that will be added 
        to the scene.  The camera position is then set.  The location the camera initially
        looks toward is specified via the lookAt method.
        */
        
        // Initialize PerspectiveCamera, with a field of view of 67 degrees.
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        // Set camera position.
        camera.position.set(10f, 10f, 10f);
        
        // Set the location the camera initially looks towards using the lookAt method.
        camera.lookAt(0,0,0);
        
        // Set near and far visiblity to 0.1 and 1000.
        camera.near = 0.01f;
        camera.far = 1000f;
        
        // Update the camera using the recently specified properties.
        camera.update();
        
        // Initialize the ModelBatch object.
        modelBatch = new ModelBatch();

        // Initialize the BaseActor3D ArrayList.
        
        // actorList = new ArrayList<BaseActor3D>();
        actorList = new ArrayList<>();
        
    }

    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    public void act(float dt)
    {
        
        // The function updates the camera and the transformation data of all model instances related to
        // the ArrayList.
        
        // Update the camera.
        camera.update();
        
        // Update the transformation data of the model instances corresponding to each BaseActor3D in 
        // the ArrayList.
        
        /*
        for (BaseActor3D ba : actorList)
        ba.act(dt);
         */
        actorList.forEach((ba) -> {
            ba.act(dt);
        });
        
    }

    // ba = BaseActor3D to add to the ArrayList.
    public void addActor(BaseActor3D ba)
    {
        
        // The function adds the passed BaseActor3D to the ArrayList.
        
        // Add passed BaseActor3D to the ArrayList.
        actorList.add( ba );
        
    }
    
    public void draw()
    {
        
        // The function renders each object in the ArrayList.
        
        // Start rendering using the perspective camera.
        modelBatch.begin(camera);
        
        // Render each object in the ArrayList.
        
        /*
        for (BaseActor3D ba : actorList)
        ba.draw(modelBatch, environment);
         */
        actorList.forEach((ba) -> {
            ba.draw(modelBatch, environment);
        });
        
        // Call end method to cause any renderables provided by render() methods to be rendered.
        modelBatch.end();
        
    }

    /*
    A Camera object stores two internal Vector3 objects:
    
    • direction:  Determines where the camera currently faces.
    • up:  Determines the direction that should be oriented toward the top of the screen.
    
    When moving the camera forward and backward, the camera should maintain a constant height 
    (even if tilted at an angle).  The y component of the vector direction can be set to 0 in order 
    to yield a vector that moves forward.  After its determination, the vector needs to be scaled 
    by the distance the camera will travel.  Then, the vector should be added to the current position
    of the camera  via the moveCamera function.  For moving to the left and right, similarly discard 
    the y component of the vector.  To transform the direction vector for pointing
    to the right, interchange the x and z values and negate the z value.
    
    Moving the camera upward is a straightforward task.  Movement will always be in the direction of 
    the y axis, and not the up vector of the camera.  Movement will occur in such a fashion since 
    when the camera is tilted, its up vector will no longer be pointing in the same orientation as 
    the y axis.
    */
    
    // x = X-component of vector by which to move camera.
    // y = Y-component of vector by which to move camera.
    // z = Z-component of vector by which to move camera.
    public void moveCamera(float x, float y, float z)
    {
        
        // The function moves the camera by the vector created by the combination of the passed x, y, and
        // z components.
        camera.position.add( x, y, z );
        
    }
    
    // v = Vector by which to move camera.
    public void moveCamera(Vector3 v)
    {
        // The function moves the camera by the passed vector.
        camera.position.add( v );
    }
    
    // dist = Distance by which to move the camera forward.
    public void moveCameraForward(float dist)
    {  
    
        /*
        The function moves the camera forward by the passed distance.
        
        When moving the camera forward and backward, the camera should maintain a constant height 
        (even if tilted at an angle).  The y component of the vector direction can be set to 0 in 
        order to yield a vector that moves forward.  After its determination, the vector needs to 
        be scaled by the distance the camera will travel.  Then, the vector should be added to the 
        current position of the camera via the moveCamera function.
        */
        
        Vector3 forward; // Vector specifying direction of camera.
        
        // Determine camera direction.
        forward = new Vector3(camera.direction.x, 0, camera.direction.z).nor();
        
        // Scale vector and move camera forward.
        moveCamera( forward.scl( dist ) );
        
    }

    // dist = Distance by which to move camera.
    public void moveCameraRight(float dist)
    {  
        
        /*
        The function moves the camera to the right the passed distance.
        
        For moving to the right, discard the y component of the vector.  To transform the direction 
        vector for pointing to the right, interchange the x and z values (in comparison to moving
        forward) and negate the z value.
        */
        
        Vector3 right; // Vector specifying direction of camera.
        
        // Determine camera direction.
        right = new Vector3( camera.direction.z, 0, -camera.direction.x).nor();
        
        // Scale vector and move camera to the right.
        moveCamera( right.scl( dist ) );
        
    }

    // dist = Distance by which to move camera up.
    public void moveCameraUp(float dist)
    {
        
        /*
        The function moves the camera up the passed distance.
        
        Moving the camera upward is a straightforward task.  Movement will always be in the direction of 
        the y axis, and not the up vector of the camera.  Movement will occur in such a fashion since 
        when the camera is tilted, its up vector will no longer be pointing in the same orientation as 
        the y axis.
        */
        
        // Move the camera up the passed distance.
        moveCamera( 0, dist, 0 );
        
    }
    
    // ba = BaseActor3D to remove from the ArrayList.
    public void removeActor(BaseActor3D ba)
    {
        
        // The function removes the passed BaseActor3D from the ArrayList.
        
        // Remove passed BaseActor3D from the ArrayList.
        actorList.remove( ba );  
        
    }
    
    // x = X-coordinate in vector specifying desired camera direction.
    // y = Y-coordinate in vector specifying desired camera direction.
    // z = Z-coordinate in vector specifying desired camera direction.
    public void setCameraDirection(float x, float y, float z)
    {
        
        // The function sets the camera direction to the vector specified by the passed x, y, and z 
        // coordinates.
        
        // Set the camera direction to the vector specified by the passed x, y, and z coordinates.
        setCameraDirection( new Vector3( x, y, z) );
        
    }
    
    // v = Vector to which to set the camera direction.
    public void setCameraDirection(Vector3 v)
    {
        
        // The function sets the camera direction to the passed vector.
        
        // Set the camera direction to the passed vector.
        
        // Orient the camera to look the desired direction.
        camera.lookAt(v);
        
        // Reset up axis of camera to the direction of the y axis to prevent camera from tilting left or 
        // right.
        camera.up.set( 0, 1, 0);
        
    }
    
    // x = X-coordinate to which to use in vector when setting camera position.
    // y = Y-coordinate to which to use in vector when setting camera position.
    // z = Z-coordinate to which to use in vector when setting camera position.
    public void setCameraPosition(float x, float y, float z)
    {
        
        // The function sets the camera position to the vector specified by the passed x, y, and z 
        // coordinates.
        
        // Set the camera position to the vector specified by the passed x, y, and z coordinates.
        camera.position.set(x, y, z);
        
    }
    
    // v = Vector to which to set the camera position.
    public void setCameraPosition(Vector3 v)
    {
        
        // The function sets the camera position to the passed vector.
        
        // Set the camera position to the passed vector.
        camera.position.set(v);
        
    }
    
    // angle = Amount by which to tilt camera up or down, in degrees.
    public void tiltCamera(float angle)
    {
        
        // The function tilts the camera up or down by the passed angle, to look higher or lower.
        
        Vector3 side; // Axis used in rotation. 
        
        // Determine axis to use for rotation.
        side = new Vector3(camera.direction.z, 0, -camera.direction.x);
        
        // Tilt camera.
        camera.direction.rotate(side, angle);
        
    }
    
    // angle = Amount by which to rotate around the y-axis, in degrees.
    public void turnCamera(float angle)
    {
        // Rotate around the y-axis by the passed angle.
        camera.rotate( Vector3.Y, -angle );  
    }

}