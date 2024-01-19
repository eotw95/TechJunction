package com.example.techjunction.screens.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.techjunction.constants.CHANNEL_URL_HATENA
import com.example.techjunction.constants.CHANNEL_URL_ZENN
import com.example.techjunction.constants.HATENA
import com.example.techjunction.constants.QIITA
import com.example.techjunction.constants.ZENN
import com.example.techjunction.viewmodel.ArticlesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun findArticleByName(
    onClick: (String) -> Unit,
    categoryName: String,
    viewModel: ArticlesViewModel?
) {
    when (categoryName) {
        QIITA -> ArticlesByQiitaApi(onClick, viewModel)
        ZENN -> ArticlesByRssFeed(onClick, CHANNEL_URL_ZENN, viewModel)
        HATENA -> ArticlesByRssFeed(onClick, CHANNEL_URL_HATENA, viewModel)
    }
}