package com.example.techjunction.screens.articles

import android.app.Application
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
    val observeRssChannels = vm?.rssChannels?.observeAsState()
    val observeRssItems = vm?.rssItems?.observeAsState()

    val allArticles = mutableListOf<ArticleOverview>()
    allArticles.apply {
        observeQiitaArticles?.value?.let {
            it.map {
                ArticleOverview(
                    it.title,
                    it.url
                )
            }
        }?.let {
            addAll(it)
        }
        observeRssItems?.value?.let {
            it.map {
                ArticleOverview(
                    it.title,
                    it.link,
                    it.imgSrc
                )
            }
        }?.let {
            addAll(it)
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalItemSpacing = 5.dp
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
                        // Todo: Zennの場合は、画像が全て入るように分岐させる
                        AsyncImage(
                            model = article.imgSrc,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
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
    val imgSrc: String? = null
)