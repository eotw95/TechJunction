package com.example.techjunction.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import com.example.techjunction.viewmodel.ArticlesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchArticles(viewModel: ArticlesViewModel?) {
    val searchArticlesObserver = viewModel?.searchArticles?.observeAsState()

    Column {
        searchArticlesObserver?.value?.forEach { article ->
            Column {
                Text(text = article.title)
                Text(text = article.user.userId)
                Divider()
            }
        }
    }
}