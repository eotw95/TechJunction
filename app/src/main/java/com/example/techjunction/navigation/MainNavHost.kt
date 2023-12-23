package com.example.techjunction.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.techjunction.R
import com.example.techjunction.screens.ArticleDetail
import com.example.techjunction.screens.ArticlesOverView
import com.example.techjunction.screens.ArticlesPager
import com.example.techjunction.screens.component.Header

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainNavHost(navController: NavHostController) {
    Scaffold(
        topBar = {
            Header()
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.White
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                Screen.items.forEach { screen ->
                    BottomNavigationItem(
                        label = {
                            Text(
                                text = stringResource(id = screen.resourceId),
                                fontSize = 12.sp
                            )
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        icon = {
                               when (screen.route) {
                                   Screen.Overview.route ->
                                       Icon(Icons.Filled.Home, null)
                                   Screen.Channel.route ->
                                       Icon(Icons.Outlined.List, null)
                                   Screen.Favorite.route ->
                                       Icon(Icons.Outlined.ThumbUp, null)
                               }
                        },
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Overview.route,
            builder = {
                composable(Screen.Overview.route) {
                    ArticlesOverView(
                        onClick = { url ->
                            navController.navigate("detail/$url")
                        }
                    )
                }
                composable(Screen.Channel.route) {
                    ArticlesPager(
                        onClick = { url ->
                            navController.navigate("detail/$url")
                        }
                    )
                }
                composable("detail/{url}") { navBackStackEntry ->
                    navBackStackEntry.arguments?.getString("url")?.let {
                        ArticleDetail(
                            url = it,
                            onClick = { navController.navigateUp() }
                        )
                    }
                }
                composable(Screen.Favorite.route) {
                    // Todo: Implement.
                }
            }
        )
    }
}

sealed class Screen(
    val route: String,
    val resourceId: Int
) {
    companion object {
        val items = listOf(
            Overview,
            Channel,
            Favorite
        )
    }

    object Overview: Screen("Overview", R.string.overview)
    object Channel: Screen("channel", R.string.channel)
    object Detail: Screen("detail", R.string.detail)
    object Favorite: Screen("favorite", R.string.favorite)
}