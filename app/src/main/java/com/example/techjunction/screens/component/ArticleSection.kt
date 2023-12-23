package com.example.techjunction.screens.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.techjunction.constants.CHANNEL_URL_HATENA
import com.example.techjunction.constants.CHANNEL_URL_ZENN
import com.example.techjunction.constants.HATENA
import com.example.techjunction.constants.QIITA
import com.example.techjunction.constants.TOP
import com.example.techjunction.constants.ZENN
import com.example.techjunction.screens.articles.MultiArticlesOverview
import com.example.techjunction.screens.articles.ArticlesByQiitaApi
import com.example.techjunction.screens.articles.ArticlesByRssFeed

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticleSection(
    onClick: (String) -> Unit,
    categoryName: String
) {
    when (categoryName) {
        TOP -> MultiArticlesOverview(onClick)
        QIITA -> ArticlesByQiitaApi(onClick)
        ZENN -> ArticlesByRssFeed(onClick, CHANNEL_URL_ZENN)
        HATENA -> ArticlesByRssFeed(onClick, CHANNEL_URL_HATENA)
    }
}