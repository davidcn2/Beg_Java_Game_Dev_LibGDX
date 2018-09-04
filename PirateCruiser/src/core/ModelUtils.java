package core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
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

Abstract:  Abstract classes are similar to interfaces.  You cannot instantiate them, and they may
contain a mix of methods declared with or without an implementation. However, with abstract classes,
you can declare fields that are not static and final, and define public, protected, and private
concrete methods.

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

public class ModelUtils 
{
    
    /* The class simplifies the process of creating materials (containing both textures and colors) and 
    applying them to models.
    
    Methods include:

    createBox:  Automates the creation of a (cube / box) model and returns a related instance.
    createCubeTexture6:  Automates the creation of a (cube / box) model and returns a related instance.
    createSphere:  Automates the creation of an (sphere) model and returns a related instance.
    createSphereInv:  Automates the creation of an inverted (sphere) model and returns a related instance.
    */
    
    
    // Declare object variables.
    public static ModelBuilder modelBuilder = new ModelBuilder(); // ModelBuilder utility class allowing creation of models.
    
    // xSize = Size of model (box / cube) in the x-direction.
    // ySize = Size of model (box / cube) in the y-direction.
    // zSize = Size of model (box / cube) in the z-direction.
    // t = Texture to apply to material.
    // c = Color to apply to material.
    public static ModelInstance createBox( float xSize, float ySize, float zSize, Texture t, Color c )
    {
        
        // The function automates the creation of a (cube / box) model and returns a related
        // instance.  The function supports the passing in of dimensions (x, y, z), and optional
        // texture and color components.  When passed, the same texture and color gets applied to
        // all sides.
        
        // Declare object variables.
        ModelInstance box; // Instance of the model, including a transformation matrix with position, 
          // rotation, and scaling data.
        Material boxMaterial; // Material which provides model its onscreen appearance.
        Model boxModel; // Model used as a template.
        Vector3 position; // Position of the model.
        
        // Declare regular variables.
        int usageCode; // Value indicating what types of data each vertex of the model will contain.
        
        /*
        First, create a Material to give the cube its appearance onscreen.  In the case below, the
        parameters determine any color or texture to apply.  Diffuse indicates the apparent color 
        of the object when illuminated by pure white light.
        */
        
        // Initialize Material, which will provide model its onscreen appearance.
        boxMaterial = new Material();
        
        // If texture passed to function, then...
        if (t != null)
            // Texture passed to function.
            // Apply texture to material for model.
            boxMaterial.set( TextureAttribute.createDiffuse(t) );
        
        // If color passed to function, then...
        if (c != null)
            // Color passed to function.
            // Apply color to material for model.
            boxMaterial.set( ColorAttribute.createDiffuse(c) );
        
        /*
        Next, determine what types of data each vertex of the model should contain.
        In every case, vertices should store a position.  In the code below, vertices also store 
        color data and a vector (called the normal vector) that is used to determine how light 
        reflects off an object, thus providing shading effects.  Each of the attributes has a 
        corresponding constant value defined in the Usage class.  Position data corresponds
        to Usage.Position, color data corresponds to Usage.ColorPacked, normal vector data 
        corresponds to Usage.Normal, and so forth.  When a combination of the data is needed, a 
        value is generated by adding together the constant values for each of the desired 
        attributes.  The resulting value is passed as a parameter to the createBox method.
        */
        
        // Configure what types of data each vertex of the model will contain.
        // In the case below, vertex information includes position, color, a Normal vector 
        // (for light-related shading), and texture coordinates.
        usageCode = Usage.Position + Usage.ColorPacked + Usage.Normal + Usage.TextureCoordinates;

        /*
        System.out.println("xSize: " + xSize);
        System.out.println("ySize: " + xSize);
        System.out.println("zSize: " + xSize);
        System.out.println("boxMaterial: " + boxMaterial.attributesHash());
        System.out.println("usageCode: " + usageCode);
        */
        
        // Create the model as a box / cube with dimensions of xSize x ySize x zSize with the 
        // material and usage code specifications just stored.
        // The model serves as a form of template for use in ModelInstance objects.
        boxModel = modelBuilder.createBox(xSize, ySize, zSize, boxMaterial, usageCode);
        
        // Set starting position of the model.
        position = new Vector3( 0, 0, 0 );

        // Create instance of the model.
        box = new ModelInstance(boxModel, position);
        
        // Return the model instance.
        return box;
        
    }

