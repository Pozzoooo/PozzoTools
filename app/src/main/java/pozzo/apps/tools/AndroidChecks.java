package pozzo.apps.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Some checks that may be used to create a better experience an control by user side.
 *
 * @author pozzo
 * @since 13/08/15.
 */
public class AndroidChecks {

	/**
	 * Show a dialog requesting if users wants to keep on with operation if bytes to be sent are
	 * 	greater than a cetain limit.
	 *
	 * 	@param bytes to be sent.
	 */
	public void checkUserWantsToSendWithSize(final Context context, long bytes,
											 final DialogInterface.OnClickListener onPositive,
											 final DialogInterface.OnClickListener onNegative,
											 final String alwaysText,
											 final String hugePostCostWarning) {
		long warningSize = 1024 * 1024;
		if(!AndroidPrefs.isShowMobileNetworkWarn(context)
				|| !AndroidUtil.isMobileNetwork(context)
				|| bytes < warningSize) {
			onPositive.onClick(null, 0);
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setPositiveButton(android.R.string.ok, onPositive);
		builder.setNegativeButton(android.R.string.cancel, onNegative);
		builder.setNeutralButton(alwaysText, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AndroidPrefs.setShowMobileNetworkWarn(context, true);
				onPositive.onClick(dialog, which);
			}
		});
		builder.setTitle(hugePostCostWarning);
		builder.create().show();
	}

	/**
	 * Get the network info
	 * @param context
	 * @return
	 */
	public static NetworkInfo getNetworkInfo(Context context){
		ConnectivityManager cm = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}

	/**
	 * Check if the connection is fast
	 * @param type
	 * @param subType
	 * @return
	 */
	public static boolean isConnectionFast(int type, int subType){
		if(type == ConnectivityManager.TYPE_WIFI) {
			return true;
		} else if(type == ConnectivityManager.TYPE_MOBILE) {
			switch(subType) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					return false; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return false; // ~ 14-64 kbps
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return false; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					return true; // ~ 400-1000 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					return true; // ~ 600-1400 kbps
				case TelephonyManager.NETWORK_TYPE_GPRS:
					return false; // ~ 100 kbps
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					return true; // ~ 2-14 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPA:
					return true; // ~ 700-1700 kbps
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					return true; // ~ 1-23 Mbps
				case TelephonyManager.NETWORK_TYPE_UMTS:
					return true; // ~ 400-7000 kbps
				/*
				 * Above API level 7, make sure to set android:targetSdkVersion
				 * to appropriate level to use these
				 */
				case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
					return true; // ~ 1-2 Mbps
				case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
					return true; // ~ 5 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
					return true; // ~ 10-20 Mbps
				case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
					return false; // ~25 kbps
				case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
					return true; // ~ 10+ Mbps
				// Unknown
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				default:
					return false;
			}
		} else {
			return false;
		}
	}
}
