package fr.wonderfulappstudio.notifymehere.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun NotifyMeHereTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = mobilecolorPalette,
        typography = Typography,
        content = content
    )
}