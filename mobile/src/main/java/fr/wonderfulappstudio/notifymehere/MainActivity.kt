package fr.wonderfulappstudio.notifymehere

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.PutDataRequest
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.AndroidEntryPoint
import fr.wonderfulappstudio.notifymehere.model.InterestPoint
import fr.wonderfulappstudio.notifymehere.theme.NotifyMeHereTheme
import fr.wonderfulappstudio.notifymehere.ui.MainViewModel
import fr.wonderfulappstudio.notifymehere.ui.details.InterestPointDetailsScreen
import fr.wonderfulappstudio.notifymehere.ui.NotifyMeHereMainScreen
import fr.wonderfulappstudio.notifymehere.ui.details.InterestPointDetailsViewModel
import fr.wonderfulappstudio.notifymehere.ui.map.MapActivity
import fr.wonderfulappstudio.notifymehere.ui.map.startMapActivityForResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val dataClient by lazy { Wearable.getDataClient(this) }


    private val mainViewModel: MainViewModel by viewModels()
    private val detailsViewModel: InterestPointDetailsViewModel by viewModels()

    private val startMapForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val latitude = data?.getDoubleExtra(MapActivity.LATITUDE_EXTRA_KEY, 0.0) ?: 0.0
                val longitude = data?.getDoubleExtra(MapActivity.LONGITUDE_EXTRA_KEY, 0.0) ?: 0.0
                detailsViewModel.setGpsPosition(Pair(latitude.toDouble(), longitude.toDouble()))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            NotifyMeHereTheme {
                NavHost(navController = navController, startDestination = Main.route) {
                    composable(Main.route) {
                        NotifyMeHereMainScreen(
                            viewModel = mainViewModel,
                            onSendToWatch = {
                                sendInterestPointsArray(it)
                            },
                            navigateToAddInterestPoint = {
                                val route = if (it == null) {
                                    Details.route
                                } else {
                                    "${Details.route}?detailsId=${it}"
                                }
                                navController.navigate(route)
                            })
                    }
                    composable(
                        Details.route + "?detailsId={detailsId}",
                        arguments = listOf(navArgument("detailsId") {
                            defaultValue = -1
                            type = NavType.IntType
                        })
                    ) { backStackEntry ->
                        val detailsIds = backStackEntry.arguments?.getInt("detailsId")
                        backStackEntry.savedStateHandle.get<Pair<Double, Double>>("position")?.let {
                            detailsViewModel.setGpsPosition(it)
                        }
                        detailsViewModel.setDetailsId(detailsIds)
                        InterestPointDetailsScreen(
                            viewModel = detailsViewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            }, onNavigateToMap = {
                                context.startMapActivityForResult(
                                    startMapForResult,
                                    it.first.toFloat(),
                                    it.second.toFloat()
                                )
                            })
                    }
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        dataClient.addListener(mainViewModel)
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(mainViewModel)
    }

    private fun createInterestPointsDataRequest(
        interestPoints: List<InterestPoint>
    ): PutDataRequest {
        val putDataMapReq = PutDataMapRequest.create(INTEREST_POINTS_PATH)
        val dataMapArray = interestPoints.map { it.toDataMap() }
        putDataMapReq.dataMap.putDataMapArrayList("interestPoints", ArrayList(dataMapArray))

        return putDataMapReq.asPutDataRequest().setUrgent()
    }

    private fun sendInterestPointsArray(interestPoints: List<InterestPoint>) {
        lifecycleScope.launch {
            try {
                val putDataRequest = createInterestPointsDataRequest(interestPoints)
                val result = dataClient.putDataItem(putDataRequest).await()
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Log.d(TAG, "Saving DataItem failed: $exception")
            }

        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val INTEREST_POINTS_PATH = "/interest_points"
    }

}