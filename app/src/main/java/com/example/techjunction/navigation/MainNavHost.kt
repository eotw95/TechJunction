package com.example.techjunction.navigation

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.techjunction.R
import com.example.techjunction.screens.component.Header
import com.example.techjunction.screens.detail.ArticleDetail
import com.example.techjunction.screens.follow.FollowArticles
import com.example.techjunction.screens.haedline.ArticlesPager
import com.example.techjunction.screens.overview.ArticlesOverview
import com.example.techjunction.screens.search.SearchArticles
import com.example.techjunction.screens.search.SearchArticlesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainNavHost(
    navController: NavHostController,
    onChangeTheme: () -> Unit
    ) {
    var iconState by remember { mutableStateOf(Icons.Filled.Search) }
    var currentRoot by remember { mutableStateOf(CurrentRoot.OVERVIEW) }
    var isShowBottomBar by remember { mutableStateOf(true) }

    val searchArticlesViewModel: SearchArticlesViewModel = viewModel(
        factory = SearchArticlesViewModel.provideFactory(
            LocalContext.current.applicationContext as Application
        )
    )

    Scaffold(
        topBar = {
            Header(
                currentRoot = currentRoot,
                viewModel = searchArticlesViewModel,
                onClickSearch = {
                    navController.navigate(Screen.Search.route)
                    iconState = Icons.Filled.ArrowBack
                    isShowBottomBar = false
                },
                onClickBack = {
                    navController.navigateUp()
                    iconState = Icons.Filled.Search
                    isShowBottomBar = navController.currentDestination?.route != Screen.Search.route
                },
                onChangeTheme = onChangeTheme
            )
        },
        bottomBar = {
            if (isShowBottomBar) {
                BottomNavigation(
                    backgroundColor = MaterialTheme.colorScheme.onPrimary
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
                                        MaterialTheme.colorScheme.onSurface
                                    } else {
                                        MaterialTheme.colorScheme.outline
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
                                                MaterialTheme.colorScheme.onSurface
                                            } else {
                                                MaterialTheme.colorScheme.outline
                                            }
                                        )
                                    Screen.Channel.route ->
                                        Icon(
                                            imageVector = Icons.Filled.List,
                                            contentDescription = null,
                                            tint = if (currentRoot == CurrentRoot.CHANNEL) {
                                                MaterialTheme.colorScheme.onSurface
                                            } else {
                                                MaterialTheme.colorScheme.outline
                                            }
                                        )
                                    Screen.Follow.route ->
                                        Icon(
                                            imageVector = Icons.Filled.ThumbUp,
                                            contentDescription = null,
                                            tint = if (currentRoot == CurrentRoot.FOLLOW) {
                                                MaterialTheme.colorScheme.onSurface
                                            } else {
                                                MaterialTheme.colorScheme.outline
                                            }
                                        )
                                }
                            },
                            onClick = {
                                iconState = Icons.Filled.Search
                                TechJunctionNavigationActions
                                    .navigateToRoute(navController, screen.route)
                                if (navController.currentDestination?.route == Screen.Detail.route) {
                                    iconState = Icons.Filled.ArrowBack
                                }
                            },
                            alwaysShowLabel = false
                        )
                    }
                }
            }
        }
    ) {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Overview.route,
                builder = {
                    composable(Screen.Overview.route) {
                        currentRoot = CurrentRoot.OVERVIEW
                        ArticlesOverview(
                            onClick = { url ->
                                navController.navigate("detail/$url")
                                iconState = Icons.Filled.ArrowBack
                                isShowBottomBar = false
                            }
                        )
                    }
                    composable(Screen.Channel.route) {
                        currentRoot = CurrentRoot.CHANNEL
                        ArticlesPager(
                            onClick = { url ->
                                navController.navigate("detail/$url")
                                iconState = Icons.Filled.ArrowBack
                                isShowBottomBar = false
                            }
                        )
                    }
                    composable("detail/{url}") { navBackStackEntry ->
                        currentRoot = CurrentRoot.DETAIL
                        navBackStackEntry.arguments?.getString("url")?.let {
                            ArticleDetail(
                                url = it
                            )
                        }
                    }
                    composable(Screen.Follow.route) {
                        currentRoot = CurrentRoot.FOLLOW
                        FollowArticles(
                            onClick =  { url ->
                                navController.navigate("detail/$url")
                                iconState = Icons.Filled.ArrowBack
                                isShowBottomBar = false
                            }
                        )
                    }
                    composable(Screen.Search.route) {
                        currentRoot = CurrentRoot.SEARCH
                        SearchArticles(
                            viewModel = searchArticlesViewModel,
                            onClick = { url ->
                                navController.navigate("detail/$url")
                            }
                        )
                    }
                }
            )
        }
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

object TechJunctionNavigationActions {
    fun navigateToRoute(navController: NavController, route: String) {
        navController.navigate(route) {
            // Todo: findStartDestination()を指定してpopする実装がよくわかってない
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
