package com.example.techjunction.screens.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.techjunction.R
import com.example.techjunction.constants.HATENA
import com.example.techjunction.constants.QIITA
import com.example.techjunction.constants.ZENN
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchArticles(
    viewModel: SearchArticlesViewModel,
    onClick: (String) -> Unit
) {
    val searchArticlesObserver = viewModel.searchArticles.observeAsState()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            searchArticlesObserver.value?.forEach { article ->
                val encodeUrl = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                Column(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .clickable { onClick(encodeUrl) }
                ) {
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    Row {
                        Icon(
                            painter = when (article.channel) {
                                ZENN -> painterResource(id = R.drawable.zenn)
                                HATENA -> painterResource(id = R.drawable.hatenabookmark)
                                QIITA -> painterResource(id = R.drawable.qiita)
                                else -> throw IllegalArgumentException("Invalid channel: ${article.channel}")
                            },
                            contentDescription = null,
                            modifier = when (article.channel) {
                                ZENN,
                                HATENA -> Modifier.scale(0.6f)
                                QIITA -> Modifier.scale(1f)
                                else -> throw IllegalArgumentException("Invalid channel: ${article.channel}")
                            },
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                        Text(
                            text = article.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    article.description?.let {
                        if (it.length > 200) {
                            Text(
                                text = it.take(200) + "...",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        } else {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    Divider(
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}