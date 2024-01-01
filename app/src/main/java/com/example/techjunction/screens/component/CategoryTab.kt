package com.example.techjunction.screens.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.techjunction.constants.HATENA
import com.example.techjunction.constants.QIITA
import com.example.techjunction.constants.TOP
import com.example.techjunction.constants.ZENN

@Composable
fun CategoryTab() {
    val categories = listOf(
        TOP,
        QIITA,
        ZENN,
        HATENA,
    )
    Divider(thickness = 0.5.dp)
    Spacer(modifier = Modifier.padding(vertical = 5.dp))
    LazyRow(content = {
        items(categories) { item ->
            Surface(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Text(
                    text = item,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.drawBehind {
                        // Todo: カテゴリ名の下部に水平線を表示させる。
                        //  記事リストの横スクロールと連動させる。
                        val borderSize = 2.dp.toPx()
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = borderSize
                        )
                    }
                )
            }
        }
    })
    Spacer(modifier = Modifier.padding(vertical = 5.dp))
    Divider(thickness = 0.5.dp)
}