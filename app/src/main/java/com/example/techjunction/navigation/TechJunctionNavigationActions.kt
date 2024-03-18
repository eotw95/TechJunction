package com.example.techjunction.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

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