package com.example.techjunction.screens.articles

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.techjunction.R
import com.example.techjunction.constants.CHANNEL_URL_HATENA
import com.example.techjunction.constants.CHANNEL_URL_ZENN
import com.example.techjunction.constants.HATENA
import com.example.techjunction.constants.ZENN
import com.example.techjunction.room.FollowArticle
import com.example.techjunction.util.DateConverter
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.example.techjunction.viewmodel.ArticlesViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticlesByRssFeed(
    onClick: (String) -> Unit,
    channelUri: String
) {
    var vm: ArticlesViewModel? = null
    LocalViewModelStoreOwner.current?.let {
        vm = viewModel(
            it,
            "ArticlesViewModel",
            ArticlesViewModelFactory(LocalContext.current.applicationContext as Application)
        )
    }

    val observeRssChannels = vm?.rssChannels?.observeAsState()
    val observeRssItems = vm?.rssItems?.observeAsState()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        val channel = observeRssChannels?.value?.first() { it.rssUrl == channelUri }
        val items = observeRssItems?.value?.filter { it.channelId == channel?.id }

        items?.forEach { item ->
            val encoderUrl = URLEncoder.encode(item.link, StandardCharsets.UTF_8.toString())
            Column(
                modifier = Modifier
                    .clickable { onClick(encoderUrl) }
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 15.dp)
                ) {
                    AsyncImage(
                        model = item.imgSrc,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        onError = { println("image error") }
                    )
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    Column {
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.height(35.dp)
                        ) {
                            val date = DateConverter.asDate(item.pubDate.toString())
                            Text(
                                text = DateConverter.dataFormat(date),
                                fontSize = 10.sp
                            )
                            val followArticle = FollowArticle(
                                title = item.title,
                                link = item.link,
                                channel = when (channelUri) {
                                    CHANNEL_URL_ZENN -> ZENN
                                    CHANNEL_URL_HATENA -> HATENA
                                    else -> throw IllegalArgumentException("Invalid channel uri: $channelUri")
                                }
                            )
                            Box(
                                contentAlignment = Alignment.CenterEnd,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Button(
                                    modifier = Modifier
                                        .border(
                                            width = 0.3.dp,
                                            color = Color.Gray,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .width(125.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = Color.Black,
                                    ),
                                    onClick = {
                                        vm?.storeArticle(followArticle)
                                    }
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.save_text),
                                        modifier = Modifier.padding(horizontal = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                }
            }
        }
    }
}