package fr.wonderfulappstudio.notifymehere.ui.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.ui.details.AlertType

@Composable
fun CustomAlert(alertType: AlertType, onDismiss: () -> Unit) {
    when (alertType) {
        AlertType.NameIsEmpty ->
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = stringResource(R.string.validation_alert_title)) },
                text = {
                    Text(
                        text = stringResource(R.string.validation_message_name_empty)
                    )
                }, confirmButton = {
                    Button(onClick = onDismiss) {
                        Text(stringResource(R.string.button_ok))
                    }
                })

        AlertType.PositionIsEmpty ->
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = stringResource(R.string.validation_alert_title)) },
                text = {
                    Text(
                        text = stringResource(R.string.validation_message_position_empty)
                    )
                }, confirmButton = {
                    Button(onClick = onDismiss) {
                        Text(stringResource(R.string.button_ok))
                    }
                })

        AlertType.StartDateGreaterEndDate ->
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = stringResource(R.string.validation_alert_title)) },
                text = {
                    Text(
                        text = stringResource(R.string.validation_message_start_date_after_end_date)
                    )
                }, confirmButton = {
                    Button(onClick = onDismiss) {
                        Text(stringResource(R.string.button_ok))
                    }
                })

        AlertType.ExplanationLocationPermission ->
            AlertDialog(
                onDismissRequest = onDismiss,
                text = {
                    Text(
                        text = stringResource(R.string.location_explanation)
                    )
                }, confirmButton = {
                    Button(onClick = onDismiss) {
                        Text(stringResource(R.string.button_ok))
                    }
                })

        AlertType.NotificationPermissionNotGranted -> AlertDialog(
            onDismissRequest = onDismiss,
            text = {
                Text(
                    text = stringResource(id = R.string.notification_permission_message_alert)
                )
            }, confirmButton = {
                Button(onClick = onDismiss) {
                    Text(stringResource(R.string.button_ok))
                }
            })

        AlertType.BackgroundLocationPermissionNotGranted -> AlertDialog(
            onDismissRequest = onDismiss,
            text = {
                Text(
                    text = stringResource(id = R.string.background_location_permission_message_alert)
                )
            }, confirmButton = {
                Button(onClick = onDismiss) {
                    Text(stringResource(R.string.button_ok))
                }
            })

        AlertType.LocationPermissionsNotGranted -> AlertDialog(
            onDismissRequest = onDismiss,
            text = {
                Text(
                    text = stringResource(id = R.string.location_permission_message_alert)
                )
            }, confirmButton = {
                Button(onClick = onDismiss) {
                    Text(stringResource(R.string.button_ok))
                }
            })
    }
}
