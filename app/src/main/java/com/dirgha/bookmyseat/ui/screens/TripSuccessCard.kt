package com.dirgha.bookmyseat.ui.screens

import android.media.RingtoneManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun SuccessScreen(
    message: String,
    title: String = "Success",
    primaryButtonText: String = "Done",
    onPrimaryClick: () -> Unit,
    secondaryButtonText: String? = null,
    onSecondaryClick: (() -> Unit)? = null
) {

    val context = LocalContext.current

    val tickScale = remember {
        Animatable(0.05f)
    }

    val screenAlpha = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {

        launch {
            screenAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(350)
            )
        }

        try {
            val notification =
                RingtoneManager.getDefaultUri(
                    RingtoneManager.TYPE_NOTIFICATION
                )

            RingtoneManager
                .getRingtone(context, notification)
                ?.play()
        } catch (_: Exception) {
        }

        tickScale.animateTo(
            targetValue = 1.35f,
            animationSpec = tween(
                durationMillis = 700,
                easing = FastOutSlowInEasing
            )
        )

        tickScale.animateTo(
            targetValue = 1.10f,
            animationSpec = tween(250)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE4F7E9))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .alpha(screenAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Success Badge

            Box(
                contentAlignment = Alignment.Center
            ) {

                // Outer Glow

                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .background(
                            Color(0xFFD8F2DF),
                            CircleShape
                        )
                )

                // Inner Circle

                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .background(
                            Color(0xFFC6ECCF),
                            CircleShape
                        )
                )

                // Animated Tick

                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF0BAA45),
                    modifier = Modifier
                        .size(120.dp)
                        .scale(tickScale.value)
                )
            }

            Spacer(
                modifier = Modifier.height(42.dp)
            )

            Text(
                text = title,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF07152D)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = message,
                textAlign = TextAlign.Center,
                color = Color(0xFF5B6472),
                fontSize = 17.sp,
                lineHeight = 24.sp
            )

            Spacer(
                modifier = Modifier.height(42.dp)
            )

            Button(
                onClick = onPrimaryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF07152D)
                )
            ) {

                Text(
                    text = primaryButtonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (
                secondaryButtonText != null &&
                onSecondaryClick != null
            ) {

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                OutlinedButton(
                    onClick = onSecondaryClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Text(
                        text = secondaryButtonText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun TripSuccessScreen(
    navController: NavController,
    message: String
) {
    SuccessScreen(
        title = "Trip Created!",
        message = message,
        primaryButtonText = "Back to Dashboard",
        onPrimaryClick = {
            navController.popBackStack(
                route = "admin_home",
                inclusive = false
            )
        },
        secondaryButtonText = "Create Another Trip",
        onSecondaryClick = {
            navController.popBackStack()
        }
    )
}