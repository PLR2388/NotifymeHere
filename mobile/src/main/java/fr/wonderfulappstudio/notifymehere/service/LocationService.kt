package fr.wonderfulappstudio.notifymehere.service

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import fr.wonderfulappstudio.common.manager.DataStoreManager
import fr.wonderfulappstudio.notifymehere.MainActivity
import fr.wonderfulappstudio.notifymehere.MainActivity.Companion.locationIdKey
import fr.wonderfulappstudio.notifymehere.NotifyMeHereApplication.Companion.MAIN_CHANNEL_ID
import fr.wonderfulappstudio.notifymehere.NotifyMeHereApplication.Companion.MAIN_SERVICE_NOTIFICATION_ID
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.extension.createDefaultNotification
import fr.wonderfulappstudio.notifymehere.model.InterestPoint
import fr.wonderfulappstudio.notifymehere.repository.InterestPointRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    @Inject
    lateinit var interestPointRepository: InterestPointRepository

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val locationRequest: LocationRequest = LocationRequest.Builder(10000L).build()

    private lateinit var interestPoints: List<InterestPoint>
    private var notificationDistance: Float = 500.0f

    override fun onBind(p0: Intent?): IBinder? = null


    override fun onCreate() {
        super.onCreate()
        serviceScope.launch {
            interestPointRepository.interestPoints.collect { interestPoints ->
                // Handle the updated list of interest points
                handleInterestPoints(interestPoints)
            }
        }

        serviceScope.launch {
            dataStoreManager.readNotificationDistance.collect { distance ->
                Log.d("LocationService", "Collected notification distance: $distance")
                handleNotificationDistance(distance)
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupLocationCallback()
    }

    private fun handleNotificationDistance(distance: Float) {
        this.notificationDistance = distance
    }

    private fun handleInterestPoints(interestPoints: List<InterestPoint>) {
        val currentDateTimestamp = Date().time
        // Do something with the list of interest points
        this.interestPoints =
            interestPoints.filter {
                !it.alreadyNotify &&
                        (it.startDate == null || it.startDate >= currentDateTimestamp) &&
                        (it.endDate == null || it.endDate <= currentDateTimestamp)
            }
    }

    fun sendNotification(title: String, content: String, id: Int) {
        try {
            // Create an explicit intent for an Activity in your app
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            intent.putExtra(locationIdKey, id)

            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

            // Build the notification
            val builder = NotificationCompat.Builder(context, MAIN_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            // Show the notification
            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                notify(MAIN_SERVICE_NOTIFICATION_ID, builder)
            }
        } catch (e: Exception) {
            Log.e("ERROR_BIG", e.toString())
        }
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val lastLocation = p0.lastLocation ?: return
                Log.e("LOCATION SERVICE", "notificationDistance=${notificationDistance}")
                for (interestPoint in interestPoints) {
                    if (interestPoint.position.distanceTo(lastLocation) < notificationDistance) {
                        interestPoint.id?.let {
                            sendNotification(
                                "You're near an interest point!", interestPoint.name,
                                it
                            )
                        }
                    }
                }
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
        startForeground(MAIN_SERVICE_NOTIFICATION_ID, createDefaultNotification())
        startLocationUpdates()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)

        // Cancel the coroutine scope when the service is destroyed
        serviceScope.cancel()
    }

}