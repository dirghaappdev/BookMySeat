package com.dirgha.bookmyseat.ui.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.R
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.ui.components.UpdateDialog
import com.dirgha.bookmyseat.utils.AppUtils
import com.dirgha.bookmyseat.utils.Constants
import com.dirgha.bookmyseat.utils.VersionUtils
import com.dirgha.bookmyseat.viewmodel.ConfigurationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val viewModel: ConfigurationViewModel =
        viewModel()

    val configuration by
    viewModel.configuration.collectAsState()

    val loading by
    viewModel.loading.collectAsState()

    var showSoftUpdate by remember {
        mutableStateOf(false)
    }

    var showForceUpdate by remember {
        mutableStateOf(false)
    }

    var hasNavigated by remember {
        mutableStateOf(false)
    }

    Log.d(
        "ENV_TEST",
        "BASE_URL = ${Constants.BASE_URL}"
    )

    fun navigateToNextScreen() {

        if (hasNavigated) return

        hasNavigated = true

        SessionManager.load(context)

        when {

            SessionManager.token.isBlank() -> {

                navController.navigate(
                    Screen.Login.route
                ) {
                    popUpTo(
                        Screen.Splash.route
                    ) {
                        inclusive = true
                    }
                }
            }

            SessionManager.role.equals(
                "ADMIN",
                true
            ) -> {

                navController.navigate(
                    "admin_home"
                ) {
                    popUpTo(
                        Screen.Splash.route
                    ) {
                        inclusive = true
                    }
                }
            }

            else -> {

                navController.navigate(
                    "member_home"
                ) {
                    popUpTo(
                        Screen.Splash.route
                    ) {
                        inclusive = true
                    }
                }
            }
        }
    }

    fun openPlayStore(url: String) {

        try {

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )

            context.startActivity(intent)

        } catch (e: Exception) {

            Log.e(
                "UPDATE",
                "Unable to open Play Store",
                e
            )
        }
    }

    /*
     * Load remote configuration
     */
    LaunchedEffect(Unit) {

        Log.d(
            "CONFIG",
            "Starting configuration loading"
        )

        viewModel.loadConfiguration()
    }

    /*
     * Process configuration
     */
    LaunchedEffect(configuration) {

        configuration?.let { config ->

            val installedVersion =
                AppUtils.getAppVersion(context)

            val latestResult =
                VersionUtils.compareVersions(
                    installedVersion,
                    config.data.config.version.latestVersion
                )

            val minimumResult =
                VersionUtils.compareVersions(
                    installedVersion,
                    config.data.config.version.minimumSupportedVersion
                )

            Log.d(
                "UPDATE",
                "Installed = $installedVersion"
            )

            Log.d(
                "UPDATE",
                "Latest = ${config.data.config.version.latestVersion}"
            )

            Log.d(
                "UPDATE",
                "Minimum = ${config.data.config.version.minimumSupportedVersion}"
            )

            when {

                minimumResult == -1 -> {

                    Log.d(
                        "UPDATE",
                        "Force update required"
                    )

                    showForceUpdate = true
                }

                latestResult == -1 &&
                        config.data.config.version.softUpdate -> {

                    Log.d(
                        "UPDATE",
                        "Soft update available"
                    )

                    showSoftUpdate = true
                }

                else -> {

                    delay(1500)

                    navigateToNextScreen()
                }
            }
        }
    }

    /*
     * If configuration API fails and
     * there is no cached config,
     * don't keep splash forever.
     */
    LaunchedEffect(loading) {

        if (!loading &&
            configuration == null
        ) {

            delay(2000)

            navigateToNextScreen()
        }
    }

    /*
     * Splash animations
     */
    val taxiScale =
        remember {
            Animatable(0.01f)
        }

    val textAlpha =
        remember {
            Animatable(0f)
        }

    LaunchedEffect(Unit) {

        launch {

            taxiScale.animateTo(
                targetValue = 1.55f,
                animationSpec = tween(
                    durationMillis = 1500,
                    easing = FastOutSlowInEasing
                )
            )

            taxiScale.animateTo(
                targetValue = 1.15f,
                animationSpec = tween(
                    durationMillis = 250
                )
            )
        }

        delay(1000)

        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 700
            )
        )
    }

    /*
     * SPLASH UI
     */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D47A1),
                        Color(0xFF1976D2),
                        Color(0xFF42A5F5)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {

            Image(
                painter =
                    painterResource(
                        R.drawable.bms_splashlogo
                    ),
                contentDescription = "Taxi",
                modifier = Modifier
                    .scale(
                        taxiScale.value
                    )
                    .size(200.dp)
            )

            Spacer(
                modifier =
                    Modifier.height(32.dp)
            )

            Text(
                text = "DailyCabs",
                modifier =
                    Modifier.alpha(
                        textAlpha.value
                    ),
                color = Color.White,
                fontSize = 34.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Intercity Shared Travel",
                modifier =
                    Modifier.alpha(
                        textAlpha.value
                    ),
                color =
                    Color.White.copy(
                        alpha = 0.9f
                    ),
                fontSize = 16.sp
            )
        }

        Text(
            text = "Powered by Dirgha",
            color =
                Color.White.copy(
                    alpha = 0.8f
                ),
            style =
                MaterialTheme.typography.bodyMedium,
            modifier =
                Modifier
                    .align(
                        Alignment.BottomCenter
                    )
                    .padding(
                        bottom = 54.dp
                    )
        )
    }

    /*
     * SOFT UPDATE
     */
    configuration?.let { config ->

        if (showSoftUpdate) {

            UpdateDialog(

                title =
                    config.data.config.version.updateTitle,

                message =
                    config.data.config.version.updateMessage,

                forceUpdate = false,

                onUpdate = {

                    openPlayStore(
                        config.data.config.version.playStoreUrl
                    )
                },

                onLater = {

                    showSoftUpdate = false

                    navigateToNextScreen()
                }
            )
        }
    }

    /*
     * FORCE UPDATE
     */
    configuration?.let { config ->

        if (showForceUpdate) {

            UpdateDialog(

                title =
                    config.data.config.version.updateTitle,

                message =
                    config.data.config.version.updateMessage,

                forceUpdate = true,

                onUpdate = {

                    openPlayStore(
                        config.data.config.version.playStoreUrl
                    )
                },

                onLater = {
                    // Do nothing for force update
                }
            )
        }
    }
}