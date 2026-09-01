package com.dirgha.bookmyseat.ui.components
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterChipItem(
    title: String,
    selected: String,
    onClick: () -> Unit
) {
    val isSelected = selected == title

    Surface(
        modifier = Modifier.clickable {
            onClick()
        },
        shape = RoundedCornerShape(10.dp),
        color =
            if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                Color(0xFFF1F5F9),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                Color(0xFFD6DCE5)   // light gray border
        )
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 8.dp
            ),
            color =
                if (isSelected)
                    Color.White
                else
                    Color.DarkGray,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}