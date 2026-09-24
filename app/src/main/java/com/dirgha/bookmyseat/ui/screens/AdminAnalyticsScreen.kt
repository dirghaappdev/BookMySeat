package com.dirgha.bookmyseat.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirgha.bookmyseat.ui.model.AdminAnalyticsResponse
import com.dirgha.bookmyseat.ui.model.ParcelTypeAnalytics
import com.dirgha.bookmyseat.ui.model.ParcelWeightAnalytics
import com.dirgha.bookmyseat.ui.model.RecentActivity
import com.dirgha.bookmyseat.ui.model.RevenueTrend
import com.dirgha.bookmyseat.ui.viewmodel.AdminAnalyticsViewModel
import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale
import kotlin.math.abs

private enum class AnalyticsFilter(
    val title: String
) {
    TODAY("Today"),
    YESTERDAY("Yesterday"),
    WEEK("This Week"),
    MONTH("This Month"),
    HISTORICAL_MONTH("Historical Month")
}

/* ================================================================
   COLORS
================================================================ */

private val AnalyticsBlue = Color(0xFF2563EB)
private val AnalyticsPurple = Color(0xFF7C3AED)
private val AnalyticsPink = Color(0xFFDB2777)
private val AnalyticsOrange = Color(0xFFF97316)
private val AnalyticsGreen = Color(0xFF16A34A)
private val AnalyticsTeal = Color(0xFF0D9488)
private val AnalyticsRed = Color(0xFFDC2626)
private val AnalyticsYellow = Color(0xFFEAB308)
private val AnalyticsIndigo = Color(0xFF4F46E5)

