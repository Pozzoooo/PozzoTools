package pozzo.apps.tools;

import android.graphics.BitmapFactory;

import junit.framework.TestCase;

/**
 * Some tests on ImageUtil.
 */
public class ImageUtilTest extends TestCase {

	public void testCalculateInSampleSize() throws Exception {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.outHeight = 320;
		options.outWidth = 320;

		int sampleSize = ImageUtil.calculateInSampleSize(options, 160, 160);
		assertEquals(2, sampleSize);
	}

	public void testGetImagePath() throws Exception {
		//TODO I need to get a sample from some cool devices and also a Samsung
	}
}
