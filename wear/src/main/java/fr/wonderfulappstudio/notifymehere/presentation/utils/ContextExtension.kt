package fr.wonderfulappstudio.notifymehere.presentation.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.presentation.service.LocationService

fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        val uri = Uri.fromParts("package", getString(R.string.package_name), null)
        data = uri
    }
    this.startActivity(intent)
}

fun Context.isLocationServiceRunning(): Boolean {
    val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    // Get a list of running services
    val services = manager.getRunningServices(Integer.MAX_VALUE)
    // Check if your service is in the list
    return services.any { LocationService::class.java.name == it.service.className }
}