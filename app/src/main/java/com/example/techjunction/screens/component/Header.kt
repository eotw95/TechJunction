package com.example.techjunction.screens.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.techjunction.constants.APP_NAME

@Composable
fun Header() {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = APP_NAME,
            textAlign = TextAlign.Center
        )
    }
}