package fr.wonderfulappstudio.notifymehere

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_LOW_POWER
import dagger.hilt.android.AndroidEntryPoint
import fr.wonderfulappstudio.notifymehere.theme.NotifyMeHereTheme
import fr.wonderfulappstudio.notifymehere.ui.details.InterestPointDetailsScreen
import fr.wonderfulappstudio.notifymehere.ui.MainViewModel
import fr.wonderfulappstudio.notifymehere.ui.map.MapScreen
import fr.wonderfulappstudio.notifymehere.ui.NotifyMeHereMainScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NotifyMeHereTheme {
                NavHost(navController = navController, startDestination = Main.route) {
                    composable(Main.route) {
                        NotifyMeHereMainScreen(
                            navigateToAddInterestPoint = {
                                navController.navigate(Details.route)
                            })
                    }
                    composable(
                        Details.route + "?detailsId={detailsId}",
                        arguments = listOf(navArgument("detailsId") { defaultValue = "" })
                    ) { backStackEntry ->
                        val detailsIds = backStackEntry.arguments?.getString("detailsId")
                        val position =
                            backStackEntry.savedStateHandle.get<Pair<Double, Double>>("position")
                        val context = LocalContext.current
                        InterestPointDetailsScreen(position = position,
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            onNavigateToMap = {
                                navController.navigate(
                                    Map.route + "?latitude=${it.first}&longitude=${it.second}"
                                )
                            })
                    }
                    composable(
                        Map.route + "?latitude={latitude}&longitude={longitude}",
                        arguments = listOf(
                            navArgument("latitude") { type = NavType.FloatType },
                            navArgument("longitude") { type = NavType.FloatType })
                    )
                    { backStackEntry ->
                        val latitude = backStackEntry.arguments?.getFloat("latitude")
                        val longitude = backStackEntry.arguments?.getFloat("longitude")
                        MapScreen(
                            selectedPosition = Pair(
                                latitude ?: 0.0f,
                                longitude ?: 0.0f
                            ), onNavigateBack = {
                                navController.popBackStack()
                            }, onValidatePicker = {
                                navController.popBackStack()
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("position", it)
                            })
                    }
                }

            }
        }
    }
}