package com.dirgha.bookmyseat.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import java.util.Locale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirgha.bookmyseat.viewmodel.MyBookingViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.ui.components.JourneySummaryCard
import kotlin.text.format

private val Blue = Color(0xFF2962FF)
private val Green = Color(0xFF16A34A)
private val Red = Color(0xFFDC2626)
private val CardBorder = Color(0xFFE5E7EB)
private val Background = Color(0xFFF8FAFC)

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyBookingsScreen( navController: NavController) {

    val viewModel: MyBookingViewModel = viewModel()

    val bookings by viewModel.bookings.collectAsState()

    var selectedFilter by remember {
        mutableStateOf("All")
    }

    LaunchedEffect(Unit) {
        viewModel.loadMyBookings()
    }
    val pullRefreshState = rememberPullToRefreshState()
    val isRefreshing by viewModel.isLoading.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val today = LocalDate.now()
    val totalTrips =
        bookings.count {
            it.bookingStatus != "REJECTED"
        }

    val upcomingTrips =
        bookings.count {

            val bookingDate = try {
                LocalDate.parse(it.date, formatter)
            } catch (e: Exception) {
                null
            }

            bookingDate != null &&
                    !bookingDate.isBefore(today) &&
                    it.bookingStatus == "CONFIRMED"
        }

    val pendingTrips =
        bookings.count {
            it.bookingStatus == "PENDING"
        }

    val rejectedTrips =
        bookings.count {
            it.bookingStatus == "REJECTED"
        }

    val totalSpent =
        bookings
            .filter {
                it.bookingStatus == "CONFIRMED"
            }
            .sumOf {
                it.fare
            }

    val confirmedTrips =
        bookings.count {
            it.bookingStatus == "CONFIRMED"
        }

    val successRate =
        if (confirmedTrips == 0)
            0
        else
            (confirmedTrips * 100) / totalTrips

    val favouriteRoute =
        bookings
            .groupingBy { it.route }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key ?: "No Trips"

    val filteredBookings = bookings.filter { booking ->

        val bookingDate = try {
            LocalDate.parse(booking.date, formatter)
        } catch (e: Exception) {
            null
        }

        when (selectedFilter) {

            "Upcoming" ->
                bookingDate != null &&
                        !bookingDate.isBefore(today) &&
                        booking.bookingStatus.lowercase() != "cancelled"

            "Completed" ->
                bookingDate != null &&
                        bookingDate.isBefore(today) &&
                        booking.bookingStatus.lowercase() != "cancelled"

            else ->
                booking.bookingStatus.lowercase() != "cancelled"
        }
    }

    val sortedBookings = filteredBookings.sortedWith(
        compareByDescending<com.dirgha.bookmyseat.data.model.Booking> {
            LocalDate.parse(it.date, formatter)
        }.thenByDescending {
            it.timeSlot
        }
    )
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFECEFF1))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "My Trips",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
//            Button(
//                onClick = {
//                    navController.navigate(
//                        Screen.MyParcels.route
//                    )
//                }
//            ) {
//                Text("My Parcels")
//            }
            Spacer(modifier = Modifier.height(12.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                FilterChipItem("All", selectedFilter) {
                    selectedFilter = "All"
                }

                FilterChipItem("Upcoming", selectedFilter) {
                    selectedFilter = "Upcoming"
                }

                FilterChipItem("Completed", selectedFilter) {
                    selectedFilter = "Completed"
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFE5E7EB)
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                viewModel.loadMyBookings()
            },
            state = pullRefreshState
        ) {

            if (sortedBookings.isEmpty()) {

                EmptyBookingsCard(selectedFilter)

            } else {

                LazyColumn(
                    modifier = Modifier.background(Color(0xFFECEFF1)),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {

                        JourneySummaryCard(
                            totalTrips = totalTrips,
                            upcomingTrips = upcomingTrips,
                            totalSpent = totalSpent,
                            pendingTrips = pendingTrips,
                            rejectedTrips = rejectedTrips,
                            successRate = successRate,
                            favouriteRoute = favouriteRoute
                        )

                    }


                    items(sortedBookings) { booking ->
                        BookingCard(booking)
                    }

                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun BookingCard(
    booking: com.dirgha.bookmyseat.data.model.Booking
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            // ---------- Route ----------
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = Blue,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = booking.route,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ---------- Date & Time ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.CalendarMonth,
                        null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    val formattedDate = LocalDate
                        .parse(booking.date)
                        .format(
                            DateTimeFormatter.ofPattern(
                                "dd MMM yyyy",
                                Locale.ENGLISH
                            )
                        )
                    Text(
                        formattedDate,
                        color = Color.DarkGray
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.AccessTime,
                        null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        booking.timeSlot,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ---------- Status & Fare ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                StatusChip(
                    status = booking.bookingStatus
                )

                Text(
                    text = "₹${booking.fare.toInt()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            // ---------- Rejection Reason ----------
            if (
                booking.bookingStatus == "REJECTED" &&
                booking.rejectionReason.isNotBlank()
            ) {
                Spacer(modifier = Modifier.height(10.dp))

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
                            "Reason for Rejection",
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
        }}}

@Composable
private fun StatusChip(
    status: String
) {

    val background = when (status.lowercase()) {
        "confirmed" -> Color(0xFFDDF5E3) // Light Green
        "pending" -> Color(0xFFFFF3CD)   // Light Yellow
        "rejected" -> Color(0xFFFDE2E2)  // Light Red
        else -> Color(0xFFDCE8FF)        // Light Blue
    }

    val textColor = when (status.lowercase()) {
        "confirmed" -> Color(0xFF16A34A) // Green
        "pending" -> Color(0xFFD97706)   // Orange/Yellow
        "rejected" -> Color(0xFFDC2626)  // Red
        else -> Blue
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(
                horizontal = 12.dp,
                vertical = 5.dp
            )
    ) {
        Text(
            text = status.replaceFirstChar {
                it.uppercase()
            },
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun EmptyBookingsCard(filter: String) {

    val (title, message) = when (filter) {

        "Upcoming" -> Pair(
            "No Upcoming Trips",
            "You don't have any upcoming bookings.\nBook your next ride now!"
        )

        "Completed" -> Pair(
            "No Completed Trips",
            "Your completed trips will appear here."
        )

        else -> Pair(
            "No Bookings Yet",
            "You haven't booked any rides yet.\nStart by booking your first trip."
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "🧳",
                    fontSize = 64.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun FilterChipItem(
    title: String,
    selected: String,
    onClick: () -> Unit
) {

    val isSelected = selected == title

    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Color.Companion.Blue else Color(0xFFF5F5F5),
        tonalElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 6.dp
            ),
            color = if (isSelected) Color.White else Color(0xFF555555),
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 12.sp
        )
    }
}