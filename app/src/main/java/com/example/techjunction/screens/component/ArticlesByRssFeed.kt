package com.example.techjunction.screens.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.techjunction.R
import com.example.techjunction.constants.CHANNEL_URL_HATENA
import com.example.techjunction.constants.CHANNEL_URL_ZENN
import com.example.techjunction.constants.HATENA
import com.example.techjunction.constants.ZENN
import com.example.techjunction.room.FollowArticle
import com.example.techjunction.screens.haedline.ArticlesPagerViewModel
import com.example.techjunction.util.DateConverter
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticlesByRssFeed(
    onClick: (String) -> Unit,
    channelUri: String,
    viewModel: ArticlesPagerViewModel
) {
    val observeRssChannels = viewModel.rssChannels.observeAsState()
    val observeRssItems = viewModel.rssItems.observeAsState()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        val channel = observeRssChannels.value?.first() { it.rssUrl == channelUri }
        val items = observeRssItems.value?.filter { it.channelId == channel?.id }

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
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.height(35.dp)
                        ) {
                            Icon(
                                painter = when (channelUri) {
                                    CHANNEL_URL_ZENN -> painterResource(id = R.drawable.zenn)
                                    CHANNEL_URL_HATENA -> painterResource(id = R.drawable.hatenabookmark)
                                    else -> throw IllegalArgumentException("Invalid channel: $channelUri")
                                },
                                contentDescription = null,
                                modifier = Modifier.scale(0.6f),
                                tint = Color.Unspecified
                            )
                            Text(
                                text = DateConverter.dataFormat(Date(item.pubDate ?: Date().time)),
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
                                    onClick = {
                                        viewModel.storeArticle(followArticle)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.background,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    )
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
                    Divider(thickness = 0.5.dp)
                }
            }
        }
    }
}