    // texSides = Array of textures for each of the six sides of the cube.
    public static ModelInstance createCubeTexture6( Texture[] texSides )
    {
        
        // The function automates the creation of a (cube / box) model and returns a related
        // instance.  The function supports the passing in of an array with textures for each
        // side of the cube.
        
        // Declare object variables.
        Material[] matSides; // Array of materials providing model its onscreen appearance, one for each side.
        Model model; // Model used as a template.
        MeshPartBuilder mpb; // Interface which contains various helper methods to create a mesh.
        
        // Declare regular variables.
        int usageCode; // Value indicating what types of data each vertex of the model will contain.
        
        // Create array of materials for each side of the (cube / box) model.
        matSides = new Material[6];
        
        // Loop through sides of the cube / box.
        for (int i = 0; i < 6; i++)
        {
            // Create a material for each of the sides.
            matSides[i] = new Material(
                TextureAttribute.createDiffuse(texSides[i]) );
        }
        
        // Start building a model.
        
        // ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        // Configure what types of data each vertex of the model will contain.
        // In the case below, vertex information includes position, color, a Normal vector 
        // (for light-related shading), and texture coordinates.
        usageCode = Usage.Position + Usage.Normal + Usage.TextureCoordinates + Usage.ColorPacked;

        // Add side 1 of 6 to model.
        modelBuilder.node().id = "negx";
        mpb = modelBuilder.part("negx", GL20.GL_TRIANGLES, usageCode, matSides[0] );
        mpb.setColor(Color.WHITE);
        mpb.rect( -0.5f,-0.5f,-0.5f, -0.5f,-0.5f,+0.5f, -0.5f,+0.5f,+0.5f, -0.5f,+0.5f,-0.5f, 0,0,-1 );
        
        // Add side 2 of 6 to model.
        modelBuilder.node().id = "posx";
        mpb = modelBuilder.part("posx", GL20.GL_TRIANGLES, usageCode, matSides[1] );
        mpb.setColor(Color.WHITE);
        mpb.rect( +0.5f,-0.5f,+0.5f, +0.5f,-0.5f,-0.5f, +0.5f,+0.5f,-0.5f, +0.5f,+0.5f,+0.5f,  0,0,1 );
        
        // Add side 3 of 6 to model.
        modelBuilder.node().id = "negy";
        mpb = modelBuilder.part("negy", GL20.GL_TRIANGLES, usageCode, matSides[2] );
        mpb.setColor(Color.WHITE);
        mpb.rect( -0.5f,-0.5f,+0.5f, -0.5f,-0.5f,-0.5f, +0.5f,-0.5f,-0.5f, +0.5f,-0.5f,+0.5f, 0,-1,0 );
        
        // Add side 4 of 6 to model.
        modelBuilder.node().id = "posy";
        mpb = modelBuilder.part("posy", GL20.GL_TRIANGLES, usageCode, matSides[3] );
        mpb.setColor(Color.WHITE);
        mpb.rect( +0.5f,+0.5f,+0.5f, +0.5f,+0.5f,-0.5f, -0.5f,+0.5f,-0.5f, -0.5f,+0.5f,+0.5f, 0,1,0 );
       
        // Add side 5 of 6 to model.
        modelBuilder.node().id = "negz";
        mpb = modelBuilder.part("negz", GL20.GL_TRIANGLES, usageCode, matSides[4] );
        mpb.setColor(Color.WHITE);
        mpb.rect(  +0.5f,-0.5f,-0.5f, -0.5f,-0.5f,-0.5f, -0.5f,+0.5f,-0.5f, +0.5f,+0.5f,-0.5f, 0,0,-1 );
        
        // Add side 6 of 6 to model.
        modelBuilder.node().id = "posz";
        mpb = modelBuilder.part("posz", GL20.GL_TRIANGLES, usageCode, matSides[5] );
        mpb.setColor(Color.WHITE);
        mpb.rect( -0.5f,-0.5f,+0.5f, +0.5f,-0.5f,+0.5f, +0.5f,+0.5f,+0.5f, -0.5f,+0.5f,+0.5f, 0,0,1 );
        
        // End building of model.
        model = modelBuilder.end();

        // Return the model instance.
        return new ModelInstance(model);
        
    }
    
