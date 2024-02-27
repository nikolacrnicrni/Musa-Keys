package com.musa.musakeys.utility

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.services.OrientationListenerRegistrationService

object MusaServiceUtility {

    private fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

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
        if (!isOrientationChangeListenerServiceRunning(context) && hasLocationPermission(context)) {
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

