package com.example.techjunction.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.techjunction.screens.component.ArticleSection
import com.example.techjunction.screens.component.CategoryTab
import com.example.techjunction.screens.component.Header

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onClick: (String) -> Unit
) {
    Column {
        Header()
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        CategoryTab()
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        ArticleSection(onClick)
    }
}