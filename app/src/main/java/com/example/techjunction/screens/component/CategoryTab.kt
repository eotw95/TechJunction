package com.example.techjunction.screens.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.techjunction.constants.*

@Composable
fun CategoryTab() {
    val dummyList = listOf(
        KEY_APP_NAME,
        KEY_QIITA,
        KEY_ZEN,
        KEY_HATENA,
        KEY_KOTLIN,
        KEY_COMPOSE,
        KEY_ANDROID
    )

    LazyRow(content = {
        items(dummyList) { item ->
            Surface(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(text = item)
            }
        }
    })
}