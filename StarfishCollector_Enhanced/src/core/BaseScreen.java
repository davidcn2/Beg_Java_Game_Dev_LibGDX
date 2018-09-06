package core;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import java.awt.Point;

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

ArrayList supports dynamic arrays that can grow as needed.
*/

 // Implements Screen and InputProcessor interfaces from LibGDX.
public abstract class BaseScreen implements Screen, InputProcessor

{
    
    // The class extends the basic functionality of Screen and InputProcessor classes in LibGDX.
    
    // Define the class as abstract so that subclasses (xyz extends BaseScreen) can implement methods in
    // different ways.  The class gets defined as abstract, instead of an interface, since it provides
    // code for some of its methods.  The reused methods in the current class must use the abstract
    // modifier.

    /* Detailed class description:
    
    The class refactors common elements from subclasses to provide for reuse and eliminate redundancy.
    The class handles the following tasks, in common to all Screen-extending classes:
    
    1.  Provide a reference to the Game object that instantiated the current class.
    2.  Initialize the stage objects.
    3.  In the render method, call the act method of the stages, clear the screen, and then
        call the draw method of the stages.
    4.  Provide empty methods for all the Screen and InputProcessor interface methods not needed
        by the program.
    5.  Provide methods for pausing the game and resizing the window.
    
    The game-specific (sub)classes no longer need to deal with much of the infrastructure, such as
    declaring and initializing Game and Stage variables.  The subclasses must now contain only two
    methods:  create and update.  The update method in the current class contains much of the act
    methods for each stage and all of the code that actually performed rendering operations.

    Note:  The creation step occurs in the subclass.
    
    LibGDX methods for screen include:
    
    dispose:  The method should get called manually when exiting a screen.  Add logic to clear memory
    associated with certain LibGDX objects.  In LibGDX, some objects need to get disposed manually,
    as they are not managed by the garbage collector.  The dispose() methode is supposed to dispose all 
    of these objects -- to release their ressources.
    Dispose of the following objects manually:  AssetManager, Bitmap, BitmapFont, BitmapFontCache, 
    CameraGroupStrategy, DecalBatch, ETC1Data, FrameBuffer, Mesh, Model, ModelBatch, ParticleEffect, 
    Pixmap, PixmapPacker, Shader, ShaderProgram, Shape, Skin, SpriteBatch, SpriteCache, Stage, Texture, 
    TextureAtlas, TileAtlas, TileMapRenderer, com.badlogic.gdx.physics.box2d.World, and all bullet
    classes.
    hide:  Called when the screen is no longer the current one for a Game.
    pause:  Called when the Application is paused, usually when not active or visible.
    render:  Called when the screen should render itself.
    resize:  Adjusts the size of the Viewport objects of the stages whenever the window size changes.
             Whenever a resize event occurs, the viewport needs to be informed and updated.
    resume:  Called when the Application is resumed from a paused state, usually when it regains focus.
    show:  Called when the screen becomes the current one for a Game.
    
    
    LibGDX methods for InputProcessor include:
    
    keyDown:  Called when user presses a key.
    keyTyped:  Called when user types a key.
    keyUp:  Called when user releases a key.
    mouseMoved:  Called when the mouse moved without pressing any buttons.
    scrolled:  Called when the user scrolls the mouse wheel.
    touchDown:  Called when touching the screen or pressing a mouse button.
    toughDragged:  Called when dragging a finger or the mouse.
    touchUp:  Called when lifting a finger from the screen or releasing a mouse button.
    
    Custom methods include:
    
    centerLabelUI:  Centers the label in the specified stage.
    isPaused:  Returns the pause state of the game (true or false).
    setPaused:  Sets the pause state of the game to the passed value.
    togglePaused:  Reverses the pause state of the game (true to false, false to true).
    update:  The abstract method (definted in the subclasses) occurs during the update phase (render method)
             and contains code related to game logic.
    */
    
    // Protected variables and methods allow the class itself to access them, classes inside of the 
    // same package to access them, and subclasses of that class to access them.
    
    // A final variable can only be initialized once, either via an initializer or an assignment statement.
    
    protected Game game; // Screen object used for current window.
    // Game objects allow an application to easily have multiple screens.
    
    protected Stage mainStage; // Stores a 2D scene graph containing the hierarchies of actors.
    // Stage handles the viewport and distributes input events.  Contains the non-UI actors.
    
    protected Stage uiStage; // Stores a 2D scene graph containing UI actors.  Includes win text / labels.
    
    private int viewWidth; // Window width.
    private int viewHeight; // Window height.
    
    private boolean paused; // Whether game paused.
    
