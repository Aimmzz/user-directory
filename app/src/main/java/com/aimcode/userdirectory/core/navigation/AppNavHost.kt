package com.aimcode.userdirectory.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aimcode.userdirectory.core.navigation.destinantion.HomeDestination
import com.aimcode.userdirectory.home.HomeScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = HomeDestination.route,
    ) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen()
        }
    }
}