package com.dirgha.bookmyseat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.data.local.ThemeMode
import com.dirgha.bookmyseat.navigation.NavGraph
import com.dirgha.bookmyseat.ui.theme.BookMySeatTheme

class MainActivity : ComponentActivity() {



    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

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