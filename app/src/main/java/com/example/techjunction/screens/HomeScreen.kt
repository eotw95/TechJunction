package com.example.techjunction.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import com.example.techjunction.screens.component.ArticleSection
import com.example.techjunction.screens.component.CategoryTab
import com.example.techjunction.screens.component.Header

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onClick: (String) -> Unit
) {
    Column {
        Header()
        CategoryTab()

        // Todo: 各サービスの記事一覧ページを独立させて、HorizontalPagerで横スクロールできるようにする
        HorizontalPager(pageCount = 3) {
            ArticleSection(onClick)
        }
    }
}