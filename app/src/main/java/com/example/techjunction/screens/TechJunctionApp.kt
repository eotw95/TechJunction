package com.example.techjunction.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.techjunction.navigation.MainNavHost
import com.example.techjunction.ui.theme.TechJunctionTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TechJunctionApp() {
    val ret = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(ret) }

    TechJunctionTheme(
        darkTheme = isDarkTheme
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            MainNavHost(navController = navController) {
                isDarkTheme = !isDarkTheme
            }
        }
    }
}