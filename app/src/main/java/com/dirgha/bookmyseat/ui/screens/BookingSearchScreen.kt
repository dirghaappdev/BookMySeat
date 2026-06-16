package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.viewmodel.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSearchScreen(
    navController: NavController,
    tripViewModel: TripViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        tripViewModel.loadTrips()
    }

    val trips by tripViewModel.trips.collectAsState()

    var selectedRoute by remember {
        mutableStateOf("")
    }

    var selectedDate by remember {
        mutableStateOf("")
    }

    var selectedTimeSlot by remember {
        mutableStateOf("")
    }

    var routeExpanded by remember {
        mutableStateOf(false)
    }

    var dateExpanded by remember {
        mutableStateOf(false)
    }

    var slotExpanded by remember {
        mutableStateOf(false)
    }

    val routes = trips
        .map { it.route }
        .distinct()
        .sorted()

    val dates = trips
        .filter {
            it.route == selectedRoute
        }
        .map {
            it.date
        }
        .distinct()
        .sorted()

    val timeSlots = trips
        .filter {
            it.route == selectedRoute &&
                    it.date == selectedDate
        }
        .map {
            it.timeSlot
        }
        .distinct()
        .sorted()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Book Your Ride",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Trip Details",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // ROUTE DROPDOWN

        ExposedDropdownMenuBox(
            expanded = routeExpanded,
            onExpandedChange = {
                routeExpanded = !routeExpanded
            }
        ) {

            OutlinedTextField(
                value = selectedRoute,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Select Route")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = routeExpanded
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = routeExpanded,
                onDismissRequest = {
                    routeExpanded = false
                }
            ) {

                routes.forEach { route ->

                    DropdownMenuItem(
                        text = {
                            Text(route)
                        },
                        onClick = {

                            selectedRoute = route

                            selectedDate = ""
                            selectedTimeSlot = ""

                            routeExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // DATE DROPDOWN

        ExposedDropdownMenuBox(
            expanded = dateExpanded,
            onExpandedChange = {
                if (selectedRoute.isNotEmpty()) {
                    dateExpanded = !dateExpanded
                }
            }
        ) {

            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Select Date")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = dateExpanded
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = dateExpanded,
                onDismissRequest = {
                    dateExpanded = false
                }
            ) {

                dates.forEach { date ->

                    DropdownMenuItem(
                        text = {
                            Text(date)
                        },
                        onClick = {

                            selectedDate = date

                            selectedTimeSlot = ""

                            dateExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // TIME SLOT DROPDOWN

        ExposedDropdownMenuBox(
            expanded = slotExpanded,
            onExpandedChange = {
                if (selectedDate.isNotEmpty()) {
                    slotExpanded = !slotExpanded
                }
            }
        ) {

            OutlinedTextField(
                value = selectedTimeSlot,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Select Time Slot")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = slotExpanded
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = slotExpanded,
                onDismissRequest = {
                    slotExpanded = false
                }
            ) {

                timeSlots.forEach { slot ->

                    DropdownMenuItem(
                        text = {
                            Text(slot)
                        },
                        onClick = {

                            selectedTimeSlot = slot

                            slotExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Button(
            onClick = {

                val selectedTrip =
                    trips.firstOrNull {

                        it.route == selectedRoute &&
                                it.date == selectedDate &&
                                it.timeSlot == selectedTimeSlot
                    }

                selectedTrip?.let {

                    navController.navigate(
                        "booking_form/${it.tripId}"
                    )
                }
            },
            enabled =
                selectedRoute.isNotEmpty() &&
                        selectedDate.isNotEmpty() &&
                        selectedTimeSlot.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}