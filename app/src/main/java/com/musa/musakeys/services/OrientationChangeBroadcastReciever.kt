package com.musa.musakeys.services

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import com.musa.musakeys.ui.welcome.WelcomeMusaActivity
import com.musa.musakeys.constants.MusaConstants

class OrientationChangeBroadcastReceiver : BroadcastReceiver() {
    @SuppressLint("WrongConstant")
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val str = clipboardManager?.primaryClip?.takeIf {
                    it.description.hasMimeType("text/html") || it.description.hasMimeType("text/plain")
                }?.getItemAt(0)?.text?.toString() ?: ""

                val intent2 = Intent(context, WelcomeMusaActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra(MusaConstants.COPIED_TEXT, str)
                    putExtra(MusaConstants.BROADCAST_RECEIVER_INTENT, true)
                }

                context.startActivity(intent2)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
