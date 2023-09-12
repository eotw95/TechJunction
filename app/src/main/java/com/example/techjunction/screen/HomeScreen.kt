package com.example.techjunction.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.techjunction.component.ArticleList
import com.example.techjunction.component.ArticleSection
import com.example.techjunction.component.CategoryTab
import com.example.techjunction.component.Header

@Composable
fun HomeScreen() {
    Column {
        Header()
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        CategoryTab()
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        ArticleSection()
    }
}