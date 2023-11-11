package fr.wonderfulappstudio.notifymehere.ui.details

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_LOW_POWER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

enum class InterestPointDetailsState {
    Add, Modify, Read
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestPointDetailsScreen(
    viewModel: InterestPointDetailsViewModel = hiltViewModel(),
    position: Pair<Double, Double>? = null,
    onNavigateBack: () -> Unit,
    onNavigateToMap: (Pair<Double, Double>) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    if (position != null) {
        viewModel.setGpsPosition(position)
    }

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val permissionsGranted = permissions.values.reduce { acc, isPermissionGranted ->
                acc && isPermissionGranted
            }

            if (!permissionsGranted) {
                //Logic when the permissions were not granted by the user
                // Display alert that explain what happen
            }
        })

    LaunchedEffect(key1 = Unit) {
        val locationPermissionsAlreadyGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!locationPermissionsAlreadyGranted) {
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Add a new interest point") }, navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            item {
                OutlinedTextField(
                    value = viewModel.uiState.name,
                    onValueChange = viewModel::setName,
                    label = { Text("Name") })

            }
            item {
                OutlinedTextField(
                    value = viewModel.uiState.description ?: "",
                    onValueChange = viewModel::setDescription,
                    label = { Text("Description (optional)") })
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.padding(start = 48.dp)
                ) {
                    OutlinedTextField(
                        value = String.format(
                            "%.10f; %.10f",
                            viewModel.uiState.gpsPosition.first,
                            viewModel.uiState.gpsPosition.second
                        ),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("GPS Position") })
                    OutlinedIconButton(onClick = {
                        if ((viewModel.uiState.gpsPosition.first == 0.0) && (viewModel.uiState.gpsPosition.second == 0.0)) {
                            scope.launch(Dispatchers.IO) {
                                val result = locationClient.getCurrentLocation(
                                    CurrentLocationRequest.Builder().setPriority(PRIORITY_LOW_POWER)
                                        .setDurationMillis(1000).setMaxUpdateAgeMillis(86_400_000)
                                        .build(), null
                                ).await()
                                withContext(Dispatchers.Main) {
                                    if (result == null) {
                                        onNavigateToMap(viewModel.uiState.gpsPosition)
                                    } else {
                                        onNavigateToMap(Pair(result.latitude, result.longitude))
                                    }
                                }
                            }
                        } else {
                            onNavigateToMap(viewModel.uiState.gpsPosition)
                        }
                    }) {
                        Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null)
                    }
                }
            }
            item {
                DatePickerField("Start Date (optional)", onDateSelected = viewModel::setStartDate)
            }
            item {
                DatePickerField("End Date (optional)", onDateSelected = viewModel::setEndDate)
            }
            item {
                Button(onClick = { viewModel.saveInterestPoint() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Add")
                }
            }
        }
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(millis))
}

@Composable
fun DatePickerField(label: String, onDateSelected: (Long?) -> Unit) {
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
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            // works like onClick
                            showDatePicker = true
                        }
                    }
                }
            })

    if (showDatePicker) {
        DatePicker(
            onDateSelected = {
                onDateSelected(it)
                date = it?.let {
                    convertMillisToDate(it)
                } ?: ""
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
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@Preview
@Composable
fun InterestPointDetailsScreenPreview() {
    InterestPointDetailsScreen(onNavigateToMap = {}, onNavigateBack = {})
}