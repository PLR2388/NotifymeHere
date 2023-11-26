package fr.wonderfulappstudio.notifymehere.presentation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NotifyMeHereApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Create Notification Channel
        val channel = NotificationChannel(
            MAIN_CHANNEL_ID,
            MAIN_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    companion object {
        const val MAIN_CHANNEL_ID: String = "mainChannelNotificationId"
        const val MAIN_SERVICE_NOTIFICATION_ID: Int = 1
        private const val MAIN_CHANNEL_NAME: String = "Notify me"
    }
}