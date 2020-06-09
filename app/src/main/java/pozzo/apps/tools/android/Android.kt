package pozzo.apps.tools.android

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ScrollView

interface Android {
    companion object {
        var instance: Android = AndroidUtil()

        operator fun invoke() = instance
    }

    fun openUrl(link: String, context: Context): Boolean
    fun hideKeyboard(context: Context, view: View)
    fun showKeyboard(context: Context, view: View)
    fun restartApplication(context: Context)
    fun redirectToLink(link: String, context: Context): Boolean
    fun redirectToLinkIntent(link: String, context: Context): Intent
    fun scrollTo(scrollView: ScrollView, bottom: Int)
    fun openFileInThirdApp(path: String, mimeType: String, context: Context): Boolean
    fun isIntentAvailable(context: Context, action: String): Boolean
    fun getDeviceName(): String
    fun errorMessage(context: Context, errorMessage: String, errorTitle: Int, okButton: Int):
            AlertDialog.Builder
}
