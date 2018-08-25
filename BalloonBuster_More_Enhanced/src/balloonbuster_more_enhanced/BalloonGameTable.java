package balloonbuster_more_enhanced;

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

public class BalloonGameTable extends BaseGame { // Extends the BaseGame class.

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
        // 4.  Adds image for mouseover (hover) version of button to Skin, with NinePatch measurements.
        // 5.  Adds image for mousedown (click) version of button to Skin, with NinePatch measurements.
        // 6.  Creates and adds a reusable text button style to the Skin.
        // 7.  Creates a new BalloonMenuButtons object used to set the screen to the main menu
        //     when the application starts.

        BitmapFont uiFont; // Reference to BitmapFont resource providing bitmapped font and associated details.
        BalloonMenuButtons ms; // BalloonMenuButtons object used to start game with main menu.
        Label.LabelStyle uiLabelStyle; // Reference to BitmapFont resource with a color specified.
        TextButton.TextButtonStyle uiTextButtonStyle; // Style associated with TextButton object (Button with Label).
        // Style requires Drawable graphic and BitmapFont and Color for Label.
        Texture downTex; // Texture to use when loading version of button for when mousedown (click) occurs.
        Texture overTex; // Texture to use when loading version of button for when mouseover (hover) occurs.
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

        // Load image for mousedown (click) version of button (to buffer).
        downTex = new Texture(Gdx.files.internal("assets/images/ninepatch-3.png"));

        // Add image for mousedown (click) version of button to Skin, with NinePatch measurements.
        skin.add("buttonDown", new NinePatch(downTex, 26,26,16,20) );

        // Add image for mousedown (click) version of button to TextButtonStyle object.
        uiTextButtonStyle.down = skin.getDrawable("buttonDown");

        // Set font color associated with TextButtonStyle during mousedown (click) event to YELLOW.
        uiTextButtonStyle.downFontColor = Color.YELLOW;

        // Add TextButtonStyle object to Skin.
        skin.add("uiTextButtonStyle", uiTextButtonStyle);

        ms = new BalloonMenuButtons(this); // Creates a new BalloonMenuButtons object to start game with main menu.
        setScreen( ms ); // Sets the screen to the main menu.

    }

}