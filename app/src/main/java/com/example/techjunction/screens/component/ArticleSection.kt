package com.example.techjunction.screens.component

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.techjunction.util.DateConverter
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.example.techjunction.viewmodel.ArticlesViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticleSection(
    onClick: (String) -> Unit
) {
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
    observeRssItems?.value?.let {
        channels.add(it)
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Column {
            Text(
                text = "Qiita Trend",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
                )
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            observeQiitaArticles?.value?.forEach { article ->
                Text(text = article.title,)
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Divider()
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 30.dp))

        observeRssChannels?.value?.let { channels ->
            repeat(channels.size) { id ->
                Column {
                    val items = observeRssItems?.value?.filter { it.channelId == id + 1 }
                    val channel = observeRssChannels.value?.first() { it.id == id + 1 }
                    Text(
                        text = requireNotNull(channel?.title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    items?.forEach { item ->
                        val encoderUrl = URLEncoder.encode(item.link, StandardCharsets.UTF_8.toString())
                        Column(
                            modifier = Modifier.clickable { onClick(encoderUrl) }
                        ) {
                            Column {
                                Text(
                                    text = item.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                val dateStr = DateConverter.asDate(item.pubDate.toString()).toString()
                                Text(
                                    text = dateStr,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.padding(vertical = 10.dp))
                            Divider()
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 15.dp))
            }
        }
    }
}