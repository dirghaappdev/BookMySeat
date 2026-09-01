package com.dirgha.bookmyseat.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.data.model.Route
import com.dirgha.bookmyseat.utils.InternetManager
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
            navController.navigate("trip_success/${Uri.encode(successMessage)}")
            //clearForm()
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

    // Shared filled-field look used across the form (matches login/register screens)
    val fieldShape = RoundedCornerShape(12.dp)
    val fieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFF1F2F4),
        focusedContainerColor = Color(0xFFF1F2F4),
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )

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
                Column(
                    modifier = Modifier
                        .navigationBarsPadding() // keeps clear of gesture nav / nav bar
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
                            if (!InternetManager.requireInternet(context)) {

                                Toast.makeText(
                                    context,
                                    "No Internet Connection",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@Button
                            }

                            adminViewModel.createTrip(
                                route = selectedDirection,
                                date = date,
                                timeSlot = timeSlot,
                                fare = fare.toDoubleOrNull() ?: 0.0,
                                seats = seats.toIntOrNull() ?: 0
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0B0C14),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = "Create Trip",
                            fontWeight = FontWeight.Bold
                        )
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ---------- Route & Schedule ----------
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Route & Schedule",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Route
                    Text(
                        text = "Route",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    ExposedDropdownMenuBox(
                        expanded = routeExpanded,
                        onExpandedChange = { routeExpanded = !routeExpanded }
                    ) {
                        TextField(
                            value = selectedRoute?.routeName ?: "",
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Select route") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = routeExpanded)
                            },
                            shape = fieldShape,
                            colors = fieldColors,
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


                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Trip",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    ExposedDropdownMenuBox(
                        expanded = directionExpanded && selectedRoute != null,
                        onExpandedChange = {
                            if (selectedRoute != null) {
                                directionExpanded = !directionExpanded
                            }
                        }
                    ) {
                        TextField(
                            value = selectedDirection,
                            onValueChange = {},
                            readOnly = true,
                            enabled = selectedRoute != null,
                            placeholder = {
                                Text(
                                    if (selectedRoute == null)
                                        "Select route first"
                                    else
                                        "Select trip"
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = directionExpanded
                                )
                            },
                            shape = fieldShape,
                            colors = fieldColors,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        if (selectedRoute != null) {
                            ExposedDropdownMenu(
                                expanded = directionExpanded,
                                onDismissRequest = {
                                    directionExpanded = false
                                }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(selectedRoute!!.up)
                                    },
                                    onClick = {
                                        selectedDirection = selectedRoute!!.up
                                        directionExpanded = false
                                    }
                                )

                                DropdownMenuItem(
                                    text = {
                                        Text(selectedRoute!!.down)
                                    },
                                    onClick = {
                                        selectedDirection = selectedRoute!!.down
                                        directionExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Trip Date
                    Text(
                        text = "Trip Date",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = date,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Select date") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            shape = fieldShape,
                            colors = fieldColors,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { datePickerDialog.show() }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Departure Time (kept as the existing single time-picker field —
                    // intentionally not the selectable morning/evening box style)
                    Text(
                        text = "Departure Time",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = timeSlot,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Select time") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            shape = fieldShape,
                            colors = fieldColors,
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
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Trip Settings",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Total Seats",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            TextField(
                                value = seats,
                                onValueChange = { seats = it },
                                placeholder = { Text("0") },
                                shape = fieldShape,
                                colors = fieldColors,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(seatFocusRequester)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Fare per Seat (₹)",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            TextField(
                                value = fare,
                                onValueChange = { fare = it },
                                placeholder = { Text("0") },
                                leadingIcon = {
                                    Text(
                                        text = "₹",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                },
                                shape = fieldShape,
                                colors = fieldColors,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { seatFocusRequester.requestFocus() }
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}