package fr.wonderfulappstudio.notifymehere.extension

import android.app.ActivityManager
import android.app.Notification
import android.content.Context
import android.widget.Toast
import fr.wonderfulappstudio.notifymehere.NotifyMeHereApplication
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.service.LocationService

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.isLocationServiceRunning(): Boolean {
    val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    // Get a list of running services
    val services = manager.getRunningServices(Integer.MAX_VALUE)
    // Check if your service is in the list
    return services.any { LocationService::class.java.name == it.service.className }
}

fun Context.createDefaultNotification(): Notification {
    val builder: Notification.Builder =
        Notification.Builder(this, NotifyMeHereApplication.MAIN_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.text_in_progress))
            .setSmallIcon(R.mipmap.ic_launcher)
    return builder.build()
}