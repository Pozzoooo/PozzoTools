package pozzo.apps.tools.android

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ScrollView

class NullAndroid : Android {
    override fun openUrl(link: String, context: Context) = true
    override fun hideKeyboard(context: Context, view: View) { }
    override fun showKeyboard(context: Context, view: View) { }
    override fun restartApplication(context: Context) { }
    override fun redirectToLink(link: String, context: Context) = true
    override fun redirectToLinkIntent(link: String, context: Context) =  Intent()
    override fun scrollTo(scrollView: ScrollView, bottom: Int) { }
    override fun openFileInThirdApp(path: String, mimeType: String, context: Context) = true
    override fun isIntentAvailable(context: Context, action: String) = true
    override fun getDeviceName() = "Null"
    override fun errorMessage(context: Context, errorMessage: String, errorTitle: Int, okButton: Int): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }
}
