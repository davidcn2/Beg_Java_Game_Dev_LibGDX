package core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class BaseGame extends Game { // Extends the Game class from LibGDX.

    // Define the class as abstract so that subclasses (xyz extends BaseScreenTable) can implement methods in
    // different ways.  The class gets defined as abstract, instead of an interface, since it provides
    // code for some of its methods.  The reused methods in the current class must use the abstract
    // modifier.

    /* Detailed class description:

    The class extends the basic functionality of a Game class to support reusable resources with Skin
    objects.  The Skin object allows the creation of common UI elements when initializing the Game
    class.  The Skin object stores the information in a data structure that can get accessed by the
    Screen objects at a later time.

    LibGDX methods for screen include:

    create:  The function occurs during the startup / create phase.
    dispose: The function occurs during the cleanup phase and clears objects from memory.

    */

    // The class extends the basic functionality of a Game class.
    // The Game abstract class provides an implementation of ApplicationListener, along with some helper
    // methods to set and handle Screen rendering.  The Game class simplifies handling of multiple
    // screens.

    public Skin skin; // Used to store resources common to multiple screens.

    public BaseGame()
    {

        // The constructor of the class:

        // 1.  Initializes the Skin object.

        // Initialize the Skin object.
        skin = new Skin();

    }

    @Override
    public abstract void create();

    @Override
    public void dispose()
    {

        /*
        The function occurs during the cleanup phase and clears objects from memory.
        */

        // Clear objects from memory.
        skin.dispose();
        super.dispose();

    }

}