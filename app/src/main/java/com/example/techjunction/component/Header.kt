package com.example.techjunction.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Header() {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "TechJunction",
            textAlign = TextAlign.Center
        )
    }
}