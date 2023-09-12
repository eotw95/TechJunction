package com.example.techjunction.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.techjunction.viewmodel.QiitaArticlesViewModel

@Composable
fun ArticleList(){
    val vm: QiitaArticlesViewModel = viewModel()
    val observeQiitaArticles = vm.articles.observeAsState()
    Column {
        observeQiitaArticles.value?.forEach { article ->
            Text(text = article.title)
            Divider()
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
        }
    }
}