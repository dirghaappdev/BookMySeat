package com.dirgha.bookmyseat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColors =
    lightColorScheme(

        primary = PrimaryBlue,

        secondary = SecondaryCyan,

        background = BackgroundLight,

        surface = SurfaceLight
    )

private val DarkColors =
    darkColorScheme(

        primary = DarkPrimary,

        secondary = DarkSecondary,

        background = BackgroundDark,

        surface = SurfaceDark
    )

@Composable
fun BookMySeatTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
){

    val colors =
        if (darkTheme)
            DarkColors
        else
            LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}