/* ================================================================
   MAIN SCREEN
================================================================ */

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminAnalyticsScreen(
    onBack: () -> Unit = {},
    viewModel: AdminAnalyticsViewModel = viewModel()
) {

    val analyticsState by viewModel.analytics.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val analytics = analyticsState

    var selectedFilter by remember {
        mutableStateOf(AnalyticsFilter.MONTH)
    }

    var selectedMonth by remember {
        mutableStateOf(YearMonth.now())
    }

    var filterExpanded by remember {
        mutableStateOf(false)
    }

    var monthExpanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(
        selectedFilter,
        selectedMonth
    ) {

        when (selectedFilter) {

            AnalyticsFilter.TODAY -> {
                viewModel.loadAnalytics(
                    period = "today"
                )
            }

            AnalyticsFilter.YESTERDAY -> {
                viewModel.loadAnalytics(
                    period = "yesterday"
                )
            }

            AnalyticsFilter.WEEK -> {
                viewModel.loadAnalytics(
                    period = "week"
                )
            }

            AnalyticsFilter.MONTH,
            AnalyticsFilter.HISTORICAL_MONTH -> {

                viewModel.loadAnalytics(
                    period = "month",
                    year = selectedMonth.year,
                    month = selectedMonth.monthValue
                )
            }
        }
    }

    Scaffold(

        containerColor = Color(0xFFF7F8FC),

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Analytics",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "BookMySeat business overview",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                actions = {

                    Surface(
                        modifier = Modifier.padding(end = 8.dp),
                        shape = CircleShape,
                        color = AnalyticsBlue.copy(alpha = 0.10f)
                    ) {

                        IconButton(
                            onClick = {

                                when (selectedFilter) {

                                    AnalyticsFilter.TODAY ->
                                        viewModel.refresh("today")

                                    AnalyticsFilter.YESTERDAY ->
                                        viewModel.refresh("yesterday")

                                    AnalyticsFilter.WEEK ->
                                        viewModel.refresh("week")

                                    AnalyticsFilter.MONTH,
                                    AnalyticsFilter.HISTORICAL_MONTH ->
                                        viewModel.refresh(
                                            "month",
                                            selectedMonth.year,
                                            selectedMonth.monthValue
                                        )
                                }
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = AnalyticsBlue
                            )
                        }
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
        ) {

            /* =====================================================
               COLORFUL HEADER
            ===================================================== */

            AnalyticsHero(
                selectedFilter = selectedFilter,
                selectedMonth = selectedMonth
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            /* =====================================================
               FILTER
            ===================================================== */

            Box {

                OutlinedButton(

                    onClick = {
                        filterExpanded = true
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(16.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = AnalyticsBlue
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text =
                            when {

                                selectedFilter ==
                                        AnalyticsFilter.MONTH ->
                                    "This Month"

                                selectedFilter ==
                                        AnalyticsFilter.HISTORICAL_MONTH ->
                                    selectedMonth.formatMonth()

                                else ->
                                    selectedFilter.title
                            },

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "⌄",
                        fontSize = 20.sp,
                        color = AnalyticsBlue
                    )
                }

                DropdownMenu(

                    expanded = filterExpanded,

                    onDismissRequest = {
                        filterExpanded = false
                    },

                    modifier = Modifier.fillMaxWidth(0.92f)
                ) {

                    AnalyticsFilter.entries.forEach { filter ->

                        DropdownMenuItem(

                            leadingIcon = {

                                Icon(
                                    imageVector = when (filter) {

                                        AnalyticsFilter.TODAY ->
                                            Icons.Default.DateRange

                                        AnalyticsFilter.YESTERDAY ->
                                            Icons.Default.History

                                        AnalyticsFilter.WEEK ->
                                            Icons.Default.ShowChart

                                        AnalyticsFilter.MONTH ->
                                            Icons.Default.CalendarMonth

                                        AnalyticsFilter.HISTORICAL_MONTH ->
                                            Icons.Default.CalendarMonth
                                    },

                                    contentDescription = null
                                )
                            },

                            text = {

                                Text(
                                    text = filter.title,
                                    fontWeight =
                                        if (selectedFilter == filter)
                                            FontWeight.Bold
                                        else
                                            FontWeight.Normal
                                )
                            },

                            onClick = {

                                selectedFilter = filter

                                if (
                                    filter ==
                                    AnalyticsFilter.MONTH
                                ) {
                                    selectedMonth =
                                        YearMonth.now()
                                }

                                filterExpanded = false
                            }
                        )
                    }
                }
            }

            /* =====================================================
               HISTORICAL MONTH
            ===================================================== */

            if (
                selectedFilter ==
                AnalyticsFilter.HISTORICAL_MONTH
            ) {

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Box {

                    OutlinedButton(

                        onClick = {
                            monthExpanded = true
                        },

                        modifier = Modifier.fillMaxWidth(),

                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = AnalyticsPurple
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = selectedMonth.formatMonth(),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "⌄",
                            fontSize = 20.sp,
                            color = AnalyticsPurple
                        )
                    }

                    DropdownMenu(

                        expanded = monthExpanded,

                        onDismissRequest = {
                            monthExpanded = false
                        },

                        modifier = Modifier.fillMaxWidth(0.92f)
                    ) {

                        historicalMonths()
                            .forEach { month ->

                                DropdownMenuItem(

                                    text = {

                                        Text(
                                            text = month.formatMonth(),
                                            fontWeight =
                                                if (
                                                    month ==
                                                    selectedMonth
                                                )
                                                    FontWeight.Bold
                                                else
                                                    FontWeight.Normal
                                        )
                                    },

                                    onClick = {

                                        selectedMonth = month
                                        monthExpanded = false
                                    }
                                )
                            }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            /* =====================================================
               LOADING
            ===================================================== */

            if (isLoading) {

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    color = AnalyticsBlue
                )
            }

            /* =====================================================
               ERROR
            ===================================================== */

            if (!errorMessage.isNullOrBlank()) {

                ErrorAnalyticsCard(
                    message = errorMessage ?: "",
                    onRetry = {

                        viewModel.refresh(
                            period =
                                when (selectedFilter) {

                                    AnalyticsFilter.TODAY ->
                                        "today"

                                    AnalyticsFilter.YESTERDAY ->
                                        "yesterday"

                                    AnalyticsFilter.WEEK ->
                                        "week"

                                    else ->
                                        "month"
                                },

                            year = selectedMonth.year,
                            month = selectedMonth.monthValue
                        )
                    }
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }

            /* =====================================================
               DATA
            ===================================================== */

            analytics?.let { data ->

                AnalyticsContent(
                    data = data
                )
            }

            /* =====================================================
               FIRST LOAD
            ===================================================== */

            if (
                analytics == null &&
                isLoading
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),

                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = AnalyticsBlue
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}

/* ================================================================
   HERO
================================================================ */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AnalyticsHero(
    selectedFilter: AnalyticsFilter,
    selectedMonth: YearMonth
) {

    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 900,
            easing = FastOutSlowInEasing
        ),
        label = "heroAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(135.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        AnalyticsBlue,
                        AnalyticsPurple,
                        AnalyticsPink
                    )
                )
            )
    ) {

        Box(
            modifier = Modifier
                .size(110.dp)
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .clip(CircleShape)
                .background(
                    Color.White.copy(
                        alpha = 0.10f * animatedProgress
                    )
                )
        )

        Box(
            modifier = Modifier
                .size(75.dp)
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .clip(CircleShape)
                .background(
                    Color.White.copy(
                        alpha = 0.08f * animatedProgress
                    )
                )
        )

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White.copy(alpha = 0.18f)
                ) {

                    Icon(
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(11.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column {

                    Text(
                        text = "Business Insights",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text =
                            when (selectedFilter) {

                                AnalyticsFilter.TODAY ->
                                    "Today's performance"

                                AnalyticsFilter.YESTERDAY ->
                                    "Yesterday's performance"

                                AnalyticsFilter.WEEK ->
                                    "Current week performance"

                                AnalyticsFilter.MONTH ->
                                    "Current month performance"

                                AnalyticsFilter.HISTORICAL_MONTH ->
                                    selectedMonth.formatMonth()
                            },

                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            Text(
                text = "Track revenue • bookings • seats • parcels",
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/* ================================================================
   ERROR
================================================================ */

@Composable
private fun ErrorAnalyticsCard(
    message: String,
    onRetry: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = CircleShape,
                    color = AnalyticsRed.copy(alpha = 0.12f)
                ) {

                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = AnalyticsRed,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "Analytics Error",
                    fontWeight = FontWeight.ExtraBold,
                    color = AnalyticsRed
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = message,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AnalyticsRed
                ),
                shape = RoundedCornerShape(12.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text("Retry")
            }
        }
    }
}

/* ================================================================
   MAIN CONTENT
================================================================ */

@Composable
private fun AnalyticsContent(
    data: AdminAnalyticsResponse
) {

    SectionTitle(
        title = "Overview",
        icon = Icons.Default.Dashboard
    )

    Spacer(
        modifier = Modifier.height(10.dp)
    )

    /* =============================================================
       OVERVIEW
    ============================================================= */

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OverviewCard(
                modifier = Modifier.weight(1f),
                title = "Total Trips",
                value = data.totalTrips.toString(),
                icon = Icons.Default.DirectionsCar,
                color = AnalyticsBlue
            )

            OverviewCard(
                modifier = Modifier.weight(1f),
                title = "Bookings",
                value = data.totalBookings.toString(),
                icon = Icons.Default.EventSeat,
                color = AnalyticsPurple
            )

            OverviewCard(
                modifier = Modifier.weight(1f),
                title = "Passengers",
                value = data.totalPassengers.toString(),
                icon = Icons.Default.People,
                color = AnalyticsPink
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OverviewCard(
                modifier = Modifier.weight(1f),
                title = "Confirmed",
                value = data.confirmedBookings.toString(),
                icon = Icons.Default.CheckCircle,
                color = AnalyticsGreen
            )

            OverviewCard(
                modifier = Modifier.weight(1f),
                title = "Pending",
                value = data.pendingBookings.toString(),
                icon = Icons.Default.History,
                color = AnalyticsOrange
            )

            OverviewCard(
                modifier = Modifier.weight(1f),
                title = "Rejected",
                value = data.rejectedBookings.toString(),
                icon = Icons.Default.Warning,
                color = AnalyticsRed
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OverviewCard(
                modifier = Modifier.weight(1f),
                title = "Revenue",
                value = "₹${data.totalRevenue.toInt()}",
                icon = Icons.Default.CurrencyRupee,
                color = AnalyticsTeal
            )

            OverviewCard(
                modifier = Modifier.weight(1f),
                title = "Seat Usage",
                value = "${data.seatUtilization.toInt()}%",
                icon = Icons.Default.EventSeat,
                color = AnalyticsIndigo
            )
        }
    }

    Spacer(
        modifier = Modifier.height(24.dp)
    )

    /* =============================================================
       CHANGES
    ============================================================= */

    ChangeCards(
        revenueChange = data.revenueChange,
        bookingsChange = data.bookingsChange
    )

    Spacer(
        modifier = Modifier.height(24.dp)
    )

    /* =============================================================
       DAILY REVENUE
    ============================================================= */

    SectionTitle(
        title = "Daily Revenue",
        icon = Icons.Default.ShowChart
    )

    Spacer(
        modifier = Modifier.height(10.dp)
    )

    RevenueChart(
        trend = data.revenueTrend
    )

    Spacer(
        modifier = Modifier.height(24.dp)
    )

    /* =============================================================
       BUSINESS MIX
    ============================================================= */

    SectionTitle(
        title = "Business Mix",
        icon = Icons.Default.DirectionsCar
    )

    Spacer(
        modifier = Modifier.height(10.dp)
    )

    BusinessMixCard(
        data = data
    )

    Spacer(
        modifier = Modifier.height(24.dp)
    )

    /* =============================================================
       SEAT ANALYTICS
    ============================================================= */

    SectionTitle(
        title = "Seat Analytics",
        icon = Icons.Default.EventSeat
    )

    Spacer(
        modifier = Modifier.height(10.dp)
    )

    SeatAnalyticsCard(
        data = data
    )

    Spacer(
        modifier = Modifier.height(24.dp)
    )

    /* =============================================================
       ROUTES
    ============================================================= */

    SectionTitle(
        title = "Route Analytics",
        icon = Icons.Default.DirectionsCar
    )

    Spacer(
        modifier = Modifier.height(10.dp)
    )

    RouteCard(
        data = data
    )

    Spacer(
        modifier = Modifier.height(24.dp)
    )

    /* =============================================================
       PARCEL
    ============================================================= */

    SectionTitle(
        title = "Parcel Analytics",
        icon = Icons.Default.Inventory
    )

    Spacer(
        modifier = Modifier.height(10.dp)
    )

    ParcelSummaryCard(
        data = data
    )

    Spacer(
        modifier = Modifier.height(12.dp)
    )

    ParcelTypeCard(
        types = data.parcelTypeAnalytics
    )

    Spacer(
        modifier = Modifier.height(12.dp)
    )

    ParcelWeightCard(
        data = data.parcelWeightAnalytics
    )

    Spacer(
        modifier = Modifier.height(24.dp)
    )

    /* =============================================================
       RECENT ACTIVITY
    ============================================================= */

    SectionTitle(
        title = "Recent Activity",
        icon = Icons.Default.History
    )

    Spacer(
        modifier = Modifier.height(10.dp)
    )

    if (data.recentBookings.isEmpty()) {

        EmptyCard()

    } else {

        data.recentBookings.forEach { activity ->

            RecentActivityCard(
                activity = activity
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }
    }
}

/* ================================================================
   SECTION TITLE
================================================================ */

@Composable
private fun SectionTitle(
    title: String,
    icon: ImageVector
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = AnalyticsBlue.copy(alpha = 0.10f)
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AnalyticsBlue,
                modifier = Modifier
                    .size(32.dp)
                    .padding(9.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Text(
            text = title,
            fontSize = 19.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

/* ================================================================
   OVERVIEW CARD
================================================================ */

@Composable
fun OverviewCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector = Icons.Default.Dashboard,
    color: Color = AnalyticsBlue
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp)
        ) {

            Surface(
                shape = RoundedCornerShape(11.dp),
                color = color.copy(alpha = 0.11f)
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .size(38.dp)
                        .padding(9.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B7280)
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = value,
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF111827)
            )
        }
    }
}

/* ================================================================
   CHANGE CARDS
================================================================ */

@Composable
private fun ChangeCards(
    revenueChange: Double,
    bookingsChange: Double
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        ChangeCard(
            modifier = Modifier.weight(1f),
            title = "Revenue Change",
            value = formatChange(revenueChange),
            positive = revenueChange >= 0
        )

        ChangeCard(
            modifier = Modifier.weight(1f),
            title = "Booking Change",
            value = formatChange(bookingsChange),
            positive = bookingsChange >= 0
        )
    }
}

@Composable
private fun ChangeCard(
    modifier: Modifier,
    title: String,
    value: String,
    positive: Boolean
) {

    val color =
        if (positive)
            AnalyticsGreen
        else
            AnalyticsRed

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.08f)
        )
    ) {

        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.13f)
            ) {

                Icon(
                    imageVector =
                        if (positive)
                            Icons.Default.TrendingUp
                        else
                            Icons.Default.TrendingDown,

                    contentDescription = null,

                    tint = color,

                    modifier = Modifier
                        .size(42.dp)
                        .padding(10.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Column {

                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280)
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = value,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            }
        }
    }
}

