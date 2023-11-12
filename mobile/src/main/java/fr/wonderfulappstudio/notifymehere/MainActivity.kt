package fr.wonderfulappstudio.notifymehere

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import fr.wonderfulappstudio.notifymehere.theme.NotifyMeHereTheme
import fr.wonderfulappstudio.notifymehere.ui.details.InterestPointDetailsScreen
import fr.wonderfulappstudio.notifymehere.ui.map.MapScreen
import fr.wonderfulappstudio.notifymehere.ui.NotifyMeHereMainScreen
import fr.wonderfulappstudio.notifymehere.ui.details.InterestPointDetailsViewModel
import fr.wonderfulappstudio.notifymehere.ui.map.MapActivity
import fr.wonderfulappstudio.notifymehere.ui.map.startMapActivityForResult

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val detailsViewModel: InterestPointDetailsViewModel by viewModels()

    private val startMapForResult: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val latitude  = data?.getDoubleExtra(MapActivity.LATITUDE_EXTRA_KEY, 0.0) ?: 0.0
            val longitude  = data?.getDoubleExtra(MapActivity.LONGITUDE_EXTRA_KEY, 0.0) ?: 0.0
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
                                context.startMapActivityForResult(startMapForResult, it.first.toFloat(), it.second.toFloat())
                            })
                    }
                }

            }
        }
    }
}