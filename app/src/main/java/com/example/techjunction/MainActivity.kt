package com.example.techjunction

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.techjunction.screens.ArticleDetailScreen
import com.example.techjunction.screens.HomeScreen
import com.example.techjunction.ui.theme.TechJunctionTheme
import com.example.techjunction.worker.RssDownloadWorker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RssDownloadWorker.start(this)
        Thread.sleep(10000)
        setContent {
            TechJunctionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        builder = {
                            composable("home") {
                                HomeScreen(
                                    onClick = { url ->
                                    navController.navigate("detail/$url")
                                    }
                                )
                            }
                            composable("detail/{url}") { navBackStackEntry ->
                                navBackStackEntry.arguments?.getString("url")?.let {
                                    ArticleDetailScreen(url = it)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}