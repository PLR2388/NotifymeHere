package fr.wonderfulappstudio.notifymehere.presentation.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.presentation.ui.main.AlertType

@Composable
fun CustomAlert(alertType: AlertType, onDismiss: () -> Unit) {
    when (alertType) {
        AlertType.NotificationPermissionNotGranted -> Alert(
            title = { Text(text = stringResource(R.string.title_alert)) },
            message = { Text(text = "You didn't give Notification permission so the app won't work correctly") }) {}

        AlertType.LocationPermissionsNotGranted -> Alert(
            title = { Text(text = stringResource(R.string.title_alert)) },
            message = { Text(text = "You didn't give location permissions so the app won't work correctly") }) {}

        AlertType.BackgroundLocationPermissionNotGranted -> Alert(
            title = { Text(text = stringResource(R.string.title_alert)) },
            message = { Text(text = "You didn't give background location permission so the app won't work correctly") }) {}
    }
}