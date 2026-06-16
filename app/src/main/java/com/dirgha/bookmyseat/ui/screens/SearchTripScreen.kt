package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirgha.bookmyseat.viewmodel.TripViewModel

@Composable
fun SearchTripScreen() {

    val viewModel: TripViewModel =
        viewModel()

    var route by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf("")
    }

    val trips by
    viewModel.trips.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Search Trips",
            style =
                MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = route,
            onValueChange = {
                route = it
            },
            label = {
                Text("Route")
            }
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = date,
            onValueChange = {
                date = it
            },
            label = {
                Text("Date (YYYY-MM-DD)")
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = {

                viewModel.searchTrips(
                    route,
                    date
                )
            }
        ) {

            Text("Search")
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        LazyColumn {

            items(trips) { trip ->

                TripCard(trip)
            }
        }
    }
}