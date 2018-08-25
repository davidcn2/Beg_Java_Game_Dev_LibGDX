package treasurequest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import core.BaseActor;
import core.BaseGame;
import java.util.ArrayList;
import core.BaseScreen;
import core.PhysicsActor;

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

public class GameScreen extends BaseScreen { // Extends the BaseScreen class.
    
    /* 
    The class extends the basic functionality of a BaseScreen class and stores information
    about the game -- paddle, ball, base objects, bricks, power-ups, and removal list and 
    related properties (map height and width, timers, speeds, ...).
    
    Methods include:
    
    create:  Sets defaults.  Configures and adds the Actors to the stage.
    keyDown:  The function gets called when the user presses a key.  Pauses the game when pressing the
              P key.  Resets the game when pressing the R key.  Generates an explosion when pressing the
              SPACE key.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    */
    
    private PhysicsActor player;
    private BaseActor door;
    private BaseActor key;
    private boolean hasKey;
    private BaseActor baseCoin;
    private ArrayList<BaseActor> coinList;
    private ArrayList<BaseActor> wallList;
    private ArrayList<BaseActor> removeList;
    private int tileSize = 64;
    private int tileCountWidth = 20;
    private int tileCountHeight = 20;
    // calculate game world dimensions
    final int mapWidth;
    final int mapHeight;
    private TiledMap tiledMap;
    private OrthographicCamera tiledCamera;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private int[] backgroundLayers = {0,1};
    private int[] foregroundLayers = {2};
    
    // g = Screen object for game window (mouse, cheese, floor, win text, and elapsed time text).
    public GameScreen(BaseGame g)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Sets game world dimensions.
        // 3.  Calls create function to configure and adds actors to the stage.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(g);
        
        // Set game world dimensions.
        this.mapWidth = tileSize * tileCountWidth;
        this.mapHeight = tileSize * tileCountHeight;
        
        // Configure and add the actors to the stage:  background, spaceship, thurster, and explosion.
        create();
        
    }
    
    public final void create()
    {
        
        // Finish BaseScreen initialization.
        finishTwoPartInitialization(mapWidth, mapHeight);
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt)
    {
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void render(float dt)
    {
    }
    
}
