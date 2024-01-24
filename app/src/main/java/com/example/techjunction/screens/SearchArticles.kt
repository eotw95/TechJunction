package com.example.techjunction.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Icon
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
import com.example.techjunction.viewmodel.ArticlesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchArticles(viewModel: ArticlesViewModel?) {
    val searchArticlesObserver = viewModel?.searchArticles?.observeAsState()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        searchArticlesObserver?.value?.forEach { article ->
            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
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
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                article.description?.let {
                    if (it.length > 200) {
                        Text(text = it.take(200) + "...")
                    } else {
                        Text(text = it)
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Divider(thickness = 0.5.dp)
            }
        }
    }
}