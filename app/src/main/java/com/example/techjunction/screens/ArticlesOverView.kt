package com.example.techjunction.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import com.example.techjunction.constants.services
import com.example.techjunction.screens.component.ArticleSection
import com.example.techjunction.screens.component.CategoryTab
import com.example.techjunction.screens.component.Header

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticlesOverView(
    onClick: (String) -> Unit
) {
    Column {
        Header()
        CategoryTab()
        HorizontalPager(pageCount = services.size) {
            ArticleSection(onClick, services[it])
        }
    }
}