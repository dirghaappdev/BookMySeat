package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirgha.bookmyseat.viewmodel.AdminBookingViewModel

@Composable
fun ManageBookingsScreen() {

    val viewModel:
            AdminBookingViewModel =
        viewModel()

    val bookings by
    viewModel.bookings.collectAsState()

    LaunchedEffect(Unit) {

        viewModel.loadBookings()
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
                    .padding(
                        vertical = 8.dp
                    )
            ) {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {

                    Text(
                        text =
                            "Booking ID: ${booking.bookingId}"
                    )
                    Text(
                        text = "Customer: ${booking.userName}"
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

                    Text(
                        text =
                            "Status: ${booking.bookingStatus}"
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    if (
                        booking.bookingStatus
                        == "PENDING"
                    ) {

                        Row {

                            Button(
                                onClick = {

                                    viewModel.confirmBooking(
                                        booking.bookingId
                                    )
                                }
                            ) {
                                Text("Confirm")
                            }

                            Spacer(
                                modifier =
                                    Modifier.width(
                                        8.dp
                                    )
                            )

                            Button(
                                onClick = {

                                    viewModel.rejectBooking(
                                        booking.bookingId
                                    )
                                }
                            ) {
                                Text("Reject")
                            }
                        }
                    }
                }
            }
        }
    }
}