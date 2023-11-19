/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package fr.wonderfulappstudio.notifymehere.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.AndroidEntryPoint
import fr.wonderfulappstudio.notifymehere.presentation.theme.NotifyMeHereTheme
import fr.wonderfulappstudio.notifymehere.presentation.ui.Details
import fr.wonderfulappstudio.notifymehere.presentation.ui.details.DetailsScreen
import fr.wonderfulappstudio.notifymehere.presentation.ui.Main
import fr.wonderfulappstudio.notifymehere.presentation.ui.main.MainScreen
import fr.wonderfulappstudio.notifymehere.presentation.ui.main.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val dataClient by lazy { Wearable.getDataClient(this) }

    private val clientDataViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp(clientDataViewModel)
        }
    }


    override fun onResume() {
        super.onResume()
        dataClient.addListener(clientDataViewModel)
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(clientDataViewModel)
    }
}

@Composable
fun WearApp(viewModel: MainViewModel) {
    val navController = rememberSwipeDismissableNavController()
    NotifyMeHereTheme {
        SwipeDismissableNavHost(navController = navController, startDestination = Main.route) {
            composable(Main.route) {
                MainScreen(viewModel = viewModel, onNavigateToDetails = {
                    navController.navigate(Details.route)
                })
            }
            composable(Details.route) {
                DetailsScreen()
            }
        }
    }
}