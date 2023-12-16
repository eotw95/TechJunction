package com.example.techjunction.screens.articles

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.techjunction.constants.CHANNEL_URL_HATENA
import com.example.techjunction.constants.CHANNEL_URL_ZENN
import com.example.techjunction.util.DateConverter
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.example.techjunction.viewmodel.ArticlesViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticlesAll(
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
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = "Qiita",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Spacer(modifier = Modifier.padding(vertical = 7.dp))
            observeQiitaArticles?.value?.forEach { article ->
                val encoderUrl = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                Column(
                    modifier = Modifier
                        .clickable { onClick(encoderUrl) }
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                ) {
                    Text(
                        text = article.title,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    Row(
                        modifier = Modifier.height(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = article.user.profileImageUrl,
                            contentDescription = "author icon",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxHeight(1f)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                        Text(text = article.user.userId)
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Divider()
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        observeRssChannels?.value?.let { channels ->
            repeat(channels.size) { id ->
                Column {
                    val items = observeRssItems?.value?.filter { it.channelId == id + 1 }
                    val channel = observeRssChannels.value?.first() { it.id == id + 1 }
                    Text(
                        // Todo: 新規のエミュレータで、アプリ起動すると、channel?.titleがnullでException発生するので、修正が必要。
                        text = convertUriToName(channel?.rssUrl),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
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
                Spacer(modifier = Modifier.padding(vertical = 15.dp))
            }
        }
    }
}

private fun convertUriToName(channelUri: String?): String {
    return when (channelUri) {
        CHANNEL_URL_HATENA -> "Hatena Bookmark"
        CHANNEL_URL_ZENN -> "Zenn"
        else -> throw IllegalArgumentException("Channel Uri is invalid")
    }
}