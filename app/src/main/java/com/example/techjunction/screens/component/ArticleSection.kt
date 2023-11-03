package com.example.techjunction.screens.component

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.techjunction.network.model.QiitaArticlesResponse
import com.example.techjunction.room.RssChannel
import com.example.techjunction.room.RssItem
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.example.techjunction.viewmodel.ArticlesViewModelFactory

@Composable
fun ArticleSection() {
    var vm: ArticlesViewModel? = null
    LocalViewModelStoreOwner.current?.let {
        vm = viewModel(
            it,
            "ArticlesViewModel",
            ArticlesViewModelFactory(LocalContext.current.applicationContext as Application)
        )
    }

    val channels = mutableListOf<List<Any>>()
    val observeQiitaArticles = vm?.articles?.observeAsState()
    val observeRssChannels = vm?.rssChannels?.observeAsState()
    val observeRssItems = vm?.rssItems?.observeAsState()
    observeQiitaArticles?.value?.let {
        channels.add(it)
    }
    observeRssChannels?.value?.let {
        channels.add(it)
    }
    observeRssItems?.value?.let {
        channels.add(it)
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        channels.forEach { channels ->
            Column {
                channels.forEach {  article ->
                    when (article) {
                        is QiitaArticlesResponse -> {
                            Text(text = article.title)
                        }
                        is RssChannel -> {
                            Text(text = requireNotNull(article.title))
                        }
                        is RssItem -> {
                            Text(text = article.title)
                        }
                    }
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 30.dp))
        }
    }
}