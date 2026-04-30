package com.aimcode.userdirectory.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aimcode.userdirectory.core.navigation.destinantion.AddUserDestination
import com.aimcode.userdirectory.core.navigation.destinantion.HomeDestination
import com.aimcode.userdirectory.home.HomeScreen
import com.aimcode.userdirectory.user.AddUserScreen

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
        composable(route = HomeDestination.route) { backStackEntry ->
            HomeScreen(
                navBackStackEntry = backStackEntry,
                onNavigateToAddUser = {
                    navController.navigate(AddUserDestination.route)
                }
            )
        }

        composable(route = AddUserDestination.route) {
            AddUserScreen(
                onNavigateBack = { navController.popBackStack() },
                onUserAdded = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("user_added", true)
                    navController.popBackStack()
                }
            )
        }
    }
}