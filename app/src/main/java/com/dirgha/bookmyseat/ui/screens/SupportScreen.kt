package com.dirgha.bookmyseat.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.viewmodel.ConfigurationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val vm: ConfigurationViewModel = viewModel()

    LaunchedEffect(Unit) {
        vm.loadConfiguration()
    }

    val response by vm.configuration.collectAsState()

    if (response == null) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    val support = response!!.data.config.support
    val version = response!!.data.config.version

    val mobile1 = support.mobile1
    val mobile2 = support.mobile2
    val email = support.email

    val privacyUrl = support.privacyUrl
    val termsUrl = support.tncUrl
    val playStoreUrl = support.playStoreUrl

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        "Help & Support",
                        fontWeight = FontWeight.Bold
                    )

                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            Icons.Default.ArrowBack,
                            null
                        )

                    }

                }

            )

        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF4F8FD))
                .padding(padding)
                .padding(horizontal = 16.dp)

        ) {

            Spacer(modifier = Modifier.height(18.dp))

            Card(

                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),

                elevation = CardDefaults.cardElevation(0.dp),

                shape = RoundedCornerShape(22.dp),

                modifier = Modifier.fillMaxWidth()

            ) {

                Box(

                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF1976D2),
                                    Color(0xFF42A5F5)
                                )
                            )
                        )
                        .padding(20.dp)

                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Surface(

                            color = Color.White.copy(alpha = .18f),

                            shape = CircleShape,

                            modifier = Modifier.size(58.dp)

                        ) {

                            Box(
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    Icons.Default.SupportAgent,
                                    null,
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )

                            }

                        }

                        Spacer(modifier = Modifier.width(18.dp))

                        Column {

                            Text(
                                "We're here to help",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                "Contact our support team anytime.",
                                color = Color.White.copy(.9f),
                                fontSize = 13.sp
                            )

                        }

                    }

                }

            }

            Spacer(modifier = Modifier.height(22.dp))
            Text(
                "Quick Actions",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            SupportItem(
                icon = Icons.Default.Call,
                title = "Call Support",
                subtitle = mobile1
            ) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:$mobile1")
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            SupportItem(
                icon = Icons.Default.Phone,
                title = "Alternate Number",
                subtitle = mobile2
            ) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:$mobile2")
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            SupportItem(
                icon = Icons.Default.Email,
                title = "Email Support",
                subtitle = email
            ) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_SENDTO,
                        Uri.parse("mailto:$email")
                    )
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                "More",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            SupportItem(
                icon = Icons.Default.PrivacyTip,
                title = "Privacy Policy",
                subtitle = ""
            ) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(privacyUrl)
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            SupportItem(
                icon = Icons.Default.Description,
                title = "Terms & Conditions",
                subtitle = ""
            ) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(termsUrl)
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            SupportItem(
                icon = Icons.Default.Star,
                title = "Rate our App",
                subtitle = ""
            ) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(playStoreUrl)
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            SupportItem(
                icon = Icons.Default.Info,
                title = "App Version",
                subtitle = version.latestVersion
            ) {}

            Spacer(modifier = Modifier.weight(1f))

//            Button(
//
//                onClick = {
//
//                    context.startActivity(
//                        Intent(
//                            Intent.ACTION_DIAL,
//                            Uri.parse("tel:$mobile1")
//                        )
//                    )
//
//                },
//
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(52.dp),
//
//                shape = RoundedCornerShape(14.dp),
//
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF1976D2)
//                )
//
//            ) {
//
//                Icon(
//                    Icons.Default.SupportAgent,
//                    null
//                )
//
//                Spacer(modifier = Modifier.width(8.dp))
//
//                Text(
//                    "Contact Support",
//                    fontWeight = FontWeight.Bold
//                )
//
//            }

            Spacer(modifier = Modifier.height(18.dp))

        }

    }

}
@Composable
fun SupportItem(

    icon: androidx.compose.ui.graphics.vector.ImageVector,

    title: String,

    subtitle: String,

    onClick: () -> Unit

) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )

    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

            Box(

                modifier = Modifier
                    .size(44.dp)
                    .background(
                        Color(0xFFE8F1FF),
                        CircleShape
                    ),

                contentAlignment = Alignment.Center

            ) {

                Icon(

                    imageVector = icon,

                    contentDescription = null,

                    tint = Color(0xFF1976D2),

                    modifier = Modifier.size(22.dp)

                )

            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(

                    text = title,

                    fontSize = 15.sp,

                    fontWeight = FontWeight.SemiBold,

                    color = Color(0xFF1F2937)

                )

                if (subtitle.isNotEmpty()) {

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(

                        text = subtitle,

                        fontSize = 12.sp,

                        color = Color(0xFF64748B)

                    )

                }

            }

            Icon(

                imageVector = Icons.Default.KeyboardArrowRight,

                contentDescription = null,

                tint = Color(0xFF94A3B8)

            )

        }

    }

}