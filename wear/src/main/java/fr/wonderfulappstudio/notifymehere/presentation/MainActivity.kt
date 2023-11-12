/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package fr.wonderfulappstudio.notifymehere.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.presentation.theme.NotifyMeHereTheme
import fr.wonderfulappstudio.notifymehere.presentation.ui.Details
import fr.wonderfulappstudio.notifymehere.presentation.ui.details.DetailsScreen
import fr.wonderfulappstudio.notifymehere.presentation.ui.Main
import fr.wonderfulappstudio.notifymehere.presentation.ui.main.MainScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }

    companion object {
        const val INTEREST_POINTS_PATH = "/interest_points"
    }
}

@Composable
fun WearApp() {
    val navController = rememberSwipeDismissableNavController()
    NotifyMeHereTheme {
        SwipeDismissableNavHost(navController = navController, startDestination = Main.route) {
            composable(Main.route) {
                MainScreen(onNavigateToDetails = {
                    navController.navigate(Details.route)
                })
            }
            composable(Details.route) {
                DetailsScreen()
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}