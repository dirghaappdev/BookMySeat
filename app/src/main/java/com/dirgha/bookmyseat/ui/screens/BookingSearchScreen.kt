package com.dirgha.bookmyseat.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.layout.FlowRow
import com.dirgha.bookmyseat.viewmodel.BookingViewModel
import com.dirgha.bookmyseat.viewmodel.TripViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSearchScreen(
    navController: NavController,
    tripViewModel: TripViewModel = viewModel()
) {

    // ============================================================
    // LOAD DATA
    // ============================================================

    LaunchedEffect(Unit) {
        tripViewModel.loadTrips()
    }

    val context = LocalContext.current
    val density = LocalDensity.current

    val bookingViewModel: BookingViewModel = viewModel()

    val bookingMessage by
    bookingViewModel.message.collectAsState()

    val trips by
    tripViewModel.trips.collectAsState()

    // ============================================================
    // STATE
    // ============================================================

    var bookingType by rememberSaveable {
        mutableStateOf("RIDE")
    }

    var selectedRoute by rememberSaveable {
        mutableStateOf("")
    }

    var selectedDate by rememberSaveable {
        mutableStateOf("")
    }

    var selectedTripId by rememberSaveable {
        mutableStateOf("")
    }

    var passengerCount by rememberSaveable {
        mutableStateOf("1")
    }

    var routeExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    // ============================================================
    // ROUTE FIELD WIDTH
    // ============================================================

    var routeFieldWidth by remember {
        mutableStateOf(0.dp)
    }

    // ============================================================
    // ROUTES
    // ============================================================

    val routes = trips
        .map { it.route }
        .distinct()
        .sorted()

    // ============================================================
    // AVAILABLE TRIPS
    // ============================================================

    val availableTrips = trips
        .filter {
            it.route == selectedRoute &&
                    it.date == selectedDate
        }
        .sortedBy {
            it.timeSlot
        }

    // ============================================================
    // SCREEN
    // ============================================================

    Scaffold(

        topBar = {

            Column {

                TopAppBar(

                    title = {

                        Column {

                            Text(
                                text = "Book Your Ride",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "Choose route, date and time",
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = {
                                navController.navigate("member_home")
                            }
                        ) {

                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },

                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor =
                                MaterialTheme.colorScheme.primary,

                            titleContentColor =
                                MaterialTheme.colorScheme.onPrimary,

                            navigationIconContentColor =
                                MaterialTheme.colorScheme.onPrimary
                        ),

                    modifier =
                        Modifier.height(90.dp)
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE5E7EB)
                )
            }
        }

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 15.dp,
                        bottom = 120.dp
                    )
            ) {

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                // ====================================================
                // ROUTE
                // ====================================================

                Text(
                    text = "Select Route",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                ExposedDropdownMenuBox(

                    expanded = routeExpanded,

                    onExpandedChange = {
                        routeExpanded = !routeExpanded
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(

                        value = selectedRoute,

                        onValueChange = {},

                        readOnly = true,

                        placeholder = {

                            Text(
                                text = "Choose your route"
                            )
                        },

                        leadingIcon = {

                            Icon(
                                Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF2962FF)
                            )
                        },

                        trailingIcon = {

                            Icon(
                                Icons.Outlined.KeyboardArrowDown,
                                contentDescription = null,
                                tint =
                                    if (routeExpanded)
                                        Color(0xFF2962FF)
                                    else
                                        Color.Gray
                            )
                        },

                        shape =
                            RoundedCornerShape(18.dp),

                        colors =
                            OutlinedTextFieldDefaults.colors(

                                // ====================================
                                // BLUE WHEN OPEN
                                // ====================================

                                focusedBorderColor =
                                    Color(0xFF2962FF),

                                // ====================================
                                // BLUE AFTER SELECTION
                                // ====================================

                                unfocusedBorderColor =
                                    if (
                                        selectedRoute.isNotEmpty() ||
                                        routeExpanded
                                    ) {
                                        Color(0xFF2962FF)
                                    } else {
                                        Color(0xFFE5E7EB)
                                    },

                                focusedContainerColor =
                                    Color.White,

                                unfocusedContainerColor =
                                    Color.White,

                                cursorColor =
                                    Color(0xFF2962FF)
                            ),

                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->

                                routeFieldWidth =
                                    with(density) {
                                        coordinates.size.width.toDp()
                                    }
                            }
                    )

                    // ====================================================
                    // ROUTE DROPDOWN
                    // ====================================================

                    ExposedDropdownMenu(

                        expanded =
                            routeExpanded,

                        onDismissRequest = {
                            routeExpanded = false
                        },

                        modifier = Modifier
                            .width(routeFieldWidth)
                    ) {

                        routes.forEach { route ->

                            DropdownMenuItem(

                                text = {

                                    Text(
                                        text = route,
                                        fontSize = 15.sp
                                    )
                                },

                                onClick = {

                                    selectedRoute =
                                        route

                                    selectedDate =
                                        ""

                                    selectedTripId =
                                        ""

                                    routeExpanded =
                                        false
                                }
                            )
                        }
                    }
                }

                // ====================================================
                // DATE
                // ====================================================

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Text(
                    text = "Travel Date",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(

                        value =
                            selectedDate,

                        onValueChange = {},

                        readOnly = true,

                        enabled = false,

                        placeholder = {

                            Text(
                                text = "Select travel date"
                            )
                        },

                        leadingIcon = {

                            Icon(
                                Icons.Outlined.CalendarMonth,
                                contentDescription = null,
                                tint = Color(0xFF2962FF)
                            )
                        },

                        trailingIcon = {

                            Icon(
                                Icons.Outlined.KeyboardArrowDown,
                                contentDescription = null,
                                tint =
                                    if (
                                        selectedDate.isNotEmpty()
                                    )
                                        Color(0xFF2962FF)
                                    else
                                        Color.Gray
                            )
                        },

                        shape =
                            RoundedCornerShape(18.dp),

                        colors =
                            OutlinedTextFieldDefaults.colors(

                                disabledTextColor =
                                    Color.Black,

                                disabledPlaceholderColor =
                                    Color.Gray,

                                disabledBorderColor =
                                    if (
                                        selectedDate.isNotEmpty()
                                    ) {
                                        Color(0xFF2962FF)
                                    } else {
                                        Color(0xFFE5E7EB)
                                    },

                                disabledContainerColor =
                                    Color.White,

                                disabledLeadingIconColor =
                                    Color(0xFF2962FF),

                                disabledTrailingIconColor =
                                    if (
                                        selectedDate.isNotEmpty()
                                    )
                                        Color(0xFF2962FF)
                                    else
                                        Color.Gray
                            ),

                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    // Invisible clickable layer
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {

                                if (
                                    selectedRoute.isNotEmpty()
                                ) {

                                    val calendar =
                                        Calendar.getInstance()

                                    val datePickerDialog =
                                        DatePickerDialog(

                                            context,

                                            { _, year, month, day ->

                                                val selectedCalendar =
                                                    Calendar.getInstance()

                                                selectedCalendar.set(
                                                    year,
                                                    month,
                                                    day
                                                )

                                                selectedDate =
                                                    SimpleDateFormat(
                                                        "yyyy-MM-dd",
                                                        Locale.getDefault()
                                                    ).format(
                                                        selectedCalendar.time
                                                    )

                                                selectedTripId = ""
                                            },

                                            calendar.get(
                                                Calendar.YEAR
                                            ),

                                            calendar.get(
                                                Calendar.MONTH
                                            ),

                                            calendar.get(
                                                Calendar.DAY_OF_MONTH
                                            )
                                        )

                                    val today =
                                        System.currentTimeMillis()

                                    datePickerDialog
                                        .datePicker
                                        .minDate =
                                        today

                                    datePickerDialog
                                        .datePicker
                                        .maxDate =
                                        today +
                                                (
                                                        3L *
                                                                24L *
                                                                60L *
                                                                60L *
                                                                1000L
                                                        )

                                    datePickerDialog.show()
                                }
                            }
                    )
                }

                // ====================================================
                // TIME
                // ====================================================

                Spacer(
                    modifier =
                        Modifier.height(24.dp)
                )

                Text(
                    text = "Choose Time Slot",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                if (
                    selectedRoute.isEmpty() ||
                    selectedDate.isEmpty()
                ) {

                    Card(

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),

                        shape =
                            RoundedCornerShape(20.dp),

                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    Color(0xFFF1F2F5)
                            ),

                        border =
                            BorderStroke(
                                1.dp,
                                Color(0xFFD6D9E0)
                            )
                    ) {

                        Column(

                            modifier =
                                Modifier.fillMaxSize(),

                            horizontalAlignment =
                                Alignment.CenterHorizontally,

                            verticalArrangement =
                                Arrangement.Center
                        ) {

                            Text(
                                text = "🕒",
                                fontSize = 40.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(10.dp)
                            )

                            Text(
                                text =
                                    "Select route and date\nto see available time slots",

                                textAlign =
                                    TextAlign.Center,

                                color =
                                    Color.Gray
                            )
                        }
                    }

                } else {

                    if (
                        availableTrips.isEmpty()
                    ) {

                        Card(

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),

                            shape =
                                RoundedCornerShape(20.dp),

                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        Color(0xFFFFF8E1)
                                )
                        ) {

                            Column(

                                modifier =
                                    Modifier.fillMaxSize(),

                                horizontalAlignment =
                                    Alignment.CenterHorizontally,

                                verticalArrangement =
                                    Arrangement.Center
                            ) {

                                Text(
                                    text = "😔",
                                    fontSize = 40.sp
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(10.dp)
                                )

                                Text(
                                    text =
                                        "No trips available\nfor the selected date",

                                    textAlign =
                                        TextAlign.Center,

                                    color =
                                        Color.Gray,

                                    fontWeight =
                                        FontWeight.Medium
                                )
                            }
                        }

                    } else {

                        FlowRow(

                            modifier =
                                Modifier.fillMaxWidth(),

                            maxItemsInEachRow = 2,

                            horizontalArrangement =
                                Arrangement.spacedBy(12.dp),

                            verticalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {

                            availableTrips.forEach { trip ->

                                val isSelected =
                                    selectedTripId ==
                                            trip.tripId

                                Card(

                                    modifier = Modifier
                                        .width(140.dp)
                                        .height(70.dp)
                                        .clickable {

                                            selectedTripId =
                                                trip.tripId
                                        },

                                    shape =
                                        RoundedCornerShape(16.dp),

                                    colors =
                                        CardDefaults.cardColors(
                                            containerColor =
                                                if (isSelected)
                                                    Color(0xFFEAF2FF)
                                                else
                                                    Color.White
                                        ),

                                    border =
                                        BorderStroke(

                                            if (isSelected)
                                                2.dp
                                            else
                                                1.dp,

                                            if (isSelected)
                                                Color(0xFF2962FF)
                                            else
                                                Color(0xFFE0E0E0)
                                        )
                                ) {

                                    Row(

                                        modifier =
                                            Modifier.fillMaxSize(),

                                        horizontalArrangement =
                                            Arrangement.Center,

                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            Icons.Outlined.AccessTime,
                                            contentDescription =
                                                null,

                                            tint =
                                                Color.Gray
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.width(8.dp)
                                        )

                                        Text(
                                            text =
                                                trip.timeSlot
                                                    .replace(
                                                        "am",
                                                        "AM",
                                                        ignoreCase = true
                                                    )
                                                    .replace(
                                                        "pm",
                                                        "PM",
                                                        ignoreCase = true
                                                    ),

                                            fontWeight =
                                                FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // ====================================================
                // PASSENGERS
                // ====================================================

                Spacer(
                    modifier =
                        Modifier.height(24.dp)
                )

                Text(
                    text =
                        if (bookingType == "RIDE")
                            "Passengers"
                        else
                            "Parcels",

                    fontSize = 14.sp,

                    color = Color.Gray,

                    fontWeight =
                        FontWeight.Medium
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(18.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        ),

                    border =
                        BorderStroke(
                            1.dp,
                            Color(0xFFE5E7EB)
                        )
                ) {

                    Row(

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 18.dp
                                ),

                        verticalAlignment =
                            Alignment.CenterVertically,

                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        OutlinedButton(

                            onClick = {

                                val count =
                                    passengerCount
                                        .toIntOrNull()
                                        ?: 1

                                if (count > 1) {

                                    passengerCount =
                                        (
                                                count - 1
                                                ).toString()
                                }
                            },

                            modifier =
                                Modifier.size(42.dp),

                            contentPadding =
                                PaddingValues(0.dp),

                            shape =
                                RoundedCornerShape(14.dp)
                        ) {

                            Text(
                                text = "−",
                                fontSize = 20.sp
                            )
                        }

                        Text(
                            text =
                                passengerCount,

                            fontSize = 34.sp,

                            fontWeight =
                                FontWeight.SemiBold,

                            color =
                                Color(0xFF0F172A)
                        )

                        OutlinedButton(

                            onClick = {

                                val count =
                                    passengerCount
                                        .toIntOrNull()
                                        ?: 1

                                if (count < 6) {

                                    passengerCount =
                                        (
                                                count + 1
                                                ).toString()
                                }
                            },

                            modifier =
                                Modifier.size(42.dp),

                            contentPadding =
                                PaddingValues(0.dp),

                            shape =
                                RoundedCornerShape(14.dp)
                        ) {

                            Text(
                                text = "+",
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(30.dp)
                )

                Text(
                    text = bookingMessage,
                    color = Color.Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(100.dp)
                )
            }

            // ========================================================
            // REVIEW BOOKING BUTTON
            // ========================================================

            Button(

                onClick = {

                    val selectedTrip =
                        trips.firstOrNull {
                            it.tripId ==
                                    selectedTripId
                        }

                    selectedTrip?.let {

                        navController.navigate(
                            "booking_review/" +
                                    "${selectedTrip.tripId}/" +
                                    "${selectedTrip.route}/" +
                                    "${selectedTrip.date}/" +
                                    "${selectedTrip.timeSlot}/" +
                                    passengerCount +
                                    "/" +
                                    "${selectedTrip.fare}/" +
                                    bookingType
                        )
                    }
                },

                enabled =
                    selectedRoute.isNotEmpty() &&
                            selectedDate.isNotEmpty() &&
                            selectedTripId.isNotEmpty() &&
                            passengerCount.isNotEmpty(),

                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 30.dp
                    )
                    .height(58.dp),

                shape =
                    RoundedCornerShape(16.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color.Black
                    )
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "Review Booking",

                        fontSize = 18.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Spacer(
                        modifier =
                            Modifier.width(10.dp)
                    )

                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowForward,

                        contentDescription =
                            null,

                        tint =
                            Color.White
                    )
                }
            }
        }
    }
}