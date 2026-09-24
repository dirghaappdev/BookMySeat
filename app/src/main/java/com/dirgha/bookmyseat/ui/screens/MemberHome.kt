package com.dirgha.bookmyseat.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.viewmodel.MyBookingViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.viewmodel.NotificationViewModel
import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.ui.platform.LocalContext
import com.dirgha.bookmyseat.utils.InternetManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MemberHomeScreen(
    navController: NavController,
    myBookingViewModel: MyBookingViewModel,
    notificationViewModel : NotificationViewModel
) {

    LaunchedEffect(Unit) {
        myBookingViewModel.loadMyBookings()
    }
    var showLogoutDialog by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(Unit) {
        notificationViewModel.loadNotifications()
    }
    val bookings by myBookingViewModel.bookings.collectAsState()

    val today = java.time.LocalDate.now()
   val context = LocalContext.current
    val upcomingRide = bookings
        .filter {
            java.time.LocalDate.parse(it.date) >= today
        }
        .minByOrNull {
            java.time.LocalDate.parse(it.date)
        }

    val recentRides = bookings
        .filter {
            java.time.LocalDate.parse(it.date) < today
        }
        .sortedByDescending {
            java.time.LocalDate.parse(it.date)
        }
        .take(3)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FC))
    )  {

        // ================= HEADER =================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2962FF),
                            Color(0xFF1E4DFF)
                        )
                    )
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 50.dp
                    )
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Welcome Back!",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "Ready for your next ride?",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        )
                    }
