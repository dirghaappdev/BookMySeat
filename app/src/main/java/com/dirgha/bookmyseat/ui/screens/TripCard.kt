package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dirgha.bookmyseat.data.model.Trip

@Composable
fun TripCard(
    trip: Trip
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = trip.route,
                style =
                    MaterialTheme.typography.titleMedium
            )

            Text("Date: ${trip.date}")

            Text("Time: ${trip.timeSlot}")

            Text("Fare: ₹${trip.fare}")

            Text(
                "Available Seats: ${trip.availableSeats}"
            )
        }
    }
}