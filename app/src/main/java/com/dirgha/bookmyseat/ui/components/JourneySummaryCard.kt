package com.dirgha.bookmyseat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Color.Companion.Blue

@Composable
fun JourneySummaryCard(

    totalTrips: Int,
    upcomingTrips: Int,
    totalSpent: Double,
    pendingTrips: Int,
    rejectedTrips: Int,
    successRate: Int,
    favouriteRoute: String

) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = "Journey Summary",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

//                Text(
//                    text = "🛣",
//                    fontSize = 18.sp
//                )
//
//                Spacer(Modifier.width(6.dp))
//
//                Text(
//                    text = favouriteRoute,
//                    color = Color.Gray,
//                    fontSize = 14.sp
//                )
            }

            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                SummaryItem(
                    "🚖",
                    totalTrips.toString(),
                    "Trips",
                    Blue
                )

                SummaryItem(
                    "📅",
                    upcomingTrips.toString(),
                    "Upcoming",
                    Color(0xFF7C3AED)
                )

                SummaryItem(
                    "⏳",
                    pendingTrips.toString(),
                    "Pending",
                    Color(0xFFF59E0B)
                )

                SummaryItem(
                    "✔",
                    "$successRate%",
                    "Success",
                    Color(0xFF0891B2)
                )
            }
            Spacer(Modifier.height(10.dp))

            HorizontalDivider()

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F9FF)
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "\uD83D\uDEA9",
                            fontSize = 22.sp
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {

                            Text(
                                text = "Most Travelled Route",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            Text(
                                text = favouriteRoute,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Blue
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryBox(
    emoji: String,
    value: String,
    title: String,
    color: Color,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.height(88.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.08f)
        ),
        border = BorderStroke(
            1.dp,
            color.copy(alpha = 0.20f)
        )
    ) {

        Column(
            modifier = Modifier
                //.fillMaxSize()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                text = emoji,
                fontSize = 20.sp
            )

            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = color
            )

            Text(
                text = title,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}
@Composable
fun SummaryItem(
    emoji: String,
    value: String,
    title: String,
    color: Color,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = emoji,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            color = color,
            fontSize = 17.sp
        )

        Text(
            text = title,
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}