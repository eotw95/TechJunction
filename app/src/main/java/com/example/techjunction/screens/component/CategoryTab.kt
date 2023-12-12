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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.techjunction.constants.*

@Composable
fun CategoryTab() {
    val categories = listOf(
        TOP,
        QIITA,
        ZENN,
        HATENA,
    )
    Divider()
    Spacer(modifier = Modifier.padding(vertical = 5.dp))
    LazyRow(content = {
        items(categories) { item ->
            Surface(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = item,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    })
    Spacer(modifier = Modifier.padding(vertical = 5.dp))
    Divider()
}