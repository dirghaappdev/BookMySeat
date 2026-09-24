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
import androidx.compose.material.icons.outlined.Luggage
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.utils.ConfigManager
import com.dirgha.bookmyseat.utils.InternetManager
import com.dirgha.bookmyseat.viewmodel.BookingViewModel

@Composable
fun BookingReviewScreen(
    navController: NavController,
    tripId: String,
    route: String,
    date: String,
    time: String,
    passengerCount: Int,
    totalFare: Double,
    bookingType: String
) {

    val bookingViewModel: BookingViewModel = viewModel()

    val bookingMessage by bookingViewModel.message.collectAsState()

    var note by remember {
        mutableStateOf("")
    }

    var agreeRules by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    var parcelType by remember {
        mutableStateOf("Documents")
    }

    var parcelWeight by remember {
        mutableStateOf("")
    }

    var parcelCount by remember {
        mutableStateOf(passengerCount)
    }

    var bookingRules by remember {
        mutableStateOf(
            emptyList<com.dirgha.bookmyseat.data.config.BookingRule>()
        )
    }

    LaunchedEffect(Unit) {

        val config =
            ConfigManager(context).getConfiguration()

        bookingRules =
            config?.data?.masters?.bookingRules
                ?.filter { it.is_active }
                ?: emptyList()
    }

    Scaffold(

        containerColor = Color(0xFFF7F7F7),

        topBar = {

            Surface(
                shadowElevation = 3.dp,
                color = MaterialTheme.colorScheme.primary
            ) {

                Text(
                    text = "Book Your Ride",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 5.dp
                        )
                )
            }
        },

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
                            vertical = 12.dp
                        )
                ) {

                    OutlinedButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {

                        Text("Back")
                    }

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Button(

                        onClick = {

                            if (!InternetManager.requireInternet(context)) {

                                Toast.makeText(
                                    context,
                                    "No Internet Connection",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@Button
                            }

                            println("================================")
                            println("SUBMIT BUTTON CLICKED")
                            println("TRIP ID = $tripId")
                            println("BOOKING TYPE = $bookingType")
                            println("================================")

                            bookingViewModel.createBooking(

                                token =
                                    "Bearer ${SessionManager.token}",

                                tripId = tripId,

                                passengerCount =
                                    if (
                                        bookingType.equals(
                                            "RIDE",
                                            ignoreCase = true
                                        )
                                    ) {
                                        passengerCount
                                    } else {
                                        0
                                    },

                                gender = "",

                                bookingType = bookingType,

                                parcelCount =
                                    if (
                                        bookingType.equals(
                                            "PARCEL",
                                            ignoreCase = true
                                        )
                                    ) {
                                        parcelCount
                                    } else {
                                        0
                                    },

                                parcelType =
                                    if (
                                        bookingType.equals(
                                            "PARCEL",
                                            ignoreCase = true
                                        )
                                    ) {
                                        parcelType
                                    } else {
                                        ""
                                    },

                                parcelWeight = parcelWeight,

                                note = note

                            ) {

                                /*
                                 * IMPORTANT:
                                 *
                                 * Do NOT use popUpTo("booking_search")
                                 * here.
                                 *
                                 * The booking_success route is already
                                 * registered in your NavHost.
                                 */

                                println("================================")
                                println("NAVIGATION CALLBACK EXECUTED")
                                println("NAVIGATING TO SUCCESS SCREEN")
                                println("TRIP ID = $tripId")
                                println("================================")

                                try {

                                    navController.navigate(
                                        "booking_success/$tripId/Confirmed"
                                    )

                                    println(
                                        "SUCCESS SCREEN NAVIGATION CALLED"
                                    )

                                } catch (e: Exception) {

                                    println(
                                        "NAVIGATION ERROR = ${e.message}"
                                    )

                                    e.printStackTrace()

                                    Toast.makeText(
                                        context,
                                        "Navigation error: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        },

                        enabled = agreeRules,

                        shape = RoundedCornerShape(10.dp),

                        modifier = Modifier
                            .weight(2f)
                            .height(48.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        )

                    ) {

                        Text("Submit")

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
                        )
                    }
                }
            }
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF7F7F7)
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(padding)
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                )
        ) {

            TripSummaryCard(
                route = route,
                date = date,
                time = time,
                quantity = passengerCount,
                bookingType = bookingType
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            FareBreakdownCard(
                bookingType = bookingType,

                quantity =
                    if (
                        bookingType.equals(
                            "RIDE",
                            ignoreCase = true
                        )
                    ) {
                        passengerCount
                    } else {
                        parcelCount
                    },

                totalFare = totalFare
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedTextField(

                value = note,

                onValueChange = {
                    note = it
                },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true,

                placeholder = {
                    Text(
                        text =
                            "Add luggage / special notes (optional)",
                        fontSize = 13.sp
                    )
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Outlined.Luggage,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },

                shape = RoundedCornerShape(20.dp),

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
                modifier = Modifier.height(12.dp)
            )

            Card(

                shape = RoundedCornerShape(18.dp),

                colors = CardDefaults.cardColors(
                    containerColor =
                        Color(0xFFFFF9E8)
                ),

                border = BorderStroke(
                    1.dp,
                    Color(0xFFFFC107)
                )

            ) {

                Column(
                    modifier = Modifier.padding(8.dp)
                ) {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector =
                                Icons.Outlined.Info,
                            contentDescription = null,
                            tint = Color(0xFFE68A00)
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = "Booking Rules",
                            color = Color(0xFFE68A00),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    bookingRules.forEach { rule ->

                        BookingRuleItem(
                            number = rule.id,
                            text = rule.message
                        )
                    }

                    Divider(
                        modifier =
                            Modifier.padding(
                                vertical = 6.dp
                            )
                    )

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Checkbox(

                            checked = agreeRules,

                            onCheckedChange = {
                                agreeRules = it
                            }
                        )

                        Spacer(
                            modifier = Modifier.width(4.dp)
                        )

                        Text(
                            text =
                                "I have read and agree to all booking rules.",
                            fontSize = 13.sp
                        )
                    }
                }
            }

            /*
             * Show API error/message only when there is one.
             */

            if (bookingMessage.isNotEmpty()) {

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = bookingMessage,
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}


@Composable
private fun TripSummaryCard(
    route: String,
    date: String,
    time: String,
    quantity: Int,
    bookingType: String
) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        border = BorderStroke(
            1.dp,
            Color(0xFFDADADA)
        ),

        colors = CardDefaults.cardColors(
            containerColor =
                Color(0xFF2962FF)
        )

    ) {

        Column {

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically

            ) {

                Text(
                    text = "📍",
                    fontSize = 18.sp
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = route,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Card(

                shape = RoundedCornerShape(
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                ),

                colors = CardDefaults.cardColors(
                    containerColor =
                        Color(0xFFF8F8F8)
                )

            ) {

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 16.dp
                        )

                ) {

                    SummaryItem(
                        modifier =
                            Modifier.weight(1f),
                        title = "DATE",
                        value = date
                    )

                    VerticalDivider()

                    SummaryItem(
                        modifier =
                            Modifier.weight(1f),
                        title = "TIME",
                        value = time
                    )

                    VerticalDivider()

                    SummaryItem(
                        modifier =
                            Modifier.weight(1f),
                        title =
                            if (
                                bookingType.equals(
                                    "RIDE",
                                    ignoreCase = true
                                )
                            ) {
                                "PAX"
                            } else {
                                "PKG"
                            },
                        value = quantity.toString()
                    )
                }
            }
        }
    }
}


