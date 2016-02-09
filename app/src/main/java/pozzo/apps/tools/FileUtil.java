package pozzo.apps.tools;

import android.webkit.MimeTypeMap;

import java.util.Locale;

/**
 * Some tools for file manipulation.
 *
 * Created by sarge on 24/12/15.
 */
public class FileUtil {

	/**
	 * @return Extension from the given file.
	 */
	public static String getFileExtension(String filePath) {
		return filePath.substring(filePath.lastIndexOf(".") + 1);
	}

	/**
	 * @return MimeType from given path.
	 */
	public static String getMimeType(String filePath) {
		return MimeTypeMap.getSingleton().getMimeTypeFromExtension(
				getFileExtension(filePath).toLowerCase(Locale.US));
	}
}
