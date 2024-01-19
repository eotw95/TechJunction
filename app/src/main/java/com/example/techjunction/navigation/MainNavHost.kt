package com.example.techjunction.navigation

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.techjunction.screens.FollowArticles
import com.example.techjunction.screens.SearchArticles
import com.example.techjunction.screens.component.Header
import com.example.techjunction.viewmodel.ArticlesViewModel
import com.example.techjunction.viewmodel.ArticlesViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainNavHost(
    navController: NavHostController,
    onChangeTheme: () -> Unit
    ) {
    var viewModel: ArticlesViewModel? = null
    LocalViewModelStoreOwner.current?.let {
        viewModel = viewModel(
            it,
            "ArticleViewModel",
            ArticlesViewModelFactory(LocalContext.current.applicationContext as Application)
        )
    }
    var iconState by remember { mutableStateOf(Icons.Filled.Search) }
    var currentRoot by remember { mutableStateOf(CurrentRoot.OVERVIEW) }
    var isShowBottomBar by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            Header(
                icon = iconState,
                viewModel = viewModel,
                onClickSearch = {
                    navController.navigate(Screen.Search.route)
                    iconState = Icons.Filled.ArrowBack
                },
                onClickBack = {
                    navController.navigateUp()
                    iconState = Icons.Filled.Search
                    isShowBottomBar = true
                },
                onChangeTheme = onChangeTheme
            )
        },
        bottomBar = {
            if (isShowBottomBar) {
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
                                    fontSize = 12.sp,
                                    color = if (currentRoot == screen.currentRoot) {
                                        Color.Black
                                    } else {
                                        Color.LightGray
                                    }
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            icon = {
                                when (screen.route) {
                                    Screen.Overview.route ->
                                        Icon(
                                            imageVector = Icons.Filled.Home,
                                            contentDescription = null,
                                            tint = if (currentRoot == CurrentRoot.OVERVIEW) {
                                                Color.Black
                                            } else {
                                                Color.LightGray
                                            }
                                        )
                                    Screen.Channel.route ->
                                        Icon(
                                            imageVector = Icons.Filled.List,
                                            contentDescription = null,
                                            tint = if (currentRoot == CurrentRoot.CHANNEL) {
                                                Color.Black
                                            } else {
                                                Color.LightGray
                                            }
                                        )
                                    Screen.Follow.route ->
                                        Icon(
                                            imageVector = Icons.Filled.ThumbUp,
                                            contentDescription = null,
                                            tint = if (currentRoot == CurrentRoot.FOLLOW) {
                                                Color.Black
                                            } else {
                                                Color.LightGray
                                            }
                                        )
                                }
                            },
                            onClick = {
                                iconState = Icons.Filled.Search
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                if (navController.currentDestination?.route == Screen.Detail.route) {
                                    iconState = Icons.Filled.ArrowBack
                                }
                                when (navController.currentDestination?.route) {
                                    Screen.Overview.route -> {
                                        currentRoot = CurrentRoot.OVERVIEW
                                    }
                                    Screen.Channel.route -> {
                                        currentRoot = CurrentRoot.CHANNEL
                                    }
                                    Screen.Follow.route -> {
                                        currentRoot = CurrentRoot.FOLLOW
                                    }
                                }
                            }
                        )
                    }
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
                            iconState = Icons.Filled.ArrowBack
                            isShowBottomBar = false
                        }
                    )
                }
                composable(Screen.Channel.route) {
                    ArticlesPager(
                        onClick = { url ->
                            navController.navigate("detail/$url")
                            iconState = Icons.Filled.ArrowBack
                            isShowBottomBar = false
                        }
                    )
                }
                composable("detail/{url}") { navBackStackEntry ->
                    navBackStackEntry.arguments?.getString("url")?.let {
                        ArticleDetail(
                            url = it
                        )
                    }
                }
                composable(Screen.Follow.route) {
                    FollowArticles(
                        onClick =  { url ->
                            navController.navigate("detail/$url")
                            iconState = Icons.Filled.ArrowBack
                            isShowBottomBar = false
                        }
                    )
                }
                composable(Screen.Search.route) {
                    SearchArticles(viewModel)
                    isShowBottomBar = false
                }
            }
        )
    }
}

sealed class Screen(
    val route: String,
    val resourceId: Int,
    val currentRoot: CurrentRoot
) {
    companion object {
        val items = listOf(
            Overview,
            Channel,
            Follow
        )
    }

    object Overview: Screen("Overview", R.string.overview, CurrentRoot.OVERVIEW)
    object Channel: Screen("channel", R.string.channel, CurrentRoot.CHANNEL)
    object Detail: Screen("detail/{url}", R.string.detail, CurrentRoot.DETAIL)
    object Follow: Screen("follow", R.string.favorite, CurrentRoot.FOLLOW)
    object Search: Screen("search", R.string.search, CurrentRoot.SEARCH)
}

enum class CurrentRoot {
    OVERVIEW,
    CHANNEL,
    DETAIL,
    FOLLOW,
    SEARCH
}
