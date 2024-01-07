package fr.wonderfulappstudio.notifymehere

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.PutDataRequest
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.AndroidEntryPoint
import fr.wonderfulappstudio.common.manager.MultiplePermissionsResultCallback
import fr.wonderfulappstudio.common.manager.PermissionManager
import fr.wonderfulappstudio.common.manager.PermissionResultCallback
import fr.wonderfulappstudio.notifymehere.extension.createDefaultNotification
import fr.wonderfulappstudio.notifymehere.extension.isLocationServiceRunning
import fr.wonderfulappstudio.notifymehere.extension.showToast
import fr.wonderfulappstudio.notifymehere.model.InterestPoint
import fr.wonderfulappstudio.notifymehere.service.LocationService
import fr.wonderfulappstudio.notifymehere.theme.NotifyMeHereTheme
import fr.wonderfulappstudio.notifymehere.ui.details.InterestPointDetailsScreen
import fr.wonderfulappstudio.notifymehere.ui.details.InterestPointDetailsViewModel
import fr.wonderfulappstudio.notifymehere.ui.main.MainScreen
import fr.wonderfulappstudio.notifymehere.ui.main.MainViewModel
import fr.wonderfulappstudio.notifymehere.ui.map.MapActivity
import fr.wonderfulappstudio.notifymehere.ui.map.startMapActivityForResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Instant
import kotlin.collections.Map


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }

    private val detailsViewModel: InterestPointDetailsViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    private val startMapForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val latitude = data?.getDoubleExtra(MapActivity.LATITUDE_EXTRA_KEY, 0.0) ?: 0.0
                val longitude = data?.getDoubleExtra(MapActivity.LONGITUDE_EXTRA_KEY, 0.0) ?: 0.0
                detailsViewModel.setPosition(latitude, longitude)
            }
        }

    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        permissionManager = PermissionManager(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionManager.requestNotificationPermission(object : PermissionResultCallback {
                override fun onPermissionResult(granted: Boolean) {
                    if (!granted) {
                        mainViewModel.displayNotificationPermissionNotGranted()
                    }
                }
            })
        }

        activateLocationServiceIfNecessary()

        val interestPointId = intent.getIntExtra(locationIdKey, -1)
        val detailsId = if (interestPointId == -1) null else interestPointId

        if (detailsId != null) {
            with(NotificationManagerCompat.from(this)) {
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                notify(NotifyMeHereApplication.MAIN_SERVICE_NOTIFICATION_ID, createDefaultNotification())
            }
        }

        setupContent(detailsId)
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
                                    detailsViewModel.displayBackgroundLocationPermissionNotGranted()
                                }
                            }
                        })
                    } else {
                        startLocationService()
                    }
                } else {
                    // Permissions denied. You can show a message to the user explaining why you need the permission.
                    detailsViewModel.displayLocationPermissionNotGranted()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        activateLocationServiceIfNecessary()
    }

    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
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

    private fun setupContent(detailsId: Int?) {
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            NotifyMeHereTheme {
                // Use LaunchedEffect to navigate once after composition
                LaunchedEffect(Unit) {
                    if (detailsId != null) {
                        navController.navigate(Details.buildRouteWithArguments(detailsId, true))
                    }
                }
                NavHost(navController = navController, startDestination = Main.route) {
                    setupNavGraph(navController, context)
                }
            }
        }
    }

    private fun NavGraphBuilder.setupNavGraph(
        navController: NavHostController,
        context: Context
    ) {

        composable(Main.route) {
            MainScreen(
                viewModel = mainViewModel,
                onSendToWatch = {
                    sendInterestPoints(it)
                },
                navigateToAddInterestPoint = {
                    val route = if (it == null) {
                        Details.route
                    } else {
                        Details.buildRouteWithArguments(it, false)
                    }
                    navController.navigate(route)
                }, startWearableActivity = {
                    startWearableActivity()
                }, onOpenPrivacy = {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_url)))
                    startActivity(browserIntent)
                })
        }
        composable(
            Details.route + "?${Details.detailsIdKey}={${Details.detailsIdKey}}&${Details.stopNotificationKey}={${Details.stopNotificationKey}}",
            arguments = listOf(navArgument(Details.detailsIdKey) {
                defaultValue = -1
                type = NavType.IntType
            }, navArgument(Details.stopNotificationKey) {
                defaultValue = false
                type = NavType.BoolType
            })
        ) { backStackEntry ->
            detailsViewModel.initInterestPoint(
                backStackEntry.arguments?.getInt(Details.detailsIdKey),
                backStackEntry.arguments?.getBoolean(Details.stopNotificationKey) == true
            )
            InterestPointDetailsScreen(
                viewModel = detailsViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }, onNavigateToMap = {
                    context.startMapActivityForResult(
                        startMapForResult,
                        it.latitude.toFloat(),
                        it.longitude.toFloat()
                    )
                }, onRequestLocationPermission = ::requestLocationPermissions
            )
        }
    }

    private fun startWearableActivity() {
        lifecycleScope.launch {
            try {
                val nodes = capabilityClient
                    .getCapability(WEAR_CAPABILITY, CapabilityClient.FILTER_REACHABLE)
                    .await()
                    .nodes

                // Send a message to all nodes in parallel
                nodes.map { node ->
                    async {
                        messageClient.sendMessage(node.id, START_ACTIVITY_PATH, byteArrayOf())
                            .await()
                    }
                }.awaitAll()

                Log.d(TAG, "Starting activity requests sent successfully")
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Log.d(TAG, "Starting activity failed: $exception")
            }
        }
    }

    private fun createInterestPointsDataRequest(
        interestPoints: List<InterestPoint>
    ): PutDataRequest {
        val putDataMapReq = PutDataMapRequest.create(INTEREST_POINTS_PATH).apply {
            val dataMapArray = interestPoints.map { it.toDataMap() }
            dataMap.putDataMapArrayList(INTEREST_POINTS_KEY, ArrayList(dataMapArray))
            dataMap.putLong(TIME_KEY, Instant.now().epochSecond)
        }
        return putDataMapReq.asPutDataRequest().setUrgent()
    }

    private fun sendInterestPoints(interestPoints: List<InterestPoint>) {
        lifecycleScope.launch {
            try {
                val request = createInterestPointsDataRequest(interestPoints)

                val result = dataClient.putDataItem(request).await()

                Log.d(TAG, "DataItem saved: $result")
                runOnUiThread {
                    showToast(getString(R.string.interest_points_sent))
                }
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Log.d(TAG, "Saving DataItem failed: $exception")
                runOnUiThread {
                    showToast(getString(R.string.interest_points_send_failed))
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val INTEREST_POINTS_PATH = "/interest-points"
        private const val START_ACTIVITY_PATH = "/start-activity"
        private const val WEAR_CAPABILITY = "wear"
        private const val INTEREST_POINTS_KEY = "interestPoints"
        private const val TIME_KEY = "time"
        const val locationIdKey = "location_id"
    }

}