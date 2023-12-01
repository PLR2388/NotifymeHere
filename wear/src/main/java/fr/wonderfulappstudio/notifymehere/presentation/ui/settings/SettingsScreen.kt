package fr.wonderfulappstudio.notifymehere.presentation.ui.settings

import androidx.compose.runtime.Composable
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.InlineSlider
import androidx.wear.compose.material.OutlinedChip
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText

@Composable
fun SettingsScreen() {
    Scaffold(timeText = { TimeText() }) {
        ScalingLazyColumn {
            item {
                Text("How close to a location should I be notified?")
            }/*item {
                InlineSlider(
                    value = ,
                    onValueChange = ,
                    valueProgression = IntProgression.fromClosedRange(),
                    decreaseIcon = { /*TODO*/ },
                    increaseIcon = { /*TODO*/ })
            }*/
            item {
                OutlinedChip(label = { Text(text = "Save") }, onClick = { /*TODO*/ })

            }

        }
    }
}