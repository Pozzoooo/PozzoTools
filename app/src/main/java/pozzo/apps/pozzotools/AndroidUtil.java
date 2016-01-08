package pozzo.apps.pozzotools;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import java.io.File;
import java.util.List;

/**
 * Utility tools related to Android.
 *
 * @author pozzo
 * @since 22/07/15.
 */
public class AndroidUtil {
	/**
	 * Hide keyaboard.
	 */
	public static void hideKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * Show keyboard.
	 */
	public static void showKeyboard(Context context, View view) {
		if(view.requestFocus()) {
			InputMethodManager keyboard = (InputMethodManager)
					context.getSystemService(Context.INPUT_METHOD_SERVICE);
			keyboard.showSoftInput(view, 0);
		}
	}

	/**
	 * Restart application.
	 */
	public static void restartApplication(Context context) {
		Intent startMain = context.getPackageManager()
				.getLaunchIntentForPackage(context.getApplicationContext().getPackageName());
		startMain.setFlags(context instanceof Activity
				? Intent.FLAG_ACTIVITY_CLEAR_TOP : Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(startMain);
	}

	/**
	 * Redirect to any link.
	 * It is not common to miss a browser, but it may happen especially in non personal devices.
	 *
	 * @return true if succed.
	 */
	public static boolean redirectLink(String link, Context context) {
		if(context == null)
			return false;

		Intent intent = redirectLinkIntent(link, context);
		PackageManager manager = context.getPackageManager();
		List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
		if (infos.size() > 0) {
			context.startActivity(intent);
			return false;
		}
		return true;
	}

	/**
	 * Intent para redirecionar para o link passado.
	 */
	public static Intent redirectLinkIntent(String link, Context context) {
		if(context == null)
			return null;

		return new Intent(Intent.ACTION_VIEW, Uri.parse(link));
	}

	/**
	 * @return true if you are connecting on a mobile network.
	 */
	public static boolean isMobileNetwork(Context context) {
		if (context == null)
			return false;

		ConnectivityManager manager = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null)
			return false;

		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return networkInfo != null && networkInfo.isConnected();
	}

	/**
	 * @return true se aparentemete houver conexao.
	 */
	public static boolean isNetworkAvailable(Context context) {
		if(context == null)
			return false;

		ConnectivityManager connectivityManager
				= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * Scroll to given position smoothly.
	 */
	public static void scrollTo(final ScrollView scrollView, final int bottom) {
		scrollView.postDelayed(new Runnable() {
			@Override
			public void run() {
				scrollView.smoothScrollTo(0, bottom);
			}
		}, 400);
	}

	/**
	 * Calls any compatible application to open the given file url (web or local).
	 *
	 * @return true if there is an app to open this path.
	 *
	 * @see FileUtil#getMimeType(String)
	 */
	public static boolean viewFile(String path, String mimeType, Context context) {
		Uri uri = Uri.parse(path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, mimeType);

		PackageManager manager = context.getPackageManager();
		List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
		if (infos.size() > 0) {
			context.startActivity(intent);
			return true;
		}
		return false;
	}

	/**
	 * Download single file using DownloadManager.
	 */
	public static File downloadFile(Context context, String path, Uri downloadUri, String fileName) {
		if(path == null || path.isEmpty())
			return null;

		File file = new File(path);
		if (!file.exists()) {
			try {
				DownloadManager downloadManager = (DownloadManager)
						context.getSystemService(Context.DOWNLOAD_SERVICE);
				DownloadManager.Request request = new DownloadManager.Request(downloadUri);

				request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
						| DownloadManager.Request.NETWORK_MOBILE);
				request.setAllowedOverRoaming(false);
				request.setTitle(fileName);
				request.setDescription(path);
				request.setDestinationUri(Uri.fromFile(file));
				request.allowScanningByMediaScanner();
				request.setNotificationVisibility(
						DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

				downloadManager.enqueue(request);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * To check the resource availability.
	 *
	 * @param context where we should request the Intent availability.
	 * @param action to be checked.
	 * @return If intent is available on the system.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
			final PackageManager packageManager = context.getPackageManager();
			final Intent intent = new Intent(action);
			List<ResolveInfo> list =
							packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
	}

	/**
	 * @return Smarthphone model.
	 */
	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return model;
		}
		return manufacturer + " " + model;
	}
}
