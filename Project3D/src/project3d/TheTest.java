package project3d;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.math.Vector3;

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

public class TheTest implements ApplicationListener // Implements the ApplicationListener interface.
{
    
    /*
    The class implements the ApplicationListener interface to display a 3D cube.
    
    Methods include:
    
    create:  Initializes and configures the Environment and PerspectiveCamera.  Creates instances of
      models to add to the scene.  Initializes the ModelBatch object, to allow for rendering.
    dispose:  The function gets called when the application is Destroyed.
    pause:  The function gets called when the application is paused.
    render:  Called when the screen should render itself.  Clears the existing graphics and renders
      objects using the perspective camera.
    resize:  The function gets called when the application is resized.
    resume:  The function gets called when the application is resumed from a paused state.
    
    An Environment contains the uniform values specific for a location.  For example, the lights are part 
    of the Environment.  Simple applications might use only Environment, while more complex applications 
    might use multiple environments depending on the location of a ModelInstance.  A ModelInstance (or 
    Renderable) can only contain one Environment, though.

    The Environment extends the Attributes class.
    
    OrthographicCamera:  Used to determine which region of a tilemap layer should be rendered, 
      analogous to the role of the Camera object that belongs to each Stage.  The Stage class 
      uses an OrthographicCamera object for rendering.  2D projects will use an orthographic
      camera 99.9% of the time.
    PerspectiveCamera:  Uses perspective projection, in which objects with two edges of the same
      length may appear different in the projection.  An edge that is further away from the 
      viewer will appear shorter.  A perspective camera tries to mimic the way the human eye 
      sees the world (instead of how the world actually works).  To the human eye, as something 
      gets further away, the smaller the object appears.  3D projects almost always will use a 
      perspective camera.
    */
    
    // Declare object variables.
    public Environment environment; // Contains the uniform values specific for a location.
    public PerspectiveCamera camera; // A Camera with perspective projection.
    public ModelBatch modelBatch; // Used to render three-dimensional objects.
    public ModelInstance boxInstance; // Instance of the boxModel object, in addition to a
      // transformation matrix with position, rotation, and scaling data.
    
    @Override
    public void create() 
    {
        
        /* 
        The function occurs during the startup / create phase and accomplishes the following:
        
        1.  Initializes and configures the Environment.
        2.  Initializes and configures the PerspectiveCamera.
        3.  Creates instances of models to add to the scene.
        4.  Initializes the ModelBatch object, to allow for rendering.
        */
        
        // Declare regular variables.
        int usageCode; // Value indicating what types of data each vertex of the model will contain.
        
        // Declare object variables.
        Material boxMaterial; // Material which provides model its onscreen appearance.
        Model boxModel; // Model set up as a 5 x 5 x 5 cube with the additional properties below.
        DirectionalLight dLight; // Instance of a DirectionalLight to add to the Environment.
        Color lightColor; // Color to assign to DirectionalLight.
        Vector3 lightVector; // Direction (vector) to assign to DirectionalLight.
        ModelBuilder modelBuilder; // ModelBuilder utility class allowing creation of models.
        
        // Initialize the Environment.
        environment = new Environment();
        
        // Add a parameter (a subclass of the Attribute class) that defines the color of the ambient light 
        // in the scene.  In general, shades of gray are used for lights (rather than colors such as 
        // yellow or blue) so that the scene will not be tinted with unexpected colors.
        environment.set( new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f) );

        // Create an instance of a DirectionalLight.
        dLight = new DirectionalLight();
        
