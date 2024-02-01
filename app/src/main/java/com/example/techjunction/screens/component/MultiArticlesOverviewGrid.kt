package com.example.techjunction.screens.component

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.techjunction.R
import com.example.techjunction.constants.CHANNEL_URL_HATENA
import com.example.techjunction.constants.CHANNEL_URL_QIITA
import com.example.techjunction.constants.CHANNEL_URL_ZENN
import com.example.techjunction.util.DateConverter
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.example.techjunction.viewmodel.ArticlesViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MultiArticlesOverviewGrid(
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

    val allArticles = mutableListOf<ArticleOverview>()
    allArticles.apply {
        observeQiitaArticles?.value?.let { items ->
            items.map {
                ArticleOverview(
                    channelUrl = CHANNEL_URL_QIITA,
                    title = it.title,
                    link = it.url,
                    userId = it.user.userId,
                    userImg = it.user.profileImageUrl,
                    date = DateConverter.dataFormat(Date(it.createdDate))
                )
            }
        }?.let {
            addAll(it)
        }
        observeRssItems?.value?.let { items ->
            addAll(
                items.mapNotNull { item ->
                    observeRssChannels?.value?.find { it.id == item.channelId }?.let {
                        ArticleOverview(
                            channelUrl = it.rssUrl,
                            title = item.title,
                            link = item.link,
                            imgSrc = item.imgSrc,
                            date = DateConverter.dataFormat(Date(item.pubDate ?: Date().time))
                        )
                    }
                }
            )
        }
    }.sortWith(compareBy{ it.date })
    allArticles.reverse()

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 70.dp ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalItemSpacing = 5.dp
        ) {
            items(allArticles) { article ->
                val encoderUrl = URLEncoder.encode(article.link, StandardCharsets.UTF_8.toString())
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier.padding(horizontal = 2.5.dp)
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
                    ) {
                        Column {
                            AsyncImage(
                                model = article.imgSrc,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp
                                        )
                                    )
                                    .fillMaxWidth(),
                                onError = { println("image error") }
                            )
                            Spacer(modifier = Modifier.padding(vertical = 5.dp))
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp)
                            ) {
                                var title = article.title
                                if (title.length > 80) {
                                    title = title.take(80) + "..."
                                }
                                Text(
                                    text = title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.padding(vertical = 5.dp))
                            Row(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = when (article.channelUrl) {
                                        CHANNEL_URL_ZENN -> painterResource(id = R.drawable.zenn)
                                        CHANNEL_URL_HATENA -> painterResource(id = R.drawable.hatenabookmark)
                                        CHANNEL_URL_QIITA -> painterResource(id = R.drawable.qiita)
                                        else -> throw IllegalArgumentException("Invalid channel: ${article.channelUrl}")
                                    },
                                    contentDescription = null,
                                    modifier = when (article.channelUrl) {
                                        CHANNEL_URL_ZENN,
                                        CHANNEL_URL_HATENA -> Modifier
                                            .scale(0.6f)
                                            .padding(end = 1.dp)
                                        CHANNEL_URL_QIITA -> Modifier
                                            .scale(1f)
                                            .padding(end = 5.dp)
                                        else -> throw IllegalArgumentException("Invalid channel: ${article.channelUrl}")
                                    },
                                    tint = Color.Unspecified
                                )
                                if (article.date != null) {
                                    Text(
                                        text = article.date,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(vertical = 1.dp))
                    }
                }
            }
        }
    }
}

data class ArticleOverview(
    val channelUrl: String,
    val title: String,
    val link: String,
    val imgSrc: String? = null,
    val userImg: String? = null,
    val userId: String? = null,
    val date: String? = null
)