/* ================================================================
   REVENUE CHART
================================================================ */

@Composable
private fun RevenueChart(
    trend: List<RevenueTrend>
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "Revenue Trend",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Daily business performance",
                        fontSize = 11.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AnalyticsBlue.copy(alpha = 0.10f)
                ) {

                    Icon(
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = null,
                        tint = AnalyticsBlue,
                        modifier = Modifier
                            .size(38.dp)
                            .padding(9.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            if (trend.isEmpty()) {

                EmptyChart()

            } else {

                val maxRevenue =
                    trend.maxOfOrNull {
                        it.revenue
                    } ?: 0.0

                val safeMax =
                    if (maxRevenue <= 0)
                        1.0
                    else
                        maxRevenue

                trend.forEach { item ->

                    val percentage =
                        (
                                item.revenue /
                                        safeMax
                                )
                            .toFloat()
                            .coerceIn(0f, 1f)

                    AnimatedRevenueRow(
                        item = item,
                        percentage = percentage
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedRevenueRow(
    item: RevenueTrend,
    percentage: Float
) {

    var started by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        started = true
    }

    val animatedProgress by animateFloatAsState(
        targetValue =
            if (started)
                percentage
            else
                0f,

        animationSpec = tween(
            durationMillis = 700
        ),

        label = "revenueBar"
    )

    Column(
        modifier = Modifier.padding(
            vertical = 5.dp
        )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = item.label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = formatCurrency(item.revenue),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = AnalyticsBlue
            )
        }

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(
                    Color(0xFFE8ECF5)
                )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        animatedProgress
                    )
                    .height(8.dp)
                    .clip(
                        RoundedCornerShape(50)
                    )
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                AnalyticsBlue,
                                AnalyticsPurple,
                                AnalyticsPink
                            )
                        )
                    )
            )
        }
    }
}

