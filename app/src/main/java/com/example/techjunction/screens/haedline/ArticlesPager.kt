package com.example.techjunction.screens.haedline

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.techjunction.constants.CHANNEL_URL_HATENA
import com.example.techjunction.constants.CHANNEL_URL_ZENN
import com.example.techjunction.constants.HATENA
import com.example.techjunction.constants.QIITA
import com.example.techjunction.constants.ZENN
import com.example.techjunction.constants.services
import com.example.techjunction.screens.component.ArticlesByQiitaApi
import com.example.techjunction.screens.component.ArticlesByRssFeed
import com.google.accompanist.pager.HorizontalPagerIndicator

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticlesPager(onClick: (String) -> Unit) {
    val viewModel: ArticlesPagerViewModel = viewModel(
        factory = ArticlesPagerViewModel.provideFactory(
            LocalContext.current.applicationContext as Application
        )
    )

    Column {
        val state = rememberPagerState()
        val count = services.size
        Surface(
            modifier = Modifier.align(Alignment.Start),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Divider(thickness = 0.5.dp)
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Row {
                    services.forEach {
                        Box(
                            modifier = Modifier.width(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = it
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Surface {
                    HorizontalPagerIndicator(
                        pagerState = state,
                        pageCount = count,
                        indicatorWidth = 100.dp,
                        indicatorHeight = 3.dp,
                        spacing = 0.dp,
                        inactiveColor = Color.Transparent,
                        activeColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                    )
                    Divider(
                        thickness = 0.5.dp,
                        modifier = Modifier.offset(y = 2.dp)
                    )
                }
            }
        }
        HorizontalPager(
            state = state,
            pageCount = count
        ) { index ->
            findArticleByName(
                onClick = onClick,
                categoryName = services[index],
                viewModel = viewModel
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun findArticleByName(
    onClick: (String) -> Unit,
    categoryName: String,
    viewModel: ArticlesPagerViewModel
) {
    when (categoryName) {
        QIITA -> ArticlesByQiitaApi(onClick, viewModel)
        ZENN -> ArticlesByRssFeed(onClick, CHANNEL_URL_ZENN, viewModel)
        HATENA -> ArticlesByRssFeed(onClick, CHANNEL_URL_HATENA, viewModel)
    }
}