        // Use a brighter shade of gray for the DirectionalLight.
        lightColor = new Color(0.75f, 0.75f, 0.75f, 1);
        
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
        camera = new PerspectiveCamera( 67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
        
        // Set camera position.
        camera.position.set(10f, 10f, 10f);
        
        // Set the location the camera initially looks towards using the lookAt method.
        camera.lookAt( 0, 0, 0 );
        
        // Set near and far visiblity to 0.1 and 1000.
        camera.near = 1f;
        camera.far  = 1000f;
        
        // Update the camera using the recently specified properties.
        camera.update();
		
        /*
        The next task is to create instances of models to add to the scene.
        
        First, Use the createBox method of the ModelBuilder class to construct a cube.  Create a 
        Material to give the cube its appearance onscreen.  In the case below, a solid blue 
        diffuse color is used.  (Diffuse indicates the apparent color of the object when 
        illuminated by pure white light.)
        
        Next, determine what types of data each vertex of the model should contain.
        In every case, vertices should store a position.  In the code below, vertices also store 
        color data and a vector (called the normal vector) that is used to determine how light 
        reflects off an object, thus providing shading effects.  Each of the attributes has a 
        corresponding constant value defined in the Usage class.  Position data corresponds
        to Usage.Position, color data corresponds to Usage.ColorPacked, normal vector data 
        corresponds to Usage.Normal, and so forth.  When a combination of the data is needed, a 
        value is generated by adding together the constant values for each of the desired 
        attributes.  The resulting value is passed as a parameter to the createBox method.
        
        Last, decide on the dimensions of the box itself.  Because of the scale used by many
        modeling programs, these values often range from 1 to 10.
        */
        
        // Initialize ModelBuilder utility class to allow creation of models.
        modelBuilder = new ModelBuilder();
        
        // Initialize Material, which will provide model its onscreen appearance.
        boxMaterial = new Material();
        
        // Configure model Material to use a solid blue diffuse color.
        boxMaterial.set( ColorAttribute.createDiffuse(Color.BLUE) );
        
        // Configure what types of data each vertex of the model will contain.
        // In the case below, vertex information includes position, color, and a Normal vector 
        // (for light-related shading).
        usageCode = Usage.Position + Usage.ColorPacked + Usage.Normal;
        
        // Create the model as a box / cube with dimensions of 5 x 5 x 5 with the material and
        // usage code specifications just stored.
        // The model serves as a form of template for use in ModelInstance objects.
        boxModel = modelBuilder.createBox( 5f, 5f, 5f, boxMaterial, usageCode );
        
        // Initialize the ModelInstance with the properties configured in boxModel.
        boxInstance = new ModelInstance(boxModel);
        
        // Initialize the ModelBatch object.
        modelBatch = new ModelBatch();
        
    }
    
    @Override
    public void dispose() 
    {
        
        // The function gets called when the application is Destroyed.
        
    }
    
    @Override
    public void pause() 
    {
        
        // The function gets called when the application is paused.
        // Pausing occurs when (1) the application is not active, (2) the application is not 
        // visible on the screen, and (3) right before being destroyed.
        
    }
    
    @Override
    public void render() 
    {
        
        /*
        The function overrides the render method of the ApplicationListener interface.
        The override occurs due to the the need to render the screen.
        
        The function occurs during the render phase and accomplishes the following:
        
        1.  Clears the existing graphics.
        2.  Render objects using the perspective camera.
        */
        
        // Clear existing graphics.
        
        // Overdraw the area with the given glClearColor.
        Gdx.gl.glClearColor( 1, 1, 1, 1);
        
        // Reapply viewport.
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        // Clear the area using the specified buffer.  Supports multiple buffers.
        // Clear the depth information, also, due to being in a 3D environment.
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
 
        // Start rendering using the perspective camera.
        // Render each object.
        // Call end method to cause any renderables provided by render() methods to be rendered.
        modelBatch.begin( camera );
        modelBatch.render( boxInstance, environment );
        modelBatch.end();
        
    }
    
    // width:  New width, in pixels.
    // height:  New height, in pixels.
    @Override
    public void resize(int width, int height) 
    {
        
        // The function gets called when the application is resized.
        
    }
    
    @Override
    public void resume() 
    {
        
        // The function gets called when the application is resumed from a paused state.
        
    }
    
}