package com.musa.musakeys.utility

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.services.OrientationListenerRegistrationService

object MusaServiceUtility {

    private fun isOrientationChangeListenerServiceRunning(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val runningServices = activityManager?.getRunningServices(Int.MAX_VALUE)
        runningServices?.let {
            for (service in it) {
                if (OrientationListenerRegistrationService::class.java.name == service.service.className) {
                    return true
                }
            }
        }
        return false
    }

    private fun startMusaPhoneBackgroundService(context: Context) {
        if (!isOrientationChangeListenerServiceRunning(context)) {
            val intent = Intent(context, OrientationListenerRegistrationService::class.java).apply {
                action = MusaConstants.START_FOREGROUND_ACTION
            }
            context.startForegroundService(intent)
        }
    }

    fun stopMusaPhoneBackgroundService(context: Context) {
        val intent = Intent(context, OrientationListenerRegistrationService::class.java).apply {
            action = MusaConstants.STOP_FOREGROUND_ACTION
        }
        context.startService(intent)
    }

    fun startBackgroundServiceAsRequired(context: Context) {
        startMusaPhoneBackgroundService(context)
    }
}