@Composable
private fun SummaryItem(
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
            text = title,
            color = Color.Gray,
            fontSize = 11.sp
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}


@Composable
private fun VerticalDivider() {

    Box(

        modifier = Modifier
            .width(1.dp)
            .height(45.dp)
            .background(
                Color(0xFFE0E0E0)
            )
    )
}


@Composable
private fun FareBreakdownCard(
    bookingType: String,
    quantity: Int,
    totalFare: Double
) {

    /*
     * IMPORTANT:
     *
     * totalFare coming into this screen is already
     * the fare for one unit from your existing flow.
     */

    val farePerUnit = totalFare

    val total =
        totalFare * quantity

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(18.dp),

        border = BorderStroke(
            1.dp,
            Color(0xFFE2E2E2)
        ),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {

        Column(

            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )

        ) {

            FareItem(

                title =
                    if (
                        bookingType.equals(
                            "RIDE",
                            ignoreCase = true
                        )
                    ) {
                        "Passengers"
                    } else {
                        "Parcels"
                    },

                value =
                    quantity.toString()
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            FareItem(

                title =
                    if (
                        bookingType.equals(
                            "RIDE",
                            ignoreCase = true
                        )
                    ) {
                        "Fare / Passenger"
                    } else {
                        "Fare / Parcel"
                    },

                value =
                    "₹$farePerUnit"
            )

            Divider(
                modifier =
                    Modifier.padding(
                        vertical = 12.dp
                    )
            )

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween

            ) {

                Text(
                    text = "Total Fare",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Text(
                    text = "₹$total",
                    color = Color(0xFF2962FF),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}


@Composable
fun FareItem(
    title: String,
    value: String
) {

    Row(

        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween

    ) {

        Text(
            text = title,
            color = Color.Gray
        )

        Text(
            text = value,
            color = Color.Gray
        )
    }
}


@Composable
private fun BookingRuleItem(
    number: Int,
    text: String
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),

        verticalAlignment =
            Alignment.Top

    ) {

        Box(

            modifier = Modifier
                .size(20.dp)
                .background(
                    Color(0xFFFFC107),
                    CircleShape
                ),

            contentAlignment =
                Alignment.Center

        ) {

            Text(
                text = number.toString(),
                fontSize = 11.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 11.sp
            )
        }

        Spacer(
            modifier = Modifier.width(4.dp)
        )

        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFF6B4E00),
            modifier = Modifier.weight(1f)
        )
    }
}