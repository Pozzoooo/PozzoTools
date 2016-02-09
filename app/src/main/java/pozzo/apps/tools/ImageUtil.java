package pozzo.apps.tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Image utilities tools.
 *
 * @since 2012
 * @author Luiz Gustavo Pozzo
 */
public class ImageUtil {
    private static final int MAX_RESOLUTION_H = 2048;
    private static final int MAX_RESOLUTION_W = 2048;
    private static final int TRASHHOLD = 1024 * 1024;
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    /**
     * Checks the image rotation and changes it to a new file if needed to the default limit.
     *
     * @param path Current file path.
     * @param context para criacao de imagem temporaria.
     * @return New path file or the same if not rotation needed.
     * @throws IOException Can't read/write?
     */
    public static String checkSizeAndRotation(String path, Context context)
            throws IOException {
        return checkSizeAndRotation(path, context, -361);
    }

	/**
	 * Checks the image rotation and changes it to a new file if needed to the default limit.
	 *
	 * @param path Current file path.
	 * @param context para criacao de imagem temporaria.
	 * @return New path file or the same if not rotation needed.
	 * @throws IOException Can't read/write?
	 */
	public static String checkSizeAndRotation(String path, Context context, int maxWidth, int maxHeigth)
			throws IOException {
		return checkSizeAndRotation(path, context, -361, maxWidth, maxHeigth);
	}

	/**
	 * Checks the image rotation and changes it to a new file if needed to the default limit.
	 * Uses default width and heigth. MAX_RESOLUTION_W, MAX_RESOLUTION_H.
	 *
	 * @param path Current file path.
	 * @param context para criacao de imagem temporaria.
	 * @param rotation rotacao do arquivo, ou -361 para desconhecido.
	 * @return New path file or the same if not rotation needed.
	 * @throws IOException Can't read/write?
	 */
	public static String checkSizeAndRotation(String path, Context context, int rotation)
			throws IOException {
		return checkSizeAndRotation(path, context, rotation, MAX_RESOLUTION_W, MAX_RESOLUTION_H);
	}

    /**
     * Checks the image rotation and changes it to a new file if needed to the default limit.
     *
     * @param path Current file path.
     * @param context para criacao de imagem temporaria.
     * @param rotation rotacao do arquivo, ou -361 para desconhecido.
	 * @param maxWidth max image width (can be shorter).
	 * @param maxHeigth max image heigth (can be shorter).
     * @return New path file or the same if not rotation needed.
     * @throws IOException Can't read/write?
     */
    public static String checkSizeAndRotation(
			String path, Context context, int rotation, int maxWidth, int maxHeigth)
            throws IOException {
        if(path == null)
            return null;

        if(rotation == -361)
            rotation = getRotation(path);
        //We first load sample to check size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        if(rotation == 0 || rotation == 180) {//Straight or upside down
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeigth);
        } else {//Otherwise we just switch the width and height
            options.inSampleSize = calculateInSampleSize(options, maxHeigth, maxWidth);
        }

        //If not needed to resize or rotate, let it as is
        if(options.inSampleSize != 1 || rotation != 0) {
            //Then full load it in sample size
            options.inJustDecodeBounds = false;
            File outputDir = context.getCacheDir();
            String tempPath = File.createTempFile(
					"temp", JPEG_FILE_SUFFIX, outputDir).getAbsolutePath();
            Bitmap source = BitmapFactory.decodeFile(path, options);
            Bitmap targetBitmap;

            //Rotate
            if(rotation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);
                targetBitmap = Bitmap.createBitmap(source, 0, 0,
						source.getWidth(), source.getHeight(), matrix, true);
            } else {//Or just resize
                targetBitmap = Bitmap.createBitmap(
						source, 0, 0, source.getWidth(), source.getHeight());
            }

            //Save
            FileOutputStream out = new FileOutputStream(tempPath);
            targetBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            path = tempPath;

            //Recycle
            source.recycle();
            targetBitmap.recycle();
        }

        return path;
    }

	/**
	 * Generated a bitmap thumbnail from given image path.
	 */
	public static Bitmap generateThumb(String path, int maxWidth, int maxHeigth) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		//Then full load it in sample size
		options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeigth);
		options.inJustDecodeBounds = false;
		Bitmap source = BitmapFactory.decodeFile(path, options);
		Bitmap targetBitmap;

		if(source == null)
			return null;//Too much memory?

		try {
			targetBitmap = Bitmap.createBitmap(
					source, 0, 0, source.getWidth(), source.getHeight());
		} catch (OutOfMemoryError e) {
			return null;
		}
		return targetBitmap;
	}

    /**
     * Tries to get the image rotation.
     *
     * http://sylvana.net/jpegcrop/exif_orientation.html
     *
     * @param path Caminho para a imagem a ser rotacionada.
     * @return Rotacao em graus.
     * @throws IOException Nao conseguiu acessar o arquivo.
     */
    public static int getRotation(String path) throws IOException {
        if(path == null)
            return 0;

        int rotation = 0;
        ExifInterface exif = new ExifInterface(path);
        String rot = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        if(rot != null)
            rotation = Integer.parseInt(rot);

        switch (rotation) {
            case ExifInterface.ORIENTATION_NORMAL:
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL://Espelhado horizontal
                rotation = 0;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
            case ExifInterface.ORIENTATION_FLIP_VERTICAL://Espelhado vertical
                rotation = 180;
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE://270
            case ExifInterface.ORIENTATION_ROTATE_270://Transpose espelhado
                rotation = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
            case ExifInterface.ORIENTATION_TRANSVERSE://90 Espelhado
                rotation = 90;
                break;
            case ExifInterface.ORIENTATION_UNDEFINED:
            default:
                //Para valores em graus (0, 180, 270...)
                break;
        }

        return rotation;
    }

    /**
     * Calculates a sample to load.
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        long maximum = Runtime.getRuntime().maxMemory();
        long total = Runtime.getRuntime().totalMemory();
        long freeMem = maximum - total;
        long goingToUse = reqWidth * reqHeight * 4;

//		What if there is no more free memory!?
        if((goingToUse * 2 + TRASHHOLD) > freeMem) {
            boolean widthOrHeigth = reqWidth > reqHeight;

            long resultSize = freeMem / 4;
            resultSize = (long) Math.sqrt(resultSize);

            if(widthOrHeigth) {
                reqWidth = (int) resultSize;
            } else {
                reqHeight = (int) resultSize;
            }
        }

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * Get images path from a Uri... (as the one returned by gallery).
	 *
	 * @throws IllegalArgumentException missing MediaStore.Images.Media.DATA field?
     */
    public static String getImagePath(Uri uri, Context ctx) throws IllegalArgumentException {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				String path = getRealPathFromURI_API19(ctx, uri);
				if(path != null)
					return path;
			} catch (RuntimeException e) {
				//Maybe thats just a old style...
			}
        }
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
			if(columnIndex >= 0) {
				cursor.moveToFirst();
				path = cursor.getString(columnIndex);
			}
            cursor.close();
        }

		if(path == null){
            path = uri.getPath();
        }

        return path;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				column, sel, new String[]{id}, null);
		if(cursor == null)
			return null;

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
}
