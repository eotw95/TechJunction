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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.techjunction.R
import com.example.techjunction.constants.QIITA
import com.example.techjunction.room.FollowArticle
import com.example.techjunction.util.DateConverter
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.example.techjunction.viewmodel.ArticlesViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Date

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

    val observeQiitaArticles = vm?.articles?.observeAsState()

    Column {
        Divider(thickness = 0.5.dp)
        Spacer(modifier = Modifier.padding(vertical = 5.dp))
        Text(
            text = QIITA,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 5.dp))
        Divider(thickness = 0.5.dp)

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
                        .padding(horizontal = 15.dp)
                ) {
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    Text(
                        text = article.title,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Row(
                        modifier = Modifier.height(50.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.height(20.dp),
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
                                Text(
                                    text = article.user.userId,
                                    fontSize = 10.sp
                                )
                            }
                            Row(
                                modifier = Modifier.height(50.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.qiita),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .scale(1f)
                                        .padding(end = 5.dp),
                                    tint = Color.Unspecified
                                )
                                Text(
                                    text = DateConverter.dataFormat(Date(article.createdDate)),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        val followArticle = FollowArticle(
                            title = article.title,
                            link = article.url,
                            channel = QIITA
                        )
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 7.dp)
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
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    Divider(thickness = 0.5.dp)
                }
            }
        }
    }
}