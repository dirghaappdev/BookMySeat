package com.dirgha.bookmyseat

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.data.local.ThemeMode
import com.dirgha.bookmyseat.navigation.NavGraph
import com.dirgha.bookmyseat.ui.theme.BookMySeatTheme

class MainActivity : ComponentActivity() {



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(
        savedInstanceState: Bundle?

    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
        super.onCreate(
            savedInstanceState
        )

        setContent {
BookMySeatTheme{
            NavGraph()
        }
        }
    }
}