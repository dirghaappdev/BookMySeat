package com.dirgha.bookmyseat.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dirgha.bookmyseat.data.config.BookingRule
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.ui.viewmodel.ParcelViewModel
import com.dirgha.bookmyseat.utils.ConfigManager
import com.dirgha.bookmyseat.utils.InternetManager

@Composable
fun ParcelReviewScreen(
    parcelViewModel: ParcelViewModel,
    navController: NavController
) {

    val context = LocalContext.current

    // ============================================================
    // VIEWMODEL STATE
    // ============================================================

    val selectedRoute by
    parcelViewModel.selectedRoute.collectAsState()

    val selectedDirection by
    parcelViewModel.selectedDirection.collectAsState()

    val parcelType by
    parcelViewModel.parcelType.collectAsState()

    val selectedCategoryId by
    parcelViewModel.selectedCategoryId.collectAsState()

    val expectedDate by
    parcelViewModel.expectedDate.collectAsState()

    val selectedFare by
    parcelViewModel.selectedFare.collectAsState()

    val categories by
    parcelViewModel.weightCategories.collectAsState()

    val isLoading by
    parcelViewModel.isLoading.collectAsState()

    val bookingResponse by
    parcelViewModel.bookingResponse.collectAsState()

    val error by
    parcelViewModel.error.collectAsState()

    // ============================================================
    // REVIEW STATE
    // ============================================================

    var note by remember {
        mutableStateOf("")
    }

    var agreeRules by remember {
        mutableStateOf(false)
    }

    var bookingRules by remember {
        mutableStateOf(
            emptyList<
                    BookingRule
                    >()
        )
    }
    val contactPersonName by
    parcelViewModel.contactPersonName.collectAsState()

    val contactPersonPhone by
    parcelViewModel.contactPersonPhone.collectAsState()
    
    // ============================================================
    // LOAD RULES
    // ============================================================

    LaunchedEffect(Unit) {

        val config =
            ConfigManager(context).getConfiguration()

        bookingRules =
            config
                ?.data
                ?.masters
                ?.bookingRules
                ?.filter {
                    it.is_active
                }
                ?: emptyList()
    }

    // ============================================================
    // SELECTED CATEGORY
    // ============================================================

    val selectedCategory =
        categories.firstOrNull {
            it.weightCategoryId ==
                    selectedCategoryId
        }

    // ============================================================
    // SUCCESS
    // ============================================================

    LaunchedEffect(bookingResponse) {

        bookingResponse?.let { response ->

            navController.navigate(
                Screen.ParcelSuccess.createRoute(
                    response.parcelId
                )
            ) {
                popUpTo(
                    Screen.ParcelBookingScreen.route
                ) {
                    inclusive = true
                }
            }

            parcelViewModel.clearBookingResponse()
        }
    }

    // ============================================================
    // SCREEN
    // ============================================================

    Scaffold(

        containerColor =
            Color(0xFFF7F7F7),

        // ========================================================
        // TOP BAR
        // ========================================================

        topBar = {

            Surface(
                shadowElevation = 3.dp,
                color = MaterialTheme.colorScheme.primary
            ) {

                Text(
                    text = "Review Your Parcel",

                    fontSize = 24.sp,

                    fontWeight = FontWeight.Bold,

                    color =
                        MaterialTheme.colorScheme.onPrimary,

                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 8.dp
                        )
                )
            }
        },

        // ========================================================
        // BOTTOM BAR
        // ========================================================

        bottomBar = {

            Surface(
                shadowElevation = 8.dp,
                color = Color.White
            ) {

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 10.dp
                        )
                ) {

                    // ====================================================
                    // BACK
                    // ====================================================

                    OutlinedButton(

                        onClick = {
                            navController.popBackStack()
                        },

                        shape =
                            RoundedCornerShape(10.dp),

                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),

                        enabled = !isLoading

                    ) {

                        Text(
                            text = "Back",
                            fontSize = 16.sp
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.width(12.dp)
                    )

                    // ====================================================
                    // CONFIRM
                    // ====================================================

                    Button(

                        onClick = {

                            if (
                                !InternetManager
                                    .requireInternet(context)
                            ) {

                                Toast.makeText(
                                    context,
                                    "No Internet Connection",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@Button
                            }

                            if (
                                selectedRoute == null
                            ) {

                                Toast.makeText(
                                    context,
                                    "Route information is missing",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@Button
                            }

                            if (
                                selectedFare == null
                            ) {

                                Toast.makeText(
                                    context,
                                    "Parcel fare is not available",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@Button
                            }

                            if (
                                selectedCategory == null
                            ) {

                                Toast.makeText(
                                    context,
                                    "Weight category is not available",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@Button
                            }

                            // ====================================================
                            // SAVE COMPLETE DRAFT
                            // ====================================================

                            parcelViewModel.saveParcelDraft(

                                route =
                                    selectedRoute!!,

                                direction =
                                    selectedDirection,

                                parcelType =
                                    parcelType.trim(),

                                categoryId =
                                    selectedCategoryId,

                                expectedDate =
                                    expectedDate,

                                note =
                                    note.trim(),

                                fare =
                                    selectedFare!!,
                                contactPersonName =
                                    contactPersonName,

                                contactPersonPhone =
                                    contactPersonPhone
                            )

                            // ====================================================
                            // CREATE PARCEL
                            // ====================================================

                            parcelViewModel.createParcel()
                        },

                        shape =
                            RoundedCornerShape(10.dp),

                        modifier = Modifier
                            .weight(2f)
                            .height(50.dp),

                        enabled =
                            agreeRules &&
                                    !isLoading &&
                                    selectedRoute != null &&
                                    selectedFare != null &&
                                    selectedCategory != null,

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color.Black
                            )
                    ) {

                        if (isLoading) {

                            CircularProgressIndicator(
                                modifier =
                                    Modifier.size(22.dp),

                                color =
                                    Color.White,

                                strokeWidth =
                                    2.dp
                            )

                        } else {

                            Text(
                                text =
                                    "Confirm Parcel",

                                fontSize =
                                    17.sp,

                                fontWeight =
                                    FontWeight.SemiBold
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(6.dp)
                            )

                            Icon(
                                imageVector =
                                    Icons.AutoMirrored.Filled.ArrowForward,

                                contentDescription =
                                    null
                            )
                        }
                    }
                }
            }
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF7F7F7)
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(paddingValues)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {

            // ====================================================
            // SUMMARY
            // ====================================================

            ParcelSummaryCard(

                route =
                    selectedRoute?.let {

                        if (
                            selectedDirection == "UP"
                        ) {
                            it.up
                        } else {
                            it.down
                        }

                    } ?: "",

                direction =
                    selectedDirection,

                date =
                    expectedDate,

                parcelType =
                    parcelType
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            // ====================================================
            // FARE CARD
            // ====================================================

            selectedCategory?.let { category ->

                selectedFare?.let { fare ->

                    Card(

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(18.dp),

                        border =
                            BorderStroke(
                                1.dp,
                                Color(0xFFE2E2E2)
                            ),

                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    Color.White
                            )
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(14.dp)
                        ) {

                            FaresItem(
                                title =
                                    "Weight Category",

                                value =
                                    category.categoryName
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            FaresItem(
                                title =
                                    "Weight",

                                value =
                                    "${category.minWeight} KG"
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            HorizontalDivider(
                                color =
                                    Color(0xFFE5E5E5)
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            FaresItem(
                                title =
                                    "Estimated Fare",

                                value =
                                    "₹${fare.minFare} - ₹${fare.maxFare}"
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            // ====================================================
            // NOTE
            // ====================================================

            OutlinedTextField(

                value =
                    note,

                onValueChange = {
                    note = it
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true,

                placeholder = {

                    Text(
                        text =
                            "Add a note (optional)",

                        fontSize =
                            15.sp
                    )
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Outlined.LocalShipping,

                        contentDescription =
                            null,

                        tint =
                            Color.Gray
                    )
                },

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    OutlinedTextFieldDefaults.colors(

                        focusedContainerColor =
                            Color.White,

                        unfocusedContainerColor =
                            Color.White,

                        focusedBorderColor =
                            Color(0xFFE0E0E0),

                        unfocusedBorderColor =
                            Color(0xFFE0E0E0)
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            // ====================================================
            // BOOKING RULES
            // ====================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color(0xFFFFF9E8)
                    ),

                border =
                    BorderStroke(
                        1.dp,
                        Color(0xFFFFC107)
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(10.dp)
                ) {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector =
                                Icons.Outlined.Info,

                            contentDescription =
                                null,

                            tint =
                                Color(0xFFE68A00)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Text(
                            text =
                                "Booking Rules",

                            color =
                                Color(0xFFE68A00),

                            fontWeight =
                                FontWeight.Bold,

                            fontSize =
                                17.sp
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    bookingRules.forEach { rule ->

                        BookingRuleItem(
                            number =
                                rule.id,

                            text =
                                rule.message
                        )
                    }

                    HorizontalDivider(
                        modifier =
                            Modifier.padding(
                                vertical = 5.dp
                            )
                    )

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Checkbox(

                            checked =
                                agreeRules,

                            onCheckedChange = {
                                agreeRules = it
                            }
                        )

                        Spacer(
                            modifier =
                                Modifier.width(4.dp)
                        )

                        Text(
                            text =
                                "I have read and agree to all booking rules.",

                            fontSize = 15.sp
                        )
                    }
                }
            }

            // ====================================================
            // ERROR
            // ====================================================

            error?.let { message ->

                if (message.isNotBlank()) {

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            message,

                        color =
                            MaterialTheme.colorScheme.error,

                        fontSize =
                            14.sp,

                        modifier =
                            Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )
        }
    }
}

// ================================================================
// PARCEL SUMMARY CARD
// ================================================================

@Composable
private fun ParcelSummaryCard(
    route: String,
    direction: String,
    date: String,
    parcelType: String
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        border =
            BorderStroke(
                1.dp,
                Color(0xFFDADADA)
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFF2962FF)
            )
    ) {

        Column {

            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 7.dp
                        ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "📦",
                    fontSize = 20.sp
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                // ONLY ROUTE — NO UP/DOWN
                Text(
                    text =
                        route.ifBlank {
                            "Route"
                        },

                    color =
                        Color.White,

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        16.sp
                )
            }

            Card(

                shape =
                    RoundedCornerShape(
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    ),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color(0xFFF8F8F8)
                    )
            ) {

                Row(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 10.dp
                            )
                ) {

                    ParcelSummaryItem(
                        modifier =
                            Modifier.weight(1f),

                        title =
                            "DATE",

                        value =
                            date
                    )

                    ParcelVerticalDivider()

                    ParcelSummaryItem(
                        modifier =
                            Modifier.weight(1f),

                        title =
                            "TYPE",

                        value =
                            parcelType
                    )
                }
            }
        }
    }
}

// ================================================================
// SUMMARY ITEM
// ================================================================

@Composable
private fun ParcelSummaryItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {

    Column(

        modifier = modifier,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text =
                title,

            color =
                Color.Gray,

            fontSize =
                13.sp
        )

        Spacer(
            modifier =
                Modifier.height(3.dp)
        )

        Text(
            text =
                value.ifBlank {
                    "-"
                },

            fontWeight =
                FontWeight.Bold,

            fontSize =
                12.sp,

            textAlign =
                TextAlign.Center,

            maxLines = 2
        )
    }
}

// ================================================================
// FARES ITEM
// ================================================================

@Composable
private fun FaresItem(
    title: String,
    value: String
) {

    Row(

        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween,

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text =
                title,

            color =
                Color.Gray,

            fontSize =
                14.sp
        )

        Text(
            text =
                value,

            color =
                Color.Gray,

            fontSize =
                14.sp,

            fontWeight =
                FontWeight.Medium,

            textAlign =
                TextAlign.End
        )
    }
}

// ================================================================
// VERTICAL DIVIDER
// ================================================================

@Composable
private fun ParcelVerticalDivider() {

    Box(

        modifier =
            Modifier
                .width(1.dp)
                .height(45.dp)
                .background(
                    Color(0xFFE0E0E0)
                )
    )
}

// ================================================================
// BOOKING RULE ITEM
// ================================================================

@Composable
private fun BookingRuleItem(
    number: Int,
    text: String
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 3.dp
                ),

        verticalAlignment =
            Alignment.Top
    ) {

        Box(

            modifier =
                Modifier
                    .size(22.dp)
                    .background(
                        Color(0xFFFFC107),
                        CircleShape
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            Text(

                text =
                    number.toString(),

                fontSize =
                    12.sp,

                color =
                    Color.White,

                fontWeight =
                    FontWeight.Bold,

                textAlign =
                    TextAlign.Center
            )
        }

        Spacer(
            modifier =
                Modifier.width(5.dp)
        )

        Text(

            text =
                text,

            fontSize =
                13.sp,

            color =
                Color(0xFF6B4E00),

            modifier =
                Modifier.weight(1f)
        )
    }
}