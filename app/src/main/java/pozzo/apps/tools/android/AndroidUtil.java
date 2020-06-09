package pozzo.apps.tools.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import java.util.List;

import pozzo.apps.tools.FileUtil;

/**
 * Utility tools related to Android.
 */
class AndroidUtil implements Android {
    /**
     * Hide keyaboard.
     */
    @Override
    public void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Show keyboard.
     */
    @Override
    public void showKeyboard(Context context, View view) {
        if (view.requestFocus()) {
            InputMethodManager keyboard = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(view, 0);
        }
    }

    /**
     * Restart application.
     */
    @Override
    public void restartApplication(Context context) {
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
    @Override
    public boolean redirectToLink(String link, Context context) {
        if (context == null)
            return false;

        Intent intent = redirectToLinkIntent(link, context);
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
    @Override
    public Intent redirectToLinkIntent(String link, Context context) {
        if (context == null)
            return null;

        return new Intent(Intent.ACTION_VIEW, Uri.parse(link));
    }

    /**
     * Scroll to given position smoothly.
     */
    @Override
    public void scrollTo(final ScrollView scrollView, final int bottom) {
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
     * @see FileUtil#getMimeType(String)
     */
    @Override
    public boolean openFileInThirdApp(String path, String mimeType, Context context) {
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
     * To check the resource availability.
     *
     * @param context where we should request the Intent availability.
     * @param action  to be checked.
     * @return If intent is available on the system.
     */
    @Override
    public boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * @return Smarthphone model.
     */
    @Override
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }

    /**
     * Redirect to any link.
     *
     * @return true if succeed.
     */
    @Override
    public boolean openUrl(String link, Context context) {
        if (context == null)
            return false;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PackageManager manager = context.getPackageManager();
        List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
        if (infos.size() > 0) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * Exibe uma mensagem de erro.
     */
    @Override
    public AlertDialog.Builder errorMessage(
            Context context, String errorMessage, int errorTitle, int okButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(errorTitle);
        builder.setMessage(errorMessage);
        builder.setPositiveButton(okButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        return builder;
    }
}
