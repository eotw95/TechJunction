package com.example.techjunction.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.techjunction.screens.articles.MultiArticlesOverview

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticlesOverView(
    onClick: (String) -> Unit
) {
    MultiArticlesOverview(onClick)
}