/* ================================================================
   BUSINESS MIX
================================================================ */

@Composable
private fun BusinessMixCard(
    data: AdminAnalyticsResponse
) {

    val total = data.totalRevenue

    val ridePercent =
        if (total > 0)
            (data.rideRevenue / total) * 100
        else
            0.0

    val parcelPercent =
        if (total > 0)
            (data.parcelRevenue / total) * 100
        else
            0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = "Revenue Distribution",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            MixRow(
                title = "Ride",
                percentage = ridePercent,
                revenue = data.rideRevenue,
                icon = Icons.Default.DirectionsCar,
                color = AnalyticsBlue
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            MixRow(
                title = "Parcel",
                percentage = parcelPercent,
                revenue = data.parcelRevenue,
                icon = Icons.Default.Inventory,
                color = AnalyticsOrange
            )
        }
    }
}

@Composable
private fun MixRow(
    title: String,
    percentage: Double,
    revenue: Double,
    icon: ImageVector,
    color: Color
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = color.copy(alpha = 0.11f)
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(42.dp)
                    .padding(10.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${percentage.toInt()}%",
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            LinearProgressIndicator(
                progress = {
                    (
                            percentage / 100
                            )
                        .toFloat()
                        .coerceIn(0f, 1f)
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),

                color = color,

                trackColor = color.copy(
                    alpha = 0.12f
                ),

                strokeCap = StrokeCap.Round
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = formatCurrency(revenue),
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}

/* ================================================================
   SEAT ANALYTICS
================================================================ */

@Composable
private fun SeatAnalyticsCard(
    data: AdminAnalyticsResponse
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AnalyticsIndigo.copy(alpha = 0.10f)
                ) {

                    Icon(
                        imageVector = Icons.Default.EventSeat,
                        contentDescription = null,
                        tint = AnalyticsIndigo,
                        modifier = Modifier
                            .size(42.dp)
                            .padding(10.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Column {

                    Text(
                        text = "Seat Utilization",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "${data.seatUtilization}% utilization",
                        fontSize = 12.sp,
                        color = AnalyticsIndigo,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            LinearProgressIndicator(
                progress = {
                    (
                            data.seatUtilization / 100
                            )
                        .toFloat()
                        .coerceIn(0f, 1f)
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(11.dp),

                color = AnalyticsIndigo,

                trackColor =
                    AnalyticsIndigo.copy(
                        alpha = 0.10f
                    ),

                strokeCap = StrokeCap.Round
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                SeatValue(
                    "Total",
                    data.totalSeats,
                    AnalyticsBlue
                )

                SeatValue(
                    "Booked",
                    data.bookedSeats,
                    AnalyticsPurple
                )

                SeatValue(
                    "Available",
                    data.availableSeats,
                    AnalyticsGreen
                )

                SeatValue(
                    "Overbooked",
                    data.overbookedSeats,
                    AnalyticsRed
                )
            }
        }
    }
}

@Composable
private fun SeatValue(
    title: String,
    value: Int,
    color: Color
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Surface(
            shape = CircleShape,
            color = color.copy(alpha = 0.10f)
        ) {

            Text(
                text = value.toString(),
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 7.dp
                ),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = color
            )
        }

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = title,
            fontSize = 9.sp,
            color = Color(0xFF6B7280)
        )
    }
}

/* ================================================================
   ROUTES
================================================================ */

@Composable
private fun RouteCard(
    data: AdminAnalyticsResponse
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            data.mostTravelledRoute?.let {

                RouteItem(
                    title = "Most Travelled",
                    route = it.route,
                    units = it.travelUnits,
                    revenue = it.revenue,
                    icon = Icons.Default.TrendingUp,
                    color = AnalyticsGreen
                )
            }

            if (
                data.mostTravelledRoute != null &&
                data.leastTravelledRoute != null
            ) {

                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = 16.dp
                    )
                )
            }

            data.leastTravelledRoute?.let {

                RouteItem(
                    title = "Least Travelled",
                    route = it.route,
                    units = it.travelUnits,
                    revenue = it.revenue,
                    icon = Icons.Default.TrendingDown,
                    color = AnalyticsRed
                )
            }

            if (
                data.routeAnalytics.isNotEmpty()
            ) {

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text = "All Routes",
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                data.routeAnalytics
                    .take(5)
                    .forEachIndexed { index, item ->

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 4.dp
                                ),
                            shape = RoundedCornerShape(12.dp),
                            color =
                                if (index % 2 == 0)
                                    AnalyticsBlue.copy(
                                        alpha = 0.05f
                                    )
                                else
                                    Color.Transparent
                        ) {

                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 8.dp
                                )
                            ) {

                                Text(
                                    text = item.route,
                                    modifier = Modifier.weight(1f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text =
                                        "${item.travelUnits} units",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AnalyticsBlue
                                )
                            }
                        }
                    }
            }
        }
    }
}

