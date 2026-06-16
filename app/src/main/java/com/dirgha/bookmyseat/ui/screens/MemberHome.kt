package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dirgha.bookmyseat.navigation.Screen
@Composable
fun MemberHomeScreen( navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = "Welcome",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = {
                navController.navigate(
                    Screen.BookingSearch.route
                )
            }
        ) {
            Text("Book My Seat")
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Button(
            onClick = {

                navController.navigate(
                    Screen.MyBookings.route
                )
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("My Bookings")
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Button(
            onClick = {

                navController.navigate(
                    Screen.SearchTrip.route
                )
            }
        ) {

            Text("Search Trips")
        }
    }
}