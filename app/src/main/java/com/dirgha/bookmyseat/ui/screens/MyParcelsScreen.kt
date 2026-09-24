package com.dirgha.bookmyseat.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.data.model.Parcel
import com.dirgha.bookmyseat.ui.viewmodel.ParcelViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// ================================================================
// COLORS
// ================================================================

private val Blue = Color(0xFF2962FF)
private val Green = Color(0xFF16A34A)
private val Red = Color(0xFFDC2626)
private val Orange = Color(0xFFD97706)
private val Background = Color(0xFFECEFF1)


// ================================================================
// MY PARCELS SCREEN
// ================================================================

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyParcelsScreen(
    navController: NavController,
    parcelViewModel: ParcelViewModel = viewModel()
) {

    // ============================================================
    // VIEWMODEL STATE
    // ============================================================

    val parcels by
    parcelViewModel.parcels.collectAsState()

    val isLoading by
    parcelViewModel.isLoading.collectAsState()

    val errorMessage by
    parcelViewModel.error.collectAsState()

    // ============================================================
    // LOCAL FILTER STATE
    // ============================================================

    var selectedFilter by
    remember {
        mutableStateOf("All")
    }

    // ============================================================
    // DATE FORMATTER
    // ============================================================

    val formatter =
        DateTimeFormatter.ofPattern(
            "yyyy-MM-dd"
        )

    val today =
        LocalDate.now()

    // ============================================================
    // INITIAL LOAD
    // ============================================================

    LaunchedEffect(Unit) {

        parcelViewModel.loadMyParcels()
    }

    // ============================================================
    // PULL TO REFRESH
    // ============================================================

    val pullRefreshState =
        rememberPullToRefreshState()

    // ============================================================
    // SUMMARY
    // ============================================================

    val totalParcels =
        parcels.count {

            !it.parcelStatus.equals(
                "CANCELLED",
                ignoreCase = true
            )
        }

    val upcomingParcels =
        parcels.count { parcel ->

            val parcelDate =
                try {

                    LocalDate.parse(
                        parcel.expectedDate,
                        formatter
                    )

                } catch (
                    e: Exception
                ) {

                    null
                }

            parcelDate != null &&
                    !parcelDate.isBefore(today) &&
                    !parcel.parcelStatus.equals(
                        "CANCELLED",
                        ignoreCase = true
                    ) &&
                    !parcel.parcelStatus.equals(
                        "REJECTED",
                        ignoreCase = true
                    )
        }

    val pendingParcels =
        parcels.count {

            it.parcelStatus.equals(
                "PENDING",
                ignoreCase = true
            )
        }

    val rejectedParcels =
        parcels.count {

            it.parcelStatus.equals(
                "REJECTED",
                ignoreCase = true
            )
        }

    val confirmedParcels =
        parcels.count {

            it.parcelStatus.equals(
                "CONFIRMED",
                ignoreCase = true
            )
        }

    val totalSpent =
        parcels
            .filter {

                it.parcelStatus.equals(
                    "CONFIRMED",
                    ignoreCase = true
                )
            }
            .sumOf {

                it.agreedFare ?: 0.0
            }

    val successRate = if (totalParcels == 0) {
        0
    } else {
        (confirmedParcels * 100) / totalParcels
    }

    val favouriteRoute =
        parcels
            .mapNotNull {
                it.route
            }
            .filter {
                it.isNotBlank()
            }
            .groupingBy {
                it
            }
            .eachCount()
            .maxByOrNull {
                it.value
            }
            ?.key
            ?: "No Parcels"

    // ============================================================
    // FILTER
    // ============================================================

    val filteredParcels =
        parcels.filter { parcel ->

            val parcelDate =
                try {

                    LocalDate.parse(
                        parcel.expectedDate,
                        formatter
                    )

                } catch (
                    e: Exception
                ) {

                    null
                }

            when (selectedFilter) {

                "Upcoming" ->

                    parcelDate != null &&
                            !parcelDate.isBefore(today) &&
                            !parcel.parcelStatus.equals(
                                "CANCELLED",
                                ignoreCase = true
                            )

                "Completed" ->

                    parcelDate != null &&
                            parcelDate.isBefore(today) &&
                            !parcel.parcelStatus.equals(
                                "CANCELLED",
                                ignoreCase = true
                            )

                else ->

                    !parcel.parcelStatus.equals(
                        "CANCELLED",
                        ignoreCase = true
                    )
            }
        }

    // ============================================================
    // SORT
    // ============================================================

    val sortedParcels =
        filteredParcels.sortedWith(

            compareByDescending<Parcel> {

                try {

                    LocalDate.parse(
                        it.expectedDate,
                        formatter
                    )

                } catch (
                    e: Exception
                ) {

                    LocalDate.MIN
                }
            }
        )

    // ============================================================
    // SCREEN
    // ============================================================

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Background
                )
    ) {

        // ========================================================
        // HEADER
        // ========================================================

        Column(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White
                    )
        ) {

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 10.dp
                        ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                IconButton(

                    onClick = {

                        navController.popBackStack()
                    }

                ) {

                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowBack,

                        contentDescription =
                            "Back"
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(5.dp)
                )

                Text(
                    text =
                        "My Parcels",

                    fontSize =
                        28.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            // ====================================================
            // FILTERS
            // ====================================================

            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(
                            rememberScrollState()
                        )
                        .padding(
                            horizontal = 16.dp
                        ),

                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                ParcelFilterChip(
                    title = "All",
                    selected =
                        selectedFilter
                ) {

                    selectedFilter =
                        "All"
                }

                ParcelFilterChip(
                    title = "Upcoming",
                    selected =
                        selectedFilter
                ) {

                    selectedFilter =
                        "Upcoming"
                }

                ParcelFilterChip(
                    title = "Completed",
                    selected =
                        selectedFilter
                ) {

                    selectedFilter =
                        "Completed"
                }
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            HorizontalDivider(
                thickness = 1.dp,
                color =
                    Color(0xFFE5E7EB)
            )
        }

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        // ========================================================
        // CONTENT
        // ========================================================

        PullToRefreshBox(

            isRefreshing =
                isLoading,

            onRefresh = {

                parcelViewModel.loadMyParcels()
            },

            state =
                pullRefreshState,

            modifier =
                Modifier.fillMaxSize()

        ) {

            // ====================================================
            // ERROR
            // ====================================================

            if (
                errorMessage != null &&
                parcels.isEmpty()
            ) {

                ParcelErrorCard(

                    message =
                        errorMessage!!,

                    onRetry = {

                        parcelViewModel.loadMyParcels()
                    }
                )
            }

            // ====================================================
            // EMPTY
            // ====================================================

            else if (
                sortedParcels.isEmpty()
            ) {

                EmptyParcelsCard(
                    filter =
                        selectedFilter
                )
            }

            // ====================================================
            // PARCEL LIST
            // ====================================================

            else {

                LazyColumn(

                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(
                                Background
                            ),

                    contentPadding =
                        PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(
                            16.dp
                        )
                ) {

                    // ==========================================
                    // SUMMARY
                    // ==========================================

                    item {

                        ParcelSummaryCard(

                            totalParcels =
                                totalParcels,

                            upcomingParcels =
                                upcomingParcels,

                            totalSpent =
                                totalSpent,

                            pendingParcels =
                                pendingParcels,

                            rejectedParcels =
                                rejectedParcels,

                            successRate =
                                successRate,

                            favouriteRoute =
                                favouriteRoute
                        )
                    }

                    // ==========================================
                    // PARCEL CARDS
                    // ==========================================

                    items(

                        items =
                            sortedParcels,

                        key = {

                            it.parcelId
                        }

                    ) { parcel ->

                        ParcelCard(
                            parcel =
                                parcel
                        )
                    }

                    item {

                        Spacer(
                            modifier =
                                Modifier.height(30.dp)
                        )
                    }
                }
            }
        }
    }
}


