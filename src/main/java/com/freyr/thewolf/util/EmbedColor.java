package com.freyr.thewolf.util;

/**
 * This interface contains all the color constants that I will use. It will make changing
 * embed colors in the future much easier. They are stored in hex codes.
 *
 * @author Freyr
 */
public interface EmbedColor {
    int DEFAULT_COLOR = Integer.parseInt("008CA0", 16); // Default color (RGB: 0, 100, 0) (Color: GREEN)

    int ERROR_COLOR = Integer.parseInt("ff0000", 16); // Error Color (RGB: 255, 0, 0) (Color: RED)
}
