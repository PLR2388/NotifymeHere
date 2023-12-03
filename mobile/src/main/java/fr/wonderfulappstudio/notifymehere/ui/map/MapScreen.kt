package fr.wonderfulappstudio.notifymehere.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import fr.wonderfulappstudio.notifymehere.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    selectedPosition: Pair<Float, Float>?,
    onNavigateBack: () -> Unit,
    onValidatePicker: (Pair<Double, Double>) -> Unit
) {
    if (selectedPosition != null) {
        viewModel.onMapLongClick(
            LatLng(
                selectedPosition.first.toDouble(),
                selectedPosition.second.toDouble()
            )
        )
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(R.string.title_map)) }, navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        }, actions = {
            IconButton(onClick = {
                onValidatePicker(Pair(viewModel.position.latitude, viewModel.position.longitude))
            }) {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
            }
        })
    }) { contentPadding ->
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(viewModel.position, 20f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            contentPadding = contentPadding,
            onMapLongClick = viewModel::onMapLongClick
        ) {
            Marker(
                state = MarkerState(position = viewModel.position),
            )
        }
    }

}