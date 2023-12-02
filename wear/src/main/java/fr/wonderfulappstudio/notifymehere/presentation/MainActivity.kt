package fr.wonderfulappstudio.notifymehere.presentation

import android.Manifest
import android.R
import android.app.Notification
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.AndroidEntryPoint
import fr.wonderfulappstudio.notifymehere.presentation.service.LocationService
import fr.wonderfulappstudio.notifymehere.presentation.theme.NotifyMeHereTheme
import fr.wonderfulappstudio.notifymehere.presentation.ui.Details
import fr.wonderfulappstudio.notifymehere.presentation.ui.Main
import fr.wonderfulappstudio.notifymehere.presentation.ui.Settings
import fr.wonderfulappstudio.notifymehere.presentation.ui.details.DetailsScreen
import fr.wonderfulappstudio.notifymehere.presentation.ui.main.MainScreen
import fr.wonderfulappstudio.notifymehere.presentation.ui.main.MainViewModel
import fr.wonderfulappstudio.notifymehere.presentation.ui.settings.SettingsScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val mainViewModel by viewModels<MainViewModel>()

    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        mainViewModel.updateGrantNotificationPermission(granted)
    }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var allPermissionsGranted = true
            permissions.entries.forEach {
                val isGranted = it.value
                allPermissionsGranted = allPermissionsGranted && isGranted
            }

            if (allPermissionsGranted) {
                // All permissions are granted. You can start the location service.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    requestBackgroundLocationPermission()
                } else {
                    startLocationService()
                }
            } else {
                // Permissions denied. You can show a message to the user explaining why you need the permission.
            }
        }

    private val backgroundLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission is granted. You can perform the operation that requires the permission.
                startLocationService()
            } else {
                // Permission is denied. You can show a message to the user explaining why you need the permission.
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isPermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        mainViewModel.updateGrantNotificationPermission(isPermissionGranted)
        if (!isPermissionGranted) {
            pushNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val interestPointId = intent.getIntExtra(locationIdKey, -1)

        val detailsId = if (interestPointId == -1) null else interestPointId

        requestPermissions()
        setContent {
            WearApp(mainViewModel, detailsId)
        }
    }

    private fun requestPermissions() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                // You can now request the background location permission
                requestBackgroundLocationPermission()
            }

            else -> {
                // Request fine location first
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Starting from Android 11 (API level 30), you can request the background location permission.
            backgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        } else {
            /*Toast.makeText(
                this,
                "Background location access is not available on this device.",
                Toast.LENGTH_SHORT
            ).show()*/
        }
    }


    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }


    override fun onResume() {
        super.onResume()
        dataClient.addListener(mainViewModel)
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(mainViewModel)
    }

    companion object {
        const val locationIdKey = "location_id"
    }
}

@Composable
fun WearApp(viewModel: MainViewModel, detailsId: Int?) {
    val navController = rememberSwipeDismissableNavController()

    // Use LaunchedEffect to navigate once after composition
    LaunchedEffect(Unit) {
        if (detailsId != null) {
            navController.navigate(Details.route + "/${detailsId}/${true}")
        }
    }

    NotifyMeHereTheme {
        SwipeDismissableNavHost(
            navController = navController,
            startDestination = Main.route
        ) {
            composable(Main.route) {
                MainScreen(viewModel = viewModel, onNavigateToDetails = {
                    navController.navigate(Details.route + "/${it.id}/${false}")
                }, onNavigateToSettings = {
                    navController.navigate(Settings.route)
                })
            }
            composable(
                Details.route + "/{detailsId}/{stopNotification}",
                arguments = listOf(navArgument("detailsId") {
                    defaultValue = -1
                    type = NavType.IntType
                }, navArgument("stopNotification") {
                    defaultValue = false
                    type = NavType.BoolType
                })
            ) {
                DetailsScreen {
                    navController.popBackStack()
                }
            }
            composable(Settings.route) {
                SettingsScreen()
            }
        }
    }
}