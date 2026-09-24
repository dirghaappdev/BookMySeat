package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dirgha.bookmyseat.navigation.Screen

@Composable
fun BookingSuccessScreen(
    navController: NavController,
    bookingId: String,
    status: String
) {

    val displayStatus =
        if (
            status.equals(
                "CONFIRMED",
                ignoreCase = true
            )
        ) {
            "PENDING"
        } else {
            status
        }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF4FBF5)
            )
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally

    ) {

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Box(

            modifier = Modifier
                .size(180.dp)
                .background(
                    Color(0xFFE8F7EC),
                    CircleShape
                ),

            contentAlignment =
                Alignment.Center

        ) {

            Icon(

                imageVector =
                    Icons.Outlined.CheckCircle,

                contentDescription = null,

                tint =
                    Color(0xFF09A53B),

                modifier =
                    Modifier.size(100.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Text(
            text = "Request Submitted!",
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(

            text =
                "Your ride request has been successfully submitted. You will receive a confirmation call shortly.",

            textAlign =
                TextAlign.Center,

            color =
                Color.Gray,

            fontSize = 15.sp
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

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
                    1.dp,
                    Color(0xFFE6ECE8)
                )

        ) {

            Column(
                modifier =
                    Modifier.padding(20.dp)
            ) {

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween

                ) {

                    Text(
                        text = "Booking ID",
                        color = Color.Gray
                    )

                    Text(

                        text =
                            if (bookingId.length >= 4)
                                "#BMS${bookingId.takeLast(4)}"
                            else
                                "#BMS$bookingId",

                        fontWeight =
                            FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween

                ) {

                    Text(
                        text = "Status",
                        color = Color.Gray
                    )

                    Text(

                        text = displayStatus,

                        color =
                            if (
                                displayStatus.equals(
                                    "PENDING",
                                    ignoreCase = true
                                )
                            ) {
                                Color(0xFFFF9800)
                            } else {
                                Color(0xFF09A53B)
                            },

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Button(

            onClick = {

                navController.navigate(
                    Screen.BookingSearch.route
                ) {

                    popUpTo(0) {
                        inclusive = true
                    }

                    launchSingleTop = true
                }
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(58.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        Color.Black
                ),

            shape =
                RoundedCornerShape(16.dp)

        ) {

            Text(
                text = "Book Another Ride",
                fontSize = 18.sp
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedButton(

            onClick = {

                navController.navigate(
                    Screen.MemberHome.route
                ) {

                    popUpTo(0) {
                        inclusive = true
                    }

                    launchSingleTop = true
                }
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(58.dp),

            shape =
                RoundedCornerShape(16.dp)

        ) {

            Icon(
                imageVector =
                    Icons.Outlined.Home,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = "Back to Home",
                fontSize = 18.sp
            )
        }
    }
}