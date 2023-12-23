package com.example.techjunction.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import com.example.techjunction.constants.services
import com.example.techjunction.screens.component.CategoryTab
import com.example.techjunction.screens.component.findArticleByName

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticlesPager(
    onClick: (String) -> Unit
) {
    Column {
        CategoryTab()
        HorizontalPager(pageCount = services.size) { index ->
            findArticleByName(onClick = onClick, categoryName = services[index])
        }
    }
}