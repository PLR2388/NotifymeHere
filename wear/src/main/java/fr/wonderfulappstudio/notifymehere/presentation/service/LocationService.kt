package fr.wonderfulappstudio.notifymehere.presentation.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import fr.wonderfulappstudio.notifymehere.presentation.NotifyMeHereApplication
import fr.wonderfulappstudio.notifymehere.presentation.NotifyMeHereApplication.Companion.MAIN_SERVICE_NOTIFICATION_ID

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val locationRequest: LocationRequest = LocationRequest.Builder(10000L).build()
    override fun onBind(p0: Intent?): IBinder? = null


    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupLocationCallback()
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                Log.d("NEW LOCATION", p0.lastLocation.toString())
            }
        }
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null /* Looper */
            )
        } catch (unlikely: SecurityException) {
            // Log or handle the exception
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(MAIN_SERVICE_NOTIFICATION_ID, createNotification())
        startLocationUpdates()
        return START_NOT_STICKY
    }

    private fun createNotification(): Notification {
        val builder: Notification.Builder =
            Notification.Builder(this, NotifyMeHereApplication.MAIN_CHANNEL_ID)
                .setContentTitle("Notify me Here!")
                .setContentText("Running...")
        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}