    // g = Screen object for current window.
    public BaseScreen(Game g, int windowWidth, int WindowHeight)
    {
        
        // The constructor of the class:
        
        // 1.  Sets the default window width and height.
        // 2.  Stores the Screen object for the current window.
        // 3.  Scales each stage and its contents to fit the current window size.
        // 4.  Sets up the input multiplexer to receive and pass all input data to current class and stages.
        
        InputMultiplexer im; // Contains a group of input processors.  The base screen and each
        // stage get added to the input multiplexer.  When input events occur, the multiplexer 
        // forwards the information to each of the attached objects.
        
        // Set defaults.
        this.viewWidth = windowWidth;
        this.viewHeight = WindowHeight;
        this.paused = false;
        
        // Store Screen object for current window.
        this.game = g;

        // Scale each stage and its contents to fit the current window size.
        // If aspect ratio of window does not match stage, fill in extra region with solid black.
        mainStage = new Stage( new FitViewport(viewWidth, viewHeight) );
        uiStage   = new Stage( new FitViewport(viewWidth, viewHeight) );
        
        // Set up input muliplexer to receive all input data and pass the information along to
        // the current class and the stages.
        im = new InputMultiplexer(this, uiStage, mainStage);
        Gdx.input.setInputProcessor( im );
        
    }
    
    // The abstract method (definted in the subclasses) occurs during the update phase (render method)
    // and contains code related to game logic.
    //
    // Sample actions might include:
    // 1.  Resets the speed of the mouse (to zero).
    // 2.  Handles key presses and mouse clicks -- adjusts x and y speed.
    // 3.  Restricts mouse to window boundaries.
    // 4.  Checks for xyz and adjusts accordingly.
    // 5.  Updates xyz and acts accordingly.
    // 6.  Centers camera on player (mouse).
    public abstract void update(float dt);
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void render(float dt)
            
    {
        
        /*
        The function occurs during the render phase and accomplishes the following:

        1.  Adjusts Actor positions and other properties in the UI stage.
        2.  If game not paused, adjusts Actor positions and other properties in the non-UI stage and processes player input.
        3.  Draws the graphics.
        */
        
        // Call the Actor.act(float) method on each actor in the UI stage.
        // Typically called each frame.  The method also fires enter and exit events.
        // Updates the position of each Actor based on time.
        uiStage.act(dt);
        
        // Only pause gameplay events, not UI events.
        
        // If game not paused, then...
        if ( !isPaused() )
        {
        
            // Game active (not paused).
            
            // Call the Actor.act(float) method on each actor in the non-UI stage.
            // Typically called each frame.  The method also fires enter and exit events.
            // Updates the position of each Actor based on time.
            mainStage.act(dt);
            
            // Handle game logic -- allow processing based on player actions / input.
            update(dt);
        }
        
        // Draw graphics.

        // Overdraw the area with the given glClearColor.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        
        // Clear the area using the specified buffer.  Supports multiple buffers.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Draw the stages -- containing the Actors.
        mainStage.draw();
        uiStage.draw();
        
    }
    
    // Pause methods follow...
    
    public boolean isPaused()
    {
        // The function returns the pause state of the game (true or false).
        return paused;
    }
    
    public void setPaused(boolean b)
    {
        // The function sets the pause state of the game to the passed value.
        paused = b;
    }
    
    public void togglePaused()
    {
        // The function reverses the pause state of the game (true to false, false to true).
        paused = !paused;
    }
    
    // Current window window.
    // Current window height.
    @Override
    public void resize(int width, int height)
    {
        
        // The function adjusts the size of the Viewport objects of the stages whenever the
        // window size changes.
        
        // Adjust size of viewport of non-UI stage to current window width and height.
        mainStage.getViewport().update(width, height, true);
        
        // Adjust size of viewport of UI stage to current window width and height.
        uiStage.getViewport().update(width, height, true);
        
        // Recalculates the projection and view matrix of the cameras.
        mainStage.getCamera().update();
        uiStage.getCamera().update();
        
    }
    
    // Provide methods required by Screen interface to prevent need to do so in subclasses.
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() 
    {
        
        // The method occurs when removing the screen and allows for clearing of related resources from memory.

        // Clear LibGDX objects from memory.
        uiStage.dispose();
        mainStage.dispose();
        game.dispose();
        
    }
    
    @Override public void show() {}
    @Override public void hide() {}
    
    // Provide methods required by InputProcessor interface to prevent need to do so in subclasses.
    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }
    
    @Override
    public boolean keyUp(int keycode)
    {
        return false;
    }
    
    @Override
    public boolean keyTyped(char c)
    {
        return false;
    }
    
    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }
    
    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }
    
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }
    
    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }
    
    // theStage = Stage in which to center label.
    // theLabel = Label to center.
    public Point centerLabelUI(Stage theStage, Label theLabel)
    {
        
        // The function centers the label in the specified stage and returns the x and y
        // coordinates used in its positions (based on the top left corner).
        
        float labelHeight; // Height of the label.
        float labelWidth; // Width of the label.
        float labelX; // New X position of the label.
        float labelY; // New Y position of the label.
        
        // Store label width and height.
        labelWidth = theLabel.getWidth();
        labelHeight = theLabel.getHeight();
        
        // Store new X and Y coordinates of the label.
        labelX = (viewWidth - (labelWidth * theLabel.getFontScaleX())) * 0.5f;
        labelY = (viewHeight - (labelHeight * theLabel.getFontScaleY())) * 0.5f;
        
        // Set coordinates of the label.
        theLabel.setPosition(labelX, labelY);
        
        // Returns the new X and Y coordinates of the label.
        return new Point((int)labelX, (int)labelY);
        
    }
    
}