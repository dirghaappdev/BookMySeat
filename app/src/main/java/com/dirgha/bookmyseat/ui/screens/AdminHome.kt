package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.viewmodel.TripViewModel

@Composable
fun AdminHomeScreen(
    navController: NavController
) {

    val tripViewModel: TripViewModel = viewModel()

    val trips by tripViewModel.trips.collectAsState()

    LaunchedEffect(Unit) {
        tripViewModel.loadTrips()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Admin Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = {
                navController.navigate(
                    Screen.CreateTrip.route
                )
            }
        ) {
            Text("Create Trip")
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
        Button(
            onClick = {

                navController.navigate(
                    Screen.ManageBookings.route
                )
            }
        ) {

            Text("Manage Bookings")
        }
        Spacer(
            modifier = Modifier.height(24.dp)
        )
        Text(
            text = "Available Trips",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        LazyColumn {

            items(trips) { trip ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        Text(
                            text = trip.route,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text("Date: ${trip.date}")

                        Text("Time: ${trip.timeSlot}")

                        Text("Fare: ₹${trip.fare}")

                        Text(
                            text = "Available Seats: ${trip.availableSeats}"
                        )

                        Text(
                            text = "Status: ${trip.status}"
                        )
                    }
                }
            }
        }
    }
}