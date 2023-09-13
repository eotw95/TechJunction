package com.example.techjunction.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.techjunction.constants.APP_NAME

@Composable
fun ArticleTitle() {
    Text(text = APP_NAME)
}