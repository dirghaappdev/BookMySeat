package com.dirgha.bookmyseat.ui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirgha.bookmyseat.data.config.RejectReason
import com.dirgha.bookmyseat.data.model.Parcel
import com.dirgha.bookmyseat.data.model.Trip
import com.dirgha.bookmyseat.ui.viewmodel.ParcelViewModel
import com.dirgha.bookmyseat.utils.ConfigManager
import java.time.LocalDate

// ============================================================================
// SHORT BMS ID
// ============================================================================

private fun shortBmsId(id: String?): String {

    if (id.isNullOrBlank()) {
        return "#BMS----"
    }

    val cleanId = id.trim()

    return "#BMS${cleanId.takeLast(4).uppercase()}"
}


// ============================================================================
// MAIN SCREEN
// ============================================================================

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageParcelBookingsScreen(
    parcelViewModel: ParcelViewModel
) {

    // ========================================================================
    // STATE
    // ========================================================================

    val parcels by parcelViewModel.adminParcels.collectAsState()
    val trips by parcelViewModel.trips.collectAsState()

    val isLoading by parcelViewModel.isLoading.collectAsState()
    val isActionLoading by parcelViewModel.isActionLoading.collectAsState()

    val error by parcelViewModel.error.collectAsState()
    val actionMessage by parcelViewModel.actionMessage.collectAsState()

    var selectedTab by remember {
        mutableStateOf(0)
    }

    var selectedParcel by remember {
        mutableStateOf<Parcel?>(null)
    }

    var showDetailsDialog by remember {
        mutableStateOf(false)
    }

    var showAllocateDialog by remember {
        mutableStateOf(false)
    }

    var showRejectDialog by remember {
        mutableStateOf(false)
    }

    var showRescheduleDialog by remember {
        mutableStateOf(false)
    }

    // ========================================================================
    // REJECTION REASONS
    // ========================================================================

    val context = androidx.compose.ui.platform.LocalContext.current

    var rejectionReasons by remember {
        mutableStateOf<List<RejectReason>>(emptyList())
    }

    LaunchedEffect(Unit) {

        val config =
            ConfigManager(context).getConfiguration()

        rejectionReasons =
            config?.data?.masters?.rejectReason
                ?.filter {
                    it.is_active
                }
                ?: emptyList()
    }

    // ========================================================================
    // LOAD DATA
    // ========================================================================

    LaunchedEffect(Unit) {

        parcelViewModel.loadAdminParcels()
        parcelViewModel.loadTrips()
    }

    // ========================================================================
    // TABS
    // ========================================================================

    val tabNames = listOf(
        "Pending",
        "Confirmed",
        "Rejected",
        "Delivered"
    )

    val selectedStatus = when (selectedTab) {

        0 -> "PENDING"

        1 -> "CONFIRMED"

        2 -> "REJECTED"

        else -> "DELIVERED"
    }

    // ========================================================================
    // FILTER
    // ========================================================================

    val filteredParcels =
        parcels.filter {

            it.parcelStatus.equals(
                selectedStatus,
                ignoreCase = true
            )
        }

    // ========================================================================
    // REFRESH
    // ========================================================================

    val pullRefreshState =
        rememberPullToRefreshState()

    // ========================================================================
    // SCREEN
    // ========================================================================

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Manage Parcel Bookings",
                        fontWeight = FontWeight.Bold
                    )
                },

                actions = {

                    IconButton(

                        onClick = {

                            parcelViewModel.loadAdminParcels()
                            parcelViewModel.loadTrips()
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF5F7FC)
                )
                .padding(paddingValues)
        ) {

            // =================================================================
            // TABS
            // =================================================================

            TabRow(
                selectedTabIndex = selectedTab
            ) {

                tabNames.forEachIndexed { index, title ->

                    Tab(

                        selected =
                            selectedTab == index,

                        onClick = {
                            selectedTab = index
                        },

                        text = {

                            Text(
                                text = title,
                                fontSize = 12.sp
                            )
                        }
                    )
                }
            }

            // =================================================================
            // LOADING
            // =================================================================

            if (isLoading) {

                Box(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator()
                }

            }

            // =================================================================
            // EMPTY
            // =================================================================

            else if (filteredParcels.isEmpty()) {

                Box(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            "No $selectedStatus parcel bookings",
                        color =
                            Color.Gray,
                        fontSize =
                            14.sp
                    )
                }

            }

            // =================================================================
            // LIST
            // =================================================================

            else {

                PullToRefreshBox(

                    isRefreshing =
                        isLoading,

                    onRefresh = {

                        parcelViewModel.loadAdminParcels()
                        parcelViewModel.loadTrips()
                    },

                    state =
                        pullRefreshState,

                    modifier =
                        Modifier.fillMaxSize()
                ) {

                    LazyColumn(

                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(
                                    Color(0xFFF5F7FC)
                                ),

                        contentPadding =
                            PaddingValues(16.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        items(

                            items =
                                filteredParcels,

                            key = {
                                it.parcelId
                            }

                        ) { parcel ->

                            ParcelAdminCard(

                                parcel =
                                    parcel,

                                onDetails = {

                                    selectedParcel =
                                        parcel

                                    showDetailsDialog =
                                        true
                                },

                                onConfirm = {

                                    selectedParcel =
                                        parcel

                                    parcelViewModel
                                        .loadTrips()

                                    showAllocateDialog =
                                        true
                                },

                                onReject = {

                                    selectedParcel =
                                        parcel

                                    showRejectDialog =
                                        true
                                },

                                onReschedule = {

                                    selectedParcel =
                                        parcel

                                    parcelViewModel
                                        .loadTrips()

                                    showRescheduleDialog =
                                        true
                                },

                                onDelivered = {

                                    parcelViewModel
                                        .markParcelDelivered(
                                            parcel.parcelId
                                        )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // ========================================================================
    // ERROR DIALOG
    // ========================================================================

    if (error != null) {

        AlertDialog(

            onDismissRequest = {

                parcelViewModel.clearError()
            },

            title = {

                Text(
                    text = "Error",
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Text(
                    text = error ?: ""
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        parcelViewModel.clearError()
                    }
                ) {

                    Text("OK")
                }
            }
        )
    }

    // ========================================================================
    // SUCCESS DIALOG
    // ========================================================================

    if (actionMessage != null) {

        AlertDialog(

            onDismissRequest = {

                parcelViewModel.clearActionMessage()
            },

            title = {

                Text(
                    text = "Success",
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Text(
                    text =
                        actionMessage ?: ""
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        parcelViewModel
                            .clearActionMessage()
                    }
                ) {

                    Text("OK")
                }
            }
        )
    }

    // ========================================================================
    // DETAILS
    // ========================================================================

    if (
        showDetailsDialog &&
        selectedParcel != null
    ) {

        ParcelDetailsDialog(

            parcel =
                selectedParcel!!,

            onDismiss = {

                showDetailsDialog =
                    false
            }
        )
    }

    // ========================================================================
    // ALLOCATE
    // ========================================================================

    if (
        showAllocateDialog &&
        selectedParcel != null
    ) {

        AllocateParcelDialog(

            parcel =
                selectedParcel!!,

            trips =
                trips,

            isLoading =
                isActionLoading,

            onDismiss = {

                showAllocateDialog =
                    false
            },

            onConfirm = {

                    tripId,
                    agreedFare ->

                parcelViewModel.allocateParcel(

                    parcelId =
                        selectedParcel!!.parcelId,

                    tripId =
                        tripId,

                    agreedFare =
                        agreedFare
                )

                showAllocateDialog =
                    false
            }
        )
    }

    // ========================================================================
    // RESCHEDULE
    // ========================================================================

    if (
        showRescheduleDialog &&
        selectedParcel != null
    ) {

        RescheduleParcelDialog(

            parcel =
                selectedParcel!!,

            trips =
                trips,

            isLoading =
                isActionLoading,

            onDismiss = {

                showRescheduleDialog =
                    false
            },

            onConfirm = {

                    tripId,
                    agreedFare ->

                parcelViewModel.rescheduleParcel(

                    parcelId =
                        selectedParcel!!.parcelId,

                    tripId =
                        tripId,

                    agreedFare =
                        agreedFare
                )

                showRescheduleDialog =
                    false
            }
        )
    }

    // ========================================================================
    // REJECT
    // ========================================================================

    if (
        showRejectDialog &&
        selectedParcel != null
    ) {

        RejectParcelDialog(

            rejectionReasons =
                rejectionReasons,

            isLoading =
                isActionLoading,

            onDismiss = {

                showRejectDialog =
                    false
            },

            onReject = { reason ->

                parcelViewModel.rejectParcel(

                    parcelId =
                        selectedParcel!!.parcelId,

                    reason =
                        reason
                )

                showRejectDialog =
                    false
            }
        )
    }
}


// ============================================================================
// PARCEL ADMIN CARD
// ============================================================================

@Composable
private fun ParcelAdminCard(
    parcel: Parcel,
    onDetails: () -> Unit,
    onConfirm: () -> Unit,
    onReject: () -> Unit,
    onReschedule: () -> Unit,
    onDelivered: () -> Unit
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
            BorderStroke(
                width = 1.dp,
                color =
                    Color(0xFFEAEAEA)
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(16.dp)
        ) {

            // =================================================================
            // HEADER
            // =================================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.Top
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(

                        text =
                            parcel.parcelType.ifBlank {
                                "Parcel"
                            },

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )

                    Text(

                        text =
                            shortBmsId(
                                parcel.parcelId
                            ),

                        fontSize =
                            13.sp,

                        color =
                            MaterialTheme.colorScheme.primary,

                        fontWeight =
                            FontWeight.Bold
                    )
                }

                StatusChip(
                    status =
                        parcel.parcelStatus
                )
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            // =================================================================
            // CUSTOMER
            // =================================================================

            SimpleInfoRow(
                label =
                    "Customer",
                value =
                    parcel.userName.ifBlank {
                        "Unknown User"
                    }
            )

            // =================================================================
            // MOBILE
            // =================================================================

            if (
                parcel.mobileNumber.isNotBlank()
            ) {

                SimpleInfoRow(
                    label =
                        "Mobile",
                    value =
                        parcel.mobileNumber
                )
            }

            // =================================================================
            // WEIGHT
            // =================================================================

            SimpleInfoRow(
                label =
                    "Weight",
                value =
                    "${parcel.weight} KG • ${parcel.categoryName}"
            )

            // =================================================================
            // ROUTE
            // =================================================================

            SimpleInfoRow(
                label =
                    "Route",
                value =
                    parcel.route?.ifBlank {
                        "Route unavailable"
                    } ?: "Route unavailable"
            )

            // =================================================================
            // EXPECTED DATE
            // =================================================================

            SimpleInfoRow(
                label =
                    "Expected",
                value =
                    parcel.expectedDate
            )

            // =================================================================
            // TRAVEL
            // =================================================================

            if (
                !parcel.actualDate.isNullOrBlank()
            ) {

                SimpleInfoRow(

                    label =
                        "Travel",

                    value =
                        "${parcel.actualDate} ${
                            parcel.actualTime ?: ""
                        }"
                )
            }

            // =================================================================
            // AGREED FARE
            // =================================================================

            if (
                parcel.agreedFare != null
            ) {

                SimpleInfoRow(

                    label =
                        "Agreed Fare",

                    value =
                        "₹${parcel.agreedFare}"
                )
            }

            // =================================================================
            // NOTE
            // =================================================================

            if (
                !parcel.note.isNullOrBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(12.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color(0xFFF3F6FC)
                        ),

                    border =
                        BorderStroke(
                            1.dp,
                            Color(0xFFEAEAEA)
                        )
                ) {

                    Column(

                        modifier =
                            Modifier.padding(14.dp)
                    ) {

                        Text(

                            text =
                                "Notes",

                            color =
                                Color.Gray,

                            fontSize =
                                12.sp,

                            fontWeight =
                                FontWeight.Medium
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                parcel.note ?: "",
                            fontSize =
                                13.sp
                        )
                    }
                }
            }

            // =================================================================
            // REJECTION REASON
            // =================================================================

            if (
                !parcel.rejectionReason.isNullOrBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(12.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color(0xFFFFF3F3)
                        ),

                    border =
                        BorderStroke(
                            1.dp,
                            Color(0xFFFFD6D6)
                        )
                ) {

                    Column(

                        modifier =
                            Modifier.padding(12.dp)
                    ) {

                        Text(

                            text =
                                "Rejection Reason",

                            color =
                                Color.Red,

                            fontWeight =
                                FontWeight.Bold,

                            fontSize =
                                13.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(

                            text =
                                parcel.rejectionReason ?: "",

                            fontSize =
                                13.sp,

                            color =
                                Color.DarkGray
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            HorizontalDivider(
                color =
                    Color(0xFFF0F0F0)
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            // =================================================================
            // PENDING ACTIONS
            // =================================================================

            if (
                parcel.parcelStatus.equals(
                    "PENDING",
                    ignoreCase = true
                )
            ) {

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    OutlinedButton(

                        modifier =
                            Modifier.weight(1f),

                        onClick =
                            onDetails,

                        shape =
                            RoundedCornerShape(12.dp),

                        contentPadding =
                            PaddingValues(
                                horizontal = 6.dp
                            )
                    ) {

                        Text(
                            text = "Details",
                            fontSize = 12.sp
                        )
                    }

                    Button(

                        modifier =
                            Modifier.weight(1f),

                        onClick =
                            onConfirm,

                        shape =
                            RoundedCornerShape(12.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFF0BAA39)
                            ),

                        contentPadding =
                            PaddingValues(
                                horizontal = 6.dp
                            )
                    ) {

                        Text(
                            text = "Confirm",
                            fontSize = 12.sp
                        )
                    }

                    Button(

                        modifier =
                            Modifier.weight(1f),

                        onClick =
                            onReject,

                        shape =
                            RoundedCornerShape(12.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFFC62828)
                            ),

                        contentPadding =
                            PaddingValues(
                                horizontal = 6.dp
                            )
                    ) {

                        Text(
                            text = "Reject",
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // =================================================================
            // CONFIRMED ACTIONS
            // =================================================================

            if (
                parcel.parcelStatus.equals(
                    "CONFIRMED",
                    ignoreCase = true
                )
            ) {

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    // ---------------------------------------------------------
                    // DETAILS
                    // ---------------------------------------------------------

                    OutlinedButton(

                        modifier =
                            Modifier.weight(1f),

                        onClick =
                            onDetails,

                        shape =
                            RoundedCornerShape(12.dp),

                        contentPadding =
                            PaddingValues(
                                horizontal = 5.dp
                            )
                    ) {

                        Text(
                            text = "Details",
                            fontSize = 11.sp
                        )
                    }

                    // ---------------------------------------------------------
                    // RESCHEDULE
                    // ---------------------------------------------------------

                    OutlinedButton(

                        modifier =
                            Modifier.weight(1f),

                        onClick =
                            onReschedule,

                        shape =
                            RoundedCornerShape(12.dp),

                        contentPadding =
                            PaddingValues(
                                horizontal = 5.dp
                            )
                    ) {

                        Text(
                            text = "Reschedule",
                            fontSize = 11.sp
                        )
                    }

                    // ---------------------------------------------------------
                    // MARK DELIVERED
                    // ---------------------------------------------------------

                    Button(

                        modifier =
                            Modifier.weight(1f),

                        onClick =
                            onDelivered,

                        shape =
                            RoundedCornerShape(12.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFF1565C0)
                            ),

                        contentPadding =
                            PaddingValues(
                                horizontal = 5.dp
                            )
                    ) {

                        Text(
                            text = "Mark Delivered",
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // =================================================================
            // REJECTED / DELIVERED
            // =================================================================

            if (
                parcel.parcelStatus.equals(
                    "REJECTED",
                    ignoreCase = true
                ) ||
                parcel.parcelStatus.equals(
                    "DELIVERED",
                    ignoreCase = true
                )
            ) {

                OutlinedButton(

                    modifier =
                        Modifier.fillMaxWidth(),

                    onClick =
                        onDetails,

                    shape =
                        RoundedCornerShape(12.dp)
                ) {

                    Text("View Details")
                }
            }
        }
    }
}


// ============================================================================
// STATUS CHIP
// ============================================================================

@Composable
private fun StatusChip(
    status: String
) {

    val normalized =
        status.trim().uppercase()

    val background =
        when (normalized) {

            "PENDING" ->
                Color(0xFFFFF3CD)

            "CONFIRMED" ->
                Color(0xFFDDF5E4)

            "REJECTED" ->
                Color(0xFFFDE3E3)

            "DELIVERED" ->
                Color(0xFFDDEEFF)

            else ->
                Color(0xFFEFEFEF)
        }

    val textColor =
        when (normalized) {

            "PENDING" ->
                Color(0xFFB8860B)

            "CONFIRMED" ->
                Color(0xFF1E8E3E)

            "REJECTED" ->
                Color(0xFFD93025)

            "DELIVERED" ->
                Color(0xFF0D47A1)

            else ->
                Color.DarkGray
        }

    Box(

        modifier =
            Modifier
                .background(
                    color =
                        background,
                    shape =
                        RoundedCornerShape(50.dp)
                )
                .padding(
                    horizontal = 10.dp,
                    vertical = 5.dp
                )
    ) {

        Text(

            text =
                normalized,

            color =
                textColor,

            fontSize =
                11.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}


// ============================================================================
// SIMPLE INFO ROW
// ============================================================================

@Composable
private fun SimpleInfoRow(
    label: String,
    value: String
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 3.dp
                )
    ) {

        Text(

            text =
                "$label: ",

            fontSize =
                13.sp,

            fontWeight =
                FontWeight.SemiBold,

            color =
                Color.DarkGray
        )

        Text(

            text =
                value.ifBlank {
                    "-"
                },

            fontSize =
                13.sp,

            color =
                Color.DarkGray
        )
    }
}


// ============================================================================
// DETAILS DIALOG
// ============================================================================

@Composable
private fun ParcelDetailsDialog(
    parcel: Parcel,
    onDismiss: () -> Unit
) {

    AlertDialog(

        onDismissRequest =
            onDismiss,

        title = {

            Text(
                text =
                    "Parcel Details",

                fontWeight =
                    FontWeight.Bold
            )
        },

        text = {

            Column {

                DetailText(
                    "Parcel Type",
                    parcel.parcelType
                )

                DetailText(
                    "Parcel ID",
                    shortBmsId(
                        parcel.parcelId
                    )
                )

                DetailText(
                    "Customer",
                    parcel.userName
                )

                DetailText(
                    "Mobile",
                    parcel.mobileNumber
                )

                DetailText(
                    "Weight",
                    "${parcel.weight} KG"
                )

                DetailText(
                    "Category",
                    parcel.categoryName
                )

                DetailText(
                    "Route",
                    parcel.route ?: "-"
                )

                DetailText(
                    "Direction",
                    parcel.direction ?: "-"
                )

                DetailText(
                    "Expected Date",
                    parcel.expectedDate
                )

                DetailText(
                    "Status",
                    parcel.parcelStatus
                )

                // -------------------------------------------------------------
                // TRIP ID
                // -------------------------------------------------------------

                if (
                    !parcel.tripId.isNullOrBlank()
                ) {

                    DetailText(
                        "Trip ID",
                        shortBmsId(
                            parcel.tripId
                        )
                    )
                }

                // -------------------------------------------------------------
                // TRAVEL DATE
                // -------------------------------------------------------------

                if (
                    !parcel.actualDate.isNullOrBlank()
                ) {

                    DetailText(
                        "Travel Date",
                        "${parcel.actualDate} ${
                            parcel.actualTime ?: ""
                        }"
                    )
                }

                // -------------------------------------------------------------
                // AGREED FARE
                // -------------------------------------------------------------

                if (
                    parcel.agreedFare != null
                ) {

                    DetailText(
                        "Agreed Fare",
                        "₹${parcel.agreedFare}"
                    )
                }

                // -------------------------------------------------------------
                // NOTE
                // -------------------------------------------------------------

                if (
                    !parcel.note.isNullOrBlank()
                ) {

                    DetailText(
                        "Note",
                        parcel.note ?: ""
                    )
                }

                // -------------------------------------------------------------
                // REJECTION
                // -------------------------------------------------------------

                if (
                    !parcel.rejectionReason.isNullOrBlank()
                ) {

                    DetailText(
                        "Rejection Reason",
                        parcel.rejectionReason ?: ""
                    )
                }
            }
        },

        confirmButton = {

            Button(
                onClick =
                    onDismiss
            ) {

                Text("Close")
            }
        }
    )
}


// ============================================================================
// DETAIL TEXT
// ============================================================================

@Composable
private fun DetailText(
    label: String,
    value: String
) {

    Column(

        modifier =
            Modifier.padding(
                vertical = 4.dp
            )
    ) {

        Text(

            text =
                label,

            fontSize =
                12.sp,

            color =
                Color.Gray
        )

        Text(

            text =
                value.ifBlank {
                    "-"
                },

            fontSize =
                14.sp,

            fontWeight =
                FontWeight.Medium
        )
    }
}


// ============================================================================
// ALLOCATE PARCEL DIALOG
// ============================================================================

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AllocateParcelDialog(
    parcel: Parcel,
    trips: List<Trip>,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (
        String,
        Double
    ) -> Unit
) {

    var selectedTrip by remember {
        mutableStateOf<Trip?>(null)
    }

    var tripExpanded by remember {
        mutableStateOf(false)
    }

    var fareText by remember {
        mutableStateOf("")
    }

    // ========================================================================
    // UPCOMING TRIPS
    // ========================================================================

    val today =
        LocalDate.now().toString()

    val upcomingTrips =
        trips.filter { trip ->

            trip.date >= today
        }

    // ========================================================================
    // MATCH ROUTE
    // ========================================================================

    val matchingTrips =
        upcomingTrips.filter { trip ->

            val parcelRoute =
                parcel.route?.trim()

            if (
                parcelRoute.isNullOrBlank()
            ) {

                true

            } else {

                trip.route.contains(
                    parcelRoute,
                    ignoreCase = true
                ) ||
                        parcelRoute.contains(
                            trip.route,
                            ignoreCase = true
                        )
            }
        }

    AlertDialog(

        onDismissRequest =
            onDismiss,

        title = {

            Text(
                text =
                    "Confirm Parcel",

                fontWeight =
                    FontWeight.Bold
            )
        },

        text = {

            Column {

                Text(

                    text =
                        "Parcel: ${shortBmsId(parcel.parcelId)}",

                    fontSize =
                        13.sp,

                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(

                    text =
                        "Customer: ${
                            parcel.userName.ifBlank {
                                "Unknown"
                            }
                        }",

                    fontSize =
                        13.sp,

                    fontWeight =
                        FontWeight.Medium
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(

                    text =
                        "Route: ${parcel.route ?: "-"}",

                    fontSize =
                        13.sp,

                    color =
                        Color.Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                // =============================================================
                // TRIP
                // =============================================================

                Box {

                    OutlinedTextField(

                        value =
                            selectedTrip?.let {

                                "${shortBmsId(it.tripId)} • ${it.date} • ${it.timeSlot}"

                            } ?: "",

                        onValueChange = {},

                        readOnly = true,

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {

                                    tripExpanded =
                                        true
                                },

                        label = {
                            Text("Select Upcoming Trip")
                        },

                        trailingIcon = {

                            Text(
                                text =
                                    "▼",
                                modifier =
                                    Modifier.clickable {
                                        tripExpanded =
                                            true
                                    }
                            )
                        }
                    )

                    DropdownMenu(

                        expanded =
                            tripExpanded,

                        onDismissRequest = {

                            tripExpanded =
                                false
                        },

                        modifier =
                            Modifier.fillMaxWidth(
                                0.82f
                            )
                    ) {

                        if (
                            matchingTrips.isEmpty()
                        ) {

                            DropdownMenuItem(

                                text = {

                                    Text(
                                        "No upcoming matching trips found"
                                    )
                                },

                                onClick = {

                                    tripExpanded =
                                        false
                                }
                            )

                        } else {

                            matchingTrips.forEach { trip ->

                                DropdownMenuItem(

                                    text = {

                                        Column {

                                            Text(

                                                text =
                                                    "${shortBmsId(trip.tripId)} • ${trip.date}",

                                                fontWeight =
                                                    FontWeight.Bold
                                            )

                                            Text(

                                                text =
                                                    "${trip.timeSlot} • ${trip.route}",

                                                fontSize =
                                                    12.sp
                                            )

                                            Text(

                                                text =
                                                    "Available seats: ${trip.availableSeats}",

                                                fontSize =
                                                    11.sp,

                                                color =
                                                    Color.Gray
                                            )
                                        }
                                    },

                                    onClick = {

                                        selectedTrip =
                                            trip

                                        tripExpanded =
                                            false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                // =============================================================
                // AGREED FARE
                // =============================================================

                OutlinedTextField(

                    value =
                        fareText,

                    onValueChange = {

                        fareText =
                            it.filter { char ->

                                char.isDigit() ||
                                        char == '.'
                            }
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Agreed Fare")
                    },

                    prefix = {
                        Text("₹ ")
                    },

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Decimal
                        ),

                    singleLine = true
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(

                    text =
                        "${parcel.parcelType} • ${parcel.weight} KG",

                    fontSize =
                        12.sp,

                    color =
                        Color.Gray
                )
            }
        },

        dismissButton = {

            OutlinedButton(

                onClick =
                    onDismiss,

                enabled =
                    !isLoading
            ) {

                Text("Cancel")
            }
        },

        confirmButton = {

            Button(

                enabled =
                    !isLoading &&
                            selectedTrip != null &&
                            fareText.toDoubleOrNull() != null,

                onClick = {

                    val fare =
                        fareText.toDoubleOrNull()

                    if (
                        selectedTrip != null &&
                        fare != null
                    ) {

                        onConfirm(

                            selectedTrip!!.tripId,

                            fare
                        )
                    }
                }
            ) {

                if (isLoading) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(18.dp),

                        strokeWidth =
                            2.dp
                    )

                } else {

                    Text("Confirm")
                }
            }
        }
    )
}


// ============================================================================
// RESCHEDULE PARCEL DIALOG
// ============================================================================

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun RescheduleParcelDialog(
    parcel: Parcel,
    trips: List<Trip>,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (
        String,
        Double
    ) -> Unit
) {

    var selectedTrip by remember {
        mutableStateOf<Trip?>(null)
    }

    var tripExpanded by remember {
        mutableStateOf(false)
    }

    var fareText by remember {

        mutableStateOf(
            parcel.agreedFare
                ?.toString()
                ?: ""
        )
    }

    // ========================================================================
    // UPCOMING TRIPS
    // ========================================================================

    val today =
        LocalDate.now().toString()

    val upcomingTrips =
        trips.filter { trip ->

            trip.date >= today
        }

    // ========================================================================
    // MATCH ROUTE
    // ========================================================================

    val matchingTrips =
        upcomingTrips.filter { trip ->

            val parcelRoute =
                parcel.route?.trim()

            if (
                parcelRoute.isNullOrBlank()
            ) {

                true

            } else {

                trip.route.contains(
                    parcelRoute,
                    ignoreCase = true
                ) ||
                        parcelRoute.contains(
                            trip.route,
                            ignoreCase = true
                        )
            }
        }

    AlertDialog(

        onDismissRequest =
            onDismiss,

        title = {

            Text(
                text =
                    "Reschedule Parcel",

                fontWeight =
                    FontWeight.Bold
            )
        },

        text = {

            Column {

                // =============================================================
                // PARCEL
                // =============================================================

                Text(

                    text =
                        "Parcel: ${shortBmsId(parcel.parcelId)}",

                    fontSize =
                        13.sp,

                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                // =============================================================
                // CURRENT TRIP
                // =============================================================

                Text(

                    text =
                        "Current Trip: ${shortBmsId(parcel.tripId)}",

                    fontSize =
                        12.sp,

                    color =
                        Color.Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                // =============================================================
                // ROUTE
                // =============================================================

                Text(

                    text =
                        "Route: ${parcel.route ?: "-"}",

                    fontSize =
                        12.sp,

                    color =
                        Color.Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                // =============================================================
                // NEW TRIP
                // =============================================================

                Box {

                    OutlinedTextField(

                        value =
                            selectedTrip?.let {

                                "${shortBmsId(it.tripId)} • ${it.date} • ${it.timeSlot}"

                            } ?: "",

                        onValueChange = {},

                        readOnly = true,

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {

                                    tripExpanded =
                                        true
                                },

                        label = {
                            Text("New Upcoming Trip")
                        },

                        trailingIcon = {

                            Text(

                                text =
                                    "▼",

                                modifier =
                                    Modifier.clickable {

                                        tripExpanded =
                                            true
                                    }
                            )
                        }
                    )

                    DropdownMenu(

                        expanded =
                            tripExpanded,

                        onDismissRequest = {

                            tripExpanded =
                                false
                        },

                        modifier =
                            Modifier.fillMaxWidth(
                                0.82f
                            )
                    ) {

                        if (
                            matchingTrips.isEmpty()
                        ) {

                            DropdownMenuItem(

                                text = {

                                    Text(
                                        "No upcoming matching trips found"
                                    )
                                },

                                onClick = {

                                    tripExpanded =
                                        false
                                }
                            )

                        } else {

                            matchingTrips.forEach { trip ->

                                DropdownMenuItem(

                                    text = {

                                        Column {

                                            Text(

                                                text =
                                                    "${shortBmsId(trip.tripId)} • ${trip.date}",

                                                fontWeight =
                                                    FontWeight.Bold
                                            )

                                            Text(

                                                text =
                                                    "${trip.timeSlot} • ${trip.route}",

                                                fontSize =
                                                    12.sp
                                            )

                                            Text(

                                                text =
                                                    "Available seats: ${trip.availableSeats}",

                                                fontSize =
                                                    11.sp,

                                                color =
                                                    Color.Gray
                                            )
                                        }
                                    },

                                    onClick = {

                                        selectedTrip =
                                            trip

                                        tripExpanded =
                                            false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                // =============================================================
                // AGREED FARE
                // =============================================================

                OutlinedTextField(

                    value =
                        fareText,

                    onValueChange = {

                        fareText =
                            it.filter { char ->

                                char.isDigit() ||
                                        char == '.'
                            }
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Agreed Fare")
                    },

                    prefix = {
                        Text("₹ ")
                    },

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Decimal
                        ),

                    singleLine = true
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(

                    text =
                        "You can change the trip and agreed fare.",

                    fontSize =
                        12.sp,

                    color =
                        Color.Gray
                )
            }
        },

        dismissButton = {

            OutlinedButton(

                onClick =
                    onDismiss,

                enabled =
                    !isLoading
            ) {

                Text("Cancel")
            }
        },

        confirmButton = {

            Button(

                enabled =
                    !isLoading &&
                            selectedTrip != null &&
                            fareText.toDoubleOrNull() != null,

                onClick = {

                    val fare =
                        fareText.toDoubleOrNull()

                    if (
                        selectedTrip != null &&
                        fare != null
                    ) {

                        onConfirm(

                            selectedTrip!!.tripId,

                            fare
                        )
                    }
                }
            ) {

                if (isLoading) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(18.dp),

                        strokeWidth =
                            2.dp
                    )

                } else {

                    Text("Reschedule")
                }
            }
        }
    )
}


// ============================================================================
// REJECT PARCEL DIALOG
// ============================================================================

@Composable
private fun RejectParcelDialog(
    rejectionReasons: List<RejectReason>,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onReject: (
        String
    ) -> Unit
) {

    var rejectionReason by remember {
        mutableStateOf<RejectReason?>(null)
    }

    var customReason by remember {
        mutableStateOf("")
    }

    AlertDialog(

        onDismissRequest =
            onDismiss,

        title = {

            Text(
                text =
                    "Reject Parcel",

                fontWeight =
                    FontWeight.Bold
            )
        },

        text = {

            Column {

//                Text(
//                    text =
//                        "Please select a reason:",
//                    fontSize =
//                        14.sp
//                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                // =============================================================
                // CONFIG REASONS
                // =============================================================

                if (rejectionReasons.isEmpty()) {

                    Text(

                        text =
                            "No rejection reasons configured.",

                        color =
                            Color.Gray,

                        fontSize =
                            13.sp
                    )

                } else {

                    rejectionReasons.forEach { reason ->

                        Row(

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        rejectionReason =
                                            reason
                                    }
                                    .padding(
                                        vertical = 2.dp
                                    ),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            androidx.compose.material3.RadioButton(

                                selected =
                                    rejectionReason == reason,

                                onClick = {

                                    rejectionReason =
                                        reason
                                }
                            )

                            Text(

                                text =
                                    reason.name,

                                fontSize =
                                    14.sp
                            )
                        }
                    }
                }

                // =============================================================
                // OTHER
                // =============================================================

                if (
                    rejectionReason?.name
                        ?.equals(
                            "Other",
                            ignoreCase = true
                        ) == true
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    OutlinedTextField(

                        value =
                            customReason,

                        onValueChange = {

                            customReason =
                                it
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text("Enter rejection reason")
                        },

                        placeholder = {
                            Text("Type your reason here")
                        },

                        minLines = 2,

                        maxLines = 4
                    )
                }
            }
        },

        dismissButton = {

            OutlinedButton(

                onClick =
                    onDismiss,

                enabled =
                    !isLoading
            ) {

                Text("Cancel")
            }
        },

        confirmButton = {

            val isOther =
                rejectionReason?.name
                    ?.equals(
                        "Other",
                        ignoreCase = true
                    ) == true

            val canReject =
                rejectionReason != null &&
                        !isLoading &&
                        (
                                !isOther ||
                                        customReason.isNotBlank()
                                )

            Button(

                enabled =
                    canReject,

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFFC62828)
                    ),

                onClick = {

                    val finalReason =
                        if (isOther) {

                            customReason.trim()

                        } else {

                            rejectionReason
                                ?.name
                                ?.trim()
                                ?: ""
                        }

                    if (
                        finalReason.isNotBlank()
                    ) {

                        onReject(
                            finalReason
                        )
                    }
                }
            ) {

                if (isLoading) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(18.dp),

                        strokeWidth =
                            2.dp
                    )

                } else {

                    Text("Reject")
                }
            }
        }
    )
}