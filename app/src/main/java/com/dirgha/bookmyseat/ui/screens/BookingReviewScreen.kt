package com.dirgha.bookmyseat.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.viewmodel.BookingViewModel
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.utils.ConfigManager
import com.dirgha.bookmyseat.utils.InternetManager

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
){

    val bookingViewModel: BookingViewModel = viewModel()
    val bookingMessage by bookingViewModel.message.collectAsState()
    val total = totalFare
    var note by remember { mutableStateOf("") }
    var agreeRules by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var parcelType by remember {
        mutableStateOf("Documents")
    }

    var parcelWeight by remember {
        mutableStateOf("")
    }

    var parcelExpanded by remember {
        mutableStateOf(false)
    }
    var parcelCount by remember {
        mutableStateOf(passengerCount)
    }
    val parcelTypes = listOf(
        "Documents",
        "Box",
        "Bag",
        "Electronics",
        "Food",
        "Other"
    )
    var bookingRules by remember {
        mutableStateOf(emptyList<com.dirgha.bookmyseat.data.config.BookingRule>())
    }
    LaunchedEffect(Unit) {

        val config = ConfigManager(context).getConfiguration()

        bookingRules = config?.data?.masters?.bookingRules
            ?.filter { it.is_active }
            ?: emptyList()
    }
    Scaffold(
        containerColor = Color(0xFFF7F7F7),

        topBar = {
            Surface(
                shadowElevation = 3.dp,
                color =MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "Book Your Ride",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
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

                    Spacer(Modifier.width(12.dp))

                    val context = LocalContext.current

                    Button(
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(2f)
                            .height(48.dp),
                        enabled = agreeRules,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        onClick = {
                            if (!InternetManager.requireInternet(context)) {
                                Toast.makeText(
                                    context,
                                    "No Internet Connection",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                            Toast.makeText(
                                context,
                                "Submit clicked",
                                Toast.LENGTH_SHORT
                            ).show()
                            println("SUBMIT STARTED")
                            bookingViewModel.createBooking(

                                token = "Bearer ${SessionManager.token}",

                                tripId = tripId,

                                // Ride
                                passengerCount =
                                    if (bookingType == "RIDE")
                                        passengerCount
                                    else
                                        0,

                                gender = "",

                                // Parcel
                                bookingType = bookingType,

                                parcelCount =
                                    if (bookingType == "PARCEL")
                                        parcelCount
                                    else
                                        0,

                                parcelType =
                                    if (bookingType == "PARCEL")
                                        parcelType
                                    else
                                        "",

                                parcelWeight =
                                    parcelWeight,

                                note = note

                            ) {

                                navController.navigate(
                                    "booking_success/$tripId/Confirmed"
                                ) {
                                    popUpTo("booking_search")
                                }
                            }
                            println("CREATE BOOKING CALLED")
                            println("Trip ID: $tripId")
                            println("Booking Type: $bookingType")
                            println("Passenger Count: $passengerCount")
                            println("Parcel Count: $parcelCount")
                            println("Parcel Type: $parcelType")
                            println("Parcel Weight: $parcelWeight")
                        }
                    ) {
                        Text("Submit")
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            null
                        )
                    }
                }
            }
        }
    ) { padding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .verticalScroll(rememberScrollState())
            .padding(padding)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {

        TripSummaryCard(
            route = route,
            date = date,
            time = time,
            quantity = passengerCount,
            bookingType = bookingType
        )

        Spacer(Modifier.height(10.dp))

        FareBreakdownCard(
            bookingType = bookingType,
            quantity =
                if (bookingType == "RIDE")
                    passengerCount
                else
                    parcelCount,

            totalFare = totalFare
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = note,
            onValueChange = {
                note = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = {
                Text("Add luggage / special notes (optional)",fontSize = 13.sp)

            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Luggage,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFFE0E0E0),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            )
        )


        Spacer(Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF9E8)
            ),
            border = BorderStroke(
                1.dp,
                Color(0xFFFFC107)
            )
        ) {

            Column(
                Modifier.padding(8.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Outlined.Info,
                        null,
                        tint = Color(0xFFE68A00)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "Booking Rules",
                        color = Color(0xFFE68A00),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(4.dp))

                bookingRules.forEach { rule ->

                    BookingRuleItem(
                        number = rule.id,
                        text = rule.message
                    )

                }

                Divider(
                    Modifier.padding(vertical = 6.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = agreeRules,
                        onCheckedChange = {
                            agreeRules = it
                        }
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        "I have read and agree to all booking rules." ,
                        fontSize = 13.sp
                    )
                }
            }
        }

//        Spacer(Modifier.height(20.dp))
//
//        Row {
//
//            OutlinedButton(
//                onClick = {
//                    navController.popBackStack()
//                },
//                modifier = Modifier.weight(1f).height(50.dp),
//            ) {
//
//                Text("Back")
//            }
//
//            Spacer(Modifier.width(12.dp))
//            val context = LocalContext.current
//            Button(
//                modifier = Modifier.weight(3f).height(50.dp),
//                enabled = agreeRules,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Black
//                ),
//                onClick = {
//                    if (!InternetManager.requireInternet(context)) {
//
//                        Toast.makeText(
//                            context,
//                            "No Internet Connection",
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        return@Button
//                    }
//
//                    bookingViewModel.createBooking(
//                        token = "Bearer ${SessionManager.token}",
//                        tripId = tripId,
//                        passengerCount = passengerCount,
//                        gender = "",
//                        note = note
//                    ) {
//
//                        navController.navigate(
//                            "booking_success/" +
//                                    tripId +
//                                    "/" +
//                                    "Confirmed"
//                        ) {
//                            popUpTo("booking_search") {
//                                inclusive = false
//                            }
//                        }
//                    }
//
//                }
//            ) {
//
//                Text("Confirm Booking")
//
//                Spacer(Modifier.width(8.dp))
//
//                Icon(
//                    Icons.AutoMirrored.Filled.ArrowForward,
//                    null
//                )
//            }
//        }
//
//        Spacer(Modifier.height(20.dp))
//
//        if (bookingMessage.isNotEmpty()) {
//
//            Text(
//                bookingMessage,
//                color = Color.Gray
//            )
//        }
//
//        Spacer(Modifier.height(30.dp))
    }
}}

