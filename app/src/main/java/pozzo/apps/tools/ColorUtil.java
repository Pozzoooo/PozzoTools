package pozzo.apps.tools;

import android.graphics.Color;

/**
 * Some utilities for color manipulation.
 *
 * @author pozzo
 * @since 08/07/15.
 */
public class ColorUtil {

	/**
	 * Make color lighter than the given one by the factor.
	 *
	 * @param color to be lightened.
	 * @param factor amount to lighter.
	 * @return Lighter color.
	 */
	public static int lighter(int color, float factor) {
		int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
		int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
		int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
		return Color.argb(Color.alpha(color), red, green, blue);
	}

	/**
	 * Make color darker than the given one by the factor.
	 *
	 * @param color to be darkened.
	 * @param factor amount to darker.
	 * @return Darker color.
	 */
	public static int darker(int color, float factor) {
		int a = Color.alpha(color);
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);

		return Color.argb(a,
				Math.max((int) (r * factor), 0),
				Math.max((int) (g * factor), 0),
				Math.max((int) (b * factor), 0));
	}
}
