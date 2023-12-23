package com.example.techjunction

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.techjunction.screens.ArticleDetail
import com.example.techjunction.screens.ArticlesOverView
import com.example.techjunction.ui.theme.TechJunctionTheme
import com.example.techjunction.worker.RssDownloadWorker

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RssDownloadWorker.start(this)
        setContent {
            TechJunctionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val items = listOf(
                        Screen.MultiArticlesOverview,
                        Screen.Channel
                    )
                    Scaffold(
                        bottomBar = {
                            BottomNavigation {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                items.forEach { screen ->
                                    BottomNavigationItem(
                                        label = { Text(text = stringResource(id = screen.resourceId)) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        icon = { Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null) },
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
                            startDestination = Screen.MultiArticlesOverview.route,
                            builder = {
                                composable(Screen.MultiArticlesOverview.route) {
                                    ArticlesOverView(
                                        onClick = { url ->
                                            navController.navigate("detail/$url")
                                        }
                                    )
                                }
                                composable("channel") {

                                }
                                composable("detail/{url}") { navBackStackEntry ->
                                    navBackStackEntry.arguments?.getString("url")?.let {
                                        ArticleDetail(
                                            url = it,
                                            onClick = { navController.navigateUp() }
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

sealed class Screen(
    val route: String,
    val resourceId: Int
) {
    object MultiArticlesOverview: Screen("MultiArticlesOverview", R.string.multiArticlesOverview)
    object Channel: Screen("channel", R.string.channel)
    object Detail: Screen("detail", R.string.detail)
}