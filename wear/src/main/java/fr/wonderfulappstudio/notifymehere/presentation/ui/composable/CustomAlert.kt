package fr.wonderfulappstudio.notifymehere.presentation.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import androidx.wear.compose.material.dialog.Dialog
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.presentation.theme.Size
import fr.wonderfulappstudio.notifymehere.presentation.ui.main.AlertType
import fr.wonderfulappstudio.notifymehere.presentation.utils.openAppSettings

@Composable
fun CustomAlert(showDialog: Boolean, alertType: AlertType?, onDismiss: () -> Unit) {
    when (alertType) {
        AlertType.NotificationPermissionNotGranted ->
            Alert(
                stringResource(R.string.notification_permission_message_alert),
                showDialog,
                onDismiss
            )


        AlertType.LocationPermissionsNotGranted ->
            Alert(
                stringResource(R.string.location_permission_message_alert),
                showDialog,
                onDismiss
            )


        AlertType.BackgroundLocationPermissionNotGranted ->
            Alert(
                stringResource(R.string.background_location_permission_message_alert),
                showDialog,
                onDismiss
            )

        null -> Column {}
    }
}

@Composable
private fun Alert(message: String, showDialog: Boolean, onDismiss: () -> Unit) {
    val context = LocalContext.current
    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismiss
    ) {
        ScalingLazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Spacer(modifier = Modifier.height(Size.small))
            }
            item {
                Text(
                    text = message,
                    modifier = Modifier.padding(bottom = Size.medium)
                )
            }
            item {
                Chip(label = {
                    Text(stringResource(R.string.button_settings))
                }, onClick = { context.openAppSettings() }, icon = {
                    Image(
                        painterResource(id = R.drawable.ic_settings),
                        contentDescription = null
                    )
                })
            }
        }
    }
}