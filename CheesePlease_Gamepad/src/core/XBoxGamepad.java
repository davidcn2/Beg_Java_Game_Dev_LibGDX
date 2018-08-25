package core;

import com.badlogic.gdx.controllers.PovDirection;

public class XBoxGamepad
{

    // The class provides constants used when working with the XBox gamepad / controller.

    // Constants...

    @SuppressWarnings("unused")
    public static final int BUTTON_A              = 0;

    @SuppressWarnings("unused")
    public static final int BUTTON_B              = 1;

    @SuppressWarnings("unused")
    public static final int BUTTON_X              = 2;

    @SuppressWarnings("unused")
    public static final int BUTTON_Y              = 3;

    @SuppressWarnings("unused")
    public static final int BUTTON_LEFT_SHOULDER  = 4;

    @SuppressWarnings("unused")
    public static final int BUTTON_RIGHT_SHOULDER = 5;

    @SuppressWarnings("unused")
    public static final int BUTTON_BACK           = 6;

    @SuppressWarnings("unused")
    public static final int BUTTON_START          = 7;

    @SuppressWarnings("unused")
    public static final int BUTTON_LEFT_STICK     = 8;

    @SuppressWarnings("unused")
    public static final int BUTTON_RIGHT_STICK    = 9;

    @SuppressWarnings("unused")
    public static final PovDirection DPAD_UP    = PovDirection.north;

    @SuppressWarnings("unused")
    public static final PovDirection DPAD_DOWN  = PovDirection.south;

    @SuppressWarnings("unused")
    public static final PovDirection DPAD_RIGHT = PovDirection.east;

    @SuppressWarnings("unused")
    public static final PovDirection DPAD_LEFT  = PovDirection.west;

    // X-axis: -1 = left, +1 = right
    // Y-axis: -1 = up  , +1 = down

    @SuppressWarnings("unused")
    public static final int AXIS_LEFT_X  = 1;

    @SuppressWarnings("unused")
    public static final int AXIS_LEFT_Y  = 0;

    @SuppressWarnings("unused")
    public static final int AXIS_RIGHT_X = 3;

    @SuppressWarnings("unused")
    public static final int AXIS_RIGHT_Y = 2;

    // Left & Right Trigger buttons treated as a single axis; same ID value
    // Values - Left trigger: 0 to +1.  Right trigger: 0 to -1.
    // Note: values are additive; they can cancel each other if both are pressed!

    @SuppressWarnings("unused")
    public static final int AXIS_LEFT_TRIGGER  = 4;

    @SuppressWarnings("unused")
    public static final int AXIS_RIGHT_TRIGGER = 4;

}