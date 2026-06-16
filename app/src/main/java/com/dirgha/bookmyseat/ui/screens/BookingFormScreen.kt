package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.viewmodel.BookingViewModel
import com.dirgha.bookmyseat.data.local.SessionManager

@Composable
fun BookingFormScreen(
    navController: NavController,
    tripId: String
) {

    val viewModel: BookingViewModel =
        viewModel()

    var passengerCount by remember {
        mutableStateOf("1")
    }

    var gender by remember {
        mutableStateOf("")
    }

    var note by remember {
        mutableStateOf("")
    }

    val message by
    viewModel.message.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Passenger Details",
            style =
                MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Trip ID"
        )

        Text(
            text = tripId
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = passengerCount,
            onValueChange = {
                passengerCount = it
            },
            label = {
                Text("Passenger Count")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = gender,
            onValueChange = {
                gender = it
            },
            label = {
                Text("Gender")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = note,
            onValueChange = {
                note = it
            },
            label = {
                Text("Note")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {

                val token =
                    "Bearer ${SessionManager.token}"

                android.util.Log.d(
                    "BOOKING",
                    "TOKEN = $token"
                )

                viewModel.createBooking(
                    token = token,
                    tripId = tripId,
                    passengerCount =
                        passengerCount.toIntOrNull() ?: 1,
                    gender = gender,
                    note = note
                ) {

                    navController.popBackStack()
                }
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Confirm Booking")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(message)
    }
}