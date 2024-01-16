package fr.wonderfulappstudio.notifymehere.presentation.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Size {
    val small: Dp = 4.dp
    val medium: Dp = 8.dp
    object SettingScreen {
        val horizontalPaddingFirstElement: Dp = 10.dp
        val topPaddingFirstElement: Dp = 60.dp
    }
}

object Distance {
    const val minNotificationDistance: Int = 50
    const val maxNotificationDistance: Int = 1000
    const val stepNotificationDistance: Int = 50
    const val defaultNotificationDistance: Int = 500
}