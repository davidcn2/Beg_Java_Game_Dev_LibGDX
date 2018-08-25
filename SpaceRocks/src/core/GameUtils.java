package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

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

public class GameUtils
{

    /*
    The GameUtils class servers the purpose of providing extra reusable functionality.

    // Methods include:

    parseSpriteSheet:  Contains logic for processing a sprite sheet with images in x by y columns and rows and
                       turning the results into an animation, which gets returned.
    */

    // filename = Name of file containing sprite sheet.
    // frameCols = Number of columns in sprite sheet.
    // frameRows = Number of rows in sprite sheet.
    // frameDuration = Duration of the animation.
    // mode = PlayMode of the animation.  Examples:  LOOP, LOOP_PINGPONG, LOOP_RANDOM, LOOP_REVERSED, NORMAL, REVERSED.
    @SuppressWarnings("SameParameterValue")
    public static Animation parseSpriteSheet(String fileName, int frameCols, int frameRows,
                                      float frameDuration, PlayMode mode)
    {

        /*
        The function contains logic for processing a sprite sheet with images in x by y columns and rows and
        turning the results into an animation, which gets returned.

        The TextureRegion class has a method called split that divides an image into rectangular sections,
        and returns the results in a two-dimensional array of TextureRegion objects.  The two-dimensional can
        be converted to a single Array and used in creating an Animation.  The function contains a nested for
        loop that transfers the contents of the two-dimensional array into a single-dimensional array before
        creating the animation.
        */

        Array<TextureRegion> framesArray; // Single-dimensional array (full list variety) of TextureRegion objects containing sprites from sheet.
        Texture t; // Texture used for sprite sheet.
        TextureRegion[] frames; // Single-dimensional array of TextureRegion objects containing sprites from sheet.
        TextureRegion[][] temp; // Two-dimensional array of TextureRegion objects containing sprites from sheet.

        int frameWidth; // Width of each sprite in the sheet -- assumes equal size.
        int frameHeight; // Height of each sprite in the sheet -- assumes equal size.
        int index; // Used to loop through frames in animation / sprites in sheet.

        // Load image for sprite sheet to buffer.
        t = new Texture(Gdx.files.internal(fileName), true);

        // Set filter type -- controlling how pixel colors are interpolated when image is rotated or stretched.
        t.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Get and store width and height of each sprite in sheet.
        frameWidth = t.getWidth() / frameCols;
        frameHeight = t.getHeight() / frameRows;

        // Divide image into rectangular sections, based on calculated (and equal) width and height of each sprite in sheet.
        temp = TextureRegion.split(t, frameWidth, frameHeight);
        frames = new TextureRegion[frameCols * frameRows];

        // Set initial frame number.
        index = 0;

        // Loop through frames in animation / sprites in sheet -- contained within two-dimensional array.

        // Loop through rows in sprite sheet.
        for (int i = 0; i < frameRows; i++)
        {
            // Loop through columns in sprite sheet.
            for (int j = 0; j < frameCols; j++)
            {
                // Copy frame from two to one-dimensional array.
                frames[index] = temp[i][j];

                // Increment frame number.
                index++;
            }
        }

        // Build Array of frames containing sprites in sheet.
        framesArray = new Array<>(frames);

        // Return Animation object.
        // return new Animation<>(frameDuration, framesArray, mode); // IntelliJ method.
        return new Animation(frameDuration, framesArray, mode); // NetBeans method.

    }

}