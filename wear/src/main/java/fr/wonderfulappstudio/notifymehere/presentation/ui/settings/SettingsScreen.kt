package fr.wonderfulappstudio.notifymehere.presentation.ui.settings


import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.InlineSlider
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.presentation.theme.Distance
import fr.wonderfulappstudio.notifymehere.presentation.theme.Size
import kotlinx.coroutines.launch

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val positionScrollState = rememberScrollState()
    val notificationDistance by viewModel.notificationDistance.collectAsState(initial = null)
    val listState = rememberScalingLazyListState()
    Scaffold(timeText = { TimeText() }, positionIndicator = {
        PositionIndicator(scrollState = positionScrollState)
    }) {
        val focusRequester = rememberActiveFocusRequester()
        val coroutineScope = rememberCoroutineScope()
        ScalingLazyColumn(modifier = Modifier
            .onRotaryScrollEvent {
                coroutineScope.launch {
                    listState.scrollBy(it.verticalScrollPixels)

                    listState.animateScrollBy(0f)
                }
                true
            }
            .focusRequester(focusRequester)
            .focusable(),
            state = listState) {

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