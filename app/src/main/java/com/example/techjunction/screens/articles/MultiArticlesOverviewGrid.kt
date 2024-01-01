package com.example.techjunction.screens.articles

import android.app.Application
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.example.techjunction.viewmodel.ArticlesViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
    val observeRssItems = vm?.rssItems?.observeAsState()

    val allArticles = mutableListOf<ArticleOverview>()
    allArticles.apply {
        observeQiitaArticles?.value?.let { items ->
            items.map {
                ArticleOverview(
                    title = it.title,
                    link = it.url,
                    userId = it.user.userId,
                    userImg = it.user.profileImageUrl
                )
            }
        }?.let {
            addAll(it)
        }
        observeRssItems?.value?.let { items ->
            items.map {
                ArticleOverview(
                    title = it.title,
                    link = it.link,
                    imgSrc = it.imgSrc,
                    date = it.pubDate.toString()
                )
            }
        }?.let {
            addAll(it)
        }
    }.shuffle()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalItemSpacing = 10.dp
    ) {
        items(allArticles) { article ->
            val encoderUrl = URLEncoder.encode(article.link, StandardCharsets.UTF_8.toString())
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
                            AsyncImage(
                                model = article.userImg,
                                contentDescription = "author icon",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxHeight(1f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                            article.date?.let { Text(text = it) }
                            article.userId?.let { Text(text = it) }
                        }
                    }
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                }
            }
        }
    }
}

data class ArticleOverview(
    val title: String,
    val link: String,
    val imgSrc: String? = null,
    val userImg: String? = null,
    val userId: String? = null,
    val date: String? = null
)