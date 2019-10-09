package oglib.components;

import static com.google.common.base.Preconditions.*;

/**
 * An RGB color with 8bit per channel
 */
public class Color {
    public final byte red;
    public final byte green;
    public final byte blue;

    public Color(int red, int green, int blue) {
        checkArgument(red >= 0 && red <= 255, "Red channel must have values between 0 and 255");
        checkArgument(green >= 0 && green <= 255, "Green channel must have values between 0 and 255");
        checkArgument(blue >= 0 && blue <= 255, "Blue channel must have values between 0 and 255");

        this.red = (byte) (0xFF & red);
        this.green = (byte) (0xFF & green);
        this.blue = (byte) (0xFF & blue);
    }

    public Color(byte red, byte green, byte blue) {
        this.red = red;
        this.blue = blue;
        this.green = green;
    }
}