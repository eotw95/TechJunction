package com.example.techjunction.screens.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.techjunction.constants.KEY_APP_NAME

@Composable
fun ArticleTitle() {
    Text(text = KEY_APP_NAME)
}