package fr.wonderfulappstudio.notifymehere.presentation.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.presentation.model.InterestPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToDetails: (InterestPoint) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val positionScrollState = rememberScrollState()
    val interestPoints by viewModel.interestPoints.collectAsState(initial = emptyList())
    val listState = rememberScalingLazyListState()
    Scaffold(
        timeText = { TimeText() },
        positionIndicator = { PositionIndicator(scrollState = positionScrollState) }) {
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
            if (interestPoints.isEmpty()) {
                item {
                    Text(stringResource(R.string.text_no_interest_points))
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(interestPoints) { interestPoint ->
                    Chip(
                        onClick = { onNavigateToDetails(interestPoint) },
                        enabled = true,
                        label = {
                            Text(
                                text = interestPoint.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        secondaryLabel = {
                            Text(
                                text = "${interestPoint.position.latitude}; ${interestPoint.position.longitude}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                    )
                }
                item {
                    Chip(label = {
                        Text(stringResource(R.string.button_settings))
                    }, onClick = onNavigateToSettings, icon = {
                        Image(
                            painterResource(id = R.drawable.ic_settings),
                            contentDescription = null
                        )
                    })
                }
            }

        }
    }
}
