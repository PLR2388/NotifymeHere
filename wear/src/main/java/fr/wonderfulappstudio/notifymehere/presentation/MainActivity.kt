package fr.wonderfulappstudio.notifymehere.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.AndroidEntryPoint
import fr.wonderfulappstudio.common.manager.MultiplePermissionsResultCallback
import fr.wonderfulappstudio.common.manager.PermissionManager
import fr.wonderfulappstudio.common.manager.PermissionResultCallback
import fr.wonderfulappstudio.notifymehere.presentation.service.LocationService
import fr.wonderfulappstudio.notifymehere.presentation.theme.NotifyMeHereTheme
import fr.wonderfulappstudio.notifymehere.presentation.ui.Details
import fr.wonderfulappstudio.notifymehere.presentation.ui.Main
import fr.wonderfulappstudio.notifymehere.presentation.ui.Settings
import fr.wonderfulappstudio.notifymehere.presentation.ui.composable.CustomAlert
import fr.wonderfulappstudio.notifymehere.presentation.ui.details.DetailsScreen
import fr.wonderfulappstudio.notifymehere.presentation.ui.main.AlertType
import fr.wonderfulappstudio.notifymehere.presentation.ui.main.MainScreen
import fr.wonderfulappstudio.notifymehere.presentation.ui.main.MainViewModel
import fr.wonderfulappstudio.notifymehere.presentation.ui.settings.SettingsScreen
import fr.wonderfulappstudio.notifymehere.presentation.utils.isLocationServiceRunning


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var permissionManager: PermissionManager

    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionManager = PermissionManager(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionManager.requestNotificationPermission(object : PermissionResultCallback {
                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        requestLocationPermissions()
                    } else {
                        mainViewModel.displayNotificationPermissionNotGranted()
                    }
                }
            })
        } else {
            requestLocationPermissions()
        }

        activateLocationServiceIfNecessary()

        val interestPointId = intent.getIntExtra(locationIdKey, -1)

        val detailsId = if (interestPointId == -1) null else interestPointId

        setContent {
            WearApp(mainViewModel, detailsId)
        }
    }

    private fun activateLocationServiceIfNecessary() {
        if (!isLocationServiceRunning() &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationService()
        }
    }

    private fun requestLocationPermissions() {
        permissionManager.requestLocationPermissions(object : MultiplePermissionsResultCallback {
            override fun onPermissionsResult(grantedPermissions: Map<String, Boolean>) {
                val allPermissionsGranted = grantedPermissions.entries.all { it.value }

                if (allPermissionsGranted) {
                    // All permissions are granted. You can start the location service.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        permissionManager.requestBackgroundLocationPermissions(object :
                            PermissionResultCallback {
                            override fun onPermissionResult(granted: Boolean) {
                                if (granted) {
                                    // Permission is granted. You can perform the operation that requires the permission.
                                    startLocationService()
                                } else {
                                    mainViewModel.displayBackgroundLocationPermissionNotGranted()
                                }
                            }
                        })
                    } else {
                        startLocationService()
                    }
                } else {
                    // Permissions denied. You can show a message to the user explaining why you need the permission.
                    mainViewModel.displayLocationPermissionNotGranted()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        dataClient.addListener(mainViewModel)
        activateLocationServiceIfNecessary()
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(mainViewModel)
    }

    companion object {
        const val locationIdKey = "location_id"
    }

    @Composable
    fun WearApp(viewModel: MainViewModel, detailsId: Int?) {
        val navController = rememberSwipeDismissableNavController()

        // Use LaunchedEffect to navigate once after composition
        LaunchedEffect(Unit) {
            if (detailsId != null) {
                navController.navigate(Details.buildRouteWithArguments(detailsId, true))
            }
        }

        CustomAlert(
            viewModel.showAlert,
            alertType = viewModel.alertType,
            onDismiss = {
                if (viewModel.alertType == AlertType.ExplanationLocationPermission) {
                    requestLocationPermissions()
                    viewModel.hideAlert()
                } else {
                    viewModel.hideAlert()
                }
            }
        )

        NotifyMeHereTheme {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = Main.route
            ) {
                composable(Main.route) {
                    MainScreen(viewModel = viewModel, onNavigateToDetails = {
                        navController.navigate(Details.buildRouteWithArguments(it.id, false))
                    }, onNavigateToSettings = {
                        navController.navigate(Settings.route)
                    })
                }
                composable(
                    Details.route + "/{${Details.detailsIdKey}}/{${Details.stopNotificationKey}}",
                    arguments = listOf(navArgument(Details.detailsIdKey) {
                        defaultValue = -1
                        type = NavType.IntType
                    }, navArgument(Details.stopNotificationKey) {
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
}

