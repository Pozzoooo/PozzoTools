package pozzo.apps.tools;

import junit.framework.TestCase;

/**
 * Testig File Util.
 *
 * Created by sarge on 24/12/15.
 */
public class FileUtilTest extends TestCase {

	public void testGetFileExtension() throws Exception {
		String path = "/storage/files/file1.txt";
		String extension = FileUtil.getFileExtension(path);
		assertEquals("txt", extension);
	}

	/**
	 * Cant test without mock Android.
	 */
//	public void testGetMimeType() throws Exception {
//		String path = "/storage/files/file1.txt";
//		String mime = FileUtil.getMimeType(path);
//		assertEquals("text/plain", mime);
//	}
}