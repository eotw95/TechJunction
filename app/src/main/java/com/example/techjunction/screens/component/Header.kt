package com.example.techjunction.screens.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.techjunction.constants.APP_NAME
import com.example.techjunction.viewmodel.ArticlesViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Header(
    icon: ImageVector,
    viewModel: ArticlesViewModel?,
    onClickSearch: () -> Unit,
    onClickBack: () -> Unit,
    onChangeTheme: () -> Unit
) {
    var isShowTextField by remember { mutableStateOf(false) }
    
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
            when (icon) {
                Icons.Filled.Search -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onClickSearch()
                            isShowTextField = true
                        }
                    )
                }
                Icons.Filled.ArrowBack -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onClickBack()
                            isShowTextField = false
                        }
                    )
                }
            }
            if (isShowTextField) {
                var inputText by remember { mutableStateOf("") }
                val keyboardController = LocalSoftwareKeyboardController.current
                TextField(
                    value = inputText,
                    onValueChange = {
                        inputText = it
                    },
                    placeholder = { Text(text = "記事を検索") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel?.getAllByQuery(inputText)
                            keyboardController?.hide()
                        }
                    ),
                    modifier = Modifier.scale(0.8f)
                )
            } else {
                Text(
                    text = APP_NAME,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 25.sp
                )
            }
            CustomSwitch(onChangeTheme)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomSwitch(
    onSwitch: () -> Unit
) {
    var checked by remember { mutableStateOf(false) }

    Switch(
        checked = checked,
        modifier = Modifier.scale(0.7f),
        onCheckedChange = {
            checked = !checked
            onSwitch()
        }
    )
}