package pozzo.apps.pozzotools;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Solves the call to a camera application and deal with return, if you ask.
 *
 * @since 2012
 * @author Luiz Gustavo Pozzo
 */
public class CameraUtil {
	private Context context;
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private Uri mCurrentPhotoPath;

	public CameraUtil(Context context) {
		this.context = context;
	}

	/**
	 * Take a picture and returns by onActivityResult and can be handled by
	 * 	{@link #handleCameraPhoto}.
	 * 
	 * @param resultCode to be tracked at onActivityResult.
	 * @throws UnsupportedOperationException If camera is not abailable.
	 */
	public void takeAPicture(int resultCode, Activity activity)
			throws IOException, UnsupportedOperationException {
		if(!isCameraAvailable()) {
			throw new UnsupportedOperationException("Camera not abailable");
		} else {//OK
			Intent cameraIntent = createPictureIntent();
			dispatchTakePictureIntent(cameraIntent, resultCode, activity);
		}
	}

	/**
	 * Take a picture and returns by onActivityResult and can be handled by
	 * 	{@link #handleCameraPhoto}.
	 *
	 * @param resultCode to be tracked at onActivityResult.
	 * @throws UnsupportedOperationException If camera is not abailable.
	 */
	public void takeAPicture(int resultCode, Fragment fragment)
			throws IOException, UnsupportedOperationException {
		if(!isCameraAvailable()) {
			throw new UnsupportedOperationException("Camera not abailable");
		} else {//OK
			Intent cameraIntent = createPictureIntent();
			dispatchTakePictureIntent(cameraIntent, resultCode, fragment);
		}
	}

	/**
	 * Take a picture and returns by onActivityResult and can be handled by
	 * 	{@link #handleCameraPhoto}.
	 *
	 * @param resultCode to be tracked at onActivityResult.
	 * @throws UnsupportedOperationException If camera is not abailable.
	 */
	public void takeAPicture(int resultCode, android.support.v4.app.Fragment fragment)
			throws IOException, UnsupportedOperationException {
		if(!isCameraAvailable()) {
			throw new UnsupportedOperationException("Camera not abailable");
		} else {//OK
			Intent cameraIntent = createPictureIntent();
			dispatchTakePictureIntent(cameraIntent, resultCode, fragment);
		}
	}

	/**
	 * @return true if camera is available, false if not =].
	 */
	public boolean isCameraAvailable() {
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
				&& AndroidUtil.isIntentAvailable(context, MediaStore.ACTION_IMAGE_CAPTURE);
	}

	/**
	 * Create a file on picture folder to be stored the shot from camera.
	 *
	 * @param filePrefix will be placed as a prefix on file name.
	 * @return File where you can save the camera picture.
	 * @throws IOException Problem creating the file.
	 */
	public static File createImageFile(String filePrefix) throws IOException {
		File mediaStore = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		if(!mediaStore.exists()) {
			if(!mediaStore.mkdirs())
				throw new IOException("Cant create directories and it does not exists");
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		String imageFileName = filePrefix + timeStamp;

		return File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, mediaStore);
	}

	/**
	 * Creates intent to request camera resource.
	 */
	private Intent createPictureIntent() throws IOException {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mCurrentPhotoPath = createTempPictureImage();

		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath);
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
					ActivityInfo.SCREEN_ORIENTATION_LOCKED);
		}
		return takePictureIntent;
	}

	/**
	 * @return Arquivo para se colocar uma imagem.
	 */
	public static Uri createTempPictureImage() throws IOException {
		File file = createImageFile("SocialBase_");
		return Uri.fromFile(file);
	}

	/**
	 * Sends the intent request.
	 */
	private void dispatchTakePictureIntent(Intent takePictureIntent, int actionCode, Activity activity) {
		activity.startActivityForResult(takePictureIntent, actionCode);
	}

	/**
	 * Sends the intent request.
	 */
	private void dispatchTakePictureIntent(Intent takePictureIntent, int actionCode, Fragment fragment) {
		fragment.startActivityForResult(takePictureIntent, actionCode);
	}

	/**
	 * Sends the intent request.
	 */
	private void dispatchTakePictureIntent(Intent takePictureIntent, int actionCode, android.support.v4.app.Fragment fragment) {
		fragment.startActivityForResult(takePictureIntent, actionCode);
	}

	/**
	 * @param intent received on onActivityResult.
	 * @return File path to the captured image.
	 */
	public Uri handleCameraPhoto(Intent intent, int resultCode) throws IOException {
		if(resultCode != Activity.RESULT_OK)
			return null;

		String filePrefix = "/file:";
		String path = mCurrentPhotoPath.toString();
		File testFile = new File(path.substring(filePrefix.length()));
		if(testFile.length() <= 0)//Some brands change its kind of return...
			mCurrentPhotoPath = intent.getData();

		return Uri.parse(ImageUtil.checkSizeAndRotation(mCurrentPhotoPath.toString(), context));
	}
}
