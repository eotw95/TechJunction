package com.example.techjunction.screens.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.techjunction.screens.ArticleDetailScreen
import com.example.techjunction.screens.HomeScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavHost(navController: NavHostController) {
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
                    ArticleDetailScreen(
                        url = it,
                        onClick = { navController.navigateUp() }
                    )
                }
            }
        }
    )
}