// =================================================================
// PARCEL SUMMARY CARD
// =================================================================

@Composable
private fun ParcelSummaryCard(

    totalParcels: Int,

    upcomingParcels: Int,

    totalSpent: Double,

    pendingParcels: Int,

    rejectedParcels: Int,

    successRate: Int,

    favouriteRoute: String

) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text =
                    "Parcel Summary",

                fontSize =
                    18.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                ParcelSummaryItem(
                    modifier =
                        Modifier.weight(1f),

                    title =
                        "TOTAL",

                    value =
                        totalParcels.toString()
                )

                ParcelSummaryItem(
                    modifier =
                        Modifier.weight(1f),

                    title =
                        "UPCOMING",

                    value =
                        upcomingParcels.toString()
                )

                ParcelSummaryItem(
                    modifier =
                        Modifier.weight(1f),

                    title =
                        "PENDING",

                    value =
                        pendingParcels.toString()
                )
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                ParcelSummaryItem(
                    modifier =
                        Modifier.weight(1f),

                    title =
                        "REJECTED",

                    value =
                        rejectedParcels.toString()
                )

                ParcelSummaryItem(
                    modifier =
                        Modifier.weight(1f),

                    title =
                        "SUCCESS",

                    value =
                        "$successRate%"
                )

                ParcelSummaryItem(
                    modifier =
                        Modifier.weight(1f),

                    title =
                        "SPENT",

                    value =
                        "₹${totalSpent.toInt()}"
                )
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text =
                    "Favourite Route",

                fontSize =
                    11.sp,

                color =
                    Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text =
                    favouriteRoute,

                fontSize =
                    14.sp,

                fontWeight =
                    FontWeight.SemiBold
            )
        }
    }
}