    // r = Radius size, in pixels.  Used to specify width, height, and depth of sphere.
    // t = Texture to apply to sphere.
    // c = Color to apply to sphere.
    public static ModelInstance createSphere( float r, Texture t, Color c )
    {
        
        /*
        The function automates the creation of a (sphere) model and returns a related instance.
        The function supports the passing in of a dimension (r), and optional texture and color 
        components.
        */
        
        // Declare object variables.
        // Declare object variables.
        Vector3 position;// Position of the model.
        Material sphereMaterial; // Material which provides model its onscreen appearance.
        ModelInstance sphere; // Instance of the model, including a transformation matrix with position, 
          // rotation, and scaling data.
        Model sphereModel; // Model used as a template.
        
        // Declare regular variables.
        int usageCode; // Value indicating what types of data each vertex of the model will contain.
        
        // Initialize Material, which will provide model its onscreen appearance.
        sphereMaterial = new Material();
        
        // If texture passed to function, then...
        if (t != null)
            // Texture passed to function.
            // Apply texture to material for model.
            sphereMaterial.set( TextureAttribute.createDiffuse(t) );
        
        // If color passed to function, then...
        if (c != null)
            // Color passed to function.
            // Apply color to material for model.
            sphereMaterial.set( ColorAttribute.createDiffuse(c) );
        
        // Configure what types of data the model will contain.
        // In the case below, information includes position, color, a Normal vector 
        // (for light-related shading), and texture coordinates.
        usageCode = Usage.Position + Usage.ColorPacked + Usage.Normal + Usage.TextureCoordinates;

        // Create the model as a sphere with radius or r and applying the 
        // material and usage code specifications just stored.
        // The model serves as a form of template for use in ModelInstance objects.
        sphereModel = modelBuilder.createSphere(r,r,r, 32,32, sphereMaterial, usageCode);
        
        // Set starting position of the model.
        position = new Vector3( 0, 0, 0 );

        // Create instance of the model.
        sphere = new ModelInstance(sphereModel, position);
        
        // Return the model instance.
        return sphere;
        
    }
    
    // r = Radius size, in pixels.  Used to specify width, height, and depth of sphere.
    // t = Texture to apply to sphere.
    // c = Color to apply to sphere.
    public static ModelInstance createSphereInv( float r, Texture t, Color c )
    {
        
        /*
        The function automates the creation of an inverted (sphere) model and returns a related
        instance.  The function supports the passing in of a dimension (r), and optional
        texture and color components.  While the ModelBuilder class can easily create a
        spherical mesh, any materials applied are displayed only from the outside, rather than 
        the inside.  Perform a geometric trick to resolve this problem.  After creating the 
        model, scale the mesh by –1 in the z direction.  The scaling will cause the sphere to 
        turn itself "inside-out", reversing the sides on which the image will be displayed.
        */
        
        // Declare object variables.
        Vector3 position;// Position of the model.
        Material sphereMaterial; // Material which provides model its onscreen appearance.
        ModelInstance sphere; // Instance of the model, including a transformation matrix with position, 
          // rotation, and scaling data.
        Model sphereModel; // Model used as a template.
        
        // Declare regular variables.
        int usageCode; // Value indicating what types of data each vertex of the model will contain.
        
        // Initialize Material, which will provide model its onscreen appearance.
        sphereMaterial = new Material();
        
        // If texture passed to function, then...
        if (t != null)
            // Texture passed to function.
            // Apply texture to material for model.
            sphereMaterial.set( TextureAttribute.createDiffuse(t) );
        
        // If color passed to function, then...
        if (c != null)
            // Color passed to function.
            // Apply color to material for model.
            sphereMaterial.set( ColorAttribute.createDiffuse(c) );
        
        // Configure what types of data the model will contain.
        // In the case below, information includes position, color, a Normal vector 
        // (for light-related shading), and texture coordinates.
        usageCode = Usage.Position + Usage.ColorPacked + Usage.Normal + Usage.TextureCoordinates;

        // Create the model as a sphere with radius or r and applying the 
        // material and usage code specifications just stored.
        // The model serves as a form of template for use in ModelInstance objects.
        sphereModel = modelBuilder.createSphere(r,r,r, 32,32, sphereMaterial, usageCode);
        
        // Loop through all meshes in the model.
        for (Mesh m : sphereModel.meshes)
        {
            // Scale the mesh by -1 in the z direction to invert the model 
            // (allowing for viewing from the inside).
            m.scale(1,1,-1) ;
        }
        
        // Set starting position of the model.
        position = new Vector3( 0, 0, 0 );

        // Create instance of the model.
        sphere = new ModelInstance(sphereModel, position);
        
        // Return the model instance.
        return sphere;
        
    }
    
}
