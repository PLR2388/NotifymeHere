package fr.wonderfulappstudio.notifymehere.presentation.ui.settings


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.InlineSlider
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.presentation.theme.Distance
import fr.wonderfulappstudio.notifymehere.presentation.theme.Size

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val positionScrollState = rememberScrollState()
    val notificationDistance by viewModel.notificationDistance.collectAsState(initial = null)
    Scaffold(timeText = { TimeText() }, positionIndicator = {
        PositionIndicator(scrollState = positionScrollState)
    }) {
        ScalingLazyColumn {

            item {
                Text(stringResource(R.string.question_accuracy))
            }
            item {
                Text(stringResource(R.string.label_accuracy))
            }
            item {
                Text(
                    text = stringResource(
                        R.string.abbreviation_meter,
                        notificationDistance ?: Distance.defaultNotificationDistance
                    )
                )
            }
            item {
                InlineSlider(
                    value = notificationDistance?.toInt() ?: Distance.defaultNotificationDistance,
                    onValueChange = viewModel::changeNotificationDistance,
                    valueProgression = IntProgression.fromClosedRange(
                        Distance.minNotificationDistance,
                        Distance.maxNotificationDistance,
                        Distance.stepNotificationDistance
                    ),
                    decreaseIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_minus),
                            contentDescription = null,
                            modifier = Modifier.padding(vertical = Size.medium)
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