package fr.wonderfulappstudio.notifymehere.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Teal200 = Color(0xFF03DAC5)
val Red400 = Color(0xFFCF6679)

internal val mobilecolorPalette: ColorScheme = lightColorScheme(
    primary = Purple200,
    secondary = Teal200,
    error = Red400,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onError = Color.Black
)