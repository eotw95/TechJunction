package com.example.techjunction.screens.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.techjunction.constants.APP_NAME

@Composable
fun Header() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        color = Color.White
    ) {
        Text(
            text = APP_NAME,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge
        )
    }
}