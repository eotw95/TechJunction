package com.example.techjunction.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArticleList(){
    Column {
        val dummyList = List(5){
            "sampleArticle"
        }
        dummyList.forEach { item ->
            Text(text = item)
            Divider()
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
        }
    }
}