//                    IconButton(
//                        onClick = {
//                            navController.navigate( Screen.ProfileScreen.route)
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.PersonOutline,
//                            contentDescription = null,
//                            tint = Color.White
//                        )
//                    }

                    BadgedBox(

                        badge = {

                            if (notificationViewModel.unreadCount > 0) {

                                Badge(

                                    modifier = Modifier.offset(
                                        x = (-4).dp,
                                        y = 4.dp
                                    )

                                )  {

                                    Text(notificationViewModel.unreadCount.toString())

                                }

                            }

                        }

                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {

                        IconButton(

                            onClick = {
                                if (!InternetManager.requireInternet(context)) {

                                    Toast.makeText(
                                        context,
                                        "No Internet Connection",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    return@IconButton
                                }
                                navController.navigate(Screen.Notifications.route)
                            }

                        ) {

                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )

                        }

                    }
                }
                    IconButton(
                        onClick = {
                            if (!InternetManager.requireInternet(context)) {

                                Toast.makeText(
                                    context,
                                    "No Internet Connection",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@IconButton
                            }
                            navController.navigate( Screen.ProfileScreen.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonOutline,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }}

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                // BOOK RIDE BUTTON

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clickable {
                            if (!InternetManager.requireInternet(context)) {

                                Toast.makeText(
                                    context,
                                    "No Internet Connection",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@clickable
                            }
                            navController.navigate(
                                Screen.BookingSearch.route
                            )
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement =
                            Arrangement.Center,
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.BookOnline,
                            contentDescription = null,
                            tint = Color(0xFF2962FF)
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = "Book a New Ride",
                            color = Color(0xFF2962FF),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp
                        )
                    }
                }
            }
        }

        // ================= CONTENT SECTION =================

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 190.dp),
            color = Color.White,
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 28.dp
                    )
            ) {

                Text(
                    text = "Quick Access",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // ========================================================
                    // PARCELS
                    // ========================================================

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(90.dp)
                            .clickable {
                                if (!InternetManager.requireInternet(context)) {

                                    Toast.makeText(
                                        context,
                                        "No Internet Connection",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    return@clickable
                                }
                                navController.navigate(
                                    Screen.ParcelBookingScreen.route
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF3E0)
                        ),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFFF7A00)
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = null,
                                tint = Color(0xFFFF7A00),
                                modifier = Modifier.size(26.dp)
                            )

                            Spacer(
                                modifier = Modifier.height(7.dp)
                            )

                            Text(
                                text = "Book Parcel",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // ========================================================
                    // MY TRIPS
                    // ========================================================

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(90.dp)
                            .clickable {
                                if (!InternetManager.requireInternet(context)) {

                                    Toast.makeText(
                                        context,
                                        "No Internet Connection",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    return@clickable
                                }
                                navController.navigate(
                                    Screen.MyBookings.route
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFEFF4FD)
                        ),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFF2962FF)
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = Color(0xFF2962FF),
                                modifier = Modifier.size(26.dp)
                            )

                            Spacer(
                                modifier = Modifier.height(7.dp)
                            )

                            Text(
                                text = "My Trips",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // ========================================================
                    // SEARCH
                    // ========================================================

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(90.dp)
                            .clickable {
                                if (!InternetManager.requireInternet(context)) {

                                    Toast.makeText(
                                        context,
                                        "No Internet Connection",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    return@clickable
                                }
                                navController.navigate(
                                    Screen.MyParcels.route
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFEAF8EE)
                        ),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFF16A34A)
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF16A34A),
                                modifier = Modifier.size(26.dp)
                            )

                            Spacer(
                                modifier = Modifier.height(7.dp)
                            )

                            Text(
                                text = "My parcels",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // ========================================================
                    // SUPPORT
                    // ========================================================

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(90.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF4EEFC)
                        ),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFF8B5CF6)
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    if (!InternetManager.requireInternet(context)) {

                                        Toast.makeText(
                                            context,
                                            "No Internet Connection",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        return@clickable
                                    }
                                    navController.navigate(
                                        Screen.Support.route
                                    )
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.SupportAgent,
                                contentDescription = null,
                                tint = Color(0xFF8B5CF6),
                                modifier = Modifier.size(26.dp)
                            )

                            Spacer(
                                modifier = Modifier.height(7.dp)
                            )

                            Text(
                                text = "Support",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }
                    }


                }

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                Text(
                    text = "Upcoming Ride",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (upcomingRide != null) {

                    val places =
                        upcomingRide.route.split( Regex("-Rahuri-|-Sangamner-"))

                    val from =
                        places.getOrNull(0)?.trim() ?: ""

                    val to =
                        places.getOrNull(1)?.trim() ?: ""

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFB8D4FF)
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement =
                                    Arrangement.SpaceBetween
                            ) {

                                Column {

                                    Row(
                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = Color(0xFF2E7D32),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(
                                            modifier = Modifier.width(8.dp)
                                        )
                                        Text(
                                            text = from,
                                            fontWeight =
                                                FontWeight.SemiBold
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.height(10.dp)
                                    )

                                    Row(
                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = Color(0xFFE53935),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(
                                            modifier = Modifier.width(8.dp)
                                        )
                                        Text(
                                            text = to,
                                            fontWeight =
                                                FontWeight.SemiBold
                                        )
                                    }
                                }

                                Surface(
                                    color = Color(0xFFE8F5E9),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text(
                                        text =
                                            upcomingRide.bookingStatus,
                                        color = Color(0xFF2E7D32),
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 6.dp
                                        )
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(18.dp)
                            )

                            Row(
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Outlined.CalendarMonth,
                                    null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(
                                    modifier = Modifier.width(6.dp)
                                )

                                Text(upcomingRide.date)

                                Spacer(
                                    modifier = Modifier.width(20.dp)
                                )

                                Icon(
                                    Icons.Outlined.AccessTime,
                                    null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(
                                    modifier = Modifier.width(6.dp)
                                )

                                Text(upcomingRide.timeSlot)
                            }
                        }
                    }
                }
                else {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .height(180.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF8FAFF)
                        ),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFE3EAF7)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "🚌",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "No Upcoming Ride",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "Book your next journey and it will appear here.",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    navController.navigate(
                                        Screen.BookingSearch.route
                                    )
                                },
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Book Now")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Recent Rides",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                    if (recentRides.isEmpty()) {

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF8FAFF)
                            ),
                            border = BorderStroke(
                                1.dp,
                                Color(0xFFE3EAF7)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 30.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = "📜",
                                    fontSize = 42.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "No Recent Rides",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Your completed trips will appear here.",
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    } else {

                        recentRides.forEach { ride ->

                            val places = ride.route.split( Regex("-Rahuri-|-Sangamner-"))

                            val from = places.getOrNull(0)?.trim() ?: ""
                            val to = places.getOrNull(1)?.trim() ?: ""

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    Color(0xFFE8EDF5)
                                )
                            ) {

                                Column(
                                    modifier = Modifier.padding(18.dp)
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement =
                                            Arrangement.SpaceBetween
                                    ) {

                                        Column {

                                            Row(
                                                verticalAlignment =
                                                    Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.LocationOn,
                                                    contentDescription = null,
                                                    tint = Color(0xFF2E7D32),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(
                                                    modifier = Modifier.width(8.dp)
                                                )
                                                Text(
                                                    text = from,
                                                    fontWeight =
                                                        FontWeight.SemiBold
                                                )
                                            }

                                            Spacer(
                                                modifier = Modifier.height(8.dp)
                                            )

                                            Row(
                                                verticalAlignment =
                                                    Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.LocationOn,
                                                    contentDescription = null,
                                                    tint = Color(0xFFE53935),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(
                                                    modifier = Modifier.width(8.dp)
                                                )
                                                Text(
                                                    text = to,
                                                    fontWeight =
                                                        FontWeight.SemiBold
                                                )
                                            }
                                        }

                                        Text(
                                            text = "₹${ride.fare}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF2962FF)
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.height(16.dp)
                                    )

                                    Row(
                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            Icons.Outlined.CalendarMonth,
                                            contentDescription = null,
                                            tint = Color.Gray,
                                            modifier = Modifier.size(18.dp)
                                        )

                                        Spacer(
                                            modifier = Modifier.width(6.dp)
                                        )

                                        Text(
                                            text = ride.date,
                                            color = Color.Gray
                                        )

                                        Spacer(
                                            modifier = Modifier.width(20.dp)
                                        )

                                        Icon(
                                            Icons.Outlined.AccessTime,
                                            contentDescription = null,
                                            tint = Color.Gray,
                                            modifier = Modifier.size(18.dp)
                                        )

                                        Spacer(
                                            modifier = Modifier.width(6.dp)
                                        )

                                        Text(
                                            text = ride.timeSlot,
                                            color = Color.Gray
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
    )}