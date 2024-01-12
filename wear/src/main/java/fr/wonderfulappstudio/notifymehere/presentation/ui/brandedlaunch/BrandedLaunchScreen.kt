package fr.wonderfulappstudio.notifymehere.presentation.ui.brandedlaunch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.wonderfulappstudio.notifymehere.R
import kotlinx.coroutines.delay

@Composable
fun BrandedLaunchScreen(navigateToMain: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
    }

    // Launching the effect to navigate after a delay
    LaunchedEffect(key1 = true) {
        delay(2000) // Delay for 2 seconds
        navigateToMain()
    }
}