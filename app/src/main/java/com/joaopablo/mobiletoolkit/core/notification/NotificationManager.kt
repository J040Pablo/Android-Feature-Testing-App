package com.joaopablo.mobiletoolkit.core.notification

import android.app.NotificationChannel
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/**
 * Manager to send local notifications using NotificationManagerCompat.
 * Handles NotificationChannel creation (Android 8+) and POST_NOTIFICATIONS
 * permission checks (Android 13+).
 */
class NotificationManager(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "mobile_toolkit_local"
        const val CHANNEL_NAME = "Mobile Toolkit"
        const val CHANNEL_DESC = "Notificações locais do Mobile Toolkit"
        const val NOTIFICATION_ID = 1001
    }

    /** Creates the notification channel required on Android 8+. Safe to call multiple times. */
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESC
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as android.app.NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Returns true if the app has permission to post notifications.
     * On Android < 13 this always returns true.
     */
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * Sends a local notification to the system tray.
     * Caller must ensure the channel is created and permission is granted before calling this.
     *
     * @return true if the notification was posted, false if permission is missing.
     */
    fun sendLocalNotification(title: String, message: String): Boolean {
        if (!hasNotificationPermission()) return false

        createNotificationChannel()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        return true
    }
}
