package core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class ColorWorks {

    // The class provides functionality related to colors, including generating a random one.

    // Methods include:

    // getColor:  Gets the current Color.
    // getRandomColor:  Generates, stores, and returns a random Color.
    // setColor:  Sets the current Color.

    private Color theColor; // A color.
    private final Color[] colorSet; // Set of colors.

    ColorWorks()
    {

        // The constructor of the class populates the array of commonly used colors.

        // Allocate memory for 18 Colors.
        colorSet = new Color[18];

        // Initialize array elements, skipping Clear.
        colorSet[0] = Color.BLACK;
        colorSet[1] = Color.BLUE;
        colorSet[2] = Color.CYAN;
        colorSet[3] = Color.DARK_GRAY;
        colorSet[4] = Color.GRAY;
        colorSet[5] = Color.GREEN;
        colorSet[6] = Color.LIGHT_GRAY;
        colorSet[7] = Color.MAGENTA;
        colorSet[8] = Color.MAROON;
        colorSet[9] = Color.NAVY;
        colorSet[10] = Color.OLIVE;
        colorSet[11] = Color.ORANGE;
        colorSet[12] = Color.PINK;
        colorSet[13] = Color.PURPLE;
        colorSet[14] = Color.RED;
        colorSet[15] = Color.TEAL;
        colorSet[16] = Color.WHITE;
        colorSet[17] = Color.YELLOW;

    }

    Color getRandomColor()
    {

        // The function generates, stores, and returns a random color.

        int colorNumber; // Random number used to determine which color to return.

        // Generate a random number used to determine which color to return.
        colorNumber = MathUtils.random(17);

        // Store the random color.
        theColor = colorSet[colorNumber];

        // Return the random color.
        return this.theColor;

    }

    public Color getColor() {
        // The function returns the Color.
        return this.theColor;
    }

    // theColor = Current Color to store.
    public void setColor(Color theColor) {
        // The function sets the Color.
        this.theColor = theColor;
    }

}