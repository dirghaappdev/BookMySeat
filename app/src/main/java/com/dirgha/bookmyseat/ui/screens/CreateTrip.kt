package com.dirgha.bookmyseat.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.data.model.Route
import com.dirgha.bookmyseat.viewmodel.AdminViewModel
import com.dirgha.bookmyseat.viewmodel.RouteViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(navController: NavController) {

    val adminViewModel: AdminViewModel = viewModel()
    val routeViewModel: RouteViewModel = viewModel()

    val routes by routeViewModel.routes.collectAsState()
    val message by adminViewModel.message.collectAsState()

    // NOTE: this requires adding a `tripCreated: StateFlow<Boolean>` and a
    // `resetTripCreatedState()` function to AdminViewModel — see the chat
    // message for the exact snippet to drop in.
    val tripCreated by adminViewModel.tripCreated.collectAsState()

    var selectedRoute by remember { mutableStateOf<Route?>(null) }
    var selectedDirection by remember { mutableStateOf("") }
    var routeExpanded by remember { mutableStateOf(false) }
    var directionExpanded by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf("") }
    var timeSlot by remember { mutableStateOf("") }
    var fare by remember { mutableStateOf("") }
    var seats by remember { mutableStateOf("") }

    fun clearForm() {
        selectedRoute = null
        selectedDirection = ""
        date = ""
        timeSlot = ""
        fare = ""
        seats = ""
    }

    LaunchedEffect(Unit) {
        routeViewModel.loadRoutes()
    }

    // Once the trip is created successfully: wipe the form, navigate to the
    // reusable success screen with the message, then reset the flag so this
    // doesn't re-fire on recomposition / back navigation.
    LaunchedEffect(tripCreated) {
        if (tripCreated) {
            val successMessage = message.ifBlank { "Trip created successfully!" }
            clearForm()
            navController.navigate("trip_success/${Uri.encode(successMessage)}")
            adminViewModel.resetTripCreatedState()
        }
    }

    val seatFocusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day)
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val today = System.currentTimeMillis()
    datePickerDialog.datePicker.minDate = today
    datePickerDialog.datePicker.maxDate = today + (7 * 24 * 60 * 60 * 1000L)

    fun showTimePicker() {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                timeSlot = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.time)
            },
            6,
            0,
            false
        ).show()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Create Trip",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        },
        bottomBar = {
            // Sticks to the bottom; only the form content above scrolls.
            Surface(

                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column( modifier = Modifier.navigationBarsPadding() // keeps clear of gesture nav / nav bar
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 28.dp)
                ) {
                    if (message.isNotEmpty() && !tripCreated) {
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Button(
                        onClick = {
                            adminViewModel.createTrip(
                                route = selectedDirection,
                                date = date,
                                timeSlot = timeSlot,
                                fare = fare.toDoubleOrNull() ?: 0.0,
                                seats = seats.toIntOrNull() ?: 0
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create Trip")
                    }
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp) // tighter gap between cards
        ) {

            // ---------- Route Details ----------
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Route Details",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = routeExpanded,
                        onExpandedChange = { routeExpanded = !routeExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedRoute?.routeName ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Route") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = routeExpanded)
                            },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = routeExpanded,
                            onDismissRequest = { routeExpanded = false }
                        ) {
                            routes.forEach { route ->
                                DropdownMenuItem(
                                    text = { Text(route.routeName) },
                                    onClick = {
                                        selectedRoute = route
                                        selectedDirection = ""
                                        routeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (selectedRoute != null) {
                        ExposedDropdownMenuBox(
                            expanded = directionExpanded,
                            onExpandedChange = { directionExpanded = !directionExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedDirection,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select Direction") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = directionExpanded)
                                },
                                modifier = Modifier.fillMaxWidth().menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = directionExpanded,
                                onDismissRequest = { directionExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(selectedRoute!!.up) },
                                    onClick = {
                                        selectedDirection = selectedRoute!!.up
                                        directionExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(selectedRoute!!.down) },
                                    onClick = {
                                        selectedDirection = selectedRoute!!.down
                                        directionExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // ---------- Schedule ----------
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Schedule",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = date,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Trip Date") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { datePickerDialog.show() }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = timeSlot,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Time Slot") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { showTimePicker() }
                        )
                    }
                }
            }

            // ---------- Trip Settings ----------
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Trip Settings",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = fare,
                            onValueChange = { fare = it },
                            label = { Text("Fare") },
                            leadingIcon = {
                                Text(
                                    text = "₹",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { seatFocusRequester.requestFocus() }
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        OutlinedTextField(
                            value = seats,
                            onValueChange = { seats = it },
                            label = { Text("Seats") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(seatFocusRequester)
                        )
                    }
                }
            }
        }
    }
}
