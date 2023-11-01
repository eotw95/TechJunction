package com.example.techjunction.screens.component

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.example.techjunction.viewmodel.ArticlesViewModelFactory

@Composable
fun ArticleList(){
    var vm: ArticlesViewModel? = null
    LocalViewModelStoreOwner.current?.let {
        vm = viewModel(
            it,
            "ArticlesViewModel",
            ArticlesViewModelFactory(LocalContext.current.applicationContext as Application)
        )
    }
    val observeQiitaArticles = vm?.articles?.observeAsState()
    Column {
        observeQiitaArticles?.value?.forEach { article ->
            Text(text = article.title)
            Divider()
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
        }
    }
}