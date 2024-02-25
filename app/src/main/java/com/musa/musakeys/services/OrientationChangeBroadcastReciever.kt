package com.musa.musakeys.services

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.musa.musakeys.ui.welcome.WelcomeMusaActivity
import com.musa.musakeys.constants.MusaConstants

class OrientationChangeBroadcastReciever : BroadcastReceiver() {
    @SuppressLint("WrongConstant")
    override fun onReceive(context: Context, intent: Intent) {
        val str: String
        if (context.resources.configuration.orientation == 2) {
            val clipboardManager: ClipboardManager =
                context.getSystemService("android.content.Context.CLIPBOARD_SERVICE") as ClipboardManager
            str =
                if (!clipboardManager.hasPrimaryClip() || !clipboardManager.primaryClipDescription
                        ?.hasMimeType("text/html")!! && !clipboardManager.primaryClipDescription
                        ?.hasMimeType("text/plain")!!
                ) {
                    ""
                } else {
                    clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
                }
            val intent2 = Intent(context, WelcomeMusaActivity::class.java)
            intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent2.putExtra(MusaConstants.COPIED_TEXT, str)
            intent2.putExtra(MusaConstants.BROADCAST_RECEIVER_INTENT, true)
            context.startActivity(intent2)
            context.packageManager
            intent2.putExtra(MusaConstants.COPIED_TEXT, "")
            intent2.putExtra(MusaConstants.BROADCAST_RECEIVER_INTENT, true)
            context.applicationContext.startActivity(intent2)
        }
    }
}
