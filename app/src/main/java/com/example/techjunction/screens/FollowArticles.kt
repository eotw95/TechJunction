package com.example.techjunction.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.techjunction.R
import com.example.techjunction.constants.HATENA
import com.example.techjunction.constants.QIITA
import com.example.techjunction.constants.ZENN
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.example.techjunction.viewmodel.ArticlesViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun FollowArticles(
    onClick: (String) -> Unit
) {
    var vm: ArticlesViewModel? = null
    LocalViewModelStoreOwner.current?.let {
        vm = viewModel(
            it,
            "ArticleViewModel",
            ArticlesViewModelFactory(LocalContext.current.applicationContext as Application)
        )
    }

    val followArticleObserver = vm?.followArticle?.observeAsState()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        vm?.fetchFollowArticles()
        followArticleObserver?.value?.forEach { article ->
            val encodeUrl = URLEncoder.encode(article.link, StandardCharsets.UTF_8.toString())
            Column(
                modifier = Modifier
                    .clickable { onClick(encodeUrl) }
                    .padding(horizontal = 15.dp)
            ) {
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    Spacer(modifier = Modifier.padding(horizontal = 15.dp))
                    Text(
                        text = article.title,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(300.dp)
                    )
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            modifier = Modifier.clickable { vm?.deleteArticle(article) }
                        )
                    }

                }
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Divider(thickness = 0.5.dp)
            }
        }
    }
}