package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.viewmodel.MyBookingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelSummaryScreen(
    navController: NavController
) {

    val viewModel: MyBookingViewModel = viewModel()

    val bookings by viewModel.bookings.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyBookings()
    }

    val totalTrips = bookings.size

    val upcoming =
        bookings.count { it.bookingStatus == "CONFIRMED" }

    val pending =
        bookings.count { it.bookingStatus == "PENDING" }

    val rejected =
        bookings.count { it.bookingStatus == "REJECTED" }

    val success =
        if (bookings.isEmpty()) 0
        else ((upcoming * 100) / bookings.size)
    val completedTrips =
        bookings.count {
            it.bookingStatus == "CONFIRMED" ||
                    it.bookingStatus == "COMPLETED"
        }
    val favouriteRoute =
        bookings
            .groupingBy { it.route}
            .eachCount()
            .maxByOrNull { it.value }
            ?.key ?: "No Trips"
    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        "Travel Summary",
                        fontWeight = FontWeight.Bold
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            Icons.Default.ArrowBack,
                            null
                        )

                    }

                }

            )

        }

    ) { padding ->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF4F5F7)),

            contentPadding = PaddingValues(16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {

            item {

                HeroSummaryCard(totalTrips)

            }

            item {

                Text(
                    "Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

            }

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.width(160.dp)) {
                        SummaryStatCard(
                            "Upcoming",
                            upcoming.toString(),
                            Icons.Default.Schedule
                        )
                    }
                    Box(modifier = Modifier.width(160.dp)) {
                        SummaryStatCard(
                            "Pending",
                            pending.toString(),
                            Icons.Default.Pending
                        )
                    }
                }

            }

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.width(160.dp)) {
                    SummaryStatCard(
                        "Rejected",
                        rejected.toString(),
                        Icons.Default.Route
                    )}
                    Box(modifier = Modifier.width(160.dp)) {
                    SummaryStatCard(
                        "Success",
                        "$success%",
                        Icons.Default.Verified
                    )}

                }

            }
            item {
                CompletedTripsCard(completedTrips)
            }

            item {
                FavouriteRouteCard(favouriteRoute)
            }

        }

    }

}

@Composable
fun HeroSummaryCard(totalTrips: Int) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Magenta
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF1565C0),
                            Color(0xFF42A5F5)
                        )
                    )
                )
                .padding(18.dp)
        ) {

            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        "🚖",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(Modifier.width(10.dp))

                    Column {

                        Text(
                            "Travel Summary",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Welcome Back 👋",
                            color = Color.White.copy(.85f),
                            style = MaterialTheme.typography.bodySmall
                        )

                    }

                }

                Spacer(Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {

                    Text(
                        text = "$totalTrips",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Total Trips",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                }

            }

        }

    }

}

@Composable
fun SummaryStatCard(

    title: String,

    value: String,

    icon: ImageVector

) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(18.dp)
    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Icon(
                icon,
                null,
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(30.dp)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(

                title,

                color = Color.Gray

            )

        }

    }

}
@Composable
fun CompletedTripsCard(completedTrips: Int) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "🏆",
                style = MaterialTheme.typography.displaySmall
            )

            Spacer(Modifier.width(12.dp))

            Column {

                Text(
                    "Completed Trips",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    "$completedTrips",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Successfully completed journeys",
                    color = Color.Gray
                )

            }

        }

    }

}
@Composable
fun FavouriteRouteCard(route: String) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8E1)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "⭐",
                style = MaterialTheme.typography.displaySmall
            )

            Spacer(Modifier.width(12.dp))

            Column {

                Text(
                    "Favourite Route",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    route,
                    style =  MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1565C0),
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "Most travelled route",
                    color = Color.Gray
                )

            }

        }

    }

}