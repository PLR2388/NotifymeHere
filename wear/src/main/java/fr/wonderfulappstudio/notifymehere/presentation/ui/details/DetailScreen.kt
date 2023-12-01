package fr.wonderfulappstudio.notifymehere.presentation.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import java.util.Date

@Composable
fun DetailsScreen(viewModel: DetailViewModel = hiltViewModel()) {
    val positionScrollState = rememberScrollState()
    Scaffold(
        timeText = { TimeText() },
        positionIndicator = { PositionIndicator(scrollState = positionScrollState) }) {
        ScalingLazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            viewModel.interestPoint?.let {
                item {
                    Text(it.name, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
                }
                if (it.description != null) {
                    item {
                        Text("Description", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

                    }
                    item {
                        Text(
                            it.description,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(Color.DarkGray),
                            fontWeight= FontWeight.Light,
                            textAlign = TextAlign.Start
                        )
                    }
                }
                item {
                    Text("Position", fontWeight = FontWeight.Bold)
                }
                item {
                    Text("${it.position.latitude}; ${it.position.longitude}")
                }

                if (it.startDate != null) {
                    item {
                        Text("Start Date", fontWeight = FontWeight.Bold)
                    }
                    item {
                        Text(Date(it.startDate).toString())
                    }
                }

                if (it.endDate != null) {
                    item {
                        Text("End Date", fontWeight = FontWeight.Bold)
                    }
                    item {
                        Text(Date(it.endDate).toString())
                    }
                }
            } ?:
                item {
                    Text("An error occurs")
                }

        }
    }


}

@Preview
@Composable
fun DetailsScreenPreview() {
    DetailsScreen()
}