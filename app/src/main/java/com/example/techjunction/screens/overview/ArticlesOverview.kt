package com.example.techjunction.screens.overview

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.techjunction.screens.component.MultiArticlesOverviewGrid

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticlesOverview(
    onClick: (String) -> Unit
) {
    MultiArticlesOverviewGrid(onClick)
}