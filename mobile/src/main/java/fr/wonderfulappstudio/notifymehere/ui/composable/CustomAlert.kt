package fr.wonderfulappstudio.notifymehere.ui.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import fr.wonderfulappstudio.notifymehere.ui.details.AlertType

@Composable
fun CustomAlert(alertType: AlertType, onDismiss: () -> Unit) {
    when (alertType) {
        AlertType.NameIsEmpty ->
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = "Validation failed") },
                text = {
                    Text(
                        text = "Name cannot be empty!"
                    )
                }, confirmButton = {
                    Button(onClick = onDismiss) {
                        Text("Ok")
                    }
                })
        AlertType.PositionIsEmpty ->
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = "Validation failed") },
                text = {
                    Text(
                        text = "Position should be set"
                    )
                }, confirmButton = {
                    Button(onClick = onDismiss) {
                        Text("Ok")
                    }
                })
        AlertType.StartDateGreaterEndDate ->
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = "Validation failed") },
                text = {
                    Text(
                        text = "Start date cannot be later than end date"
                    )
                }, confirmButton = {
                    Button(onClick = onDismiss) {
                        Text("Ok")
                    }
                })
    }
}