@Composable
private fun RouteItem(
    title: String,
    route: String,
    units: Int,
    revenue: Double,
    icon: ImageVector,
    color: Color
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = color.copy(alpha = 0.10f)
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(46.dp)
                    .padding(11.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                fontSize = 11.sp,
                color = Color(0xFF6B7280)
            )

            Text(
                text = route.ifBlank {
                    "No route"
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "$units travel units",
                fontSize = 11.sp
            )
        }

        Text(
            text = formatCurrency(revenue),
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
    }
}

/* ================================================================
   PARCEL SUMMARY
================================================================ */

@Composable
private fun ParcelSummaryCard(
    data: AdminAnalyticsResponse
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(
                rememberScrollState()
            ),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        SmallCard(
            title = "Requests",
            value = data.parcelBookings.toString(),
            color = AnalyticsBlue
        )

        SmallCard(
            title = "Confirmed",
            value = data.confirmedParcels.toString(),
            color = AnalyticsGreen
        )

        SmallCard(
            title = "Delivered",
            value = data.deliveredParcels.toString(),
            color = AnalyticsPurple
        )

        SmallCard(
            title = "Weight",
            value =
                "${formatNumber(
                    data.confirmedParcelWeight
                )} kg",
            color = AnalyticsOrange
        )

        SmallCard(
            title = "Revenue",
            value = formatCurrency(
                data.parcelRevenue
            ),
            color = AnalyticsPink
        )
    }
}

