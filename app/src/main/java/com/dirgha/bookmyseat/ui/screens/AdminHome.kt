package com.dirgha.bookmyseat.ui.screens

import android.R
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.utils.InternetManager
import com.dirgha.bookmyseat.viewmodel.TripViewModel
import com.dirgha.bookmyseat.ui.screens.LogoutDialog
import com.dirgha.bookmyseat.data.model.Trip
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminHomeScreen(
    navController: NavController
) {

    val tripViewModel: TripViewModel = viewModel()

    val trips by tripViewModel.trips.collectAsState()
    val recentTrips = remember(trips) {
        trips
            .sortedByDescending { it.date }
            .take(4)
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        tripViewModel.loadTrips()
    }

    val context =LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color(0xFFF5F7FC))
            .background(Color(0xFFF5F7FC))
    ){

        // ---------- Header ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .background(
                    MaterialTheme.colorScheme.primary
                )
        ){

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 24.dp,
                        end = 20.dp,
                        top = 25.dp
                    )
            ) {
                Text(
                    text = "Admin Panel",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "BookMySeat Admin",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Manage trips, routes, and booking requests",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Notification bell (decorative for now - no notification logic exists yet)
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = 30.dp,
                        end = 20.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Notification Button
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(

                        onClick = {

                            navController.navigate(
                                Screen.Notifications.route
                            )

                        }

                    ) {

                        Icon(

                            Icons.Default.Notifications,

                            contentDescription = null,
                            tint = Color.White


                        )

                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFA726))
                    )
                }

                // Profile Button
                IconButton(
                    onClick = {
                        navController.navigate( Screen.ProfileScreen.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonOutline,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }

        // ---------- Stats dashboard + Today's Revenue cards: hidden for now ----------
        // When ready to bring these back, this is where the 4-stat grid
        // (Total Trips / Pending / Accepted / Rejected) and the revenue
        // card from the design would sit, right above Quick Actions.

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 110.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .background(Color(0xFFF5F7FC))
                    .fillMaxSize()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 10.dp
                    )
            ) {

            Spacer(modifier = Modifier.height(20.dp))

                // ---------- Quick Actions ----------
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    // --------------------------------------------------------
                    // CREATE TRIP
                    // --------------------------------------------------------

                    DashboardActionCard(
                        modifier = Modifier.weight(1f),
                        title = "Create",
                        icon = Icons.Default.AddCircle,
                        background = Color(0xFFEAF2FF),
                        iconColor = Color(0xFF2962FF)
                    ) {

                        if (!InternetManager.requireInternet(context)) {

                            Toast.makeText(
                                context,
                                "No Internet Connection",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@DashboardActionCard
                        }

                        navController.navigate(
                            Screen.CreateTrip.route
                        )
                    }

                    // --------------------------------------------------------
                    // BOOKING REQUESTS
                    // --------------------------------------------------------

                    DashboardActionCard(
                        modifier = Modifier.weight(1f),
                        title = "Bookings",
                        icon = Icons.Default.List,
                        background = Color(0xFFEAF8EE),
                        iconColor = Color(0xFF16A34A)
                    ) {

                        if (!InternetManager.requireInternet(context)) {

                            Toast.makeText(
                                context,
                                "No Internet Connection",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@DashboardActionCard
                        }

                        navController.navigate(
                            Screen.ManageBookings.route
                        )
                    }
                    // --------------------------------------------------------
                    // PARCEL REQUESTS
                    // --------------------------------------------------------

                    DashboardActionCard(
                        modifier = Modifier.weight(1f),
                        title = "Parcels",
                        icon = Icons.Default.Inventory2,
                        background = Color(0xFFFFF3E0),
                        iconColor = Color(0xFFFF7A00)
                    ) {

                        if (!InternetManager.requireInternet(context)) {

                            Toast.makeText(
                                context,
                                "No Internet Connection",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@DashboardActionCard
                        }

                        navController.navigate(
                            Screen.ManageParcelBookings.route
                        )
                    }

                    // --------------------------------------------------------
                    // ALL TRIPS
                    // --------------------------------------------------------

                    DashboardActionCard(
                        modifier = Modifier.weight(1f),
                        title = "All Trips",
                        icon = Icons.Default.DirectionsBus,
                        background = Color(0xFFF4EEFC),
                        iconColor = Color(0xFF8B5CF6)
                    ) {

                        if (!InternetManager.requireInternet(context)) {

                            Toast.makeText(
                                context,
                                "No Internet Connection",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@DashboardActionCard
                        }

                        navController.navigate(
                            Screen.AllTrips.route
                        )
                    }




                }

            Spacer(modifier = Modifier.height(28.dp))

            // ---------- Available Trips ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Available Trips",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                // Not wired to a route yet - hook this up once a "view all" screen exists.
//                Text(
//                    text = "View all",
//                    color = MaterialTheme.colorScheme.primary,
//                    fontWeight = FontWeight.Medium
//                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)

            ) {

                items(recentTrips) { trip ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFEAEAEA)
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = trip.route,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text = "${trip.date} • ${trip.timeSlot}",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )

                                Spacer(
                                    modifier = Modifier.height(2.dp)
                                )

                                Text(
                                    text = "${trip.availableSeats}/${trip.totalSeats} seats",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End
                            ) {

                                TripsStatusBadge(trip)

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Text(
                                    text = "₹${trip.fare}",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

    }}
    LogoutDialog(
        showDialog = showLogoutDialog,
        onDismiss = {
            showLogoutDialog = false
        },
        onConfirm = {
            showLogoutDialog = false

            SessionManager.logout(context)

            navController.navigate(Screen.Login.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    )

}

@Composable
fun DashboardActionCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    background: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        border = BorderStroke(
            width = 1.dp, color = iconColor),
        colors = CardDefaults.cardColors(
            containerColor = background
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TripsStatusBadge(
    trip: Trip
) {

    val status =
        when {

            trip.status.equals(
                "CANCELLED",
                true
            ) -> "Cancelled"

            LocalDate.parse(trip.date)
                .isBefore(LocalDate.now()) ->
                "Completed"

            trip.availableSeats == 0 ->
                "Full"

            else ->
                "Open"
        }

    val (bg, fg) =
        when (status) {

            "Open" ->
                Color(0xFFDDF5E4) to
                        Color(0xFF1E8E3E)

            "Full" ->
                Color(0xFFFFF3CD) to
                        Color(0xFFB8860B)

            "Completed" ->
                Color(0xFFE8EAF6) to
                        Color(0xFF3949AB)
            else ->
                Color(0xFFFDE3E3) to
                        Color(0xFFD93025)
        }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    ) {

        Text(
            text = status,
            color = fg,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

