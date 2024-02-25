package com.musa.musakeys.ui.musa_phone

import com.musa.musakeys.R
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.musa.musakeys.constants.SharedPreferenceEnum
import com.musa.musakeys.utility.MusaServiceUtility.startBackgroundServiceAsRequired
import com.musa.musakeys.utility.SharedPreferenceHelperUtil.getSharedPreferenceBoolean
import com.musa.musakeys.utility.SharedPreferenceHelperUtil.updateSharedPreferenceBoolean


object MusaClipboardModePopup {
    fun renderPopup(activity: Activity) {
        if (getSharedPreferenceBoolean(
                activity,
                SharedPreferenceEnum.CLIPBOARD_POPUP_NO_SHOW,
                false
            )
        ) {
            startClipboardService(activity)
            return
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflate: View =
            activity.layoutInflater.inflate(R.layout.clipboard_help_screen, null as ViewGroup?)
        (inflate.findViewById<View>(R.id.got_it) as Button).setOnClickListener {
            startClipboardService(
                activity
            )
        }
        (inflate.findViewById<View>(R.id.no_popup) as Button).setOnClickListener {
            updateSharedPreferenceBoolean(
                activity,
                SharedPreferenceEnum.CLIPBOARD_POPUP_NO_SHOW,
                true
            )
            startClipboardService(activity)
        }
        builder.setView(inflate)
        builder.setCancelable(false)
    }

    /* access modifiers changed from: private */
    fun startClipboardService(activity: Activity) {
        startBackgroundServiceAsRequired(activity)
        activity.finish()
    }
}
