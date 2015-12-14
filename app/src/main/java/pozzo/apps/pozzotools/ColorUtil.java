package pozzo.apps.pozzotools;

import android.graphics.Color;

/**
 * Some utilities for color manipulation.
 *
 * @author pozzo
 * @since 08/07/15.
 */
public class ColorUtil {

	/**
	 * @param color a ser clareada.
	 * @param factor que sera clareado.
	 * @return Nova cor.
	 */
	public static int lighter(int color, float factor) {
		int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
		int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
		int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
		return Color.argb(Color.alpha(color), red, green, blue);
	}

	/**
	 * @param color a ser escurecida.
	 * @param factor que sera escurecido.
	 * @return Nova cor.
	 */
	public static int darker (int color, float factor) {
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
