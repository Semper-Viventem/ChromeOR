package ru.semper_viventem.chromeor.presentation.dialog

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.widget.ImageButton
import android.widget.TextView
import com.pawegio.kandroid.find
import com.pawegio.kandroid.inflateLayout
import ru.semper_viventem.chromeor.R
import ru.semper_viventem.chromeor.presentation.model.LoginEntity


/**
 * @author Kulikov Konstantin
 * @since 25.01.2017.
 */
class DialogManager(
        val mContext: Context
    ) {

    var mProgressDialog: ProgressDialog = ProgressDialog(mContext)

    fun showInformationDialog(title: String, text: String) {
        AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(R.string.close, { dialog, which -> })
                .show()
    }

    fun showProgressDialog(title: String, text: String) {
        mProgressDialog.setTitle(title)
        mProgressDialog.setMessage(text)
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog.isIndeterminate = true
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.hide()
    }

    fun showLoginDetails(loginEntity: LoginEntity) {
        val builderdialog = AlertDialog.Builder(mContext)
        val view = mContext.inflateLayout(R.layout.dialog_edit, attachToRoot = false)

        view.find<TextView>(R.id.acctionEditText).text = loginEntity.actionUrl
        view.find<TextView>(R.id.originEditText).text = loginEntity.originUrl
        view.find<TextView>(R.id.usernameEditText).text = loginEntity.usernameValue
        view.find<TextView>(R.id.passwordEditText).text = loginEntity.passwordValue
        view.find<ImageButton>(R.id.vButtonNext).setOnClickListener {
            val inBrowserIntent = Intent(Intent.ACTION_VIEW)
            inBrowserIntent.data = Uri.parse(loginEntity.originUrl)
            mContext.startActivity(inBrowserIntent)
        }

        builderdialog.setView(view)
        builderdialog.setPositiveButton(R.string.close) { dialog, which ->
            //TODO что-то
        }

        builderdialog
                .create()
                .show()
    }

}