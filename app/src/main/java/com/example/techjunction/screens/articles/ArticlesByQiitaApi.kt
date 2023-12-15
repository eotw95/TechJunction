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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.techjunction.constants.LIMIT_NUMBER_15
import com.example.techjunction.constants.LIMIT_NUMBER_5
import com.example.techjunction.constants.QUERY_KOTLIN
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.example.techjunction.viewmodel.ArticlesViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticlesByQiitaApi(
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
    vm?.fetchQiitaArticles(QUERY_KOTLIN, LIMIT_NUMBER_15)

    val observeQiitaArticles = vm?.articles?.observeAsState()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
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
        }
    }
}