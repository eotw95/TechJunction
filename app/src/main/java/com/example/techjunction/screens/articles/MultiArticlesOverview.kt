package com.example.techjunction.screens.articles

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MultiArticlesOverview(
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

    val observeQiitaArticles = vm?.articles?.observeAsState()
    val observeRssChannels = vm?.rssChannels?.observeAsState()
    val observeRssItems = vm?.rssItems?.observeAsState()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        val articles = observeQiitaArticles?.value
        val size = observeQiitaArticles?.value?.size
        if (size != null) {
            Column {
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Text(
                    text = "Qiita",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                Spacer(modifier = Modifier.padding(vertical = 7.dp))
                HorizontalPager(
                    pageCount = size,
                    contentPadding = PaddingValues(horizontal = 30.dp)
                ) { index ->
                    val encoderUrl = URLEncoder.encode(
                        articles?.get(index)?.url,
                        StandardCharsets.UTF_8.toString()
                    )
                    Card(
                        colors = CardDefaults.cardColors(
//                            containerColor = MaterialTheme.colorScheme.background
                            // Todo: WA setting color
                              containerColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .background(Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .clickable { onClick(encoderUrl) }
                                .fillMaxWidth()
                                .border(
                                    width = 0.1.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(10.dp)
                                .height(100.dp)
                        ) {
                            if (articles?.get(index) != null) {
                                var title = articles[index].title
                                if (title.length > 80) {
                                    title = title.take(80) + "..."
                                }
                                Text(
                                    text = title,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                                Row(
                                    modifier = Modifier.height(32.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = articles?.get(index)?.user?.profileImageUrl,
                                        contentDescription = "author icon",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxHeight(1f)
                                            .aspectRatio(1f)
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                                    Text(text = articles[index].user.userId)
                                }
                            }
                        }
                    }
                }
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
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    if (items != null) {
                        HorizontalPager(
                            pageCount = items.size,
                            contentPadding = PaddingValues(horizontal = 30.dp)
                            ) { index ->
                            val encoderUrl = URLEncoder.encode(items[index].link, StandardCharsets.UTF_8.toString())
                            Card(
                                colors = CardDefaults.cardColors(
//                                    containerColor = MaterialTheme.colorScheme.background
                                    // Todo: WA setting color
                                    containerColor = Color.White
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .clickable { onClick(encoderUrl) }
                                        .fillMaxWidth()
                                        .border(
                                            width = 0.1.dp,
                                            color = Color.Gray,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .height(300.dp)
                                ) {
                                    Column {
                                        // Todo: Zennの場合は、画像が全て入るように分岐させる
                                        AsyncImage(
                                            model = items[index].imgSrc,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .clip(
                                                    RoundedCornerShape(
                                                        topStart = 16.dp,
                                                        topEnd = 16.dp
                                                    )
                                                )
                                                .fillMaxWidth()
                                                .height(200.dp),
                                            onError = { println("image error") }
                                        )
                                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                                        Column(
                                            modifier = Modifier.padding(horizontal = 10.dp)
                                        ) {
                                            var title = items[index].title
                                            if (title.length > 80) {
                                                title = title.take(80) + "..."
                                            }
                                            Text(
                                                text = title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )
                                            Spacer(modifier = Modifier.padding(vertical = 5.dp))
                                            Text(
                                                text = DateConverter.asDate(items[index].pubDate.toString()).toString(),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                                }
                            }
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