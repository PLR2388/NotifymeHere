package fr.wonderfulappstudio.notifymehere.presentation.ui.settings


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.InlineSlider
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import fr.wonderfulappstudio.notifymehere.R

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val notificationDistance by viewModel.notificationDistance.collectAsState(initial = null)
    Scaffold(timeText = { TimeText() }) {
        ScalingLazyColumn {

            item {
                Text("How close to a location should you be notified?")
            }
            item {
                Text("Current accuracy")
            }
            item {
                Text(text = "${notificationDistance ?: 500} m")
            }
            item {
                InlineSlider(
                    value = notificationDistance?.toInt() ?: 500,
                    onValueChange = viewModel::changeNotificationDistance,
                    valueProgression = IntProgression.fromClosedRange(50, 1000, 50),
                    decreaseIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_minus),
                            contentDescription = null,
                            modifier = Modifier.padding(vertical= 8.dp)
                        )
                    },
                    increaseIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_plus),
                            contentDescription = null
                        )
                    },
                    segmented = true
                )
            }
        }
    }
}