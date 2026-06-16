package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirgha.bookmyseat.viewmodel.MyBookingViewModel

@Composable
fun MyBookingsScreen() {

    val viewModel:
            MyBookingViewModel =
        viewModel()

    val bookings by
    viewModel.bookings.collectAsState()

    LaunchedEffect(Unit) {

        viewModel.loadMyBookings()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        items(bookings) { booking ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {

                    Text(
                        text = booking.route,
                        style =
                            MaterialTheme
                                .typography
                                .titleLarge
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Date: ${booking.date}"
                    )

                    Text(
                        text =
                            "Time: ${booking.timeSlot}"
                    )

                    Text(
                        text =
                            "Passengers: ${booking.passengerCount}"
                    )

                    Text(
                        text =
                            "Gender: ${booking.gender}"
                    )

                    Text(
                        text =
                            "Fare: ₹${booking.fare}"
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    AssistChip(
                        onClick = {},
                        label = {

                            Text(
                                booking.bookingStatus
                            )
                        }
                    )
                }
            }
            }
        }
    }