@Composable
private fun TripSummaryCard(
    route: String,
    date: String,
    time: String,
    quantity: Int,
    bookingType: String
){

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFDADADA)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2962FF)
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
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "📍",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

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
                    containerColor = Color(0xFFF8F8F8)
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {

                    SummaryItem(
                        modifier = Modifier.weight(1f),
                        title = "DATE",
                        value = date
                    )

                    VerticalDivider()

                    SummaryItem(
                        modifier = Modifier.weight(1f),
                        title = "TIME",
                        value = time
                    )

                    VerticalDivider()

                    SummaryItem(
                        modifier = Modifier.weight(1f),
                        title =
                            if (bookingType == "RIDE")
                                "PAX"
                            else
                                "PKG",

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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = title,
            color = Color.Gray,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

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
            .background(Color(0xFFE0E0E0))
    )
}

@Composable
private fun FareBreakdownCard(
    bookingType: String,
    quantity: Int,
    totalFare: Double
) {

    val farePerUnit = totalFare
    val total = totalFare * quantity

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

                if (bookingType == "RIDE")
                    "Passengers"
                else
                    "Parcels",

                quantity.toString()
            )

            Spacer(modifier = Modifier.height(6.dp))

            FareItem(

                if (bookingType == "RIDE")
                    "Fare / Passenger"
                else
                    "Fare / Parcel",

                "₹$farePerUnit"
            )

            Divider(
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    "Total Fare",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Text(
                    "₹$total",
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
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            title,
            color = Color.Gray
        )

        Text(
            value,
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
        verticalAlignment = Alignment.Top
    ) {

        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    Color(0xFFFFC107),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
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

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFF6B4E00),
            modifier = Modifier.weight(1f)
        )
    }
}