@Composable
private fun SmallCard(
    title: String,
    value: String,
    color: Color
) {

    Card(
        modifier = Modifier.width(135.dp),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(
                alpha = 0.09f
            )
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.12f)
            ) {

                Icon(
                    imageVector =
                        when (title) {

                            "Requests" ->
                                Icons.Default.Inventory

                            "Confirmed" ->
                                Icons.Default.CheckCircle

                            "Delivered" ->
                                Icons.Default.CheckCircle

                            "Weight" ->
                                Icons.Default.Scale

                            else ->
                                Icons.Default.CurrencyRupee
                        },

                    contentDescription = null,

                    tint = color,

                    modifier = Modifier
                        .size(34.dp)
                        .padding(8.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = title,
                fontSize = 11.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
        }
    }
}

/* ================================================================
   PARCEL TYPE
================================================================ */

@Composable
private fun ParcelTypeCard(
    types: List<ParcelTypeAnalytics>
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = "Parcel Types",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if (types.isEmpty()) {

                Text(
                    text = "No parcel type data",
                    color = Color(0xFF6B7280)
                )

            } else {

                types.forEachIndexed { index, item ->

                    val color =
                        when (index % 5) {
                            0 -> AnalyticsBlue
                            1 -> AnalyticsPurple
                            2 -> AnalyticsOrange
                            3 -> AnalyticsGreen
                            else -> AnalyticsPink
                        }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 4.dp
                            ),
                        shape = RoundedCornerShape(14.dp),
                        color = color.copy(
                            alpha = 0.05f
                        )
                    ) {

                        Row(
                            modifier = Modifier.padding(
                                10.dp
                            ),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Surface(
                                shape = CircleShape,
                                color = color.copy(
                                    alpha = 0.12f
                                )
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.Inventory,
                                    contentDescription = null,
                                    tint = color,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .padding(9.dp)
                                )
                            }

                            Spacer(
                                modifier = Modifier.width(10.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text =
                                        item.parcelType
                                            .ifBlank {
                                                "Other"
                                            },
                                    fontWeight =
                                        FontWeight.Bold
                                )

                                Text(
                                    text =
                                        "${item.weight} kg",
                                    fontSize = 11.sp,
                                    color =
                                        Color(0xFF6B7280)
                                )
                            }

                            Column(
                                horizontalAlignment =
                                    Alignment.End
                            ) {

                                Text(
                                    text =
                                        "${item.parcels} parcels",
                                    fontWeight =
                                        FontWeight.ExtraBold
                                )

                                Text(
                                    text =
                                        formatCurrency(
                                            item.revenue
                                        ),
                                    fontSize = 11.sp,
                                    color = color
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ================================================================
   PARCEL WEIGHT
================================================================ */

@Composable
private fun ParcelWeightCard(
    data: List<ParcelWeightAnalytics>
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AnalyticsOrange.copy(
                        alpha = 0.10f
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Scale,
                        contentDescription = null,
                        tint = AnalyticsOrange,
                        modifier = Modifier
                            .size(42.dp)
                            .padding(10.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "Parcel Weight",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if (data.isEmpty()) {

                Text(
                    text = "No weight data available",
                    color = Color(0xFF6B7280)
                )

            } else {

                data.forEachIndexed { index, item ->

                    val color =
                        when (index % 4) {
                            0 -> AnalyticsBlue
                            1 -> AnalyticsPurple
                            2 -> AnalyticsOrange
                            else -> AnalyticsTeal
                        }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 4.dp
                            ),
                        shape = RoundedCornerShape(12.dp),
                        color = color.copy(
                            alpha = 0.05f
                        )
                    ) {

                        Row(
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 9.dp
                            ),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(
                                text = item.category,
                                modifier = Modifier.weight(1f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text =
                                    "${item.parcels} parcels",
                                modifier =
                                    Modifier.width(90.dp),
                                textAlign =
                                    TextAlign.End,
                                fontSize = 12.sp
                            )

                            Text(
                                text =
                                    "${item.weight} kg",
                                modifier =
                                    Modifier.width(75.dp),
                                textAlign =
                                    TextAlign.End,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = color
                            )
                        }
                    }
                }
            }
        }
    }
}

/* ================================================================
   RECENT ACTIVITY
================================================================ */

@Composable
private fun RecentActivityCard(
    activity: RecentActivity
) {

    val isParcel =
        activity.bookingType
            .uppercase() == "PARCEL"

    val accentColor =
        if (isParcel)
            AnalyticsOrange
        else
            AnalyticsBlue

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = accentColor.copy(
                    alpha = 0.10f
                )
            ) {

                Icon(
                    imageVector =
                        if (isParcel)
                            Icons.Default.Inventory
                        else
                            Icons.Default.EventSeat,

                    contentDescription = null,

                    modifier = Modifier
                        .size(48.dp)
                        .padding(12.dp),

                    tint = accentColor
                )
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text =
                        activity.route
                            .ifBlank {
                                "Unknown Route"
                            },

                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = activity.date,
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280)
                )

                Text(
                    text =
                        if (isParcel) {

                            if (
                                activity.parcelType
                                    .isNotBlank()
                            ) {

                                "${activity.parcelType} • " +
                                        "${activity.parcelWeight} kg"

                            } else {

                                "Parcel • " +
                                        "${activity.parcelWeight} kg"
                            }

                        } else {

                            "${activity.passengers} passenger(s)"
                        },

                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment =
                    Alignment.End
            ) {

                Text(
                    text =
                        formatCurrency(
                            activity.totalFare
                        ),

                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = accentColor.copy(
                        alpha = 0.10f
                    )
                ) {

                    Text(
                        text =
                            activity.status
                                .ifBlank {
                                    "UNKNOWN"
                                },

                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 5.dp
                        ),

                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = accentColor
                    )
                }
            }
        }
    }
}

