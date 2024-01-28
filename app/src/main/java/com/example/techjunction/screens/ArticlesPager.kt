package com.example.techjunction.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import com.example.techjunction.constants.services
import com.example.techjunction.screens.component.findArticleByName
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.google.accompanist.pager.HorizontalPagerIndicator

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticlesPager(
    onClick: (String) -> Unit,
    viewModel: ArticlesViewModel?
) {
    Column {
        val state = rememberPagerState()
        val count = services.size
        Surface(
            modifier = Modifier.align(Alignment.Start)
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
                    Divider(
                        thickness = 0.5.dp,
                        modifier = Modifier.offset(y = 2.dp)
                    )
                    HorizontalPagerIndicator(
                        pagerState = state,
                        pageCount = count,
                        indicatorWidth = 100.dp,
                        indicatorHeight = 3.dp,
                        spacing = 0.dp,
                        inactiveColor = Color.Transparent,
                        activeColor = MaterialTheme.colorScheme.onSurface
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