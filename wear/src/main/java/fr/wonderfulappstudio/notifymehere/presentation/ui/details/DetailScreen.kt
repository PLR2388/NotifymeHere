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
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText

@Composable
fun DetailsScreen() {
    val positionScrollState = rememberScrollState()
    Scaffold(
        timeText = { TimeText() },
        positionIndicator = { PositionIndicator(scrollState = positionScrollState) }) {
        ScalingLazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            item {
                Text("Name of task", fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
            }
            item {
                Text("Description", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

            }
            item {
                Text(
                    "TaskDescription",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.DarkGray),
                    fontWeight= FontWeight.Light,
                    textAlign = TextAlign.Start
                )
            }
            item {
                Text("Position", fontWeight = FontWeight.Bold)
            }
            item {
                Text("position")
            }
            item {
                Text("Start Date", fontWeight = FontWeight.Bold)
            }
            item {
                Text("date")
            }
            item {
                Text("End Date", fontWeight = FontWeight.Bold)
            }
            item {
                Text("date")
            }
        }
    }


}

@Preview
@Composable
fun DetailsScreenPreview() {
    DetailsScreen()
}