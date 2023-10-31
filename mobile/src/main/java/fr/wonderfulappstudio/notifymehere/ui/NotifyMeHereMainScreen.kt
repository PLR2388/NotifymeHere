package fr.wonderfulappstudio.notifymehere.ui

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.wonderfulappstudio.notifymehere.InterestPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifyMeHereMainScreen(
    interestPoints: List<InterestPoint>,
    navigateToAddInterestPoint: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("Notify me here!") }, actions = {
            IconButton(onClick = navigateToAddInterestPoint) {
                Image(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        })
    }) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            if (interestPoints.isEmpty()) {
                item { Text(text = "You don't have any interest point! Click on the + icon to add one!") }
            } else {
                items(interestPoints) { interestPoint ->
                    InterestPointCard(interestPoint = interestPoint, navigateTo = {})
                }
            }
        }
    }
}

@Composable
fun InterestPointCard(interestPoint: InterestPoint, navigateTo: (InterestPoint) -> Unit) {
    Card(
        modifier = Modifier.clickable(
            onClick = { navigateTo(interestPoint) },
            role = Role.Button
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(interestPoint.name, fontWeight = FontWeight.Bold)
                Text(
                    "${interestPoint.gpsPosition.first}, ${interestPoint.gpsPosition.second}",
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
            Pair(48.862725, 2.287592),
            1698755587,
            1698955587
        ), navigateTo = {})
}

@Preview
@Composable
fun NotifyMeHereMainScreenPreview() {
    NotifyMeHereMainScreen(
        listOf(
            InterestPoint(
                1,
                "House",
                "Where I live",
                Pair(48.862725, 2.287592),
                1698755587,
                1698955587
            )
        ),
        {}
    )
}