// =================================================================
// SUMMARY ITEM
// =================================================================

@Composable
private fun ParcelSummaryItem(

    modifier: Modifier,

    title: String,

    value: String

) {

    Column(

        modifier =
            modifier,

        horizontalAlignment =
            Alignment.CenterHorizontally

    ) {

        Text(
            text =
                title,

            fontSize =
                10.sp,

            color =
                Color.Gray
        )

        Spacer(
            modifier =
                Modifier.height(4.dp)
        )

        Text(
            text =
                value,

            fontSize =
                16.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}


// =================================================================
// PARCEL CARD
// =================================================================

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ParcelCard(
    parcel: Parcel
) {

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
            CardDefaults.outlinedCardBorder()
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            // ====================================================
            // ROUTE
            // ====================================================

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.LocationOn,

                    contentDescription =
                        null,

                    tint =
                        Blue,

                    modifier =
                        Modifier.size(20.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(
                    text =
                        parcel.route
                            ?: "Route not available",

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color.Black
                )
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // ====================================================
            // EXPECTED DATE
            // ====================================================

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.CalendarMonth,

                    contentDescription =
                        null,

                    tint =
                        Color.Gray,

                    modifier =
                        Modifier.size(18.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(5.dp)
                )

                Text(
                    text =
                        formatParcelDate(
                            parcel.expectedDate
                        ),

                    color =
                        Color.DarkGray
                )
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // ====================================================
            // PARCEL TYPE
            // ====================================================

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.Inventory2,

                    contentDescription =
                        null,

                    tint =
                        Blue,

                    modifier =
                        Modifier.size(18.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(6.dp)
                )

                Column {

                    Text(
                        text =
                            "PARCEL TYPE",

                        fontSize =
                            10.sp,

                        color =
                            Color.Gray
                    )

                    Text(
                        text =
                            parcel.parcelType,

                        fontSize =
                            14.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            // ====================================================
            // WEIGHT + CATEGORY
            // ====================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        16.dp
                    )
            ) {

                Row(
                    modifier =
                        Modifier.weight(1f),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Outlined.Scale,

                        contentDescription =
                            null,

                        tint =
                            Blue,

                        modifier =
                            Modifier.size(18.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(5.dp)
                    )

                    Column {

                        Text(
                            text =
                                "WEIGHT",

                            fontSize =
                                10.sp,

                            color =
                                Color.Gray
                        )

                        Text(
                            text =
                                "${parcel.weight} KG",

                            fontSize =
                                14.sp,

                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }
                }

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "CATEGORY",

                        fontSize =
                            10.sp,

                        color =
                            Color.Gray
                    )

                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )

                    Text(
                        text =
                            parcel.categoryName,

                        fontSize =
                            14.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }

            // ====================================================
            // ACTUAL DATE / TIME
            // ====================================================

            if (
                !parcel.actualDate.isNullOrBlank() ||
                !parcel.actualTime.isNullOrBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Outlined.AccessTime,

                        contentDescription =
                            null,

                        tint =
                            Color.Gray,

                        modifier =
                            Modifier.size(18.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(5.dp)
                    )

                    Text(
                        text =
                            buildString {

                                parcel.actualDate
                                    ?.takeIf {
                                        it.isNotBlank()
                                    }
                                    ?.let {

                                        append(
                                            formatParcelDate(
                                                it
                                            )
                                        )
                                    }

                                parcel.actualTime
                                    ?.takeIf {
                                        it.isNotBlank()
                                    }
                                    ?.let {

                                        if (isNotEmpty()) {
                                            append(" • ")
                                        }

                                        append(it)
                                    }
                            },

                        color =
                            Color.DarkGray,

                        fontSize =
                            13.sp
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            // ====================================================
            // STATUS + FARE
            // ====================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                ParcelStatusChip(
                    status =
                        parcel.parcelStatus
                )

                parcel.agreedFare?.let { fare ->

                    Text(
                        text =
                            "₹${fare.toInt()}",

                        fontSize =
                            22.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            // ====================================================
            // REJECTION REASON
            // ====================================================

            if (
                parcel.parcelStatus.equals(
                    "REJECTED",
                    ignoreCase = true
                ) &&
                !parcel.rejectionReason.isNullOrBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Card(

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color(0xFFFFF3F3)
                        )
                ) {

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                    ) {

                        Text(
                            text =
                                "Reason for Rejection",

                            color =
                                Red,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                parcel.rejectionReason!!
                        )
                    }
                }
            }
        }
    }
}


// =================================================================
// STATUS CHIP
// =================================================================

@Composable
private fun ParcelStatusChip(
    status: String
) {

    val background =

        when (
            status.lowercase()
        ) {

            "confirmed" ->
                Color(0xFFDDF5E3)

            "pending" ->
                Color(0xFFFFF3CD)

            "rejected" ->
                Color(0xFFFDE2E2)

            "delivered" ->
                Color(0xFFDDF5E3)

            else ->
                Color(0xFFDCE8FF)
        }

    val textColor =

        when (
            status.lowercase()
        ) {

            "confirmed" ->
                Green

            "pending" ->
                Orange

            "rejected" ->
                Red

            "delivered" ->
                Green

            else ->
                Blue
        }

    Box(

        modifier =
            Modifier
                .clip(
                    RoundedCornerShape(50)
                )
                .background(
                    background
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 5.dp
                )
    ) {

        Text(

            text =
                status.replaceFirstChar {
                    it.uppercase()
                },

            color =
                textColor,

            fontSize =
                12.sp,

            fontWeight =
                FontWeight.SemiBold
        )
    }
}


// =================================================================
// FILTER CHIP
// =================================================================

@Composable
private fun ParcelFilterChip(

    title: String,

    selected: String,

    onClick: () -> Unit

) {

    val isSelected =
        selected == title

    Surface(

        modifier =
            Modifier.clickable {
                onClick()
            },

        shape =
            RoundedCornerShape(10.dp),

        color =
            if (isSelected)
                Blue
            else
                Color(0xFFF5F5F5),

        tonalElevation =
            if (isSelected)
                2.dp
            else
                0.dp
    ) {

        Text(

            text =
                title,

            modifier =
                Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 6.dp
                ),

            color =
                if (isSelected)
                    Color.White
                else
                    Color(0xFF555555),

            fontWeight =
                if (isSelected)
                    FontWeight.SemiBold
                else
                    FontWeight.Medium,

            fontSize =
                12.sp
        )
    }
}


// =================================================================
// EMPTY PARCELS
// =================================================================

@Composable
private fun EmptyParcelsCard(
    filter: String
) {

    val title: String
    val message: String

    when (filter) {

        "Upcoming" -> {

            title =
                "No Upcoming Parcels"

            message =
                "You don't have any upcoming parcels.\nBook your next parcel now!"
        }

        "Completed" -> {

            title =
                "No Completed Parcels"

            message =
                "Your completed parcels will appear here."
        }

        else -> {

            title =
                "No Parcels Yet"

            message =
                "You haven't booked any parcels yet.\nStart by booking your first parcel."
        }
    }

    Box(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),

        contentAlignment =
            Alignment.Center
    ) {

        Card(

            shape =
                RoundedCornerShape(20.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                ),

            elevation =
                CardDefaults.cardElevation(
                    6.dp
                )
        ) {

            Column(

                modifier =
                    Modifier
                        .padding(28.dp)
                        .fillMaxWidth(),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
                        "📦",

                    fontSize =
                        64.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Text(
                    text =
                        title,

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        20.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        message,

                    color =
                        Color.Gray,

                    textAlign =
                        TextAlign.Center
                )
            }
        }
    }
}


// =================================================================
// ERROR CARD
// =================================================================

@Composable
private fun ParcelErrorCard(
    message: String,
    onRetry: () -> Unit
) {

    Box(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),

        contentAlignment =
            Alignment.Center
    ) {

        Card(

            shape =
                RoundedCornerShape(20.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                )
        ) {

            Column(

                modifier =
                    Modifier.padding(28.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
                        "Unable to load parcels",

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        18.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        message,

                    color =
                        Color.Gray,

                    textAlign =
                        TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Button(
                    onClick =
                        onRetry
                ) {

                    Text(
                        text =
                            "Retry"
                    )
                }
            }
        }
    }
}


// =================================================================
// DATE FORMAT
// =================================================================

@RequiresApi(Build.VERSION_CODES.O)
private fun formatParcelDate(
    date: String
): String {

    return try {

        LocalDate
            .parse(date)
            .format(
                DateTimeFormatter.ofPattern(
                    "dd MMM yyyy",
                    Locale.ENGLISH
                )
            )

    } catch (
        e: Exception
    ) {

        date
    }
}