package fr.wonderfulappstudio.notifymehere.presentation.ui.main

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.presentation.model.InterestPoint

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToDetails: (InterestPoint) -> Unit
) {
    val interestPoints by viewModel.interestPoints.collectAsState(initial = emptyList())
    Scaffold(timeText = { TimeText() }) {
        ScalingLazyColumn {
            if (interestPoints.isEmpty()) {
                item {
                    Text("You haven't send any interest point!")
                }
            } else {
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
                    Button(onClick = { /*TODO*/ }) {
                        Image(
                            painterResource(id = R.drawable.ic_settings),
                            contentDescription = null
                        )
                    }
                }
            }

        }
    }
}