/* ================================================================
   EMPTY
================================================================ */

@Composable
private fun EmptyCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Surface(
                shape = CircleShape,
                color = AnalyticsBlue.copy(
                    alpha = 0.10f
                )
            ) {

                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = AnalyticsBlue,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(15.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "No activity found",
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text =
                    "There is no activity for this period.",
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
private fun EmptyChart() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),

        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Surface(
                shape = CircleShape,
                color = AnalyticsBlue.copy(
                    alpha = 0.10f
                )
            ) {

                Icon(
                    imageVector = Icons.Default.ShowChart,
                    contentDescription = null,
                    tint = AnalyticsBlue,
                    modifier = Modifier
                        .size(52.dp)
                        .padding(13.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "No revenue data available",
                color = Color(0xFF6B7280),
                fontSize = 12.sp
            )
        }
    }
}

/* ================================================================
   HISTORICAL MONTHS
================================================================ */

@RequiresApi(Build.VERSION_CODES.O)
private fun historicalMonths(): List<YearMonth> {

    val current = YearMonth.now()

    return (0..23).map {
        current.minusMonths(
            it.toLong()
        )
    }
}

/* ================================================================
   YEAR MONTH
================================================================ */

@RequiresApi(Build.VERSION_CODES.O)
private fun YearMonth.formatMonth(): String {

    return "${month.name.lowercase().replaceFirstChar {
        it.uppercase()
    }} $year"
}

/* ================================================================
   NUMBER FORMAT
================================================================ */

private fun formatCurrency(
    amount: Double
): String {

    return NumberFormat
        .getCurrencyInstance(
            Locale("en", "IN")
        )
        .format(amount)
}

private fun formatNumber(
    value: Double
): String {

    return String.format(
        Locale.US,
        "%.2f",
        value
    )
}

private fun formatChange(
    value: Double
): String {

    return when {

        value > 0 ->
            "↑ ${
                String.format(
                    Locale.US,
                    "%.1f",
                    value
                )
            }%"

        value < 0 ->
            "↓ ${
                String.format(
                    Locale.US,
                    "%.1f",
                    abs(value)
                )
            }%"

        else ->
            "0.0%"
    }
}