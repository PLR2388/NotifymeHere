package fr.wonderfulappstudio.notifymehere.ui.composable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.extension.convertMillisToDate

@Composable
fun DatePickerField(label: String, readOnly: Boolean, onDateSelected: (Long?) -> Unit) {
    var date by remember {
        mutableStateOf("-")
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = date,
        onValueChange = {},
        readOnly = true,
        label = {
            Text(text = label)
        }, interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                if (!readOnly) {
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                // Works like onClick but only if not readOnly
                                showDatePicker = true
                            }
                        }
                    }
                }
            })

    if (showDatePicker && !readOnly) {
        DatePicker(
            onDateSelected = {
                onDateSelected(it)
                date = it?.convertMillisToDate() ?: ""
            },
            onDismiss = { showDatePicker = false }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis > System.currentTimeMillis()
        }
    })

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }

            ) {
                Text(text = stringResource(id = R.string.button_ok))
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = stringResource(R.string.button_cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}