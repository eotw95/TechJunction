package com.example.techjunction.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArticleSection() {
    LazyColumn(content = {
        items(5) {
            ArticleTitle()
            Spacer(modifier = Modifier.padding(vertical = 5.dp))
            ArticleList()
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
        }
    })
}