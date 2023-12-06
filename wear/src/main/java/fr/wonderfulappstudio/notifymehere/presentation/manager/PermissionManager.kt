package fr.wonderfulappstudio.notifymehere.presentation.manager

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

interface PermissionResultCallback {
    fun onPermissionResult(granted: Boolean)
}

interface MultiplePermissionsResultCallback {
    fun onPermissionsResult(grantedPermissions: Map<String, Boolean>)
}


class PermissionManager(private val activity: ComponentActivity) {

    private var notificationPermissionCallback: PermissionResultCallback? = null
    private var locationPermissionsCallback: MultiplePermissionsResultCallback? = null
    private var backgroundLocationPermissionsCallback: PermissionResultCallback? = null

    private val pushNotificationPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        notificationPermissionCallback?.onPermissionResult(granted)
    }

    private val locationPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            locationPermissionsCallback?.onPermissionsResult(permissions)
        }

    private val backgroundLocationPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            backgroundLocationPermissionsCallback?.onPermissionResult(isGranted)
        }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    fun requestNotificationPermission(callback: PermissionResultCallback) {
        notificationPermissionCallback = callback

        val isGranted = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!isGranted) {
            pushNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    fun requestLocationPermissions(callback: MultiplePermissionsResultCallback) {
        locationPermissionsCallback = callback

        val isGranted = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        if (isGranted) {
            callback.onPermissionsResult(mapOf(Pair(Manifest.permission.ACCESS_FINE_LOCATION, true), Pair(Manifest.permission.ACCESS_COARSE_LOCATION, true)))
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    fun requestBackgroundLocationPermissions(callback: PermissionResultCallback) {
        backgroundLocationPermissionsCallback = callback

        val isGranted = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        if (!isGranted) {
            backgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }
}