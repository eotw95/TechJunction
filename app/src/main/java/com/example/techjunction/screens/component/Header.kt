package com.example.techjunction.screens.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.techjunction.constants.APP_NAME

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Header() {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 15.dp)
        ) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            Text(
                text = APP_NAME,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayLarge,
                fontSize = 25.sp
            )
            CustomSwitch()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomSwitch() {
    val darkTheme = isSystemInDarkTheme()
    var checked by remember { mutableStateOf(darkTheme) }

    Switch(
        checked = checked,
        modifier = Modifier.scale(0.7f),
        onCheckedChange = {
            checked = it
        }
    )
}