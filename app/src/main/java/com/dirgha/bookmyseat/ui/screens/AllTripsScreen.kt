package com.dirgha.bookmyseat.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.data.model.Trip
import com.dirgha.bookmyseat.viewmodel.TripViewModel
import java.time.LocalDate
import java.time.temporal.WeekFields
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTripsScreen(
    navController: NavController,
    tripViewModel: TripViewModel = viewModel()
) {

    val trips by tripViewModel.trips.collectAsState()

    var selectedFilter by remember {
        mutableStateOf("ALL")
    }
    val isRefreshing by tripViewModel.isLoading.collectAsState()

    val pullRefreshState = rememberPullToRefreshState()


    LaunchedEffect(Unit) {
        tripViewModel.loadTrips()
    }

    val filteredTrips = remember(
        trips,
        selectedFilter
    ) {
        val today = LocalDate.now()

        val sorted = trips.sortedByDescending {
            LocalDate.parse(it.date)
        }

        when (selectedFilter) {

            "WEEK" -> {
                sorted.filter {
                    val date = LocalDate.parse(it.date)

                    date.year == today.year &&
                            date.get(WeekFields.ISO.weekOfWeekBasedYear()) ==
                            today.get(WeekFields.ISO.weekOfWeekBasedYear())
                }
            }

            "MONTH" -> {
                sorted.filter {
                    val date = LocalDate.parse(it.date)

                    date.month == today.month &&
                            date.year == today.year
                }
            }

            else -> sorted
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("All Trips")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            null
                        )
                    }
                }
            )
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                tripViewModel.loadTrips()
            },
            state = pullRefreshState
        ) {
            Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F7FC))
        ) {

            FilterSection(
                selectedFilter
            ) {
                selectedFilter = it
            }


            LazyColumn(
                contentPadding =
                    PaddingValues(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                items(filteredTrips) { trip ->

                    TripsCard(trip)
                }
            }
        }
    }
}}
@Composable
private fun FilterSection(
    selected: String,
    onSelected: (String) -> Unit
) {

    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        listOf(
            "ALL",
            "WEEK",
            "MONTH"
        ).forEach {

            FilterChip(
                selected = selected == it,
                onClick = {
                    onSelected(it)
                },
                label = {
                    Text(
                        when (it) {
                            "WEEK" -> "This Week"
                            "MONTH" -> "This Month"
                            else -> "All"
                        }
                    )
                }
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TripsCard(
    trip: Trip
) {

    Card(
        shape = RoundedCornerShape(16.dp),
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
                .padding(16.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    trip.route,
                    fontWeight = FontWeight.Bold,
                    fontSize =  14.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    "${trip.date} • ${trip.timeSlot}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    "${trip.availableSeats}/${trip.totalSeats} seats",
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment =
                    Alignment.End
            ) {

                TripStatusBadge(trip)

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    "₹${trip.fare}",
                    fontWeight = FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TripStatusBadge(
    trip: Trip
) {

    val today = LocalDate.now()
    val tripDate =
        LocalDate.parse(trip.date)

    val status =
        when {

            trip.status == "CANCELLED" ->
                "Cancelled"

            tripDate.isBefore(today) ->
                "Completed"

            trip.availableSeats == 0 ->
                "Full"

            else ->
                "Open"
        }

    val (bg, fg) =
        when (status) {

            "Open" ->
                Color(0xFFDDF5E4) to Color(0xFF1E8E3E)

            "Full" ->
                Color(0xFFFFF3CD) to Color(0xFFB8860B)

            "Completed" ->
                Color(0xFFE8EAF6) to Color(0xFF3949AB)

            else ->
                Color(0xFFFDE3E3) to Color(0xFFD93025)
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