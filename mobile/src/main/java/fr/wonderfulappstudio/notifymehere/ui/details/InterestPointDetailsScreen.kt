package fr.wonderfulappstudio.notifymehere.ui.details

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_LOW_POWER
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.extension.convertMillisToDate
import fr.wonderfulappstudio.notifymehere.theme.Size
import fr.wonderfulappstudio.notifymehere.theme.Time
import fr.wonderfulappstudio.notifymehere.ui.composable.CustomAlert
import fr.wonderfulappstudio.notifymehere.ui.composable.DatePickerField
import fr.wonderfulappstudio.notifymehere.ui.composable.LoadingScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

enum class InterestPointDetailsState {
    Add, Modify, Read
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestPointDetailsScreen(
    viewModel: InterestPointDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToMap: (Pair<Double, Double>) -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
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
                viewModel.displayNoPermissionForLocationAlert()
            }
        })

    if (viewModel.showAlert) {
        viewModel.alertType?.let {
            if (it == AlertType.ExplanationLocationPermission) {
                CustomAlert(alertType = it) {
                    locationPermissionLauncher.launch(locationPermissions)
                    viewModel.hideAlert()
                }
            } else {
                CustomAlert(alertType = it, onDismiss = viewModel::hideAlert)
            }
        }
    }

    LaunchedEffect(key1 = viewModel.showSuccess) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.message_interest_point_saved),
                duration = SnackbarDuration.Long
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        val locationPermissionsAlreadyGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!locationPermissionsAlreadyGranted) {
            viewModel.displayLocationPermissionExplanation()
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                when (viewModel.state) {
                    InterestPointDetailsState.Add -> stringResource(R.string.title_details_add)
                    InterestPointDetailsState.Modify -> stringResource(
                        R.string.title_details_modify,
                        viewModel.uiState.name
                    )

                    InterestPointDetailsState.Read -> viewModel.uiState.name
                }
            )
        }, navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }, actions = {
            when (viewModel.state) {
                InterestPointDetailsState.Add -> {}
                InterestPointDetailsState.Modify -> {
                    IconButton(onClick = viewModel::toggleEditMode) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                    }
                }

                InterestPointDetailsState.Read -> {
                    IconButton(onClick = viewModel::toggleEditMode) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                    }
                }
            }

        })
    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) {
        }
    }) { contentPadding ->
        LoadingScreen(isShown = viewModel.isLoading) {
            LazyColumn(
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(Size.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Size.medium)
            ) {
                item {
                    OutlinedTextField(
                        value = viewModel.uiState.name,
                        onValueChange = viewModel::setName,
                        readOnly = viewModel.state == InterestPointDetailsState.Read,
                        label = { Text(stringResource(R.string.label_name)) })

                }
                item {
                    OutlinedTextField(
                        value = viewModel.uiState.description ?: "",
                        onValueChange = viewModel::setDescription,
                        readOnly = viewModel.state == InterestPointDetailsState.Read,
                        label = { Text(stringResource(R.string.label_description)) })
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.padding(start = Size.extraLargeDetail)
                    ) {
                        OutlinedTextField(
                            value = String.format(
                                "%.10f; %.10f",
                                viewModel.uiState.gpsPosition.first,
                                viewModel.uiState.gpsPosition.second
                            ),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.label_gps_position)) })
                        OutlinedIconButton(onClick = {
                            if (viewModel.state == InterestPointDetailsState.Read) return@OutlinedIconButton
                            if (viewModel.uiState.gpsPosition == Pair(0.0, 0.0)) {
                                scope.launch(Dispatchers.IO) {
                                    val result = locationClient.getCurrentLocation(
                                        CurrentLocationRequest.Builder()
                                            .setPriority(PRIORITY_LOW_POWER)
                                            .setDurationMillis(Time.oneSecondInMilliseconds)
                                            .setMaxUpdateAgeMillis(Time.oneDayInMilliseconds)
                                            .build(), null
                                    ).await()
                                    withContext(Dispatchers.Main) {
                                        if (result == null) {
                                            onNavigateToMap(viewModel.uiState.gpsPosition)
                                        } else {
                                            onNavigateToMap(
                                                Pair(
                                                    result.latitude,
                                                    result.longitude
                                                )
                                            )
                                        }
                                    }
                                }
                            } else {
                                onNavigateToMap(viewModel.uiState.gpsPosition)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null
                            )
                        }
                    }
                }
                item {
                    DatePickerField(
                        label = stringResource(R.string.label_start_date),
                        text = viewModel.uiState.startDate?.convertMillisToDate() ?: "-",
                        readOnly = viewModel.state == InterestPointDetailsState.Read,
                        onDateSelected = viewModel::setStartDate
                    )
                }
                item {
                    DatePickerField(
                        label = stringResource(R.string.label_end_date),
                        text = viewModel.uiState.endDate?.convertMillisToDate() ?: "-",
                        readOnly = viewModel.state == InterestPointDetailsState.Read,
                        onDateSelected = viewModel::setEndDate
                    )
                }
                item {
                    if (viewModel.state != InterestPointDetailsState.Read) {
                        Button(
                            onClick = { viewModel.saveInterestPoint(onDismiss = onNavigateBack) },
                            shape = RoundedCornerShape(Size.medium),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Size.large)
                        ) {
                            Text(
                                if (viewModel.state == InterestPointDetailsState.Add) stringResource(
                                    R.string.button_add
                                ) else stringResource(R.string.button_modify)
                            )
                        }
                    }
                }
                item {
                    if (viewModel.state != InterestPointDetailsState.Add) {
                        Button(
                            onClick = { viewModel.delete(onNavigateBack) },
                            shape = RoundedCornerShape(Size.medium),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Size.large),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text(stringResource(R.string.button_delete))
                        }
                    }
                }
            }
        }
    }
}


