package fr.wonderfulappstudio.notifymehere.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

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
                        value = "${viewModel.uiState.gpsPosition.first};${viewModel.uiState.gpsPosition.second}",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("GPS Position") })
                    OutlinedIconButton(onClick = { onNavigateToMap(viewModel.uiState.gpsPosition) }) {
                        Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null)
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = "5/02/2024 14:30",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(text = "Start Date (optional)")
                    })
            }
            item {
                OutlinedTextField(
                    value = "5/02/2024 14:30",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(text = "End Date (optional)")
                    })
            }
            item {
                Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Add")
                }
            }
        }
    }
}

@Preview
@Composable
fun InterestPointDetailsScreenPreview() {
    InterestPointDetailsScreen(onNavigateToMap = {}, onNavigateBack = {})
}