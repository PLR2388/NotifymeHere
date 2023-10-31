package fr.wonderfulappstudio.notifymehere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import fr.wonderfulappstudio.notifymehere.theme.NotifyMeHereTheme
import fr.wonderfulappstudio.notifymehere.ui.InterestPointDetailsScreen
import fr.wonderfulappstudio.notifymehere.ui.MainViewModel
import fr.wonderfulappstudio.notifymehere.ui.MapView
import fr.wonderfulappstudio.notifymehere.ui.NotifyMeHereMainScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NotifyMeHereTheme {
                NavHost(navController = navController, startDestination = Main.route) {
                    composable(Main.route) {
                        NotifyMeHereMainScreen(
                            viewModel.interestPoints,
                            navigateToAddInterestPoint = {
                                navController.navigate(Details.route)
                            })
                    }
                    composable(
                        Details.route + "?detailsId={detailsId}",
                        arguments = listOf(navArgument("detailsId") { defaultValue = "" })
                    ) { backStackEntry ->
                        val detailsIds = backStackEntry.arguments?.getString("detailsId")
                        InterestPointDetailsScreen(onNavigateBack = {
                            navController.popBackStack()
                        }, onNavigateToMap = {
                            navController.navigate(Map.route)
                        })
                    }
                    composable(Map.route) {
                        MapView()
                    }
                }

            }
        }
    }
}