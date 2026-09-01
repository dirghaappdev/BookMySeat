package com.dirgha.bookmyseat.ui.screens

import android.os.Build
import android.content.Intent
import androidx.compose.material.icons.outlined.Share
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.viewmodel.AdminBookingViewModel
import java.time.LocalDate
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.platform.LocalContext
import com.dirgha.bookmyseat.data.config.RejectReason
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.ui.components.FilterChipItem
import com.dirgha.bookmyseat.utils.ConfigManager


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ManageBookingsScreen(navController: NavController) {

    val viewModel:
            AdminBookingViewModel =
        viewModel()

    val uriHandler = LocalUriHandler.current

    val focusManager = LocalFocusManager.current


    val bookings by
    viewModel.bookings.collectAsState()

    val error by
    viewModel.error.collectAsState()

    val today = LocalDate.now()
    val tomorrow = today.plusDays(1)

    var hasLoaded by remember { mutableStateOf(false) }
    val isRefreshing by viewModel.isLoading.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()
    val context = LocalContext.current

    var rejectionReasons by remember {
        mutableStateOf(emptyList<com.dirgha.bookmyseat.data.config.RejectReason>())
    }

    LaunchedEffect(Unit) {

        val config = ConfigManager(context).getConfiguration()

        rejectionReasons = config?.data?.masters?.rejectReason
            ?.filter { it.is_active }
            ?: emptyList()

    }

    LaunchedEffect(Unit) {
        viewModel.loadBookings()
        hasLoaded = true
    }

    var showRejectDialog by remember {
        mutableStateOf(false)
    }

    var selectedBookingId by remember {
        mutableStateOf("")
    }

    var rejectionReason by remember {
        mutableStateOf<RejectReason?>(null)
    }

    var customReason by remember {
        mutableStateOf("")
    }

    var phoneSearch by remember {
        mutableStateOf("")
    }


    var selectedFilter by remember { mutableStateOf("All") }

    val filters = listOf("All", "Pending", "Confirmed", "Rejected","Today's Rides", "Tomorrow's Rides", "Customer")

    val filteredBookings = when (selectedFilter) {

        "All" -> bookings

        "Pending" ->
            bookings.filter {
                it.bookingStatus.equals("PENDING", true)
            }

        "Confirmed" ->
            bookings.filter {
                it.bookingStatus.equals("CONFIRMED", true)
            }

        "Rejected" ->
            bookings.filter {
                it.bookingStatus.equals("REJECTED", true)
            }

        "Today's Rides" ->
            bookings.filter {
                LocalDate.parse(it.date) == today
            }

        "Tomorrow's Rides" ->
            bookings.filter {
                LocalDate.parse(it.date) == tomorrow
            }

        "Customer" ->
            bookings.filter {
                it.mobileNumber.contains(
                    phoneSearch,
                    ignoreCase = true
                )
            }

        else -> bookings
    }.sortedByDescending {
        LocalDate.parse(it.date)
    }.sortedWith(
            compareByDescending<com.dirgha.bookmyseat.data.model.Booking> {
                LocalDate.parse(it.date)
            }.thenByDescending {
                it.timeSlot
            }
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color.White)
            .background(Color(0xFFF5F7FC))
    ) {

        // ---------- Header ----------
        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(top = 15.dp)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "Booking Requests",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
//            Button(
//                onClick = {
//                    navController.navigate(
//                        Screen.ManageParcelBookings.route
//                    )
//                }
//            ) {
//                Text("Manage Parcels")
//            }
        }

        // ---------- Filters ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //.background(Color(0xFFF5F7FC))
                .horizontalScroll(rememberScrollState())
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                FilterChipItem(
                    title = filter,
                    selected = selectedFilter
                ) {
                    selectedFilter = filter
                }
            }
        }
        if (selectedFilter == "Customer") {

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = phoneSearch,
                onValueChange = {
                    // Allow only digits and max 10 characters
                    if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
                        phoneSearch = it
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text("Search by mobile number")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {

                        focusManager.clearFocus()   // Hides keyboard
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color(0xFFF8FAFF),
                    unfocusedContainerColor = Color(0xFFF8FAFF)
                )
            )
            if (phoneSearch.length == 10 && filteredBookings.isNotEmpty()) {

                val customerName = filteredBookings.first().userName

                val totalRides = filteredBookings.size
                val confirmedRides = filteredBookings.count {
                    it.bookingStatus.equals("CONFIRMED", true)
                }
                val rejectedRides = filteredBookings.count {
                    it.bookingStatus.equals("REJECTED", true)
                }
                val pendingRides = filteredBookings.count {
                    it.bookingStatus.equals("PENDING", true)
                }

                val totalRevenue = filteredBookings.sumOf {
                    it.fare.toDouble()
                }

                val confirmationPercentage =
                    if (totalRides == 0) 0
                    else ((confirmedRides * 100.0) / totalRides).toInt()

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF8FAFF)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xFFD6E4FF)    // Light Blue Border
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = customerName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Text(
                                text = phoneSearch,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            SummaryMiniCard(
                                title = "Confirm",
                                value = confirmedRides.toString(),
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.weight(1f)
                            )

                            SummaryMiniCard(
                                title = "Pending",
                                value = pendingRides.toString(),
                                color = Color(0xFFFF9800),
                                modifier = Modifier.weight(1f)
                            )

                            SummaryMiniCard(
                                title = "Reject",
                                value = rejectedRides.toString(),
                                color = Color.Red,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            SummaryMiniCard(
                                title = "Total",
                                value = totalRides.toString(),
                                color = Color(0xFF1565C0),
                                modifier = Modifier.weight(1f)
                            )

                            SummaryMiniCard(
                                title = "Earned",
                                value = "₹${totalRevenue.toInt()}",
                                color = Color(0xFF00897B),
                                modifier = Modifier.weight(1f)
                            )

                            SummaryMiniCard(
                                title = "Success",
                                value = "$confirmationPercentage%",
                                color = when {
                                    confirmationPercentage >= 80 -> Color(0xFF2E7D32)
                                    confirmationPercentage >= 50 -> Color(0xFFFF9800)
                                    else -> Color.Red
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(color = Color(0xFFF0F0F0))

        // ---------- Content ----------
        when {
            !hasLoaded -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error.isNotBlank() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.loadBookings() }) {
                            Text("Retry")
                        }
                    }
                }
            }

            filteredBookings.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (bookings.isEmpty()) {
                                "No booking requests yet"
                            } else {
                                "No \"$selectedFilter\" bookings"
                            },
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            else -> {PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    viewModel.loadBookings()
                },
                state = pullRefreshState
            ){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F7FC))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(filteredBookings) { booking ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            border = BorderStroke(
                                1.dp,
                                Color(0xFFEAEAEA)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                // Header
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            "\uD83D\uDC64 ${booking.userName}",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp
                                        )

                                        Text(
                                            "\uD83D\uDCDE ${booking.mobileNumber}",
                                            color = Color(0xFF2962FF),
                                            fontSize = 13.sp,
                                            modifier = Modifier.clickable {
                                                uriHandler.openUri("tel:${booking.mobileNumber}")
                                            }
                                        )
                                    }

                                    BookingStatusPill(
                                        booking.bookingStatus
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Route
                                Text(
                                    text = "📍 ${booking.route}",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "\uD83C\uDD94 #BMS${booking.bookingId.takeLast(4)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )


                                Spacer(modifier = Modifier.height(6.dp))

                                // Date row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "${booking.date} · ${booking.timeSlot}",
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        text = "${booking.passengerCount} pax",
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.width(20.dp))

                                    Text(
                                        text = "₹${booking.fare.toInt()}",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }

                                if (
                                    booking.bookingStatus == "REJECTED" &&
                                    booking.rejectionReason.isNotBlank()
                                ) {
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFFFFF3F3)
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp)
                                        ) {
                                            Text(
                                                "Rejection Reason",
                                                color = Color.Red,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(
                                                booking.rejectionReason
                                            )
                                        }
                                    }
                                }

                                // Notes
                                if (booking.note.isNotBlank()) {

                                    Spacer(modifier = Modifier
                                        .height(12.dp))

                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFFF3F6FC)
                                        ),
                                        border = BorderStroke(
                                            1.dp,
                                            Color(0xFFEAEAEA)
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp)
                                        ) {
                                            Text(
                                                "Notes",
                                                color = Color.Gray,
                                                fontSize = 12.sp
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(booking.note)
                                        }
                                    }
                                }

                                //Booking Overload
                                if (booking.isOverBooked) {

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFFFFF3F3)
                                        ),
                                        border = BorderStroke(
                                            1.dp,
                                            Color.Red
                                        )
                                    ) {

                                        Column(
                                            modifier = Modifier.padding(14.dp)
                                        ) {

                                            Text(
                                                text = "⚠ OVERBOOKED TRIP",
                                                color = Color.Red,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

//                                            Text(
//                                                text = "Vehicle Capacity : ${booking.totalSeats}"
//                                            )
//
//                                            Text(
//                                                text = "Booked Seats : ${booking.bookedPassengers}"
//                                            )
//
//                                            Text(
//                                                text = "Extra Seats : ${booking.overBookedSeats}",
//                                                color = Color.Red,
//                                                fontWeight = FontWeight.Bold
//                                            )
//
//                                            Spacer(modifier = Modifier.height(6.dp))

                                            Text(
                                                text = "Arrange an additional vehicle before confirming these bookings.",
                                                color = Color.DarkGray
                                            )
                                        }
                                    }

                                    // ============================================================
                                    // SHARE OVERBOOKED RIDE
                                    // ============================================================

                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedButton(
                                        onClick = {

                                            val shareMessage = """
BookMySeat – Ride Request

Passenger: ${booking.userName}
Mobile: ${booking.mobileNumber}

Route: ${booking.route}
Date: ${booking.date}
Time: ${booking.timeSlot}
Passengers: ${booking.passengerCount}
Fare: ₹${booking.fare.toInt()}

Booking ID: #BMS${booking.bookingId.takeLast(4)}

This ride is currently overbooked and the vehicle is not available.

Please check if you can take this ride.
""".trimIndent()


                                            val intent = Intent(Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(
                                                    Intent.EXTRA_TEXT,
                                                    shareMessage
                                                )
                                            }

                                            context.startActivity(
                                                Intent.createChooser(
                                                    intent,
                                                    "Share Ride"
                                                )
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        border = BorderStroke(
                                            1.dp,
                                            Color(0xFF25D366)
                                        ),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = Color(0xFF128C7E)
                                        )
                                    ) {

                                        Icon(
                                            imageVector = Icons.Outlined.Share,
                                            contentDescription = null
                                        )

                                        Spacer(
                                            modifier = Modifier.width(8.dp)
                                        )

                                        Text(
                                            text = "Share Ride with Driver",
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }


                                // Buttons
                                if (booking.bookingStatus == "PENDING") {

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {

                                        OutlinedButton(
                                            onClick = {
                                                selectedBookingId = booking.bookingId
                                                rejectionReason = null
                                                showRejectDialog = true
                                            },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text("Reject")
                                        }

                                        Button(
                                            onClick = {
                                                viewModel.confirmBooking(
                                                    booking.bookingId
                                                )
                                            },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor =   if (booking.isOverBooked)
                                                    Color(0xFFFF9800)
                                                else
                                                    Color(0xFF0BAA39)
                                            )
                                        ) {
                                            Text(
                                                    "Accept"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (showRejectDialog) {

                    AlertDialog(
                        onDismissRequest = {
                            showRejectDialog = false
                        },
                        title = {
                            Text("Reject Booking")
                        },
                        text = {
                            Column {

                                Text(
                                    "Please select a reason:"
                                )

                                Spacer(
                                    Modifier.height(12.dp)
                                )

//                                val reasons = listOf(
//                                    "No seats available",
//                                    "Trip cancelled",
//                                    "Passenger information incomplete",
//                                    "Invalid contact number",
//                                    "Passenger did not respond to calls",
//                                    "Passenger requested cancellation",
//                                    "Duplicate booking",
//                                    "Violation of booking rules",
//                                    "Other"
//                                )

                                rejectionReasons.forEach { reason ->

                                    Row(
                                        verticalAlignment =
                                            Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                rejectionReason = reason
                                            }
                                    ) {

                                        RadioButton(
                                            selected =
                                                rejectionReason == reason,
                                            onClick = {
                                                rejectionReason = reason
                                            }
                                        )

                                        Text(reason.name)
                                    }
                                }
                                if (rejectionReason?.name == "Other") {

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = customReason,
                                    onValueChange = {
                                        customReason = it
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = {
                                        Text("Enter rejection reason")
                                    },
                                    placeholder = {
                                        Text("Type your reason here")
                                    },
                                    singleLine = true,
                                    maxLines = 1
                                )
                            }
                            }
                        },
                        confirmButton = {

                            Button(
                                enabled = !rejectionReason?.name.isNullOrBlank(),
                                onClick = {
                                    val finalReason =
                                        if (rejectionReason?.name == "Other")
                                            customReason
                                        else
                                            rejectionReason?.name ?: ""

                                    viewModel.rejectBooking(
                                        selectedBookingId,
                                        finalReason
                                    )

                                    showRejectDialog = false
                                    rejectionReason = null
                                    customReason = ""
                                }
                            ) {
                                Text("Reject")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showRejectDialog = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }}

        }
    }
}
@Composable
fun SummaryMiniCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = title,
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun BookingStatusPill(status: String) {

    val (bg, fg) = when (status.trim().uppercase()) {
        "PENDING" -> Color(0xFFFFF3CD) to Color(0xFFB8860B)
        "CONFIRMED" -> Color(0xFFDDF5E4) to Color(0xFF1E8E3E)
        "REJECTED" -> Color(0xFFFDE3E3) to Color(0xFFD93025)
        else -> Color(0xFFEFEFEF) to Color.DarkGray
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.uppercase(),
            color = fg,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
