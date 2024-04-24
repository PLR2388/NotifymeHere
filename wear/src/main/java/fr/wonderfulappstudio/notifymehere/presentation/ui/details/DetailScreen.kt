package fr.wonderfulappstudio.notifymehere.presentation.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.presentation.extension.convertMillisToDate
import fr.wonderfulappstudio.notifymehere.presentation.theme.Size
import fr.wonderfulappstudio.notifymehere.presentation.ui.composable.BasicText
import kotlinx.coroutines.launch

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun DetailsScreen(viewModel: DetailViewModel = hiltViewModel(), onBack: () -> Unit) {
    val listState = rememberScalingLazyListState()
    Scaffold(
        timeText = { TimeText() },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }) {
        val focusRequester = rememberActiveFocusRequester()
        val coroutineScope = rememberCoroutineScope()
        ScalingLazyColumn(
            modifier = Modifier
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)

                        listState.animateScrollBy(0f)
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable(),
            state = listState,
            contentPadding = PaddingValues(horizontal = Size.medium)
        ) {
            viewModel.interestPoint?.let {
                item {
                    BasicText(
                        text = it.name,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                }
                if (it.description != null) {
                    item {
                        BasicText(
                            text = stringResource(R.string.label_description),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    item {
                        BasicText(
                            text = it.description,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Size.medium)
                                .background(Color.DarkGray),
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Start,
                        )
                    }
                }
                item {
                    BasicText(
                        text = stringResource(R.string.label_position),
                        fontWeight = FontWeight.Bold,
                    )
                }
                item {
                    BasicText(text = "${it.position.latitude}; ${it.position.longitude}")
                }

                if (it.startDate != null) {
                    item {
                        BasicText(
                            text = stringResource(R.string.label_start_date),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    item {
                        BasicText(text = it.startDate.convertMillisToDate())
                    }
                }

                if (it.endDate != null) {
                    item {
                        BasicText(
                            text = stringResource(R.string.label_end_date),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    item {
                        BasicText(text = it.endDate.convertMillisToDate())
                    }
                }

                item {
                    if (it.alreadyNotify) {
                        Chip(
                            label = { Text(stringResource(R.string.button_activate_notification)) },
                            onClick = viewModel::reactivateNotification
                        )
                    }
                }

                item {
                    Chip(
                        label = { Text(stringResource(R.string.button_delete_interest_point)) },
                        onClick = {
                            viewModel.deleteInterestPoint()
                            onBack()
                        },
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Red)
                    )
                }
            } ?: item {
                BasicText(stringResource(R.string.text_empty_details))
            }

        }
    }


}

@Preview
@Composable
fun DetailsScreenPreview() {
    DetailsScreen {}
}