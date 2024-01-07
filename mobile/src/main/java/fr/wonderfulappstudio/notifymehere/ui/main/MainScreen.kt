package fr.wonderfulappstudio.notifymehere.ui.main

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import fr.wonderfulappstudio.notifymehere.R
import fr.wonderfulappstudio.notifymehere.extension.showToast
import fr.wonderfulappstudio.notifymehere.model.InterestPoint
import fr.wonderfulappstudio.notifymehere.theme.Size

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigateToAddInterestPoint: (Int?) -> Unit,
    onSendToWatch: (List<InterestPoint>) -> Unit,
    startWearableActivity: () -> Unit,
    onOpenPrivacy: () -> Unit
) {
    val interestPoints by viewModel.interestPoints.collectAsState(initial = emptyList())
    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(R.string.title_main)) }, actions = {
            IconButton(onClick = onOpenPrivacy) {
                Image(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.onBackground
                    )
                )
            }
            IconButton(onClick = {
                context.showToast(context.getString(R.string.toast_start_wear_app))
                startWearableActivity()
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_watch),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.onBackground
                    )
                )
            }
            if (interestPoints.isNotEmpty()) {
                IconButton(onClick = {
                    context.showToast(context.getString(R.string.toast_send_interest_points))
                    onSendToWatch(interestPoints)
                }) {
                    Image(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { navigateToAddInterestPoint(null) }) {
            Image(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Size.medium)
        ) {
            if (interestPoints.isEmpty()) {
                item { Text(text = stringResource(R.string.text_empty_interest_points)) }
            } else {
                items(interestPoints) { interestPoint ->
                    InterestPointCard(interestPoint = interestPoint, navigateTo = {
                        it.id?.let { id -> navigateToAddInterestPoint(id) }
                    })
                }
            }
        }
    }
}

@Composable
fun InterestPointCard(interestPoint: InterestPoint, navigateTo: (InterestPoint) -> Unit) {
    Card(
        modifier = Modifier
            .clickable(
                onClick = { navigateTo(interestPoint) },
                role = Role.Button
            )
            .padding(Size.medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Size.heightRowInMain)
                .padding(horizontal = Size.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                Text(interestPoint.name, fontWeight = FontWeight.Bold)
                Text(
                    "${interestPoint.position.latitude}, ${interestPoint.position.longitude}",
                    fontWeight = FontWeight.Light
                )
            }
            Image(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun InterestPointCardPreview() {
    InterestPointCard(
        interestPoint = InterestPoint(
            1,
            "House",
            "Where I live",
            Location("app").apply {
                latitude = 48.862725
                longitude = 2.287592
            },
            1698755587,
            1698955587
        ), navigateTo = {})
}