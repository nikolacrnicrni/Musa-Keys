package com.musa.musakeys.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.musa.musakeys.R
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.ui.welcome.WelcomeMusaActivity

class OrientationListenerRegistrationService : Service() {
    private val receiver by lazy { OrientationChangeBroadcastReciever() }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        registerReceiver(receiver, IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let {
            when (it) {
                MusaConstants.START_FOREGROUND_ACTION -> startForegroundService()
                MusaConstants.STOP_FOREGROUND_ACTION -> stopForegroundService()
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun startForegroundService() {
        val notificationBuilder = getNotificationBuilder("myChannelId")
            .setContentTitle(getString(R.string.musa_keys))
            .setOngoing(true)
            .setSmallIcon(R.drawable.notification_icon_transparent)
            .setContentIntent(getDefaultPendingIntent())

        startForeground(100, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }

    private fun getNotificationBuilder(channelId: String): NotificationCompat.Builder {
        prepareChannel(channelId, NotificationManager.IMPORTANCE_DEFAULT)
        return NotificationCompat.Builder(this, channelId)
    }

    private fun prepareChannel(channelId: String, importance: Int) {
        val appName = getString(R.string.app_name)
        val description = "Channel Description"

        val channel = NotificationChannel(channelId, appName, importance).apply {
            this.description = description
            setShowBadge(false)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun getDefaultPendingIntent(): PendingIntent {
        val intent = Intent(this, WelcomeMusaActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }
}
