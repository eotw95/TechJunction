package com.example.techjunction.screens.articles

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.techjunction.util.DateConverter
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.example.techjunction.viewmodel.ArticlesViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticlesByRssFeed(
    onClick: (String) -> Unit,
    channelUri: String
) {
    var vm: ArticlesViewModel? = null
    LocalViewModelStoreOwner.current?.let {
        vm = viewModel(
            it,
            "ArticlesViewModel",
            ArticlesViewModelFactory(LocalContext.current.applicationContext as Application)
        )
    }

    val observeRssChannels = vm?.rssChannels?.observeAsState()
    val observeRssItems = vm?.rssItems?.observeAsState()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        val channel = observeRssChannels?.value?.first() { it.rssUrl == channelUri }
        val items = observeRssItems?.value?.filter { it.channelId == channel?.id }

        items?.forEach { item ->
            val encoderUrl = URLEncoder.encode(item.link, StandardCharsets.UTF_8.toString())
            Column(
                modifier = Modifier
                    .clickable { onClick(encoderUrl) }
                    .fillMaxWidth()
            ) {
                Column {
                    AsyncImage(
                        model = item.imgSrc,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth(),
                        onError = { println("image error") }
                    )
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 5.dp)
                    ) {
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        Text(
                            text = DateConverter.asDate(item.pubDate.toString()).toString(),
                            fontSize = 10.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Divider()
            }
        }
    }
}