package cheeseplease_gamepad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import core.BaseGame;

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

public class CheeseGame extends BaseGame { // Extends the BaseGame class.

	// The class extends the basic functionality of a BaseGame class, sets up a Skin with reusable resources,
	// and creates a new Screen object with the main menu.  The main menu launches when the application starts.

	// Methods include:

	// create:  Initialize resources common to multiple screens and sets the screen to the main menu.

	@Override
	public void create()

	{

		// The function occurs when launching the application and:

		// 1.  Creates and adds a reusable font to the Skin.
		// 2.  Creates and adds a reusable label style (font with color) to the Skin.
		// 3.  Adds image for up version of button to Skin, with NinePatch measurements.
		// 4.  Adds image for mouse over (hover) version of button to Skin, with NinePatch measurements.
		// 5.  Adds image for mouse down (click) version of button to Skin, with NinePatch measurements.
		// 6.  Creates and adds a reusable text button style to the Skin.
		// 7.  Creates a new MenuScreen object used to set the screen to the main menu
		//     when the application starts.

		BitmapFont uiFont; // Reference to BitmapFont resource providing bitmapped font and associated details.
		MenuScreen ms; // MenuScreen object used to start game with main menu.
		Label.LabelStyle uiLabelStyle; // Reference to BitmapFont resource with a color specified.
		TextButton.TextButtonStyle uiTextButtonStyle; // Style associated with TextButton object (Button with Label).
		// Style requires Drawable graphic and BitmapFont and Color for Label.
		Texture downTex; // Texture to use when loading version of button for when mouse down (click) occurs.
		Texture overTex; // Texture to use when loading version of button for when mouse over (hover) occurs.
		Texture upTex; // Texture to use when loading up version of button.

		// Initialize resources common to multiple screens.

		// 1.  Initialize Bitmap font style.

		// Initialize the BitmapFont object with a FileHandle to the FNT file.
		uiFont = new BitmapFont(Gdx.files.internal("assets/fonts/cooper.fnt"));

		// Access the Texture data contained within the BitmapFont object.  Getting a reference to the
		// Texture data allows actions like setting a filter to obtain a smoother appearance when scaling images.
		uiFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// Add the uiFont object to the Skin.  Skins allow for storing resources common to multiple screens.
		skin.add("uiFont", uiFont);

		// 2.  Initialize Label style.

		// Create resource with the BitmapFont just produced and a color (ORANGE).
		uiLabelStyle = new LabelStyle(uiFont, Color.ORANGE);

		// Add the LabelStyle object to the Skin.
		skin.add("uiLabelStyle", uiLabelStyle);

		// 3.  Initialize TextButton style.

		// Create new TextButtonStyle object to provide style for TextButton objects.
		uiTextButtonStyle = new TextButtonStyle();

		// Set font associated with TextButtonStyle to uiFont from Skin.
		uiTextButtonStyle.font      = uiFont;

		// Set font color associated with TextButtonStyle to ORANGE.
		uiTextButtonStyle.fontColor = Color.ORANGE;

		// Load image for up version of button (to buffer).
		upTex = new Texture(Gdx.files.internal("assets/images/ninepatch-1.png"));

		// Note:  The NinePatch class, allows the scaling of an image in a particular way,
		// with the goal of preventing distortion.
		// A NinePatch object gets initialized using a Texture followed by four integers, as follows:
		// NinePatch np = new NinePatch( texture, left, right, top, bottom );
		// The integers represent distances, measured in pixels, from the correspondingly
		// named edge of the image. They are used to divide the texture into nine regions.

		// Add image for up version of button to Skin, with NinePatch measurements.
		skin.add("buttonUp", new NinePatch(upTex, 26,26,16,20));

		// Add image for up version of button to TextButtonStyle object.
		uiTextButtonStyle.up = skin.getDrawable("buttonUp");

		// Load image for mouseover (hover) version of button (to buffer).
		overTex = new Texture(Gdx.files.internal("assets/images/ninepatch-2.png"));

		// Add image for mouseover (hover) version of button to Skin, with NinePatch measurements.
		skin.add("buttonOver", new NinePatch(overTex, 26,26,16,20) );

		// Add image for mouseover (hover) version of button to TextButtonStyle object.
		uiTextButtonStyle.over = skin.getDrawable("buttonOver");

		// Set font color associated with TextButtonStyle during mouseover (hover) event to YELLOW.
		uiTextButtonStyle.overFontColor = Color.YELLOW;

		// Load image for mouse down (click) version of button (to buffer).
		downTex = new Texture(Gdx.files.internal("assets/images/ninepatch-3.png"));

		// Add image for mouse down (click) version of button to Skin, with NinePatch measurements.
		skin.add("buttonDown", new NinePatch(downTex, 26,26,16,20) );

		// Add image for mouse down (click) version of button to TextButtonStyle object.
		uiTextButtonStyle.down = skin.getDrawable("buttonDown");

		// Set font color associated with TextButtonStyle during mouse down (click) event to YELLOW.
		uiTextButtonStyle.downFontColor = Color.YELLOW;

		// Add TextButtonStyle object to Skin.
		skin.add("uiTextButtonStyle", uiTextButtonStyle);

		ms = new MenuScreen(this); // Create a new MenuScreen object to start game with main menu.
		setScreen( ms ); // Set the screen to the main menu.

	}

}